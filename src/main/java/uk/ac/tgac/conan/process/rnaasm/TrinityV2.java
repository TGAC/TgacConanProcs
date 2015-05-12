package uk.ac.tgac.conan.process.rnaasm;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;


public class TrinityV2 extends AbstractConanProcess {

    public static final String EXE = "Trinity";

    public TrinityV2(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public TrinityV2(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    public File getLogDir() {
        return new File(this.getArgs().getOutput(), "logs");
    }

    public File getNormalizedReadsDir() {
        return new File(this.getArgs().getOutput(), "normalized_reads");
    }

    @Override
    public String getName() {
        return "Trinity_V2";
    }

    public void setup() throws IOException {

        // Change to working directory
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutput().getAbsolutePath());
        this.addPostCommand("cd " + pwd);

        // Create grid config file if required
        if (this.getArgs().isUseGridSupport() && this.getArgs().getGridConfFile() == null && this.conanExecutorService.usingScheduler()) {

            StringBuilder sb = new StringBuilder();

            int expectedWallTime = (this.getArgs().getCmdsPerJob() / this.getArgs().getGridNodeCpu()) * 2;     // Allow for approx 3mins per command

            sb.append("grid=").append(this.conanExecutorService.getExecutionContext().getScheduler().getName()).append("\n");
            if (this.conanExecutorService.getExecutionContext().getScheduler().getName().equalsIgnoreCase("LSF")) {
                sb.append("cmd=bsub -q ").append(this.conanExecutorService.getExecutionContext().getScheduler().getArgs().getQueueName())
                .append(" -n ").append(this.getArgs().getGridNodeCpu())
                .append(" -Rrusage[mem=").append(this.getArgs().getGridNodeMaxMemory().substring(0, this.getArgs().getGridNodeMaxMemory().length() - 1) + "000").append("]")
                .append("span[ptile=").append(this.getArgs().getGridNodeCpu()).append("]")
                .append(" -We ").append(expectedWallTime).append("\n");
            }
            else {
                throw new UnsupportedOperationException("Need to implement handling for schedulers other than LSF!");
            }
            sb.append("max_nodes=").append(this.getArgs().getMaxGridNodes()).append("\n");
            sb.append("cmds_per_node=").append(this.getArgs().getCmdsPerJob()).append("\n");

            String gridConf = sb.toString();

            this.getArgs().setGridConfFile(this.getArgs().getDefaultGridConfFile());

            FileUtils.write(this.getArgs().getGridConfFile(), gridConf);
        }
    }


    public static class Args extends AbstractProcessArgs {

        private static final int DEFAULT_MAX_MEMORY_GB = 20;
        private static final int DEFAULT_MIN_CONTIG_LENGTH = 200;
        private static final int DEFAULT_CPUS = 2;
        private static final int DEFAULT_KMER_SIZE = 25;
        private static final int DEFAULT_INCHWORM_MIN_KMER_CVG = 1;
        private static final int DEFAULT_CHYSALIS_MAX_READS_PER_GRAPH = 200000;
        private static final int DEFAULT_BUTTERFLY_GROUP_PAIRS_DISTANCE = 500;
        private static final int DEFAULT_BUTTERFLY_PATH_REINF_DISTANCE = -1;
        private static final int DEFAULT_MAX_INTRON_SIZE = 100000;
        private static final int DEFAULT_MAX_GRID_NODES = 10;
        private static final int DEFAULT_CMDS_PER_NODE = 500;
        private static final int DEFAULT_GRID_NODE_CPU = 1;
        private static final String DEFAULT_GRID_NODE_MAX_MEMORY = "1G";
        private static final int DEFAULT_GENOME_GUIDED_MIN_COVERAGE = 1;

        private Params params = new Params();

        public static enum SeqType {
            FA,
            FQ;

            public String toArgString() {
                return this.name().toLowerCase();
            }
        }

        public static enum SSLibType {
            RF,
            FR,
            F,
            R;

            public static SSLibType fromStrandedness(Library.Strandedness strandedness) {

                if (strandedness == Library.Strandedness.FF_UNSTRANDED || strandedness == Library.Strandedness.FR_UNSTRANDED) {
                    return null;
                }
                else if (strandedness == Library.Strandedness.FR_FIRST_STRAND) {
                    return RF;
                }
                else if (strandedness == Library.Strandedness.FR_SECOND_STRAND) {
                    return FR;
                }
                else {
                    throw new UnsupportedOperationException("Not sure how to translate this type of strandeded RNAseq library into format suitable for Trinity");
                }
            }
        }


        // General
        private SeqType seqType;
        private int maxMemory;
        private File leftInput;
        private File rightInput;
        private File singleInput;
        private File genomeGuidedBam;
        private SSLibType ssLibType;
        private File output;
        private int cpu;
        private int minContigLength;
        private int kmerSize;
        private boolean normaliseReads;
        private boolean trimmomatic;
        private boolean fullCleanUp;
        private File targetGenome;
        private boolean jaccardClip;
        private int genomeGuidedMaxIntron;
        private int genomeGuidedMinCoverage;
        private int cmdsPerJob;
        private int maxGridNodes;
        private File gridConfFile;
        private int gridNodeCpu;
        private String gridNodeMaxMemory;
        private boolean useGridSupport;

        // Inchworm
        private int inchwormMinKmerCoverage;

        // Chysalis
        private int chysalisMaxReadsPerGraph;

        // Butterfly
        private int butterflyGroupPairsDistance;
        private int butterflyPathReinforcementDistance;

        public Args() {
            super(new Params());
            this.seqType = SeqType.FQ;
            this.maxMemory = 20;
            this.leftInput = null;
            this.rightInput = null;
            this.singleInput = null;
            this.genomeGuidedBam = null;
            this.ssLibType = null;
            this.output = new File("");
            this.cpu = DEFAULT_CPUS;
            this.fullCleanUp = false;
            this.minContigLength = DEFAULT_MIN_CONTIG_LENGTH;
            this.kmerSize = DEFAULT_KMER_SIZE;
            this.normaliseReads = false;
            this.targetGenome = null;
            this.jaccardClip = false;
            this.genomeGuidedMaxIntron = DEFAULT_MAX_INTRON_SIZE;
            this.genomeGuidedMinCoverage = DEFAULT_GENOME_GUIDED_MIN_COVERAGE;
            this.inchwormMinKmerCoverage = DEFAULT_INCHWORM_MIN_KMER_CVG;
            this.chysalisMaxReadsPerGraph = DEFAULT_CHYSALIS_MAX_READS_PER_GRAPH;
            this.butterflyGroupPairsDistance = DEFAULT_BUTTERFLY_GROUP_PAIRS_DISTANCE;
            this.butterflyPathReinforcementDistance = DEFAULT_BUTTERFLY_PATH_REINF_DISTANCE;
            this.maxGridNodes = DEFAULT_MAX_GRID_NODES;
            this.cmdsPerJob = DEFAULT_CMDS_PER_NODE;
            this.gridConfFile = null;
            this.gridNodeCpu = DEFAULT_GRID_NODE_CPU;
            this.gridNodeMaxMemory = DEFAULT_GRID_NODE_MAX_MEMORY;
            this.useGridSupport = false;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getDefaultGridConfFile() {
            return new File(this.getOutput(), "grid_conf.txt");
        }

        public File getOutputTranscripts() {
            return new File(this.getOutput(), "Trinity_GG.fasta");
        }

        public SeqType getSeqType() {
            return seqType;
        }

        public void setSeqType(SeqType seqType) {
            this.seqType = seqType;
        }

        public int getMaxMemory() {
            return maxMemory;
        }

        public void setMaxMemory(int maxMemory) {
            this.maxMemory = maxMemory;
        }

        public File getLeftInput() {
            return leftInput;
        }

        public void setLeftInput(File leftInput) {
            this.leftInput = leftInput;
        }

        public File getRightInput() {
            return rightInput;
        }

        public void setRightInput(File rightInput) {
            this.rightInput = rightInput;
        }

        public File getSingleInput() {
            return singleInput;
        }

        public void setSingleInput(File singleInput) {
            this.singleInput = singleInput;
        }

        public File getGenomeGuidedBam() {
            return genomeGuidedBam;
        }

        public void setGenomeGuidedBam(File genomeGuidedBam) {
            this.genomeGuidedBam = genomeGuidedBam;
        }

        public SSLibType getSsLibType() {
            return ssLibType;
        }

        public void setSsLibType(SSLibType ssLibType) {
            this.ssLibType = ssLibType;
        }

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        public int getCpu() {
            return cpu;
        }

        public void setCpu(int cpu) {
            this.cpu = cpu;
        }

        public int getMinContigLength() {
            return minContigLength;
        }

        public void setMinContigLength(int minContigLength) {
            this.minContigLength = minContigLength;
        }

        public int getKmerSize() {
            return kmerSize;
        }

        public void setKmerSize(int kmerSize) {
            this.kmerSize = kmerSize;
        }

        public boolean isTrimmomatic() {
            return trimmomatic;
        }

        public void setTrimmomatic(boolean trimmomatic) {
            this.trimmomatic = trimmomatic;
        }

        public boolean isNormaliseReads() {
            return normaliseReads;
        }

        public void setNormaliseReads(boolean normaliseReads) {
            this.normaliseReads = normaliseReads;
        }

        public File getTargetGenome() {
            return targetGenome;
        }

        public void setTargetGenome(File targetGenome) {
            this.targetGenome = targetGenome;
        }

        public boolean isJaccardClip() {
            return jaccardClip;
        }

        public void setJaccardClip(boolean jaccardClip) {
            this.jaccardClip = jaccardClip;
        }

        public boolean isFullCleanUp() {
            return fullCleanUp;
        }

        public void setFullCleanUp(boolean fullCleanUp) {
            this.fullCleanUp = fullCleanUp;
        }

        public int getGenomeGuidedMaxIntron() {
            return genomeGuidedMaxIntron;
        }

        public void setGenomeGuidedMaxIntron(int genomeGuidedMaxIntron) {
            this.genomeGuidedMaxIntron = genomeGuidedMaxIntron;
        }

        public int getInchwormMinKmerCoverage() {
            return inchwormMinKmerCoverage;
        }

        public void setInchwormMinKmerCoverage(int inchwormMinKmerCoverage) {
            this.inchwormMinKmerCoverage = inchwormMinKmerCoverage;
        }

        public int getChysalisMaxReadsPerGraph() {
            return chysalisMaxReadsPerGraph;
        }

        public void setChysalisMaxReadsPerGraph(int chysalisMaxReadsPerGraph) {
            this.chysalisMaxReadsPerGraph = chysalisMaxReadsPerGraph;
        }

        public int getButterflyGroupPairsDistance() {
            return butterflyGroupPairsDistance;
        }

        public void setButterflyGroupPairsDistance(int butterflyGroupPairsDistance) {
            this.butterflyGroupPairsDistance = butterflyGroupPairsDistance;
        }

        public int getButterflyPathReinforcementDistance() {
            return butterflyPathReinforcementDistance;
        }

        public void setButterflyPathReinforcementDistance(int butterflyPathReinforcementDistance) {
            this.butterflyPathReinforcementDistance = butterflyPathReinforcementDistance;
        }

        public int getGenomeGuidedMinCoverage() {
            return genomeGuidedMinCoverage;
        }

        public void setGenomeGuidedMinCoverage(int genomeGuidedMinCoverage) {
            this.genomeGuidedMinCoverage = genomeGuidedMinCoverage;
        }

        public int getCmdsPerJob() {
            return cmdsPerJob;
        }

        public void setCmdsPerJob(int cmdsPerJob) {
            this.cmdsPerJob = cmdsPerJob;
        }

        public int getMaxGridNodes() {
            return maxGridNodes;
        }

        public void setMaxGridNodes(int maxGridNodes) {
            this.maxGridNodes = maxGridNodes;
        }

        public File getGridConfFile() {
            return gridConfFile;
        }

        public void setGridConfFile(File gridConfFile) {
            this.gridConfFile = gridConfFile;
        }

        public int getGridNodeCpu() {
            return gridNodeCpu;
        }

        public void setGridNodeCpu(int gridNodeCpu) {
            this.gridNodeCpu = gridNodeCpu;
        }

        public String getGridNodeMaxMemory() {
            return gridNodeMaxMemory;
        }

        public void setGridNodeMaxMemory(String gridNodeMaxMemory) {
            this.gridNodeMaxMemory = gridNodeMaxMemory;
        }

        public boolean isUseGridSupport() {
            return useGridSupport;
        }

        public void setUseGridSupport(boolean useGridSupport) {
            this.useGridSupport = useGridSupport;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(this.params.getSeqType())) {
                this.seqType = SeqType.valueOf(value.toUpperCase());
            }
            else if (param.equals(this.params.getMaxMemory())) {
                this.maxMemory = value.toUpperCase().endsWith("G") ?
                        Integer.parseInt(value.substring(0, value.length() - 1)) :
                        Integer.parseInt(value);
            }
            else if (param.equals(this.params.getLeftInput())) {
                this.leftInput = new File(value);
            }
            else if (param.equals(this.params.getRightInput())) {
                this.rightInput = new File(value);
            }
            else if (param.equals(this.params.getSingleInput())) {
                this.singleInput = new File(value);
            }
            else if (param.equals(this.params.getSsLibType())) {
                this.ssLibType = SSLibType.valueOf(value.toUpperCase());
            }
            else if (param.equals(this.params.getOutput())) {
                this.output = new File(value);
            }
            else if (param.equals(this.params.getCpu())) {
                this.cpu = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getMinContigLength())) {
                this.minContigLength = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getKmerSize())) {
                this.kmerSize = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getGenomeGuidedBam())) {
                this.genomeGuidedBam = new File(value);
            }
            else if (param.equals(this.params.getNormaliseReads())) {
                this.normaliseReads = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getTrimmomatic())) {
                this.trimmomatic = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getJaccardClip())) {
                this.jaccardClip = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getFullCleanUp())) {
                this.fullCleanUp = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getGenomeGuidedMaxIntron())) {
                this.genomeGuidedMaxIntron = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getGenomeGuidedMinCoverage())) {
                this.genomeGuidedMinCoverage = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getInchwormMinKmerCoverage())) {
                this.inchwormMinKmerCoverage = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getChysalisMaxReadsPerGraph())) {
                this.chysalisMaxReadsPerGraph = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getButterflyGroupPairsDistance())) {
                this.butterflyGroupPairsDistance = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getButterflyPathReinforcementDistance())) {
                this.butterflyPathReinforcementDistance = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getGridConfFile())) {
                this.gridConfFile = new File(value);
            }
            else if (param.equals(this.params.getGridNodeCpu())) {
                this.gridNodeCpu = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getGridNodeMaxMemory())) {
                this.gridNodeMaxMemory = value;
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            ParamMap pvp = new DefaultParamMap();

            if (this.seqType != null) {
                pvp.put(params.getSeqType(), this.seqType.toArgString());
            }

            pvp.put(params.getMaxMemory(), Integer.toString(this.maxMemory) + "G");

            if (this.leftInput != null) {
                pvp.put(params.getLeftInput(), this.leftInput.getAbsolutePath());
            }

            if (this.rightInput != null) {
                pvp.put(params.getRightInput(), this.rightInput.getAbsolutePath());
            }

            if (this.singleInput != null) {
                pvp.put(params.getSingleInput(), this.singleInput.getAbsolutePath());
            }

            if (this.ssLibType != null) {
                pvp.put(params.getSsLibType(), this.ssLibType.toString());
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            if (this.cpu > DEFAULT_CPUS) {
                pvp.put(params.getCpu(), Integer.toString(this.cpu));
            }

            if (this.minContigLength != DEFAULT_MIN_CONTIG_LENGTH && this.minContigLength > 0) {
                pvp.put(params.getMinContigLength(), Integer.toString(this.minContigLength));
            }

            if (this.kmerSize != DEFAULT_KMER_SIZE) {
                pvp.put(params.getKmerSize(), Integer.toString(this.kmerSize));
            }

            if (this.genomeGuidedBam != null) {
                pvp.put(params.getGenomeGuidedBam(), this.genomeGuidedBam.getAbsolutePath());
            }

            if (this.normaliseReads) {
                pvp.put(params.getNormaliseReads(), Boolean.toString(this.normaliseReads));
            }

            if (this.trimmomatic) {
                pvp.put(params.getTrimmomatic(), Boolean.toString(this.trimmomatic));
            }

            if (this.jaccardClip) {
                pvp.put(params.getJaccardClip(), Boolean.toString(this.jaccardClip));
            }

            if (this.fullCleanUp) {
                pvp.put(params.getFullCleanUp(), Boolean.toString(this.fullCleanUp));
            }

            if (this.genomeGuidedBam != null) {
                pvp.put(params.getGenomeGuidedMaxIntron(), Integer.toString(this.genomeGuidedMaxIntron));
            }

            if (this.genomeGuidedMinCoverage != DEFAULT_GENOME_GUIDED_MIN_COVERAGE) {
                pvp.put(params.getGenomeGuidedMinCoverage(), Integer.toString(this.genomeGuidedMinCoverage));
            }

            if (this.inchwormMinKmerCoverage != DEFAULT_INCHWORM_MIN_KMER_CVG && this.inchwormMinKmerCoverage > 0) {
                pvp.put(params.getInchwormMinKmerCoverage(), Integer.toString(this.inchwormMinKmerCoverage));
            }

            if (this.butterflyGroupPairsDistance != DEFAULT_BUTTERFLY_GROUP_PAIRS_DISTANCE && this.butterflyGroupPairsDistance > 0) {
                pvp.put(params.getButterflyGroupPairsDistance(), Integer.toString(this.butterflyGroupPairsDistance));
            }

            if (this.butterflyPathReinforcementDistance != DEFAULT_BUTTERFLY_PATH_REINF_DISTANCE && this.butterflyPathReinforcementDistance > 0) {
                pvp.put(params.getButterflyPathReinforcementDistance(), Integer.toString(this.butterflyPathReinforcementDistance));
            }

            if (this.gridConfFile != null) {
                pvp.put(params.getGridConfFile(), this.gridConfFile.getAbsolutePath());
            }

            if (this.gridNodeCpu != DEFAULT_GRID_NODE_CPU) {
                pvp.put(params.getGridNodeCpu(), Integer.toString(this.gridNodeCpu));
            }

            if (!this.gridNodeMaxMemory.equalsIgnoreCase(DEFAULT_GRID_NODE_MAX_MEMORY)) {
                pvp.put(params.getGridNodeMaxMemory(), this.gridNodeMaxMemory.toUpperCase());
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        // General
        private ConanParameter seqType;
        private ConanParameter maxMemory;
        private ConanParameter leftInput;
        private ConanParameter rightInput;
        private ConanParameter singleInput;

        private ConanParameter genomeGuidedBam;
        private ConanParameter ssLibType;
        private ConanParameter output;
        private ConanParameter cpu;
        private ConanParameter minContigLength;
        private ConanParameter jaccardClip;
        private ConanParameter longReads;
        private ConanParameter trimmomatic;
        private ConanParameter kmerSize;
        private ConanParameter normaliseReads;
        private ConanParameter fullCleanUp;

        private ConanParameter genomeGuidedMaxIntron;
        private ConanParameter genomeGuidedMinCoverage;

        private ConanParameter gridConfFile;
        private ConanParameter gridNodeCpu;
        private ConanParameter gridNodeMaxMemory;

        // Inchworm
        private ConanParameter inchwormMinKmerCoverage;

        // Chysalis
        private ConanParameter chysalisMaxReadsPerGraph;

        // Butterfly
        private ConanParameter butterflyGroupPairsDistance;
        private ConanParameter butterflyPathReinforcementDistance;


        public Params() {

            this.seqType = new ParameterBuilder()
                    .isOptional(false)
                    .longName("seqType")
                    .description("type of reads: ( fa, or fq )")
                    .create();

            this.maxMemory = new ParameterBuilder()
                    .isOptional(false)
                    .longName("max_memory")
                    .description("suggested max memory to use by Trinity where limiting can be enabled. (jellyfish, sorting, etc) " +
                                "provided in Gb of RAM, ie.  '--max_memory 10G'")
                    .create();

            this.leftInput = new ParameterBuilder()
                    .longName("left")
                    .description("left reads, one or more (separated by space)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.rightInput = new ParameterBuilder()
                    .longName("right")
                    .description("right reads, one or more (separated by space)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.singleInput = new ParameterBuilder()
                    .longName("single")
                    .description("single reads, one or more (note, if single file contains pairs, can use flag: --run_as_paired )")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.ssLibType = new ParameterBuilder()
                    .longName("SS_lib_type")
                    .description("Strand-specific RNA-Seq read orientation.  If paired: RF or FR, if single: F or R. " +
                            "(dUTP method = RF).  See web documentation.")
                    .create();

            this.genomeGuidedBam = new ParameterBuilder()
                    .longName("genome_guided_bam")
                    .description("set to genome guided mode, only retains assembly fasta file.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .longName("output")
                    .description("name of directory for output (will be created if it doesn't already exist). Default(\"trinity_out_dir\")")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.cpu = new ParameterBuilder()
                    .longName("CPU")
                    .description("number of CPUs to use, default: 2.  NOTE: this is only used in de novo mode")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minContigLength = new ParameterBuilder()
                    .longName("min_contig_length")
                    .description("minimum assembled contig length to report (def=200)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.longReads = new ParameterBuilder()
                    .longName("long_reads")
                    .description("fasta file containing error-corrected or circular consensus (CCS) pac bio reads")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.trimmomatic = new ParameterBuilder()
                    .longName("trimmomatic")
                    .isFlag(true)
                    .description("run Trimmomatic to quality trim reads")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.kmerSize = new ParameterBuilder()
                    .longName("KMER_SIZE")
                    .description("kmer length to use (default: 25)  max=32")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.normaliseReads = new ParameterBuilder()
                    .longName("normalize_reads")
                    .description("run in silico normalization of reads. Defaults to max. read coverage of 50.")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

            this.fullCleanUp = new ParameterBuilder()
                    .longName("full_cleanup")
                    .description("only retain the Trinity fasta file, rename as ${output_dir}.Trinity.fasta")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

            this.jaccardClip = new ParameterBuilder()
                    .isFlag(true)
                    .longName("jaccard_clip")
                    .description("option, set if you have paired reads and you expect high gene density with UTR " +
                            "overlap (use FASTQ input file format for reads). (note: jaccard_clip is an expensive " +
                            "operation, so avoid using it unless necessary due to finding excessive fusion " +
                            "transcripts w/o it.)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.genomeGuidedMaxIntron = new ParameterBuilder()
                    .longName("genome_guided_max_intron")
                    .description("Used for aligning reads to genome in genome guided mode only")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.genomeGuidedMinCoverage = new ParameterBuilder()
                    .longName("genome_guided_min_coverage")
                    .description("minimum read coverage for identifying and expressed region of the genome. (default: 1)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.gridConfFile = new ParameterBuilder()
                    .longName("grid_conf_file")
                    .description("configuration file for supported compute farms.  ex.  TRINITY_HOME/htc_conf/BroadInst_LSF.conf currently supported computing gris: LSF, SGE")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.gridNodeCpu = new ParameterBuilder()
                    .longName("grid_node_CPU")
                    .description("number of threads for each parallel process to leverage. (default: 1)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.gridNodeMaxMemory = new ParameterBuilder()
                    .longName("grid_node_max_memory")
                    .description("max memory targeted for each grid node. (default: 1G)")
                    .create();


            this.inchwormMinKmerCoverage = new ParameterBuilder()
                    .longName("min_kmer_cov")
                    .description("min count for K-mers to be assembled by Inchworm.  Useful when you have very large numbers of reads. (default: 1).")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.chysalisMaxReadsPerGraph = new ParameterBuilder()
                    .longName("max_reads_per_graph")
                    .description("maximum number of reads to anchor within a single graph (default: 200000)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.butterflyGroupPairsDistance = new ParameterBuilder()
                    .longName("group_pairs_distance")
                    .description("maximum length expected between fragment pairs (default: 500) (reads outside this distance are treated as single-end)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.butterflyPathReinforcementDistance = new ParameterBuilder()
                    .longName("path_reinforcement_distance")
                    .description("minimum overlap of reads with growing transcript path (default: PE: 75, SE: 25) Set to 1 for the most lenient path extension requirements.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

        }

        public ConanParameter getSeqType() {
            return seqType;
        }

        public ConanParameter getMaxMemory() {
            return maxMemory;
        }

        public ConanParameter getLeftInput() {
            return leftInput;
        }

        public ConanParameter getRightInput() {
            return rightInput;
        }

        public ConanParameter getSingleInput() {
            return singleInput;
        }

        public ConanParameter getSsLibType() {
            return ssLibType;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getCpu() {
            return cpu;
        }

        public ConanParameter getMinContigLength() {
            return minContigLength;
        }

        public ConanParameter getGenomeGuidedBam() {
            return genomeGuidedBam;
        }

        public ConanParameter getNormaliseReads() {
            return normaliseReads;
        }

        public ConanParameter getJaccardClip() {
            return jaccardClip;
        }

        public ConanParameter getLongReads() {
            return longReads;
        }

        public ConanParameter getTrimmomatic() {
            return trimmomatic;
        }

        public ConanParameter getFullCleanUp() {
            return fullCleanUp;
        }

        public ConanParameter getKmerSize() {
            return kmerSize;
        }

        public ConanParameter getGenomeGuidedMaxIntron() {
            return genomeGuidedMaxIntron;
        }

        public ConanParameter getInchwormMinKmerCoverage() {
            return inchwormMinKmerCoverage;
        }

        public ConanParameter getChysalisMaxReadsPerGraph() {
            return chysalisMaxReadsPerGraph;
        }

        public ConanParameter getButterflyGroupPairsDistance() {
            return butterflyGroupPairsDistance;
        }

        public ConanParameter getButterflyPathReinforcementDistance() {
            return butterflyPathReinforcementDistance;
        }

        public ConanParameter getGenomeGuidedMinCoverage() {
            return genomeGuidedMinCoverage;
        }

        public ConanParameter getGridConfFile() {
            return gridConfFile;
        }

        public ConanParameter getGridNodeCpu() {
            return gridNodeCpu;
        }

        public ConanParameter getGridNodeMaxMemory() {
            return gridNodeMaxMemory;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.seqType,
                    this.maxMemory,
                    this.leftInput,
                    this.rightInput,
                    this.singleInput,
                    this.ssLibType,
                    this.output,
                    this.cpu,
                    this.minContigLength,
                    this.genomeGuidedBam,
                    this.normaliseReads,
                    this.trimmomatic,
                    this.kmerSize,
                    this.jaccardClip,
                    this.fullCleanUp,
                    this.longReads,
                    this.genomeGuidedMaxIntron,
                    this.genomeGuidedMinCoverage,
                    this.inchwormMinKmerCoverage,
                    this.chysalisMaxReadsPerGraph,
                    this.butterflyGroupPairsDistance,
                    this.butterflyPathReinforcementDistance,
                    this.gridConfFile,
                    this.gridNodeCpu,
                    this.gridNodeMaxMemory
            };
        }
    }
}
