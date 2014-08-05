package uk.ac.tgac.conan.process.rnaasm;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;
import java.io.IOException;

/**
 * Created by maplesod on 25/07/14.
 */
public class TransdecoderV2013 extends AbstractConanProcess {

    protected static final String NAME = "Transdecoder_V2013";
    protected static final String EXE = "TransDecoder";

    public TransdecoderV2013() {
        this(null);
    }

    public TransdecoderV2013(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public TransdecoderV2013(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);
        this.setFormat(CommandLineFormat.POSIX_SPACE);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    public void setup() {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());

        if (!this.getArgs().getTranscripts().getAbsolutePath().equals(this.getArgs().getTranscriptsInput().getAbsolutePath())) {
            this.addPreCommand("ln -s -f " + this.getArgs().getTranscripts().getAbsolutePath() + " " +
                    this.getArgs().getTranscriptsInput().getAbsolutePath());
        }

        this.addPostCommand("cd " + pwd);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_THREADS = 2;
        public static final int DEFAULT_MIN_PROTEIN_LENGTH = 100;

        private File outputDir;
        private File transcripts;
        private boolean strandSpecific;
        private int threads;
        private int minProteinLength;

        public Args() {
            super(new Params());

            this.outputDir = null;
            this.transcripts = null;
            this.strandSpecific = false;
            this.threads = DEFAULT_THREADS;
            this.minProteinLength = DEFAULT_MIN_PROTEIN_LENGTH;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public File getTranscripts() {
            return transcripts;
        }

        public void setTranscripts(File transcripts) {
            this.transcripts = transcripts;
        }

        public boolean isStrandSpecific() {
            return strandSpecific;
        }

        public void setStrandSpecific(boolean strandSpecific) {
            this.strandSpecific = strandSpecific;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public int getMinProteinLength() {
            return minProteinLength;
        }

        public void setMinProteinLength(int minProteinLength) {
            this.minProteinLength = minProteinLength;
        }

        public File getTranscriptsInput() {
            return new File(this.outputDir, "transcripts.fasta");
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getTranscripts())) {
                this.transcripts = new File(value);
            }
            else if (param.equals(params.getStrandSpecific())) {
                this.strandSpecific = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinProteinLength())) {
                this.minProteinLength = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void parseCommandLine(CommandLine commandLine) {

        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            // Forcing use of the symbolic link for input
            if (this.getTranscriptsInput() != null) {
                pvp.put(params.getTranscripts(), this.getTranscriptsInput().getAbsolutePath());
            }

            if (this.strandSpecific) {
                pvp.put(params.getStrandSpecific(), Boolean.toString(this.strandSpecific));
            }

            if (this.threads > 0 && this.threads != DEFAULT_THREADS) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.minProteinLength > 0 && this.minProteinLength != DEFAULT_MIN_PROTEIN_LENGTH) {
                pvp.put(params.getMinProteinLength(), Integer.toString(this.minProteinLength));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter transcripts;
        private ConanParameter strandSpecific;
        private ConanParameter threads;
        private ConanParameter minProteinLength;

        public Params() {

            this.transcripts = new ParameterBuilder()
                    .shortName("t")
                    .isOptional(false)
                    .description("Assembled transcripts in fasta format")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.strandSpecific = new ParameterBuilder()
                    .shortName("S")
                    .isFlag(true)
                    .description("strand-specific (only analyzes top strand)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.threads = new ParameterBuilder()
                    .longName("CPU")
                    .description("number of threads to use; (default: 2)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minProteinLength = new ParameterBuilder()
                    .shortName("m")
                    .description("minimum protein length (default: 100)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getTranscripts() {
            return transcripts;
        }

        public ConanParameter getStrandSpecific() {
            return strandSpecific;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMinProteinLength() {
            return minProteinLength;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.transcripts,
                    this.strandSpecific,
                    this.threads,
                    this.minProteinLength
            };
        }
    }
}
