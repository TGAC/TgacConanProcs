package uk.ac.tgac.conan.process.align;

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

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 15/01/14
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class SamtoolsViewV1 extends AbstractConanProcess {

    public static final String EXE = "samtools";

    public SamtoolsViewV1(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public SamtoolsViewV1(ConanExecutorService conanExecutorService, Args args) {
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
        return "Samtools_View_V1.X";
    }

    public static class Args extends AbstractProcessArgs {

        private File input;
        private File output;

        private boolean outputBam;
        private int threads;

        public Args() {

            super(new Params());

            this.input = null;
            this.output = null;
            this.outputBam = false;
            this.threads = 1;
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

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutputBam())) {
                this.outputBam = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getOutput())) {
                this.output = new File(value);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
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

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            if (this.outputBam) {
                pvp.put(params.getOutputBam(), Boolean.toString(this.outputBam));
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {


        private ConanParameter input;
        private ConanParameter output;
        private ConanParameter outputBam;
        private ConanParameter threads;

        public Params() {

            this.input = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("Input file to convert, either SAM or BAM format")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .shortName("o")
                    .isOptional(false)
                    .description("The output file, will be either BAM or SAM format depending on input and selected options")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.outputBam = new ParameterBuilder()
                    .isFlag(true)
                    .shortName("b")
                    .description("output BAM")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("@")
                    .description("number of BAM compression threads [0]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getOutputBam() {
            return outputBam;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                 input,
                 output,
                 outputBam,
                 threads
            };
        }
    }
}
