package uk.ac.tgac.conan.process.kmer.kat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatCompV1 extends AbstractConanProcess {

    public static final String EXE = "kat";
    public static final String MODE = "comp";

    public KatCompV1() {
        this(new Args());
    }

    public KatCompV1(Args args) {
        super(EXE, args, new Params());
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getCommand() throws ConanParameterException {

        ParamMap map = this.getProcessArgs().getArgMap();

        // Ensure all parameters are valid before we try to make a command
        map.validate(this.getProcessParams());

        List<ConanParameter> exclusions = new ArrayList<>();
        exclusions.add(new Params().getMemoryMb());


        List<String> commands = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(EXE).append(" ").append(MODE);

        // Add the options
        String options = map.buildOptionString(CommandLineFormat.POSIX, exclusions).trim();
        if (!options.isEmpty()) {
            sb.append(" ").append(options);
        }

        // Add the arguments
        String args = map.buildArgString().trim();
        if (!args.isEmpty()) {
            sb.append(" ").append(args);
        }

        commands.add(sb.toString().trim());

        String command = StringUtils.join(commands, "; ");

        return command;
    }

    @Override
    public String getName() {
        return "KAT_Comp_V1.0";
    }


    public static class Args extends AbstractProcessArgs {

        private File jellyfishHash1;
        private File jellyfishHash2;
        private File jellyfishHash3;
        private String outputPrefix;
        private int threads;
        private int memoryMb;

        public Args() {

            super(new Params());

            this.jellyfishHash1 = null;
            this.jellyfishHash2 = null;
            this.jellyfishHash3 = null;
            this.outputPrefix = "";
            this.threads = 1;
            this.memoryMb = 0;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getJellyfishHash1() {
            return jellyfishHash1;
        }

        public void setJellyfishHash1(File jellyfishHash1) {
            this.jellyfishHash1 = jellyfishHash1;
        }

        public File getJellyfishHash2() {
            return jellyfishHash2;
        }

        public void setJellyfishHash2(File jellyfishHash2) {
            this.jellyfishHash2 = jellyfishHash2;
        }

        public File getJellyfishHash3() {
            return jellyfishHash3;
        }

        public void setJellyfishHash3(File jellyfishHash3) {
            this.jellyfishHash3 = jellyfishHash3;
        }

        public String getOutputPrefix() {
            return outputPrefix;
        }

        public void setOutputPrefix(String outputPrefix) {
            this.outputPrefix = outputPrefix;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public int getMemoryMb() {
            return memoryMb;
        }

        public void setMemoryMb(int memoryMb) {
            this.memoryMb = memoryMb;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.jellyfishHash1 != null) {
                pvp.put(params.getJellyfishHash1(), this.jellyfishHash1.getAbsolutePath());
            }

            if (this.jellyfishHash2 != null) {
                pvp.put(params.getJellyfishHash2(), this.jellyfishHash2.getAbsolutePath());
            }

            if (this.jellyfishHash3 != null) {
                pvp.put(params.getJellyfishHash3(), this.jellyfishHash3.getAbsolutePath());
            }

            if (this.outputPrefix != null && !this.outputPrefix.isEmpty()) {
                pvp.put(params.getOutputPrefix(), this.outputPrefix);
            }

            pvp.put(params.getThreads(), Integer.toString(this.threads));
            pvp.put(params.getMemoryMb(), Integer.toString(this.memoryMb));

            return pvp;
        }



        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            } else if (param.equals(params.getMemoryMb())) {
                this.memoryMb = Integer.parseInt(value);
            } else if (param.equals(params.getOutputPrefix())) {
                this.outputPrefix = value;
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getJellyfishHash1())) {
                this.jellyfishHash1 = new File(value);
            } else if (param.equals(params.getJellyfishHash2())) {
                this.jellyfishHash2 = new File(value);
            } else if (param.equals(params.getJellyfishHash3())) {
                this.jellyfishHash3 = new File(value);
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter jellyfishHash1;
        private ConanParameter jellyfishHash2;
        private ConanParameter jellyfishHash3;
        private ConanParameter outputPrefix;
        private ConanParameter threads;
        private ConanParameter memoryMb;

        public Params() {

            this.jellyfishHash1 = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("First jellyfish hash")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.jellyfishHash2 = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(1)
                    .description("Second jellyfish hash")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.jellyfishHash3 = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(true)
                    .argIndex(2)
                    .description("Third jellyfish hash")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .longName("output")
                    .description("Output prefix")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .longName("threads")
                    .description("Number of threads (1)")
                    .argValidator(ArgValidator.DIGITS)
                    .isOptional(true)
                    .create();

            this.memoryMb = new ParameterBuilder()
                    .longName("memory")
                    .description("Amount of memory to request from the scheduler")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getJellyfishHash1() {
            return jellyfishHash1;
        }

        public ConanParameter getJellyfishHash2() {
            return jellyfishHash2;
        }

        public ConanParameter getJellyfishHash3() {
            return jellyfishHash3;
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMemoryMb() {
            return memoryMb;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.jellyfishHash1,
                    this.jellyfishHash2,
                    this.jellyfishHash3,
                    this.outputPrefix,
                    this.threads,
                    this.memoryMb
            };
        }

    }
}
