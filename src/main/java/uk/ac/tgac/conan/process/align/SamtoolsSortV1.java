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
public class SamtoolsSortV1 extends AbstractConanProcess {

    public static final String EXE = "samtools";

    public SamtoolsSortV1(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public SamtoolsSortV1(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
        this.setMode("sort");
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
        return "Samtools_Sort_V1.X";
    }

    public static enum Format {
        SAM,
        BAM,
        CRAM
    }

    public static class Args extends AbstractProcessArgs {

        private File input;
        private File output;

        private File tempFilePrefix;
        private boolean sortByName;
        private Format format;
        private int threads;

        public Args() {

            super(new Params());

            this.input = null;
            this.output = null;
            this.tempFilePrefix = null;
            this.sortByName = false;
            this.format = Format.BAM;
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

        public boolean isSortByName() {
            return sortByName;
        }

        public void setSortByName(boolean sortByName) {
            this.sortByName = sortByName;
        }

        public Format getFormat() {
            return format;
        }

        public void setFormat(Format format) {
            this.format = format;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public File getTempFilePrefix() {
            return tempFilePrefix;
        }

        public void setTempFilePrefix(File tempFilePrefix) {
            this.tempFilePrefix = tempFilePrefix;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getSortByName())) {
                this.sortByName = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getOutput())) {
                this.output = new File(value);
            }
            else if (param.equals(params.getFormat())) {
                this.format = Format.valueOf(value.trim().toUpperCase());
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getTempFilePrefix())) {
                this.tempFilePrefix = new File(value);
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

            if (this.sortByName) {
                pvp.put(params.getSortByName(), Boolean.toString(this.sortByName));
            }

            if (this.format != null) {
                pvp.put(params.getFormat(), this.format.toString().toLowerCase());
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.tempFilePrefix != null) {
                pvp.put(params.getTempFilePrefix(), this.tempFilePrefix.getAbsolutePath());
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {


        private ConanParameter input;
        private ConanParameter output;
        private ConanParameter sortByName;
        private ConanParameter threads;
        private ConanParameter format;
        private ConanParameter tempFilePrefix;

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

            this.sortByName = new ParameterBuilder()
                    .isFlag(true)
                    .shortName("n")
                    .description("Sort by read name")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("@")
                    .description("number of BAM compression threads [0]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.format = new ParameterBuilder()
                    .shortName("O")
                    .isOptional(false)
                    .description("Write output as FORMAT ('sam'/'bam'/'cram')")
                    .create();

            this.tempFilePrefix = new ParameterBuilder()
                    .shortName("T")
                    .isOptional(false)
                    .description("Write temporary files to PREFIX.nnnn.bam")
                    .argValidator(ArgValidator.PATH)
                    .create();
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getSortByName() {
            return sortByName;
        }

        public ConanParameter getFormat() {
            return format;
        }

        public ConanParameter getTempFilePrefix() {
            return tempFilePrefix;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.input,
                    this.output,
                    this.sortByName,
                    this.threads,
                    this.format,
                    this.tempFilePrefix
            };
        }
    }
}
