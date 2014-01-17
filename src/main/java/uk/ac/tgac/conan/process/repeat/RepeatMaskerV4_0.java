package uk.ac.tgac.conan.process.repeat;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.tgac.conan.core.util.PathUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 16/01/14
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */
public class RepeatMaskerV4_0 extends AbstractConanProcess {

    public static final String EXE = "sortmerna";

    public RepeatMaskerV4_0() {
        this(new Args());
    }

    public RepeatMaskerV4_0(Args args) {
        super(EXE, args, new Params());
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "RepeatMasker_V4.0.X";
    }


    public static class Args extends AbstractProcessArgs {

        private Params params = new Params();

        public static enum Engine {
            CROSSMATCH,
            WUBLAST,
            ADDBLAST,
            DECYPHER,
            NCBI,
            HMMER;

            public String toArgString() {
                return this.name().toLowerCase();
            }
        }


        private File[] input;
        private Engine engine;
        private int threads;
        private String species;
        private File outputDir;
        private boolean gff;
        private int frag;

        public Args() {
            this.input = null;
            this.engine = null;
            this.threads = 1;
            this.species = "";
            this.outputDir = null;
            this.gff = false;
            this.frag = -1;
        }

        public File[] getInput() {
            return input;
        }

        public void setInput(File[] input) {
            this.input = input;
        }

        public Engine getEngine() {
            return engine;
        }

        public void setEngine(Engine engine) {
            this.engine = engine;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public String getSpecies() {
            return species;
        }

        public void setSpecies(String species) {
            this.species = species;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public boolean isGff() {
            return gff;
        }

        public void setGff(boolean gff) {
            this.gff = gff;
        }

        public int getFrag() {
            return frag;
        }

        public void setFrag(int frag) {
            this.frag = frag;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            if (param.equals(this.params.getEngine())) {
                this.engine = Engine.valueOf(value.toUpperCase());
            }
            else if (param.equals(this.params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getSpecies())) {
                this.species = value;
            }
            else if (param.equals(this.params.getOutputDir())) {
                this.outputDir = new File(value);
            }
            else if (param.equals(this.params.getGff())) {
                this.gff = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getFrag())) {
                this.frag = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            if (param.equals(this.params.getInput())) {
                this.input = PathUtils.splitPaths(value, " ");
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

            ParamMap pvp = new DefaultParamMap();

            if (this.input != null && this.input.length > 0) {
                pvp.put(params.getInput(), PathUtils.joinAbsolutePaths(this.input, " "));
            }

            if (this.engine != null) {
                pvp.put(params.getEngine(), this.engine.toArgString());
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.species != null && !this.species.isEmpty()) {
                pvp.put(params.getSpecies(), this.species);
            }

            if (this.outputDir != null) {
                pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
            }

            if (this.gff) {
                pvp.put(params.getGff(), Boolean.toString(this.gff));
            }

            if (this.frag != -1) {
                pvp.put(params.getFrag(), Integer.toString(this.frag));
            }

            return pvp;
        }


    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter input;
        private ConanParameter engine;
        private ConanParameter threads;
        private ConanParameter species;
        private ConanParameter outputDir;
        private ConanParameter gff;
        private ConanParameter frag;

        public Params() {

            this.input = new ParameterBuilder()
                    .argIndex(0)
                    .isOption(false)
                    .isOptional(false)
                    .description("seqfiles(s) in fasta format")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.engine = new ParameterBuilder()
                    .shortName("e")
                    //.longName("engine")
                    .description("[crossmatch|wublast|abblast|ncbi|hmmer|decypher] Use an alternate search engine to the default.")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("pa")
                    //.longName("parallel")
                    .description("The number of processors to use in parallel (only works for batch files or sequences over 50 kb)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.species = new ParameterBuilder()
                    .shortName("species")
                    .description("Specify the species or clade of the input sequence. The species name\n" +
                            "        must be a valid NCBI Taxonomy Database species name and be contained\n" +
                            "        in the RepeatMasker repeat database. Some examples are:\n" +
                            "\n" +
                            "          -species human\n" +
                            "          -species mouse\n" +
                            "          -species rattus\n" +
                            "          -species \"ciona savignyi\"\n" +
                            "          -species arabidopsis\n" +
                            "\n" +
                            "        Other commonly used species:\n" +
                            "\n" +
                            "        mammal, carnivore, rodentia, rat, cow, pig, cat, dog, chicken, fugu,\n" +
                            "        danio, \"ciona intestinalis\" drosophila, anopheles, elegans,\n" +
                            "        diatoaea, artiodactyl, arabidopsis, rice, wheat, and maize\n")
                    .create();

            this.outputDir = new ParameterBuilder()
                    .shortName("dir")
                    .description("Writes output to this directory (default is query file directory, will write to current directory).)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.gff = new ParameterBuilder()
                    .shortName("gff")
                    .isFlag(true)
                    .description("Creates an additional Gene Feature Finding format output")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.frag = new ParameterBuilder()
                    .shortName("frag")
                    .description("Maximum sequence length masked without fragmenting (default 60000,\n" +
                            "        300000 for DeCypher)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getEngine() {
            return engine;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getSpecies() {
            return species;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getGff() {
            return gff;
        }

        public ConanParameter getFrag() {
            return frag;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.input,
                    this.engine,
                    this.threads,
                    this.species,
                    this.outputDir,
                    this.gff,
                    this.frag
            };
        }
    }
}
