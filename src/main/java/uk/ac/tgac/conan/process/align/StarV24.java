package uk.ac.tgac.conan.process.align;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 15/01/14
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public class StarV24 extends AbstractConanProcess {

    public static final String EXE = "STAR";

    public StarV24(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public StarV24(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
        this.setFormat(CommandLineFormat.POSIX_SPACE);
    }

    @Override
    public String getName() {
        return "STAR_V2.4";
    }

    public static class Args extends AbstractProcessArgs {

        //"source star-2.4.1d; STAR --runThreadN 16  --genomeDir $index --readFilesIn $r1 $r2 --outSAMtype BAM SortedByCoordinate --outFilterMismatchNmax 4 --outSAMattributes NH HI AS nM NM MD --outSAMstrandField intronMotif --alignIntronMax 50000 --outFileNamePrefix star/$name/$name --limitBAMsortRAM 28175452066"


        private static final int DEFAULT_LOCAL_SPLICED_DISTANCE = 200000;
        private static final int DEFAULT_NB_PATHS = 100;
        private static final int DEFAULT_BATCH = 2;

        private File genomeDir;
        private String genomeDB;
        private File read1;
        private File read2;
        private boolean novelSplicing;
        private int localSplicedDist;
        private int threads;
        private String outputFormat;
        private int nbPaths;
        private File outputFile;
        private File logFile;
        private int batch;
        private boolean expandOffsets;
        private int kmer;

        public Args() {

            super(new Params());

            this.genomeDir = null;
            this.genomeDB = "";
            this.read1 = null;
            this.read2 = null;
            this.novelSplicing = false;
            this.localSplicedDist = DEFAULT_LOCAL_SPLICED_DISTANCE;
            this.threads = 1;
            this.outputFormat = "SAM";
            this.nbPaths = DEFAULT_NB_PATHS;
            this.outputFile = null;
            this.logFile = null;
            this.batch = DEFAULT_BATCH;
            this.expandOffsets = false;
            this.kmer = 0;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getGenomeDir() {
            return genomeDir;
        }

        public void setGenomeDir(File genomeDir) {
            this.genomeDir = genomeDir;
        }

        public String getGenomeDB() {
            return genomeDB;
        }

        public void setGenomeDB(String genomeDB) {
            this.genomeDB = genomeDB;
        }

        public File getRead1() {
            return read1;
        }

        public void setRead1(File read1) {
            this.read1 = read1;
        }

        public File getRead2() {
            return read2;
        }

        public void setRead2(File read2) {
            this.read2 = read2;
        }

        public boolean isNovelSplicing() {
            return novelSplicing;
        }

        public void setNovelSplicing(boolean novelSplicing) {
            this.novelSplicing = novelSplicing;
        }

        public int getLocalSplicedDist() {
            return localSplicedDist;
        }

        public void setLocalSplicedDist(int localSplicedDist) {
            this.localSplicedDist = localSplicedDist;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public String getOutputFormat() {
            return outputFormat;
        }

        public void setOutputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
        }

        public int getNbPaths() {
            return nbPaths;
        }

        public void setNbPaths(int nbPaths) {
            this.nbPaths = nbPaths;
        }

        public File getOutputFile() {
            return outputFile;
        }

        public void setOutputFile(File outputFile) {
            this.outputFile = outputFile;
        }

        public File getLogFile() {
            return logFile;
        }

        public void setLogFile(File logFile) {
            this.logFile = logFile;
        }

        public int getBatch() {
            return batch;
        }

        public void setBatch(int batch) {
            this.batch = batch;
        }

        public boolean isExpandOffsets() {
            return expandOffsets;
        }

        public void setExpandOffsets(boolean expandOffsets) {
            this.expandOffsets = expandOffsets;
        }

        public int getKmer() {
            return kmer;
        }

        public void setKmer(int kmer) {
            this.kmer = kmer;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            /*if (param.equals(params.getGenomeDir())) {
                this.genomeDir = new File(value);
            }
            else if (param.equals(params.getGenomeDB())) {
                this.genomeDB = value;
            }
            else if (param.equals(params.getNovelSplicing())) {
                this.novelSplicing = Integer.parseInt(value) > 0;
            }
            else if (param.equals(params.getLocalSplicedDist())) {
                this.localSplicedDist = Integer.parseInt(value);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getOutputFormat())) {
                this.outputFormat = value;
            }
            else if (param.equals(params.getNbPaths())) {
                this.nbPaths = Integer.parseInt(value);
            }
            else if (param.equals(params.getBatch())) {
                this.batch = Integer.parseInt(value);
            }
            else if (param.equals(params.getExpandOffsets())) {
                this.expandOffsets = value.equalsIgnoreCase("1") ? true : false;
            }
            else if (param.equals(params.getKmer())) {
                this.kmer = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }*/
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            /*if (param.equals(params.getRead1())) {
                this.read1 = new File(value);
            }
            else if (param.equals(params.getRead2())) {
                this.read2 = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }*/
        }

        @Override
        protected void setStdOutRedirectFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            /*if (param.equals(params.getOutputFile())) {
                this.outputFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }*/
        }

        @Override
        protected void setStdErrRedirectFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            /*if (param.equals(params.getLogFile())) {
                this.logFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }*/
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.genomeDir != null) {
                pvp.put(params.getGenomeDir(), this.genomeDir.getAbsolutePath());
            }

            /*if (this.genomeDB != null && !this.genomeDB.isEmpty()) {
                pvp.put(params.getGenomeDB(), this.genomeDB);
            }

            if (this.read1 != null) {
                pvp.put(params.getRead1(), this.read1.getAbsolutePath());
            }

            if (this.read2 != null) {
                pvp.put(params.getRead2(), this.read2.getAbsolutePath());
            }

            if (this.outputFile != null) {
                pvp.put(params.getOutputFile(), this.outputFile.getAbsolutePath());
            }

            if (this.logFile != null) {
                pvp.put(params.getLogFile(), this.logFile.getAbsolutePath());
            }

            if (this.novelSplicing) {
                pvp.put(params.getNovelSplicing(), "1");
            }

            if (this.localSplicedDist != DEFAULT_LOCAL_SPLICED_DISTANCE) {
                pvp.put(params.getLocalSplicedDist(), Integer.toString(this.localSplicedDist));
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.outputFormat != null && !this.outputFormat.isEmpty()) {
                pvp.put(params.getOutputFormat(), this.outputFormat);
            }

            if (this.nbPaths != DEFAULT_NB_PATHS) {
                pvp.put(params.getNbPaths(), Integer.toString(this.nbPaths));
            }

            if (this.batch != DEFAULT_BATCH) {
                pvp.put(params.getBatch(), Integer.toString(this.batch));
            }

            if (this.expandOffsets) {
                pvp.put(params.getExpandOffsets(), "1");
            }

            if (this.kmer > 0) {
                pvp.put(params.getKmer(), Integer.toString(this.kmer));
            }*/

            return pvp;
        }
    }

    public static enum RunMode {
        GENOME_GENERATE {
            @Override
            public String getCommandForm() {
                return "genomeGenerate";
            }
        };

        public abstract String getCommandForm();
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter runMode;
        private ConanParameter genomeDir;
        private ConanParameter genomeFastaFiles;
        private ConanParameter sjdbGTFFile;
        private ConanParameter sjdbOverhang;
        private ConanParameter readFilesIn;
        private ConanParameter outSamType;
        private ConanParameter outFilterMismatchNmax;
        private ConanParameter outSAMattributes;
        private ConanParameter outSAMstrandField;
        private ConanParameter alignIntronMax;
        private ConanParameter outFileNamePrefix;
        private ConanParameter runThreadN;
        private ConanParameter limitBAMsortRAM;

        public Params() {

            this.runMode = new ParameterBuilder()
                    .longName("runMode")
                    .description("Alternative runmodes")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.genomeDir = new ParameterBuilder()
                    .longName("genomeDir")
                    .isOptional(false)
                    .description("Path to genome directory")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.sjdbGTFFile = new ParameterBuilder()
                    .longName("sjdbGTFFile")
                    .description("GTF file containing annotated transcripts")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.sjdbOverhang = new ParameterBuilder()
                    .longName("sjdbOverhang")
                    .description("Length of genomic sequence around annotated junction")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.runThreadN = new ParameterBuilder()
                    .longName("runThreadN")
                    .description("Number of threads")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.genomeFastaFiles = new ParameterBuilder()
                    .longName("genomeFastaFiles")
                    .description("Fasta file or files for the genome to index")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.readFilesIn = new ParameterBuilder()
                    .longName("readFilesIn")
                    .description("Input files for input reads (separate multiple files with a space)")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.outSamType = new ParameterBuilder()
                    .longName("outSAMtype")
                    .description("What kind of SAM output to generate")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.outFilterMismatchNmax = new ParameterBuilder()
                    .longName("outFilterMismatchNmax")
                    .description("Maximum number of mismatches per pair")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.outSAMattributes = new ParameterBuilder()
                    .longName("outSAMattributes")
                    .description("Attributes to output into SAM file")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.outSAMstrandField = new ParameterBuilder()
                    .longName("outSAMstrandField")
                    .description("Useful to driving cufflinks with unstranded data (auto generates XS tag)")
                    .create();

            this.alignIntronMax = new ParameterBuilder()
                    .longName("alignIntronMax")
                    .description("Maximum intron length.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.outFileNamePrefix = new ParameterBuilder()
                    .longName("outFileNamePrefix")
                    .description("Output file name prefix")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.limitBAMsortRAM = new ParameterBuilder()
                    .longName("limitBAMsortRAM")
                    .description("BAM sort RAM")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getRunMode() {
            return runMode;
        }

        public ConanParameter getGenomeDir() {
            return genomeDir;
        }

        public ConanParameter getGenomeFastaFiles() {
            return genomeFastaFiles;
        }

        public ConanParameter getSjdbGTFFile() {
            return sjdbGTFFile;
        }

        public ConanParameter getSjdbOverhang() {
            return sjdbOverhang;
        }

        public ConanParameter getReadFilesIn() {
            return readFilesIn;
        }

        public ConanParameter getOutSamType() {
            return outSamType;
        }

        public ConanParameter getOutFilterMismatchNmax() {
            return outFilterMismatchNmax;
        }

        public ConanParameter getOutSAMattributes() {
            return outSAMattributes;
        }

        public ConanParameter getOutSAMstrandField() {
            return outSAMstrandField;
        }

        public ConanParameter getAlignIntronMax() {
            return alignIntronMax;
        }

        public ConanParameter getOutFileNamePrefix() {
            return outFileNamePrefix;
        }

        public ConanParameter getRunThreadN() {
            return runThreadN;
        }

        public ConanParameter getLimitBAMsortRAM() {
            return limitBAMsortRAM;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.runMode,
                    this.genomeDir,
                    this.genomeFastaFiles,
                    this.sjdbGTFFile,
                    this.sjdbOverhang,
                    this.readFilesIn,
                    this.outSamType,
                    this.outFilterMismatchNmax,
                    this.outSAMattributes,
                    this.alignIntronMax,
                    this.outFileNamePrefix,
                    this.runThreadN,
                    this.limitBAMsortRAM,
            };
        }
    }
}
