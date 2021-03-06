package uk.ac.tgac.conan.process.rnaasm;

import org.apache.commons.cli.CommandLine;
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

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 17/01/14
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
public class CufflinksV2 extends AbstractConanProcess {

    public static final String EXE = "cufflinks";

    public CufflinksV2(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public CufflinksV2(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Cufflinks_V2";
    }

    public static class Args extends AbstractProcessArgs {

        private static final int DEFAULT_THREADS = 1;
        private static final int DEFAULT_SEED = 0;
        private static final LibraryType DEFAULT_LIBRARY_TYPE = LibraryType.FR_UNSTRANDED;
        private static final int DEFAULT_MIN_INTRON_LENGTH = 50;
        private static final int DEFAULT_MAX_INTRON_LENGTH = 300000;
        private static final String DEFAULT_LIBRARY_NORM_METHOD = "classic-fpkm";


        public static enum LibraryType {
            FF_FIRSTSTRAND,
            FF_SECONDSTRAND,
            FF_UNSTRANDED,
            FR_FIRSTSTRAND,
            FR_SECONDSTRAND,
            FR_UNSTRANDED,
            TRANSFLAGS;

            public String toArgString() {
                return this.toString().replace('_','-').toLowerCase();
            }

            public static LibraryType fromArgString(String value) {
                return LibraryType.valueOf(value.replace('-','_').toUpperCase());
            }

            public static LibraryType fromStrandedness(Library.Strandedness strandedness) {

                if (strandedness == Library.Strandedness.FF_UNSTRANDED) return FF_UNSTRANDED;
                else if (strandedness == Library.Strandedness.FF_FIRST_STRAND) return FF_FIRSTSTRAND;
                else if (strandedness == Library.Strandedness.FF_SECOND_STRAND) return FF_SECONDSTRAND;
                else if (strandedness == Library.Strandedness.FR_UNSTRANDED) return FR_UNSTRANDED;
                else if (strandedness == Library.Strandedness.FR_FIRST_STRAND) return FR_FIRSTSTRAND;
                else if (strandedness == Library.Strandedness.FR_SECOND_STRAND) return FR_SECONDSTRAND;

                return null;
            }
        }

        private File input;
        private File outputDir;
        private int threads;
        private int seed;
        private File maskFile;
        private LibraryType libraryType;
        private int minIntronLength;
        private int maxIntronLength;
        private boolean noUpdateCheck;
        private String libraryNormMethod;

        public Args() {

            super(new Params());

            this.input = null;
            this.outputDir = null;
            this.threads = DEFAULT_THREADS;
            this.seed = DEFAULT_SEED;
            this.maskFile = null;
            this.libraryType = DEFAULT_LIBRARY_TYPE;
            this.minIntronLength = DEFAULT_MIN_INTRON_LENGTH;
            this.maxIntronLength = DEFAULT_MAX_INTRON_LENGTH;
            this.noUpdateCheck = true;
            this.libraryNormMethod = DEFAULT_LIBRARY_NORM_METHOD;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getInput() {
            return input;
        }

        public void setInput(File input) {
            this.input = input;
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

        public int getSeed() {
            return seed;
        }

        public void setSeed(int seed) {
            this.seed = seed;
        }

        public File getMaskFile() {
            return maskFile;
        }

        public void setMaskFile(File maskFile) {
            this.maskFile = maskFile;
        }

        public LibraryType getLibraryType() {
            return libraryType;
        }

        public void setLibraryType(LibraryType libraryType) {
            this.libraryType = libraryType;
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

        public boolean isNoUpdateCheck() {
            return noUpdateCheck;
        }

        public void setNoUpdateCheck(boolean noUpdateCheck) {
            this.noUpdateCheck = noUpdateCheck;
        }

        public String getLibraryNormMethod() {
            return libraryNormMethod;
        }

        public void setLibraryNormMethod(String libraryNormMethod) {
            this.libraryNormMethod = libraryNormMethod;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
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

            if (this.input != null) {
                pvp.put(params.getInput(), this.input.getAbsolutePath());
            }

            if (this.outputDir != null) {
                pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
            }

            if (this.threads != DEFAULT_THREADS && this.threads > 0) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.seed != DEFAULT_SEED) {
                pvp.put(params.getSeed(), Integer.toString(this.seed));
            }

            if (this.maskFile != null) {
                pvp.put(params.getMaskFile(), this.maskFile.getAbsolutePath());
            }

            if (this.libraryType != null) {
                pvp.put(params.getLibraryType(), this.libraryType.toArgString());
            }

            if (this.minIntronLength != DEFAULT_MIN_INTRON_LENGTH && this.minIntronLength > 0) {
                pvp.put(params.getMinIntronLength(), Integer.toString(this.minIntronLength));
            }

            if (this.maxIntronLength != DEFAULT_MAX_INTRON_LENGTH && this.maxIntronLength > 0) {
                pvp.put(params.getMaxIntronLength(), Integer.toString(this.maxIntronLength));
            }

            if (this.noUpdateCheck) {
                pvp.put(params.getNoUpdateCheck(), Boolean.toString(this.noUpdateCheck));
            }

            // Commented out for now as this seems to cause cufflinks to fail!  Debug later...
            /*if (this.libraryNormMethod != null && !this.libraryNormMethod.isEmpty()) {
                pvp.put(params.getLibraryNormMethod(), this.libraryNormMethod);
            } */


            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter input;
        private ConanParameter outputDir;
        private ConanParameter threads;
        private ConanParameter seed;
        private ConanParameter maskFile;
        private ConanParameter libraryType;
        private ConanParameter minIntronLength;
        private ConanParameter maxIntronLength;
        private ConanParameter noUpdateCheck;
        private ConanParameter libraryNormMethod;

        public Params() {

            this.input = new ParameterBuilder()
                    .argIndex(0)
                    .isOption(false)
                    .isOptional(false)
                    .description("The SAM/BAM file containing alignments for input")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.outputDir = new ParameterBuilder()
                    .shortName("o")
                    .longName("output-dir")
                    .isOptional(false)
                    .description("write all output files to this directory")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("p")
                    .longName("num-threads")
                    .description("number of threads used during analysis")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.seed = new ParameterBuilder()
                    .longName("seed")
                    .description("value of random number generator seed")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maskFile = new ParameterBuilder()
                    .shortName("M")
                    .longName("mask-file")
                    .description("ignore all alignment within transcripts in this file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.libraryType = new ParameterBuilder()
                    .longName("library-type")
                    .description("library prep used for input reads")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.minIntronLength = new ParameterBuilder()
                    .longName("min-intron-length")
                    .description("minimum intron size allowed in genome")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxIntronLength = new ParameterBuilder()
                    .shortName("I")
                    .longName("max-intron-length")
                    .description("ignore alignments with gaps longer than this")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.noUpdateCheck = new ParameterBuilder()
                    .longName("no-update-check")
                    .description("do not contact server to check for update availability")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

            this.libraryNormMethod = new ParameterBuilder()
                    .longName("library-norm-method")
                    .description("Method used to normalize library sizes (classic-fpkm)")
                    .create();
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getSeed() {
            return seed;
        }

        public ConanParameter getMaskFile() {
            return maskFile;
        }

        public ConanParameter getLibraryType() {
            return libraryType;
        }

        public ConanParameter getMinIntronLength() {
            return minIntronLength;
        }

        public ConanParameter getMaxIntronLength() {
            return maxIntronLength;
        }

        public ConanParameter getNoUpdateCheck() {
            return noUpdateCheck;
        }

        public ConanParameter getLibraryNormMethod() {
            return libraryNormMethod;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.input,
                    this.outputDir,
                    this.threads,
                    this.seed,
                    this.maskFile,
                    this.libraryType,
                    this.minIntronLength,
                    this.maxIntronLength,
                    this.noUpdateCheck,
                    this.libraryNormMethod
            };
        }
    }
}
