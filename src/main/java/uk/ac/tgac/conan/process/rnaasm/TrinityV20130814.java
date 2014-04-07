package uk.ac.tgac.conan.process.rnaasm;

import org.apache.commons.io.FileUtils;
import uk.ac.ebi.fgpt.conan.core.context.DefaultExecutionContext;
import uk.ac.ebi.fgpt.conan.core.context.locality.Local;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionResult;
import uk.ac.ebi.fgpt.conan.model.context.ExitStatus;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.process.align.SamtoolsViewV0_1;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This process is handled quite differently from most other processes.  Trinity is actually a package of scripts and
 * tools and whilst typically they are used in a very predictable way there can be some variation.  In this process
 * we try to build in all the tools into a single workflow.  For example, the user can request normalization of reads
 * up front.  Or they can use the genome guided workflow.
 *
 * We make a few assumptions:
 * 1. input is a single pair of read files, or a single read file (if you want to handle multiple
 * files then you'll have to cat them yourself first).
 * 2. When normalizing we assume you want max coverage at 30
 * 3... probably we assume more... fill in later
 */
public class TrinityV20130814 extends AbstractConanProcess {

    public static final String EXE = "Trinity.pl";
    public static final String NORMALIZE = "normalize_by_kmer_coverage.pl";
    public static final String GG_PREP_ALNS = "prep_rnaseq_alignments_for_genome_assisted_assembly.pl";
    public static final String GG_WRITE_CMDS = "GG_write_trinity_cmds.pl";
    public static final String GG_CHUNK_EXECUTOR = "trinity_gg_chunk_executor.py";
    public static final String GG_ALIGN_READS = "alignReads.pl";
    public static final String GG_ACC_INC = "GG_trinity_accession_incrementer.pl";

    public TrinityV20130814(ConanExecutorService conanExecutorService) throws IOException {
        this(conanExecutorService, new Args());
    }

    public TrinityV20130814(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
        this.initialise();
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
        return "Trinity_V2013_08_14";
    }

    public void initialise() {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutput().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    private File getNormalizedFile(final File originalFile) throws IOException {

        if (originalFile == null) {
            return null;
        }

        File parentDir = originalFile.getAbsoluteFile().getParentFile();

        File[] files = parentDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(originalFile.getName() + ".normalized");
            }
        });

        if (files == null || files.length == 0) {
            return null;
        }

        if (files.length > 1) {
            throw new IOException("Found multiple files matching expected normalized file name: " + originalFile.getName() + ".normalized*");
        }

        return files[0];
    }

    private String loadTrinity(ExecutionContext executionContext) {

        String loadCommand = executionContext.getExternalProcessConfiguration().getCommand(this.getName());
        return loadCommand == null || loadCommand.isEmpty() ? "" : (loadCommand + "; ");
    }

    @Override
    public boolean execute(ExecutionContext executionContext) throws InterruptedException, ProcessExecutionException {

        try {

            Args args = this.getArgs();

            Format formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String now = formatter.format(new Date());

            String jobPrefix = "trinity-" + now;

            if (!this.getLogDir().exists()) {
                this.getLogDir().mkdirs();
            }

            File normalizedReadsDir = this.getNormalizedReadsDir();

            // Normalise the reads first if requested
            if (args.normalise) {

                // Run alignments
                String input = "";
                if (args.singleInput == null) {
                    input += " --left " + args.leftInput.getAbsolutePath() + " --right " + args.rightInput.getAbsolutePath() + " --pairs_together";
                } else {
                    input += " --single " + args.singleInput.getAbsolutePath();
                }

                String normCommand = this.loadTrinity(executionContext) + NORMALIZE +
                        " --seqType " + args.getSeqType().toArgString() +
                        input +
                        " --JM " + args.jellyfishMemory + "G" +
                        " --max_cov 30 " +
                        " --JELLY_CPU " + args.cpu +
                        (args.ssLibType != null ? " --SS_lib_type " + args.getSsLibType().toString() : "") +
                        " --output " + normalizedReadsDir.getAbsolutePath() +
                        " 2>&1";

                this.conanExecutorService.executeProcess(
                        normCommand,
                        this.getLogDir(),
                        jobPrefix + "-norm",
                        args.cpu,
                        args.jellyfishMemory * 1000,
                        false);
            }

            File left = args.normalise && args.leftInput != null ? this.getNormalizedFile(args.leftInput) : args.leftInput;
            File right = args.normalise && args.rightInput != null ? this.getNormalizedFile(args.rightInput) : args.rightInput;
            File single = args.normalise && args.singleInput != null ? this.getNormalizedFile(args.singleInput) : args.singleInput;

            // If not running genome guided just execute the default process configuration.  If genome guided then this
            // this quite a complicated process
            if (!args.genomeGuided) {

                try {

                    StringBuilder sb = new StringBuilder();
                    sb.append(EXE);

                    Params params = (Params)this.getProcessParams();

                    List<ConanParameter> exclusions = new ArrayList<>();
                    exclusions.add(params.getLeftInput());
                    exclusions.add(params.getRightInput());
                    exclusions.add(params.getSingleInput());

                    // Add the options
                    String options = args.getArgMap().buildOptionString(CommandLineFormat.POSIX_SPACE, exclusions).trim();
                    if (!options.isEmpty()) {
                        sb.append(" ").append(options);
                    }

                    if (left != null) {
                        sb.append(" ").append("--left " + left.getAbsolutePath());
                        sb.append(" ").append("--right " + right.getAbsolutePath());
                    }
                    else {
                        sb.append(" ").append("--single " + single.getAbsolutePath());
                    }

                    String command = sb.toString().trim();

                    this.conanExecutorService.executeProcess(
                            this.loadTrinity(executionContext) + command,
                            this.getLogDir(),
                            jobPrefix + "-denovo",
                            args.cpu,
                            args.jellyfishMemory,
                            false);

                } catch (ConanParameterException cpe) {
                    throw new ProcessExecutionException(-1, cpe);
                }
            } else {
                try {
                    this.genomeGuidedAssembly(left, right, single, jobPrefix + "-gg", executionContext);
                } catch (IOException ioe) {
                    throw new ProcessExecutionException(1, ioe);
                }
            }
        }
        catch (IOException ioe) {
            throw new ProcessExecutionException(1, ioe);
        }

        return true;
    }

    private void localExecute(String command) throws ProcessExecutionException, InterruptedException {
        this.getConanProcessService().execute(command, new DefaultExecutionContext(new Local(), null, null));
    }

    protected void genomeGuidedAssembly(File left, File right, File single, String jobPrefix, ExecutionContext executionContext)
            throws ProcessExecutionException, InterruptedException, IOException {

        Args args = this.getArgs();

        File alignmentDir = new File(args.getOutput(), "alignments");

        File samFile = new File(alignmentDir, "gsnap.coordSorted.sam");

        if (args.ggStage == null || args.ggStage == Args.GGStage.ALIGN) {

            // Run alignments
            String input = "";
            if (single == null) {
                input += " --left " + left.getAbsolutePath() + " --right " + right.getAbsolutePath();
            } else {
                input += " --single " + single.getAbsolutePath();
            }


            String alignCommand = GG_ALIGN_READS +
                    " --seqType " + args.seqType.toArgString() +
                    input +
                    " --target " + args.getTargetGenome().getAbsolutePath() +
                    " --output " + alignmentDir.getAbsolutePath() +
                    " --aligner gsnap -- -t " + args.cpu + " --nofails " +
                    " 2>&1";

            this.conanExecutorService.executeProcess(
                    this.loadTrinity(executionContext) + alignCommand,
                    this.getLogDir(),
                    jobPrefix + "-1-align",
                    args.cpu,
                    args.jellyfishMemory / 2,  // This should be fairly conservative... especially if we normalised the data first (I think ... :s )
                    false);

            // Run samtools (convert bam to sam)
            SamtoolsViewV0_1.Args samtoolsViewArgs = new SamtoolsViewV0_1.Args();
            samtoolsViewArgs.setInput(new File(alignmentDir, "gsnap.coordSorted.bam"));
            samtoolsViewArgs.setOutput(samFile);

            SamtoolsViewV0_1 samtoolsView = new SamtoolsViewV0_1(this.conanExecutorService, samtoolsViewArgs);

            this.conanExecutorService.executeProcess(
                    samtoolsView,
                    this.getLogDir(),
                    jobPrefix + "-2-bamtosam",
                    1,
                    2000,
                    false);
        }

        File assembliesDir = new File(args.getOutput(), "assemblies");
        String commandsFileName = assembliesDir.getAbsolutePath() + "/trinity_GG.cmds";

        if (!assembliesDir.exists()) {
            assembliesDir.mkdirs();
        }

        if (args.ggStage == null || args.ggStage == Args.GGStage.PREP) {
            // Prep alignments
            String readFilesListFileName = assembliesDir.getAbsolutePath() + "/read_files.list";
            String prepCommand = GG_PREP_ALNS +
                    " --coord_sorted_SAM " + samFile.getAbsolutePath() +
                    " -I " + args.getMaxIntronSize() +
                    (args.ssLibType != null ? " --SS_lib_type " + args.ssLibType.name() : "") +
                    " 2>&1";

            String cdAlign = "cd " + assembliesDir.getAbsolutePath() + "; ";
            String pwdFull = new File(".").getAbsolutePath();
            String pwd = pwdFull.substring(0, pwdFull.length() - 2);
            String cdBack = "cd " + pwd + "; ";

            this.conanExecutorService.executeProcess(
                    this.loadTrinity(executionContext) + cdAlign + prepCommand + "; " + cdBack,
                    this.getLogDir(),
                    jobPrefix + "-3-prep",
                    1,
                    2000,
                    false);

            // Gather prepped reads (should be real fast so do it locally instead of scheduling a job)
            this.localExecute("find " + assembliesDir.getAbsolutePath() + "/Dir_* -name \"*reads\" > " + readFilesListFileName);

            // Create commands to execute
            String createCommands = GG_WRITE_CMDS +
                    " --reads_list_file " + readFilesListFileName +
                    (args.leftInput != null ? " --paired" : "") +
                    (args.ssLibType != null ? " --SS" : "") +
                    (args.jaccardClip ? " --jaccard_clip" : "") +
                    "> " + commandsFileName;

            this.conanExecutorService.executeProcess(
                    this.loadTrinity(executionContext) + createCommands,
                    this.getLogDir(),
                    jobPrefix + "-4-create_trinity_commands",
                    1,
                    100,
                    false);
        }


        if (args.ggStage == null || args.ggStage == Args.GGStage.ASSEMBLE) {
            // Execute chunks in parallel if requested, otherwise run linearly
            if (args.getParallelJobs() > 1) {

                this.chunkCommands(assembliesDir, new File(commandsFileName), jobPrefix);

            } else {

                String command = GG_CHUNK_EXECUTOR + " -i " + commandsFileName;

                this.conanExecutorService.executeProcess(
                        command,
                        this.getLogDir(),
                        jobPrefix + "-6-assemble",
                        4,
                        2048,
                        false);
            }

            // Collect results from each command and combine into a single transcript file and increment accessions as we go

            String combineCommand = "find " + assembliesDir.getAbsolutePath() + "/Dir_* -name \"*inity.fasta\" | " + GG_ACC_INC + " > " +
                    args.getOutput().getAbsolutePath() + "/Trinity_GG.fasta";

            this.conanExecutorService.executeProcess(
                    this.loadTrinity(executionContext) + combineCommand,
                    this.getLogDir(),
                    jobPrefix + "-7-combine_transcripts",
                    1,
                    100,
                    false);
        }

        // Done.  Path to output transcripts can be accessed using the "args.getOutputTranscripts()" method.
    }



    private void chunkCommands(File assembliesDir, File commandsFile, String jobPrefix) throws IOException, ProcessExecutionException, InterruptedException {

        Args args = this.getArgs();

        List<String> commands = FileUtils.readLines(commandsFile);

        int commandCounter = 0;

        // Initialise separated command lists data structure
        List<List<String>> separateCommandLists = new ArrayList<>();
        for(int i = 0; i < args.getParallelJobs(); i++) {
            separateCommandLists.add(new ArrayList<String>());
        }


        for(String command : commands) {
            int remainder = commandCounter % args.getParallelJobs();
            separateCommandLists.get(remainder).add(command);
            commandCounter++;
        }

        //print "Number of chunks requested: " + str(args.chunks)
        //print "Number of commands: " + str(command_counter)
        //print "Number of commands per chunk: " + str(commands_per_chunk)

        // Make sure a directory exists to store the chunks
        File chunkDir = new File(assembliesDir, "chunks");
        if (!chunkDir.exists()) {
            chunkDir.mkdirs();
        }

        int index = 1;
        List<File> chunkFiles = new ArrayList<>();
        for (List<String> commandList : separateCommandLists) {
            File chunkFile = new File(chunkDir, "trinity_gg_chunk-" + (index++) + ".cmds");
            chunkFiles.add(chunkFile);
            FileUtils.writeLines(chunkFile, commandList);
        }

        // Execute each chunk using python script
        int i = 1;
        List<Integer> jobIds = new ArrayList<>();
        for(File chunk : chunkFiles) {

            String command = GG_CHUNK_EXECUTOR + " -i " + chunk.getAbsolutePath() +
            " 2>&1";

            ExecutionResult result = this.conanExecutorService.executeProcess(
                    command,
                    this.getLogDir(),
                    jobPrefix + "-6-assemble-" + i++,
                    4,
                    2048,
                    true);

            jobIds.add(result.getJobId());
        }

        // Wait for chunks to finish
        this.conanExecutorService.executeScheduledWait(
                jobIds,
                jobPrefix + "-6-assemble-*",
                ExitStatus.Type.COMPLETED_ANY,
                jobPrefix + "_chunk_assembly_wait",
                this.getLogDir());

    }

    @Override
    public boolean isOperational(ExecutionContext executionContext) {

        Args args = this.getArgs();

        String loadCommand = executionContext.getExternalProcessConfiguration().getCommand(this.getName());
        loadCommand = loadCommand == null || loadCommand.isEmpty() ? "" : loadCommand + "; ";

        if (args.normalise) {
            if (!this.getConanProcessService().executableOnPath(NORMALIZE, loadCommand, executionContext))
                return false;
        }

        if (!args.genomeGuided) {
            return super.isOperational(executionContext);
        }
        else {


            return super.isOperational(executionContext) &&
                    new SamtoolsViewV0_1(this.conanExecutorService).isOperational(executionContext) &&
                    this.getConanProcessService().executableOnPath(GG_ALIGN_READS, loadCommand, executionContext) &&
                    this.getConanProcessService().executableOnPath(GG_PREP_ALNS, loadCommand, executionContext) &&
                    this.getConanProcessService().executableOnPath(GG_WRITE_CMDS, loadCommand, executionContext) &&
                    this.getConanProcessService().executableOnPath(GG_ACC_INC, loadCommand, executionContext) &&
                    this.getConanProcessService().executableOnPath("find", executionContext);
        }
    }


    public static class Args extends AbstractProcessArgs {

        private static final int DEFAULT_MIN_CONTIG_LENGTH = 200;
        private static final int DEFAULT_CPUS = 2;
        private static final int DEFAULT_INCHWORM_MIN_KMER_CVG = 1;
        private static final int DEFAULT_CHYSALIS_MAX_READS_PER_GRAPH = 200000;
        private static final int DEFAULT_BUTTERFLY_GROUP_PAIRS_DISTANCE = 500;
        private static final int DEFAULT_BUTTERFLY_PATH_REINF_DISTANCE = -1;
        private static final int DEFAULT_MAX_INTRON_SIZE = 100000;
        private static final int DEFAULT_PARALLEL_JOBS = 1;


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
        }

        public static enum GGStage {
            ALIGN,
            PREP,
            ASSEMBLE
        }

        // General
        private SeqType seqType;
        private int jellyfishMemory;
        private File leftInput;
        private File rightInput;
        private File singleInput;
        private SSLibType ssLibType;
        private File output;
        private int cpu;
        private int minContigLength;
        private boolean genomeGuided;
        private GGStage ggStage;
        private boolean normalise;
        private File targetGenome;
        private boolean jaccardClip;
        private int maxIntronSize;
        private int parallelJobs;

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
            this.jellyfishMemory = 20;
            this.leftInput = null;
            this.rightInput = null;
            this.singleInput = null;
            this.ssLibType = null;
            this.output = new File("");
            this.cpu = DEFAULT_CPUS;
            this.minContigLength = DEFAULT_MIN_CONTIG_LENGTH;
            this.genomeGuided = false;
            this.ggStage = null;
            this.normalise = false;
            this.targetGenome = null;
            this.jaccardClip = false;
            this.maxIntronSize = DEFAULT_MAX_INTRON_SIZE;
            this.parallelJobs = DEFAULT_PARALLEL_JOBS;
            this.inchwormMinKmerCoverage = DEFAULT_INCHWORM_MIN_KMER_CVG;
            this.chysalisMaxReadsPerGraph = DEFAULT_CHYSALIS_MAX_READS_PER_GRAPH;
            this.butterflyGroupPairsDistance = DEFAULT_BUTTERFLY_GROUP_PAIRS_DISTANCE;
            this.butterflyPathReinforcementDistance = DEFAULT_BUTTERFLY_PATH_REINF_DISTANCE;
        }

        public Params getParams() {
            return (Params)this.params;
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

        public int getJellyfishMemory() {
            return jellyfishMemory;
        }

        public void setJellyfishMemory(int jellyfishMemory) {
            this.jellyfishMemory = jellyfishMemory;
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

        public boolean isGenomeGuided() {
            return genomeGuided;
        }

        public void setGenomeGuided(boolean genomeGuided) {
            this.genomeGuided = genomeGuided;
        }

        public GGStage getGgStage() {
            return ggStage;
        }

        public void setGgStage(GGStage ggStage) {
            this.ggStage = ggStage;
        }

        public boolean isNormalise() {
            return normalise;
        }

        public void setNormalise(boolean normalise) {
            this.normalise = normalise;
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

        public int getMaxIntronSize() {
            return maxIntronSize;
        }

        public void setMaxIntronSize(int maxIntronSize) {
            this.maxIntronSize = maxIntronSize;
        }

        public int getParallelJobs() {
            return parallelJobs;
        }

        public void setParallelJobs(int parallelJobs) {
            this.parallelJobs = parallelJobs;
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

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(this.params.getSeqType())) {
                this.seqType = SeqType.valueOf(value.toUpperCase());
            }
            else if (param.equals(this.params.getJellyfishMemory())) {
                this.jellyfishMemory = Integer.parseInt(value);
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
            else if (param.equals(this.params.getGenomeGuided())) {
                this.genomeGuided = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getGgStage())) {
                this.ggStage = GGStage.valueOf(value.toUpperCase());
            }
            else if (param.equals(this.params.getNormalise())) {
                this.normalise = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getTargetGenome())) {
                this.targetGenome = new File(value);
            }
            else if (param.equals(this.params.getJaccardClip())) {
                this.jaccardClip = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getMaxIntronSize())) {
                this.maxIntronSize = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getParallelJobs())) {
                this.parallelJobs = Integer.parseInt(value);
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
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            ParamMap pvp = new DefaultParamMap();

            if (this.seqType != null) {
                pvp.put(params.getSeqType(), this.seqType.toArgString());
            }

            pvp.put(params.getJellyfishMemory(), Integer.toString(this.jellyfishMemory));

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

            if (this.cpu != DEFAULT_CPUS && this.cpu > 0) {
                pvp.put(params.getCpu(), Integer.toString(this.cpu));
            }

            if (this.minContigLength != DEFAULT_MIN_CONTIG_LENGTH && this.minContigLength > 0) {
                pvp.put(params.getMinContigLength(), Integer.toString(this.minContigLength));
            }

            if (this.genomeGuided) {
                pvp.put(params.getGenomeGuided(), Boolean.toString(this.genomeGuided));
            }

            if (this.ggStage != null) {
                pvp.put(params.getGgStage(), this.ggStage.toString());
            }

            if (this.normalise) {
                pvp.put(params.getNormalise(), Boolean.toString(this.normalise));
            }

            if (this.targetGenome != null) {
                pvp.put(params.getTargetGenome(), this.targetGenome.getAbsolutePath());
            }

            if (this.jaccardClip) {
                pvp.put(params.getJaccardClip(), Boolean.toString(this.jaccardClip));
            }

            if (this.maxIntronSize != DEFAULT_MAX_INTRON_SIZE) {
                pvp.put(params.getMaxIntronSize(), Integer.toString(this.maxIntronSize));
            }

            if (this.parallelJobs != DEFAULT_PARALLEL_JOBS) {
                pvp.put(params.getParallelJobs(), Integer.toString(this.parallelJobs));
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

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        // General
        private ConanParameter seqType;
        private ConanParameter jellyfishMemory;
        private ConanParameter leftInput;
        private ConanParameter rightInput;
        private ConanParameter singleInput;
        private ConanParameter ssLibType;
        private ConanParameter output;
        private ConanParameter cpu;
        private ConanParameter minContigLength;
        private ConanParameter jaccardClip;

        // Genome Guided Parameters
        private ConanParameter genomeGuided;
        private ConanParameter ggStage;
        private ConanParameter normalise;
        private ConanParameter targetGenome;
        private ConanParameter maxIntronSize;
        private ConanParameter parallelJobs;

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

            this.jellyfishMemory = new ParameterBuilder()
                    .isOptional(false)
                    .longName("JM")
                    .description("(Jellyfish Memory) number of GB of system memory to use for k-mer counting by jellyfish " +
                            "(eg. 10G) *include the 'G' char")
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

            this.genomeGuided = new ParameterBuilder()
                    .isFlag(true)
                    .longName("genome_guided")
                    .description("set to genome guided mode, only retains assembly fasta file.")
                    .create();

            this.ggStage = new ParameterBuilder()
                    .description("If running in genome guided mode, this option allows you to select a specific stage of the pipeline to execute.  Options: [ALIGN,PREP,ASSEMBLE]")
                    .create();

            this.normalise = new ParameterBuilder()
                    .description("If enabled, reads are normalised prior to assembly.  Assumes max coverage of 30 a min coverage of 2.")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.targetGenome = new ParameterBuilder()
                    .longName("target")
                    .description("The target genome")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.jaccardClip = new ParameterBuilder()
                    .isFlag(true)
                    .longName("jaccard_clip")
                    .description("option, set if you have paired reads and you expect high gene density with UTR " +
                            "overlap (use FASTQ input file format for reads). (note: jaccard_clip is an expensive " +
                            "operation, so avoid using it unless necessary due to finding excessive fusion " +
                            "transcripts w/o it.)")
                    .create();

            this.maxIntronSize = new ParameterBuilder()
                    .description("Used for aligning reads to genome in genome guided mode only")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.parallelJobs = new ParameterBuilder()
                    .description("Number of assembly processes to execute in parallel when in genome guided mode")
                    .argValidator(ArgValidator.DIGITS)
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

        public ConanParameter getJellyfishMemory() {
            return jellyfishMemory;
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

        public ConanParameter getGenomeGuided() {
            return genomeGuided;
        }

        public ConanParameter getGgStage() {
            return ggStage;
        }

        public ConanParameter getNormalise() {
            return normalise;
        }

        public ConanParameter getTargetGenome() {
            return targetGenome;
        }

        public ConanParameter getJaccardClip() {
            return jaccardClip;
        }

        public ConanParameter getMaxIntronSize() {
            return maxIntronSize;
        }

        public ConanParameter getParallelJobs() {
            return parallelJobs;
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

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.seqType,
                    this.jellyfishMemory,
                    this.leftInput,
                    this.rightInput,
                    this.singleInput,
                    this.ssLibType,
                    this.output,
                    this.cpu,
                    this.minContigLength,
                    this.genomeGuided,
                    this.ggStage,
                    this.normalise,
                    this.targetGenome,
                    this.jaccardClip,
                    this.maxIntronSize,
                    this.parallelJobs,
                    this.inchwormMinKmerCoverage,
                    this.chysalisMaxReadsPerGraph,
                    this.butterflyGroupPairsDistance,
                    this.butterflyPathReinforcementDistance
            };
        }
    }
}
