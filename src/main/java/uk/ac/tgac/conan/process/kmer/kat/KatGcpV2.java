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
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.DefaultExecutorService;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatGcpV2 extends AbstractConanProcess {


    public static final String EXE = "kat";
    public static final String MODE = "gcp";

    public KatGcpV2() {
        this(new DefaultExecutorService(), new Args());
    }

    public KatGcpV2(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public KatGcpV2(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "KAT_GCP_V2.0";
    }

    public static class Args extends AbstractProcessArgs {

        private File[] inputFiles;
        private boolean canonical;
        private long hashSize;
        private int kmer;
        private String outputPrefix;
        private int threads;
        private int memoryMb;

        public Args() {

            super(new Params());

            this.inputFiles = null;
            this.canonical = false;
            this.hashSize = -1;
            this.kmer = 27;
            this.outputPrefix = "";
            this.threads = 1;
            this.memoryMb = 0;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File[] getInputFiles() {
            return inputFiles;
        }

        public void setInputFiles(File[] inputFiles) {
            this.inputFiles = inputFiles;
        }

        public boolean isCanonical() {
            return canonical;
        }

        public void setCanonical(boolean canonical) {
            this.canonical = canonical;
        }

        public long getHashSize() {
            return hashSize;
        }

        public void setHashSize(long hashSize) {
            this.hashSize = hashSize;
        }

        public int getKmer() {
            return kmer;
        }

        public void setKmer(int kmer) {
            this.kmer = kmer;
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

            if (this.inputFiles != null) {

                StringJoiner sj = new StringJoiner(" ");
                for(File f : this.inputFiles) {
                    sj.add(f.getAbsolutePath());
                }

                pvp.put(params.getInput(), sj.toString());
            }

            if (this.outputPrefix != null && !this.outputPrefix.isEmpty()) {
                pvp.put(params.getOutputPrefix(), this.outputPrefix);
            }

            pvp.put(params.getThreads(), Integer.toString(this.threads));

            if (this.hashSize != -1) {
                pvp.put(params.getHashSize(), Long.toString(this.hashSize));
            }

            if (this.canonical) {
                pvp.put(params.getCanonical(), Boolean.toString(this.canonical));
            }

            pvp.put(params.getKmer(), Integer.toString(this.kmer));

            return pvp;
        }



        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            } else if (param.equals(params.getOutputPrefix())) {
                this.outputPrefix = value;
            } else if (param.equals(params.getCanonical())) {
                this.canonical = Boolean.parseBoolean(value);
            } else if (param.equals(params.getHashSize())) {
                this.hashSize = Long.parseLong(value);
            } else if (param.equals(params.getKmer())) {
                this.kmer = Integer.parseInt(value);
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getInput())) {

                String[] files = value.split(" ");
                this.inputFiles = new File[files.length];
                for(int i = 0; i < files.length; i++) {
                    this.inputFiles[i] = new File(files[i]);
                }
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter input;
        private ConanParameter outputPrefix;
        private ConanParameter threads;
        private ConanParameter canonical;
        private ConanParameter hashSize;
        private ConanParameter kmer;

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

            this.canonical = new ParameterBuilder()
                    .shortName("C")
                    .longName("canonical")
                    .description("Whether the jellyfish hashes contains K-mers produced for both strands.  If this is not set to the " +
                            "same value as was produced during jellyfish counting then output will be unpredictable.")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.hashSize = new ParameterBuilder()
                    .shortName("H")
                    .longName("hash_size")
                    .description("If kmer counting is required for the input, then use this value as the hash size.  If this hash size is " +
                            "not large enough for your dataset then the default behaviour is to double the size of the hash and recount, which will increase runtime and memory usage.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.kmer = new ParameterBuilder()
                    .shortName("m")
                    .longName("mer_len")
                    .description("The kmer length to use in the kmer hashes.  Larger values will provide more discriminating power between kmers but at the expense of additional memory and lower coverage.")
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

        public ConanParameter getCanonical() {
            return canonical;
        }

        public ConanParameter getHashSize() {
            return hashSize;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.input,
                    this.outputPrefix,
                    this.threads,
                    this.canonical,
                    this.hashSize,
                    this.kmer
            };
        }
    }

}
