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

import java.io.File;

/**
 * Created by maplesod on 25/07/14.
 */
public class TransdecoderV2 extends AbstractConanProcess {

    protected static final String NAME = "Transdecoder_V2";
    protected static final String EXE = "TransDecoder.LongOrfs";

    public TransdecoderV2() {
        this(null);
    }

    public TransdecoderV2(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public TransdecoderV2(ConanExecutorService ces, Args args) {
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

        // Just slip this in here for now for simplicity.  Maybe add ability to control other Predict options later.
        this.addPostCommand("TransDecoder.Predict -t " + this.getArgs().getTranscriptsInput().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }


    @Override
    public String getName() {
        return NAME;
    }

    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_MIN_PROTEIN_LENGTH = 100;

        private File outputDir;
        private File transcripts;
        private boolean strandSpecific;
        private int minProteinLength;

        public Args() {
            super(new Params());

            this.outputDir = null;
            this.transcripts = null;
            this.strandSpecific = false;
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

            if (this.minProteinLength > 0 && this.minProteinLength != DEFAULT_MIN_PROTEIN_LENGTH) {
                pvp.put(params.getMinProteinLength(), Integer.toString(this.minProteinLength));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter transcripts;
        private ConanParameter strandSpecific;
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

        public ConanParameter getMinProteinLength() {
            return minProteinLength;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.transcripts,
                    this.strandSpecific,
                    this.minProteinLength
            };
        }
    }
}
