package uk.ac.tgac.conan.process.align;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
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
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 15/01/14
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class SamtoolsViewV0_1 extends AbstractConanProcess {

    public static final String EXE = "samtools";

    public SamtoolsViewV0_1(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public SamtoolsViewV0_1(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
        this.setMode("view");
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getCommand() throws ConanParameterException {
        return super.getCommand(CommandLineFormat.POSIX_SPACE);
    }


    @Override
    public String getName() {
        return "Samtools_View_V0.1.X";
    }

    public static class Args extends AbstractProcessArgs {

        private File input;
        private File output;

        private boolean outputBam;
        private boolean inputSam;

        public Args() {

            super(new Params());

            this.input = null;
            this.output = null;
            this.outputBam = false;
            this.inputSam = false;
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

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        public boolean isOutputBam() {
            return outputBam;
        }

        public void setOutputBam(boolean outputBam) {
            this.outputBam = outputBam;
        }

        public boolean isInputSam() {
            return inputSam;
        }

        public void setInputSam(boolean inputSam) {
            this.inputSam = inputSam;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutputBam())) {
                this.outputBam = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getInputSam())) {
                this.inputSam = Boolean.parseBoolean(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getInput())) {
                this.input = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setRedirectFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutput())) {
                this.output = new File(value);
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

            if (this.input != null) {
                pvp.put(params.getInput(), this.input.getAbsolutePath());
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            if (this.inputSam) {
                pvp.put(params.getInputSam(), Boolean.toString(this.inputSam));
            }

            if (this.outputBam) {
                pvp.put(params.getOutputBam(), Boolean.toString(this.outputBam));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {


        private ConanParameter input;
        private ConanParameter output;
        private ConanParameter inputSam;
        private ConanParameter outputBam;

        public Params() {

            this.input = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("Input file to convert, either SAM or BAM format")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .type(DefaultConanParameter.ParamType.REDIRECTION)
                    .description("The output file, will be either BAM or SAM format depending on input and selected options")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.inputSam = new ParameterBuilder()
                    .isFlag(true)
                    .shortName("S")
                    .description("input is SAM")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.outputBam = new ParameterBuilder()
                    .isFlag(true)
                    .shortName("b")
                    .description("output BAM")
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getInputSam() {
            return inputSam;
        }

        public ConanParameter getOutputBam() {
            return outputBam;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                 input,
                 output,
                 inputSam,
                 outputBam
            };
        }
    }
}
