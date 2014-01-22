package uk.ac.tgac.conan.process.align;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.tgac.conan.core.util.PathUtils;

import java.io.File;
import java.io.IOException;

/**
 * Aligns RNA reads against a genome to identify exon-exon splice junctions.  Built on top of bowtie.
 */
public class TophatV2_0 extends AbstractConanProcess {

    public static final String EXE = "tophat2";

    public TophatV2_0() {
        this(new Args());
    }

    public TophatV2_0(Args args) {
        super(EXE, args, new Params());
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Tophat2_V2.0.X";
    }


    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_READ_MISMATCHES = 2;
        public static final int DEFAULT_MIN_INTRON_LENGTH = 50;
        public static final int DEFAULT_MAX_INTRON_LENGTH = 500000;

        public static enum LibraryType {

            UNSTRANDED,
            FIRSTSTRAND,
            SECONDSTRAND;

            public String toArgString() {
                return "fr-" + this.name().toLowerCase();
            }

            public static LibraryType fromArgString(String value) {
                return LibraryType.valueOf(value.substring(3).toUpperCase());
            }
        }

        private File genomeIndexBase;
        private File[] leftReads;
        private File[] rightReads;
        private File[] singleEndReads;
        private File outputDir;
        private int threads;
        private int readMismatches;
        private int minIntronLength;
        private int maxIntronLength;
        private LibraryType libraryType;


        public Args() {

            super(new Params());

            this.genomeIndexBase = null;
            this.leftReads = null;
            this.rightReads = null;
            this.singleEndReads = null;
            this.outputDir = null;
            this.threads = 1;
            this.readMismatches = DEFAULT_READ_MISMATCHES;
            this.minIntronLength = DEFAULT_MIN_INTRON_LENGTH;
            this.maxIntronLength = DEFAULT_MAX_INTRON_LENGTH;
            this.libraryType = null;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getGenomeIndexBase() {
            return genomeIndexBase;
        }

        public void setGenomeIndexBase(File genomeIndexBase) {
            this.genomeIndexBase = genomeIndexBase;
        }

        public File[] getLeftReads() {
            return leftReads;
        }

        public void setLeftReads(File[] leftReads) {
            this.leftReads = leftReads;
        }

        public File[] getRightReads() {
            return rightReads;
        }

        public void setRightReads(File[] rightReads) {
            this.rightReads = rightReads;
        }

        public File[] getSingleEndReads() {
            return singleEndReads;
        }

        public void setSingleEndReads(File[] singleEndReads) {
            this.singleEndReads = singleEndReads;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public int getReadMismatches() {
            return readMismatches;
        }

        public void setReadMismatches(int readMismatches) {
            this.readMismatches = readMismatches;
        }

        public int getMinIntronLength() {
            return minIntronLength;
        }

        public void setMinIntronLength(int minIntronLength) {
            this.minIntronLength = minIntronLength;
        }

        public int getMaxIntronLength() {
            return maxIntronLength;
        }

        public void setMaxIntronLength(int maxIntronLength) {
            this.maxIntronLength = maxIntronLength;
        }

        public LibraryType getLibraryType() {
            return libraryType;
        }

        public void setLibraryType(LibraryType libraryType) {
            this.libraryType = libraryType;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutputDir())) {
                this.outputDir = new File(value);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getReadMismatches())) {
                this.readMismatches = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinIntronLength())) {
                this.minIntronLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxIntronLength())) {
                this.maxIntronLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getLibraryType())) {
                this.libraryType = LibraryType.fromArgString(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getGenomeIndexBase())) {
                this.genomeIndexBase = new File(value);
            }
            else if (param.equals(params.getLeftReads())) {
                this.leftReads = PathUtils.splitPaths(value, ",");
            }
            else if (param.equals(params.getRightReads())) {
                this.rightReads = PathUtils.splitPaths(value, ",");
            }
            else if (param.equals(params.getSingleEndReads())) {
                this.singleEndReads = PathUtils.splitPaths(value, ",");
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.genomeIndexBase != null) {
                pvp.put(params.getGenomeIndexBase(), this.genomeIndexBase.getAbsolutePath());
            }

            if (this.leftReads != null && this.leftReads.length > 0) {
                pvp.put(params.getLeftReads(), PathUtils.joinAbsolutePaths(this.leftReads, ","));
            }

            if (this.rightReads != null && this.rightReads.length > 0) {
                pvp.put(params.getRightReads(), PathUtils.joinAbsolutePaths(this.rightReads, ","));
            }

            if (this.singleEndReads != null && this.singleEndReads.length > 0) {
                pvp.put(params.getSingleEndReads(), PathUtils.joinAbsolutePaths(this.singleEndReads, ","));
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.readMismatches != DEFAULT_READ_MISMATCHES) {
                pvp.put(params.getReadMismatches(), Integer.toString(this.readMismatches));
            }

            if (this.minIntronLength != DEFAULT_MIN_INTRON_LENGTH) {
                pvp.put(params.getMinIntronLength(), Integer.toString(this.minIntronLength));
            }

            if (this.maxIntronLength != DEFAULT_MAX_INTRON_LENGTH) {
                pvp.put(params.getMaxIntronLength(), Integer.toString(this.maxIntronLength));
            }

            if (this.libraryType != null) {
                pvp.put(params.getLibraryType(), this.libraryType.toArgString());
            }

            return pvp;
        }

    }


    public static class Params extends AbstractProcessParams {

        private ConanParameter genomeIndexBase;
        private ConanParameter leftReads;
        private ConanParameter rightReads;
        private ConanParameter singleEndReads;
        private ConanParameter outputDir;
        private ConanParameter threads;
        private ConanParameter readMismatches;
        private ConanParameter minIntronLength;
        private ConanParameter maxIntronLength;
        private ConanParameter libraryType;


        public Params() {

            this.genomeIndexBase = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description(
                            "The basename of the genome index to be searched. The basename is the name of any of the index " +
                                    "files up to but not including the first period. Bowtie first looks in the current directory for " +
                                    "the index files, then looks in the indexes subdirectory under the directory where the " +
                                    "currently-running bowtie executable is located, then looks in the directory specified in the " +
                                    "BOWTIE_INDEXES  (or BOWTIE2_INDEXES) environment variable.\n" +
                                    "Please note that it is highly recommended that a FASTA file with the sequence(s) the genome " +
                                    "being indexed be present in the same directory with the Bowtie index files and having the name " +
                                    "<genome_index_base>.fa. If not present, TopHat will automatically rebuild this FASTA file from " +
                                    "the Bowtie index files.")
                    .create();

            this.leftReads = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(1)
                    .description(
                            "A comma-separated list of files containing reads in FASTQ or FASTA format. When running TopHat " +
                                    "with paired-end reads, this should be the *_1 (\"left\") set of files.)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.rightReads = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(true)
                    .argIndex(2)
                    .description(
                            "A comma-separated list of files containing reads in FASTA or FASTA format. Only used when " +
                                    "running TopHat with paired end reads, and contains the *_2 (\"right\") set of files. The *_2 " +
                                    "files MUST appear in the same order as the *_1 files.")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.singleEndReads = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(true)
                    .argIndex(3)
                    .description(
                            "A comma-separated list of files containing reads in FASTA or FASTA format. This is not an " +
                                    "explicit option ")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.outputDir = new ParameterBuilder()
                    .shortName("o")
                    .longName("output-dir")
                    .description(
                            "Sets the name of the directory in which TopHat will write all of its output. The default is " +
                                    "\"./tophat_out")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("p")
                    .longName("num-threads")
                    .description("Use this many threads to align reads. The default is 1.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.readMismatches = new ParameterBuilder()
                    .shortName("N")
                    .longName("read-mismatches")
                    .description("Final read alignments having more than these many mismatches are discarded. The default is 2.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minIntronLength = new ParameterBuilder()
                    .shortName("i")
                    .longName("min-intron-length")
                    .description("The minimum intron length. TopHat will ignore donor/acceptor pairs closer than this " +
                            "many bases apart. The default is 70.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxIntronLength = new ParameterBuilder()
                    .shortName("I")
                    .longName("max-intron-length")
                    .description("The maximum intron length. When searching for junctions ab initio, TopHat will ignore " +
                            "donor/acceptor pairs farther than this many bases apart, except when such a pair is supported by a split segment alignment of a long read. The default is 500000.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.libraryType = new ParameterBuilder()
                    .longName("library-type")
                    .description("The default is unstranded (fr-unstranded). If either fr-firststrand or fr-secondstrand " +
                            "is specified, every read alignment will have an XS attribute tag as explained below. " +
                            "Consider supplying library type options below to select the correct RNA-seq protocol.")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

        }

        public ConanParameter getGenomeIndexBase() {
            return genomeIndexBase;
        }

        public ConanParameter getLeftReads() {
            return leftReads;
        }

        public ConanParameter getRightReads() {
            return rightReads;
        }

        public ConanParameter getSingleEndReads() {
            return singleEndReads;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getReadMismatches() {
            return readMismatches;
        }

        public ConanParameter getMinIntronLength() {
            return minIntronLength;
        }

        public ConanParameter getMaxIntronLength() {
            return maxIntronLength;
        }

        public ConanParameter getLibraryType() {
            return libraryType;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.genomeIndexBase,
                    this.leftReads,
                    this.rightReads,
                    this.singleEndReads,
                    this.outputDir,
                    this.threads,
                    this.readMismatches,
                    this.minIntronLength,
                    this.maxIntronLength,
                    this.libraryType
            };
        }

    }
}
