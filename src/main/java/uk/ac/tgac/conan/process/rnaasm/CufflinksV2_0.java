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

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 17/01/14
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
public class CufflinksV2_0 extends AbstractConanProcess {

    public static final String EXE = "cufflinks";

    public CufflinksV2_0() {
        this(new Args());
    }

    public CufflinksV2_0(Args args) {
        super(EXE, args, new Params());
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Cufflinks_V2.0.X";
    }

    public static class Args extends AbstractProcessArgs {

        private static final int DEFAULT_THREADS = 1;
        private static final int DEFAULT_SEED = 0;
        private static final LibraryType DEFAULT_LIBRARY_TYPE = LibraryType.FR_UNSTRANDED;
        private static final int DEFAULT_MIN_INTRON_LENGTH = 50;
        private static final int DEFAULT_MAX_INTRON_LENGTH = 300000;


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
        }

        private File inputSam;
        private File outputDir;
        private int threads;
        private int seed;
        private File maskFile;
        private LibraryType libraryType;
        private int minIntronLength;
        private int maxIntronLength;


        public Args() {

            super(new Params());

            this.inputSam = null;
            this.outputDir = null;
            this.threads = DEFAULT_THREADS;
            this.seed = DEFAULT_SEED;
            this.maskFile = null;
            this.libraryType = DEFAULT_LIBRARY_TYPE;
            this.minIntronLength = DEFAULT_MIN_INTRON_LENGTH;
            this.maxIntronLength = DEFAULT_MAX_INTRON_LENGTH;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getInputSam() {
            return inputSam;
        }

        public void setInputSam(File inputSam) {
            this.inputSam = inputSam;
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

            if (this.inputSam != null) {
                pvp.put(params.getInputSam(), this.inputSam.getAbsolutePath());
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



            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter inputSam;
        private ConanParameter outputDir;
        private ConanParameter threads;
        private ConanParameter seed;
        private ConanParameter maskFile;
        private ConanParameter libraryType;
        private ConanParameter minIntronLength;
        private ConanParameter maxIntronLength;

        public Params() {

            this.inputSam = new ParameterBuilder()
                    .argIndex(0)
                    .isOption(false)
                    .isOptional(false)
                    .description("The SAM file containing alignments for input")
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
        }

        public ConanParameter getInputSam() {
            return inputSam;
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

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.inputSam,
                    this.outputDir,
                    this.threads,
                    this.seed,
                    this.maskFile,
                    this.libraryType,
                    this.minIntronLength,
                    this.maxIntronLength
            };
        }
    }
}
