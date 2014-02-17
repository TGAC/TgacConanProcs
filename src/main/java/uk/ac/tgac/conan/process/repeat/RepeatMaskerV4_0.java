package uk.ac.tgac.conan.process.repeat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.tgac.conan.core.util.PathUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 16/01/14
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */
public class RepeatMaskerV4_0 extends AbstractConanProcess {

    public static final String EXE = "RepeatMasker";

    public RepeatMaskerV4_0(ConanProcessService conanProcessService) {
        this(conanProcessService, new Args());
    }

    public RepeatMaskerV4_0(ConanProcessService conanProcessService, Args args) {
        super(EXE, args, new Params());

        this.conanProcessService = conanProcessService;
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    public void init() {

        // Ensure the output directory exists
        if (!this.getArgs().outputDir.exists()) {
            this.getArgs().outputDir.mkdirs();
        }

        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        // Be careful here... if the user executes this twice then we'll end up adding these pre/post commands twice as well!!
        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @Override
    public String getName() {
        return "RepeatMasker_V4.0.X";
    }

    public static class Args extends AbstractProcessArgs {

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
        private File lib;
        private boolean lowComplexityOnly;
        private boolean interspersedOnly;
        private File outputDir;
        private boolean gff;
        private int frag;

        public Args() {

            super(new Params());

            this.input = null;
            this.engine = null;
            this.threads = 1;
            this.species = "";
            this.lib = null;
            this.lowComplexityOnly = false;
            this.interspersedOnly = false;
            this.outputDir = null;
            this.gff = false;
            this.frag = -1;
        }

        public Params getParams() {
            return (Params)this.params;
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

        public File getLib() {
            return lib;
        }

        public void setLib(File lib) {
            this.lib = lib;
        }

        public boolean isLowComplexityOnly() {
            return lowComplexityOnly;
        }

        public void setLowComplexityOnly(boolean lowComplexityOnly) {
            this.lowComplexityOnly = lowComplexityOnly;
        }

        public boolean isInterspersedOnly() {
            return interspersedOnly;
        }

        public void setInterspersedOnly(boolean interspersedOnly) {
            this.interspersedOnly = interspersedOnly;
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

        public File getOutputGffFile() {
            return new File(this.outputDir, this.input[0].getName() + ".out.gff");
        }

        public File getOutputTblFile() {
            return new File(this.outputDir, this.input[0].getName() + ".tbl");
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getEngine())) {
                this.engine = Engine.valueOf(value.toUpperCase());
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getSpecies())) {
                this.species = value;
            }
            else if (param.equals(params.getLib())) {
                this.lib = new File(value);
            }
            else if (param.equals(params.getLowOnly())) {
                this.lowComplexityOnly = true;
            }
            else if (param.equals(params.getIntOnly())) {
                this.interspersedOnly = true;
            }
            else if (param.equals(params.getOutputDir())) {
                this.outputDir = new File(value);
            }
            else if (param.equals(params.getGff())) {
                this.gff = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getFrag())) {
                this.frag = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getInput())) {
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

            Params params = this.getParams();

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

            if (this.lib != null) {
                pvp.put(params.getLib(), this.lib.getAbsolutePath());
            }

            if (this.lowComplexityOnly) {
                pvp.put(params.getLowOnly(), Boolean.toString(this.lowComplexityOnly));
            }

            if (this.interspersedOnly) {
                pvp.put(params.getIntOnly(), Boolean.toString(this.interspersedOnly));
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
        private ConanParameter lib;
        private ConanParameter lowOnly;
        private ConanParameter intOnly;
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

            this.lib = new ParameterBuilder()
                    .shortName("lib")
                    .description("Allows use of a custom library (e.g. from another species")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.lowOnly = new ParameterBuilder()
                    .shortName("noint")
                    .isFlag(true)
                    .description("Only masks low complex/simple repeats (no interspersed repeats)")
                    .create();

            this.intOnly = new ParameterBuilder()
                    .shortName("nolow")
                    .isFlag(true)
                    .description("Only masks interspersed repeats (no low complex/simple repeat)")
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

        public ConanParameter getLib() {
            return lib;
        }

        public ConanParameter getLowOnly() {
            return lowOnly;
        }

        public ConanParameter getIntOnly() {
            return intOnly;
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
                    this.lib,
                    this.lowOnly,
                    this.intOnly,
                    this.outputDir,
                    this.gff,
                    this.frag
            };
        }
    }

    public static class Report {

        private File rmTableFile;
        private String label;

        private int sequences;
        private int totalLength;
        private int totalLengthExcGaps;
        private double gc;
        private int basesMasked;
        private double percMasked;

        private int nbUnclassifiedInterspersed;
        private int lenUnclassifiedInterspersed;
        private double percUnclassifiedInterspersed;

        private int lenTotalInterspersed;
        private double percTotalInterspersed;

        private int nbSrna;
        private int lenSrna;
        private double percSrna;

        private int nbSatellites;
        private int lenSatellites;
        private double percSatellites;

        private int nbSimple;
        private int lenSimple;
        private double percSimple;

        private int nbLowComp;
        private int lenLowComp;
        private double percLowComp;


        public Report(File rmTableFile, String label) throws IOException {
            this.rmTableFile = rmTableFile;
            this.label = label;

            this.parse(rmTableFile);
        }

        private void parse(File rmTableFile) throws IOException {

            List<String> lines = FileUtils.readLines(rmTableFile);

            for(String line : lines) {
                String[] parts = line.split("\\s+");

                if (line.startsWith("sequences:")) {
                    this.sequences = Integer.parseInt(parts[1]);
                }
                else if (line.startsWith("total length:")) {
                    this.totalLength = Integer.parseInt(parts[2]);
                    this.totalLengthExcGaps = Integer.parseInt(parts[4].substring(1));
                }
                else if (line.startsWith("GC level:")) {
                    this.gc = Double.parseDouble(parts[2]);
                }
                else if (line.startsWith("bases masked:")) {
                    this.basesMasked = Integer.parseInt(parts[2]);
                    this.percMasked = Double.parseDouble(parts[5]);
                }
                else if (line.startsWith("Unclassified:")) {
                    this.nbUnclassifiedInterspersed = Integer.parseInt(parts[1]);
                    this.lenUnclassifiedInterspersed = Integer.parseInt(parts[2]);
                    this.percUnclassifiedInterspersed = Double.parseDouble(parts[4]);
                }
                else if (line.startsWith("Total ")) {
                    this.lenTotalInterspersed = Integer.parseInt(parts[3]);
                    this.percTotalInterspersed = Double.parseDouble(parts[5]);
                }
                else if (line.startsWith("Small RNA:")) {
                    this.nbSrna = Integer.parseInt(parts[2]);
                    this.lenSrna = Integer.parseInt(parts[3]);
                    this.percSrna = Double.parseDouble(parts[5]);
                }
                else if (line.startsWith("Satellites:")) {
                    this.nbSatellites = Integer.parseInt(parts[1]);
                    this.lenSatellites = Integer.parseInt(parts[2]);
                    this.percSatellites = Double.parseDouble(parts[4]);
                }
                else if (line.startsWith("Simple ")) {
                    this.nbSimple = Integer.parseInt(parts[2]);
                    this.lenSimple = Integer.parseInt(parts[3]);
                    this.percSimple = Double.parseDouble(parts[5]);
                }
                else if (line.startsWith("Low complexity:")) {
                    this.nbLowComp = Integer.parseInt(parts[2]);
                    this.lenLowComp = Integer.parseInt(parts[3]);
                    this.percLowComp = Double.parseDouble(parts[5]);
                }
            }
        }

        public File getRmTableFile() {
            return rmTableFile;
        }

        public String getLabel() {
            return label;
        }

        public int getSequences() {
            return sequences;
        }

        public int getTotalLength() {
            return totalLength;
        }

        public int getTotalLengthExcGaps() {
            return totalLengthExcGaps;
        }

        public double getGc() {
            return gc;
        }

        public int getBasesMasked() {
            return basesMasked;
        }

        public double getPercMasked() {
            return percMasked;
        }

        public int getNbUnclassifiedInterspersed() {
            return nbUnclassifiedInterspersed;
        }

        public int getLenUnclassifiedInterspersed() {
            return lenUnclassifiedInterspersed;
        }

        public double getPercUnclassifiedInterspersed() {
            return percUnclassifiedInterspersed;
        }

        public int getLenTotalInterspersed() {
            return lenTotalInterspersed;
        }

        public double getPercTotalInterspersed() {
            return percTotalInterspersed;
        }

        public int getNbSrna() {
            return nbSrna;
        }

        public int getLenSrna() {
            return lenSrna;
        }

        public double getPercSrna() {
            return percSrna;
        }

        public int getNbSatellites() {
            return nbSatellites;
        }

        public int getLenSatellites() {
            return lenSatellites;
        }

        public double getPercSatellites() {
            return percSatellites;
        }

        public int getNbSimple() {
            return nbSimple;
        }

        public int getLenSimple() {
            return lenSimple;
        }

        public double getPercSimple() {
            return percSimple;
        }

        public int getNbLowComp() {
            return nbLowComp;
        }

        public int getLenLowComp() {
            return lenLowComp;
        }

        public double getPercLowComp() {
            return percLowComp;
        }

        @Override
        public String toString() {

            String[] columns = new String[] {
                    this.label,
                    this.rmTableFile.getAbsolutePath(),
                    Integer.toString(this.sequences),
                    Integer.toString(this.totalLength),
                    Integer.toString(this.totalLengthExcGaps),
                    Double.toString(this.gc),
                    Integer.toString(this.basesMasked),
                    Double.toString(this.percMasked),
                    Integer.toString(this.nbUnclassifiedInterspersed),
                    Integer.toString(this.lenUnclassifiedInterspersed),
                    Double.toString(this.percUnclassifiedInterspersed),
                    Integer.toString(this.lenTotalInterspersed),
                    Double.toString(this.percTotalInterspersed),
                    Integer.toString(this.nbSrna),
                    Integer.toString(this.lenSrna),
                    Double.toString(this.percSrna),
                    Integer.toString(this.nbSatellites),
                    Integer.toString(this.lenSatellites),
                    Double.toString(this.percSatellites),
                    Integer.toString(this.nbSimple),
                    Integer.toString(this.lenSimple),
                    Double.toString(this.percSimple),
                    Integer.toString(this.nbLowComp),
                    Integer.toString(this.lenLowComp),
                    Double.toString(this.percLowComp)
            };

            return StringUtils.join(columns, "|");
        }


    }

    public static class CombinedReport extends ArrayList<Report> {

        public String header() {

            String[] columns = new String[] {
                    "label",
                    "file_path",
                    "sequences",
                    "total_length",
                    "total_length_exc_gaps",
                    "gc%",
                    "bases_masked",
                    "perc_masked",
                    "#_unclassified",
                    "len_unclassied",
                    "%_unclassified",
                    "len_total_interspersed",
                    "%_total_interspersed",
                    "#_small_rna",
                    "len_small_rna",
                    "%_small_rna",
                    "#_satellites",
                    "len_satellites",
                    "%_satellites",
                    "nb_simple",
                    "len_simple",
                    "%_simple",
                    "nb_low_complexity",
                    "len_low_complexity",
                    "%_low_complexity"
            };

            return StringUtils.join(columns, "|");
        }

        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();

            sb.append(this.header()).append("\n");

            for(Report report : this) {
                sb.append(report.toString()).append("\n");
            }

            return sb.toString();
        }

        public void save(File outputFile) throws IOException {

            FileUtils.write(outputFile, this.toString());
        }
    }
}
