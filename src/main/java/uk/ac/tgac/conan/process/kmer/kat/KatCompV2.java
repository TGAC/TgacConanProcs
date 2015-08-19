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

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatCompV2 extends AbstractConanProcess {

    public static final String EXE = "kat";
    public static final String MODE = "comp";

    public KatCompV2() {
        this(new DefaultExecutorService(), new Args());
    }

    public KatCompV2(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public KatCompV2(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "KAT_Comp_V2.0";
    }


    public static class Args extends AbstractProcessArgs {

        private String input1;
        private String input2;
        private String input3;
        private String outputPrefix;
        private int threads;
        private long hashSize1;
        private long hashSize2;
        private long hashSize3;
        private boolean canonical1;
        private boolean canonical2;
        private boolean canonical3;
        private int kmer;

        public Args() {

            super(new Params());

            this.input1 = null;
            this.input2 = null;
            this.input3 = null;
            this.outputPrefix = "";
            this.threads = 1;
            this.hashSize1 = -1;
            this.hashSize2 = -1;
            this.hashSize3 = -1;
            this.canonical1 = false;
            this.canonical2 = false;
            this.canonical3 = false;
            this.kmer = -1;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public String getInput1() {
            return input1;
        }

        public void setInput1(String input1) {
            this.input1 = input1;
        }

        public String getInput2() {
            return input2;
        }

        public void setInput2(String input2) {
            this.input2 = input2;
        }

        public String getInput3() {
            return input3;
        }

        public void setInput3(String input3) {
            this.input3 = input3;
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

        public long getHashSize1() {
            return hashSize1;
        }

        public void setHashSize1(long hashSize1) {
            this.hashSize1 = hashSize1;
        }

        public long getHashSize2() {
            return hashSize2;
        }

        public void setHashSize2(long hashSize2) {
            this.hashSize2 = hashSize2;
        }

        public long getHashSize3() {
            return hashSize3;
        }

        public void setHashSize3(long hashSize3) {
            this.hashSize3 = hashSize3;
        }

        public boolean isCanonical1() {
            return canonical1;
        }

        public void setCanonical1(boolean canonical1) {
            this.canonical1 = canonical1;
        }

        public boolean isCanonical2() {
            return canonical2;
        }

        public void setCanonical2(boolean canonical2) {
            this.canonical2 = canonical2;
        }

        public boolean isCanonical3() {
            return canonical3;
        }

        public void setCanonical3(boolean canonical3) {
            this.canonical3 = canonical3;
        }

        public int getKmer() {
            return kmer;
        }

        public void setKmer(int kmer) {
            this.kmer = kmer;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.input1 != null) {
                pvp.put(params.getInput1(), this.input1);
            }

            if (this.input2 != null) {
                pvp.put(params.getInput2(), this.input2);
            }

            if (this.input3 != null) {
                pvp.put(params.getInput3(), this.input3);
            }

            if (this.outputPrefix != null && !this.outputPrefix.isEmpty()) {
                pvp.put(params.getOutputPrefix(), this.outputPrefix);
            }

            if (this.hashSize1 != -1) {
                pvp.put(params.getHashSize1(), Long.toString(this.hashSize1));
            }

            if (this.hashSize2 != -1) {
                pvp.put(params.getHashSize2(), Long.toString(this.hashSize2));
            }

            if (this.hashSize3 != -1) {
                pvp.put(params.getHashSize3(), Long.toString(this.hashSize3));
            }

            if (this.canonical1) {
                pvp.put(params.getCanonical1(), Boolean.toString(this.canonical1));
            }

            if (this.canonical2) {
                pvp.put(params.getCanonical2(), Boolean.toString(this.canonical2));
            }

            if (this.canonical3) {
                pvp.put(params.getCanonical3(), Boolean.toString(this.canonical3));
            }

            if (this.kmer != -1) {
                pvp.put(params.getKmer(), Integer.toString(this.kmer));
            }

            pvp.put(params.getThreads(), Integer.toString(this.threads));

            return pvp;
        }



        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            } else if (param.equals(params.getKmer())) {
                this.kmer = Integer.parseInt(value);
            } else if (param.equals(params.getOutputPrefix())) {
                this.outputPrefix = value;
            } else if (param.equals(params.getHashSize1())) {
                this.hashSize1 = Long.parseLong(value);
            } else if (param.equals(params.getHashSize2())) {
                this.hashSize2 = Long.parseLong(value);
            } else if (param.equals(params.getHashSize3())) {
                this.hashSize3 = Long.parseLong(value);
            } else if (param.equals(params.getCanonical1())) {
                this.canonical1 = Boolean.parseBoolean(value);
            } else if (param.equals(params.getCanonical2())) {
                this.canonical2 = Boolean.parseBoolean(value);
            } else if (param.equals(params.getCanonical3())) {
                this.canonical3 = Boolean.parseBoolean(value);
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getInput1())) {
                this.input1 = value;
            } else if (param.equals(params.getInput2())) {
                this.input2 = value;
            } else if (param.equals(params.getInput3())) {
                this.input3 = value;
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter input1;
        private ConanParameter input2;
        private ConanParameter input3;
        private ConanParameter outputPrefix;
        private ConanParameter threads;
        private ConanParameter hashSize1;
        private ConanParameter hashSize2;
        private ConanParameter hashSize3;
        private ConanParameter canonical1;
        private ConanParameter canonical2;
        private ConanParameter canonical3;
        private ConanParameter kmer;

        public Params() {

            this.input1 = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("First input")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.input2 = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(1)
                    .description("Second input")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.input3 = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(true)
                    .argIndex(2)
                    .description("Third input")
                    .argValidator(ArgValidator.OFF)
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

            this.hashSize1 = new ParameterBuilder()
                    .shortName("H")
                    .longName("hash_size_1")
                    .description("Size of hash 1")
                    .argValidator(ArgValidator.INTEGER)
                    .isOptional(true)
                    .create();

            this.hashSize2 = new ParameterBuilder()
                    .shortName("I")
                    .longName("hash_size_2")
                    .description("Size of hash 2")
                    .argValidator(ArgValidator.INTEGER)
                    .isOptional(true)
                    .create();

            this.hashSize3 = new ParameterBuilder()
                    .shortName("J")
                    .longName("hash_size_3")
                    .description("Size of hash 3")
                    .argValidator(ArgValidator.INTEGER)
                    .isOptional(true)
                    .create();

            this.kmer = new ParameterBuilder()
                    .shortName("m")
                    .longName("mer_len")
                    .description("The kmer length to use in the kmer hashes.  Larger values will provide more discriminating power between " +
                            "kmers but at the expense of additional memory and lower coverage.")
                    .argValidator(ArgValidator.DIGITS)
                    .isOptional(true)
                    .create();

            this.canonical1 = new ParameterBuilder()
                    .shortName("C")
                    .longName("canonical1")
                    .description("Treat kmers as canonical for input 1")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

            this.canonical2 = new ParameterBuilder()
                    .shortName("D")
                    .longName("canonical2")
                    .description("Treat kmers as canonical for input 2")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

            this.canonical3 = new ParameterBuilder()
                    .shortName("E")
                    .longName("canonical3")
                    .description("Treat kmers as canonical for input 3")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

        }

        public ConanParameter getInput1() {
            return input1;
        }

        public ConanParameter getInput2() {
            return input2;
        }

        public ConanParameter getInput3() {
            return input3;
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getHashSize1() {
            return hashSize1;
        }

        public ConanParameter getHashSize2() {
            return hashSize2;
        }

        public ConanParameter getHashSize3() {
            return hashSize3;
        }

        public ConanParameter getCanonical1() {
            return canonical1;
        }

        public ConanParameter getCanonical2() {
            return canonical2;
        }

        public ConanParameter getCanonical3() {
            return canonical3;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.input1,
                    this.input2,
                    this.input3,
                    this.outputPrefix,
                    this.threads,
                    this.hashSize1,
                    this.hashSize2,
                    this.hashSize3,
                    this.kmer,
                    this.canonical1,
                    this.canonical2,
                    this.canonical3
            };
        }

    }
}
