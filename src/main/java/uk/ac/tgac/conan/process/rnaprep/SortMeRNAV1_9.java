package uk.ac.tgac.conan.process.rnaprep;

import org.apache.commons.cli.CommandLine;
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

/**
 * SortMeRNA is a software designed to filter metatranscriptomic reads data. It takes as input a file
 * of reads (fasta or fastq format) and an rRNA database file, and sorts apart the accepted reads and
 * the rejected reads into two files specified by the user.
 */
public class SortMeRNAV1_9 extends AbstractConanProcess {

    public static final String EXE = "sortmerna";

    public SortMeRNAV1_9() {
        this(new Args());
    }

    public SortMeRNAV1_9(Args args) {
        super(EXE, args, new Params());
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "SortMeRNAV1_9";
    }

    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_MEMORY = 262144;

        private File[] illuminaReads;
        private File[] roche454Reads;
        private File[] databases;
        private File acceptedReads;
        private File rejectedReads;
        private boolean bydbs;
        private File log;
        private boolean pairedIn;
        private boolean pairedOut;
        private double ratioHitsToLength;
        private boolean forwardStrandOnly;
        private boolean reverseComplementStrandOnly;
        private int threads;
        private int memory;

        public Args() {
            super(new Params());
            this.illuminaReads = null;
            this.roche454Reads = null;
            this.databases = null;
            this.acceptedReads = null;
            this.rejectedReads = null;
            this.bydbs = false;
            this.log = null;
            this.pairedIn = false;
            this.pairedOut = false;
            this.ratioHitsToLength = -1.0;
            this.forwardStrandOnly = false;
            this.reverseComplementStrandOnly = false;
            this.threads = 1;
            this.memory = DEFAULT_MEMORY;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File[] getIlluminaReads() {
            return illuminaReads;
        }

        public void setIlluminaReads(File[] illuminaReads) {
            this.illuminaReads = illuminaReads;
        }

        public File[] getRoche454Reads() {
            return roche454Reads;
        }

        public void setRoche454Reads(File[] roche454Reads) {
            this.roche454Reads = roche454Reads;
        }

        public File[] getDatabases() {
            return databases;
        }

        public void setDatabases(File[] databases) {
            this.databases = databases;
        }

        public File getAcceptedReads() {
            return acceptedReads;
        }

        public void setAcceptedReads(File acceptedReads) {
            this.acceptedReads = acceptedReads;
        }

        public File getRejectedReads() {
            return rejectedReads;
        }

        public void setRejectedReads(File rejectedReads) {
            this.rejectedReads = rejectedReads;
        }

        public boolean isBydbs() {
            return bydbs;
        }

        public void setBydbs(boolean bydbs) {
            this.bydbs = bydbs;
        }

        public File getLog() {
            return log;
        }

        public void setLog(File log) {
            this.log = log;
        }

        public boolean isPairedIn() {
            return pairedIn;
        }

        public void setPairedIn(boolean pairedIn) {
            this.pairedIn = pairedIn;
        }

        public boolean isPairedOut() {
            return pairedOut;
        }

        public void setPairedOut(boolean pairedOut) {
            this.pairedOut = pairedOut;
        }

        public double getRatioHitsToLength() {
            return ratioHitsToLength;
        }

        public void setRatioHitsToLength(double ratioHitsToLength) {
            this.ratioHitsToLength = ratioHitsToLength;
        }

        public boolean isForwardStrandOnly() {
            return forwardStrandOnly;
        }

        public void setForwardStrandOnly(boolean forwardStrandOnly) {
            this.forwardStrandOnly = forwardStrandOnly;
        }

        public boolean isReverseComplementStrandOnly() {
            return reverseComplementStrandOnly;
        }

        public void setReverseComplementStrandOnly(boolean reverseComplementStrandOnly) {
            this.reverseComplementStrandOnly = reverseComplementStrandOnly;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public int getMemory() {
            return memory;
        }

        public void setMemory(int memory) {
            this.memory = memory;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            /*if (param.equals(this.params.getIlluminaReads())) {
                this.illuminaReads = new File(value);
            }
            else if (param.equals(this.params.getLeftReads())) {
                this.leftReads = splitCommaSepPaths(value);
            }
            else if (param.equals(this.params.getRightReads())) {
                this.rightReads = splitCommaSepPaths(value);
            }
            else if (param.equals(this.params.getSingleEndReads())) {
                this.singleEndReads = splitCommaSepPaths(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            } */
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

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.illuminaReads != null && this.illuminaReads.length > 0) {
                pvp.put(params.getIlluminaReads(), PathUtils.joinAbsolutePaths(this.illuminaReads, " "));
            }

            if (this.roche454Reads != null && this.roche454Reads.length > 0) {
                pvp.put(params.getRoche454Reads(), PathUtils.joinAbsolutePaths(this.roche454Reads, " "));
            }

            if (this.databases != null && this.databases.length > 0) {
                pvp.put(params.getDatabases(), Integer.toString(this.databases.length) + " --db " + PathUtils.joinAbsolutePaths(this.databases, " "));
            }

            if (this.acceptedReads != null) {
                pvp.put(params.getAcceptedReads(), this.acceptedReads.getAbsolutePath());
            }

            if (this.rejectedReads != null) {
                pvp.put(params.getRejectedReads(), this.rejectedReads.getAbsolutePath());
            }

            if (this.bydbs) {
                pvp.put(params.getBydbs(), Boolean.toString(this.bydbs));
            }

            if (this.log != null) {
                pvp.put(params.getLog(), this.log.getAbsolutePath());
            }

            if (this.pairedIn) {
                pvp.put(params.getPairedIn(), Boolean.toString(this.pairedIn));
            }

            if (this.pairedOut) {
                pvp.put(params.getPairedOut(), Boolean.toString(this.pairedOut));
            }

            if (this.ratioHitsToLength > 0.0) {
                pvp.put(params.getRatioHitsToLength(), Double.toString(this.ratioHitsToLength));
            }

            if (this.forwardStrandOnly) {
                pvp.put(params.getForwardStrandOnly(), Boolean.toString(this.forwardStrandOnly));
            }

            if (this.reverseComplementStrandOnly) {
                pvp.put(params.getReverseComplementStrandOnly(), Boolean.toString(this.reverseComplementStrandOnly));
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.memory != DEFAULT_MEMORY) {
                pvp.put(params.getMemory(), Integer.toString(this.memory));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter illuminaReads;
        private ConanParameter roche454Reads;
        private ConanParameter databases;
        private ConanParameter acceptedReads;
        private ConanParameter rejectedReads;
        private ConanParameter bydbs;
        private ConanParameter log;
        private ConanParameter pairedIn;
        private ConanParameter pairedOut;
        private ConanParameter ratioHitsToLength;
        private ConanParameter forwardStrandOnly;
        private ConanParameter reverseComplementStrandOnly;
        private ConanParameter threads;
        private ConanParameter memory;

        public Params() {

            this.illuminaReads = new ParameterBuilder()
                    .longName("I")
                    .description("illumina reads file name {fasta/fastq}")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.roche454Reads = new ParameterBuilder()
                    .longName("454")
                    .description("roche 454 reads file name {fasta/fastq}")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.databases = new ParameterBuilder()
                    .shortName("n")
                    .isOptional(false)
                    .description("number of databases to use followed by --db <rrnas database name(s) (space separated)>")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.acceptedReads = new ParameterBuilder()
                    .longName("accept")
                    .description("accepted reads file name")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.rejectedReads = new ParameterBuilder()
                    .longName("other")
                    .description("rejected reads file name")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.bydbs = new ParameterBuilder()
                    .longName("bydbs")
                    .isFlag(true)
                    .description("output the accepted reads by database (default: concatenated file of reads)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.log = new ParameterBuilder()
                    .longName("log")
                    .description("overall statistics file name (default: no statistics file created)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.pairedIn = new ParameterBuilder()
                    .longName("paired-in")
                    .description("put both paired-end reads into --accept file")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.pairedOut = new ParameterBuilder()
                    .longName("paired-out")
                    .description("put both paired-end reads into --other file (default: if one read is accepted and the " +
                            "other is not, separate the reads into --accept and --other files)")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.ratioHitsToLength = new ParameterBuilder()
                    .shortName("r")
                    .description("ratio of the number of hits on the read / read length (default Illumina: 0.25, Roche 454: 0.15)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.forwardStrandOnly = new ParameterBuilder()
                    .shortName("F")
                    .description("search only the forward strand (default: both strands are searched)")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.reverseComplementStrandOnly = new ParameterBuilder()
                    .shortName("R")
                    .description("search only the reverse-complementary strand (default: both strands are searched)")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("a")
                    .description("number of threads to use (default: 1")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.memory = new ParameterBuilder()
                    .shortName("m")
                    .description("(m x 4096 bytes) for loading the reads into memory \n" +
                            "                      ex. `-m 4' means 4*4096 = 16384 bytes will be allocated for the reads \n" +
                            "                      note: maximum -m is 490611\n" +
                            "                      (default: m = 262144 = 1GB)\n")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getIlluminaReads() {
            return illuminaReads;
        }

        public ConanParameter getRoche454Reads() {
            return roche454Reads;
        }

        public ConanParameter getDatabases() {
            return databases;
        }

        public ConanParameter getAcceptedReads() {
            return acceptedReads;
        }

        public ConanParameter getRejectedReads() {
            return rejectedReads;
        }

        public ConanParameter getBydbs() {
            return bydbs;
        }

        public ConanParameter getLog() {
            return log;
        }

        public ConanParameter getPairedIn() {
            return pairedIn;
        }

        public ConanParameter getPairedOut() {
            return pairedOut;
        }

        public ConanParameter getRatioHitsToLength() {
            return ratioHitsToLength;
        }

        public ConanParameter getForwardStrandOnly() {
            return forwardStrandOnly;
        }

        public ConanParameter getReverseComplementStrandOnly() {
            return reverseComplementStrandOnly;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMemory() {
            return memory;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.illuminaReads,
                    this.roche454Reads,
                    this.databases,
                    this.acceptedReads,
                    this.rejectedReads,
                    this.bydbs,
                    this.log,
                    this.pairedIn,
                    this.pairedOut,
                    this.ratioHitsToLength,
                    this.forwardStrandOnly,
                    this.reverseComplementStrandOnly,
                    this.threads,
                    this.memory
            };
        }
    }
}
