package uk.ac.tgac.conan.process.kmer.kat;

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
 * Date: 20/11/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatGcpV1 extends AbstractConanProcess {


    public static final String EXE = "kat";
    public static final String MODE = "gcp";

    public KatGcpV1() {
        this(new Args());
    }

    public KatGcpV1(Args args) {
        super(EXE, args, new Params());
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "KAT_GCP_V1.0";
    }

    public static class Args extends AbstractProcessArgs {

        private File jellyfishHash;
        private String outputPrefix;
        private int threads;
        private int memoryMb;

        public Args() {

            super(new Params());

            this.jellyfishHash = null;
            this.outputPrefix = "";
            this.threads = 1;
            this.memoryMb = 0;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getJellyfishHash() {
            return jellyfishHash;
        }

        public void setJellyfishHash(File jellyfishHash) {
            this.jellyfishHash = jellyfishHash;
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

            if (this.jellyfishHash != null) {
                pvp.put(params.getInput(), this.jellyfishHash.getAbsolutePath());
            }

            if (this.outputPrefix != null && !this.outputPrefix.isEmpty()) {
                pvp.put(params.getOutputPrefix(), this.outputPrefix);
            }

            pvp.put(params.getThreads(), Integer.toString(this.threads));

            return pvp;
        }



        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            } else if (param.equals(params.getOutputPrefix())) {
                this.outputPrefix = value;
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getInput())) {
                this.jellyfishHash = new File(value);
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter input;
        private ConanParameter outputPrefix;
        private ConanParameter threads;

        public Params() {

            this.input = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .argValidator(ArgValidator.PATH)
                    .description("Jellyfish hash to analyse")
                    .create();

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .longName("output_prefix")
                    .argValidator(ArgValidator.PATH)
                    .description("Path prefix for files generated by this program (\"kat-gcp\").")
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .longName("threads")
                    .description("The number of threads to use (1).")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.input,
                    this.outputPrefix,
                    this.threads
            };
        }
    }

}
