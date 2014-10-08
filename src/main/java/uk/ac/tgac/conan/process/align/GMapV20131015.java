package uk.ac.tgac.conan.process.align;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;

/**
 * Created by maplesod on 18/02/14.
 */
public class GMapV20131015 extends AbstractConanProcess {

    public static final String EXE = "gmap";

    public GMapV20131015(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public GMapV20131015(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "GMap_V20131015";
    }

    public static enum OutputFormat {

        PSL,
        GFF3_GENE,
        GFF3_MATCH_CDNA,
        GFF3_MATCH_EST,
        SPLICESITES,
        INTRONS,
        MAP_EXONS,
        MAP_RANGES,
        COORDS,
        SAMPE,
        SAMSE;

        public static OutputFormat parse(String value) {
            return OutputFormat.valueOf(value.toUpperCase());
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_THREADS = 1;
        public static final int DEFAULT_MAX_INTRON_LENGTH = 1000000;
        public static final int DEFAULT_MAX_TOTAL_INTRON_LENGTH = 2400000;
        public static final int DEFAULT_MAX_PATHS = 5;
        public static final double DEFAULT_MIN_TRIMMED_COVERAGE = 0.0;
        public static final double DEFAULT_MIN_IDENTITY = 0.0;


        private File genomeDir;
        private String genomeDB;
        private File query;
        private File output;
        private boolean showAlignments;
        private OutputFormat outputFormat;
        private int threads;
        private int maxIntronLength;
        private int maxTotalIntronLength;
        private int maxPaths;
        private boolean ordered;
        private double minTrimmedCoverage;
        private double minIdentity;

        public Args() {

            super(new Params());

            this.genomeDir = null;
            this.genomeDB = "";
            this.query = null;
            this.output = null;
            this.showAlignments = false;
            this.outputFormat = null;
            this.threads = DEFAULT_THREADS;
            this.maxIntronLength = DEFAULT_MAX_INTRON_LENGTH;
            this.maxTotalIntronLength = DEFAULT_MAX_TOTAL_INTRON_LENGTH;
            this.maxPaths = DEFAULT_MAX_PATHS;
            this.ordered = false;
            this.minTrimmedCoverage = DEFAULT_MIN_TRIMMED_COVERAGE;
            this.minIdentity = DEFAULT_MIN_IDENTITY;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getGenomeDir() {
            return genomeDir;
        }

        public void setGenomeDir(File genomeDir) {
            this.genomeDir = genomeDir;
        }

        public String getGenomeDB() {
            return genomeDB;
        }

        public void setGenomeDB(String genomeDB) {
            this.genomeDB = genomeDB;
        }

        public File getQuery() {
            return query;
        }

        public void setQuery(File query) {
            this.query = query;
        }

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        public boolean isShowAlignments() {
            return showAlignments;
        }

        public void setShowAlignments(boolean showAlignments) {
            this.showAlignments = showAlignments;
        }

        public OutputFormat getOutputFormat() {
            return outputFormat;
        }

        public void setOutputFormat(OutputFormat outputFormat) {
            this.outputFormat = outputFormat;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public int getMaxIntronLength() {
            return maxIntronLength;
        }

        public void setMaxIntronLength(int maxIntronLength) {
            this.maxIntronLength = maxIntronLength;
        }

        public int getMaxTotalIntronLength() {
            return maxTotalIntronLength;
        }

        public void setMaxTotalIntronLength(int maxTotalIntronLength) {
            this.maxTotalIntronLength = maxTotalIntronLength;
        }

        public int getMaxPaths() {
            return maxPaths;
        }

        public void setMaxPaths(int maxPaths) {
            this.maxPaths = maxPaths;
        }

        public boolean isOrdered() {
            return ordered;
        }

        public void setOrdered(boolean ordered) {
            this.ordered = ordered;
        }

        public double getMinTrimmedCoverage() {
            return minTrimmedCoverage;
        }

        public void setMinTrimmedCoverage(double minTrimmedCoverage) {
            this.minTrimmedCoverage = minTrimmedCoverage;
        }

        public double getMinIdentity() {
            return minIdentity;
        }

        public void setMinIdentity(double minIdentity) {
            this.minIdentity = minIdentity;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getGenomeDir())) {
                this.genomeDir = new File(value);
            }
            else if (param.equals(params.getGenomeDB())) {
                this.genomeDB = value;
            }
            else if (param.equals(params.getShowAlignments())) {
                this.showAlignments = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getOutputFormat())) {
                this.outputFormat = OutputFormat.parse(value);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxIntronLength())) {
                this.maxIntronLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxTotalIntronLength())) {
                this.maxTotalIntronLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxPaths())) {
                this.maxPaths = Integer.parseInt(value);
            }
            else if (param.equals(params.getOrdered())) {
                this.ordered = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getMinTrimmedCoverage())) {
                this.minTrimmedCoverage = Double.parseDouble(value);
            }
            else if (param.equals(params.getMinIdentity())) {
                this.minIdentity = Double.parseDouble(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getQuery())) {
                this.query = new File(value);
            }
            else if (param.equals(params.getOutput())) {
                this.output = new File(value);
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
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.genomeDir != null) {
                pvp.put(params.getGenomeDir(), this.genomeDir.getAbsolutePath());
            }

            if (this.genomeDB != null && !this.genomeDB.isEmpty()) {
                pvp.put(params.getGenomeDB(), this.genomeDB);
            }

            if (this.query != null) {
                pvp.put(params.getQuery(), this.query.getAbsolutePath());
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            if (this.showAlignments) {
                pvp.put(params.getShowAlignments(), Boolean.toString(this.showAlignments));
            }

            if (this.outputFormat != null) {
                pvp.put(params.getOutputFormat(), this.outputFormat.toString());
            }

            if (this.threads != DEFAULT_THREADS) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.maxIntronLength != DEFAULT_MAX_INTRON_LENGTH) {
                pvp.put(params.getMaxIntronLength(), Integer.toString(this.maxIntronLength));
            }

            if (this.maxTotalIntronLength != DEFAULT_MAX_TOTAL_INTRON_LENGTH) {
                pvp.put(params.getMaxTotalIntronLength(), Integer.toString(this.maxTotalIntronLength));
            }

            if (this.maxPaths != DEFAULT_MAX_PATHS) {
                pvp.put(params.getMaxPaths(), Integer.toString(this.maxPaths));
            }

            if (this.ordered) {
                pvp.put(params.getOrdered(), Boolean.toString(this.ordered));
            }

            if (this.minTrimmedCoverage != DEFAULT_MIN_TRIMMED_COVERAGE) {
                pvp.put(params.getMinTrimmedCoverage(), Double.toString(this.minTrimmedCoverage));
            }

            if (this.minIdentity != DEFAULT_MIN_IDENTITY) {
                pvp.put(params.getMinIdentity(), Double.toString(this.minIdentity));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter genomeDir;
        private ConanParameter genomeDB;
        private ConanParameter query;
        private ConanParameter output;
        private ConanParameter showAlignments;
        private ConanParameter outputFormat;
        private ConanParameter threads;
        private ConanParameter maxIntronLength;
        private ConanParameter maxTotalIntronLength;
        private ConanParameter maxPaths;
        private ConanParameter ordered;
        private ConanParameter minTrimmedCoverage;
        private ConanParameter minIdentity;


        public Params() {

            this.genomeDir = new ParameterBuilder()
                    .shortName("D")
                    .longName("dir")
                    .isOptional(false)
                    .description("Genome directory")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.genomeDB = new ParameterBuilder()
                    .shortName("d")
                    .longName("db")
                    .isOptional(false)
                    .description("Genome database")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.query = new ParameterBuilder()
                    .type(DefaultConanParameter.ParamType.ARGUMENT)
                    .isOptional(false)
                    .argIndex(0)
                    .description("Query file containing content to align")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .isOptional(false)
                    .type(DefaultConanParameter.ParamType.REDIRECTION)
                    .description("Standard output redirected to file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.showAlignments = new ParameterBuilder()
                    .isFlag(true)
                    .shortName("A")
                    .longName("align")
                    .description("Show alignments")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.outputFormat = new ParameterBuilder()
                    .shortName("f")
                    .longName("format")
                    .description("Other format for output")
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .longName("nthreads")
                    .description("Number of worker threads")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxIntronLength = new ParameterBuilder()
                    .shortName("K")
                    .longName("intronlength")
                    .description("Max length for one internal intron (default 1000000)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxTotalIntronLength = new ParameterBuilder()
                    .shortName("L")
                    .longName("totallength")
                    .description("Max total intron length (default 2400000)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxPaths = new ParameterBuilder()
                    .shortName("n")
                    .longName("npaths")
                    .description("Maximum number of paths to show (default 5).  If set to 1, GMAP will not report " +
                            "chimeric alignments, since those imply two paths.  If you want a single alignment plus " +
                            "chimeric alignments, then set this to be 0.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.ordered = new ParameterBuilder()
                    .shortName("O")
                    .longName("ordered")
                    .description("Print output in same order as input (relevant only if there is more than one worker thread)")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.minTrimmedCoverage = new ParameterBuilder()
                    .longName("min-trimmed-coverage")
                    .description("Do not print alignments with trimmed coverage less this value (default=0.0, which " +
                            "means no filtering).  Note that chimeric alignments will be output regardless of this filter")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.minIdentity = new ParameterBuilder()
                    .longName("min-identity")
                    .description("Do not print alignments with identity less this value (default=0.0, which means no " +
                            "filtering).  Note that chimeric alignments will be output regardless of this filter")
                    .argValidator(ArgValidator.FLOAT)
                    .create();
        }

        public ConanParameter getGenomeDir() {
            return genomeDir;
        }

        public ConanParameter getGenomeDB() {
            return genomeDB;
        }

        public ConanParameter getQuery() {
            return query;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getShowAlignments() {
            return showAlignments;
        }

        public ConanParameter getOutputFormat() {
            return outputFormat;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMaxIntronLength() {
            return maxIntronLength;
        }

        public ConanParameter getMaxTotalIntronLength() {
            return maxTotalIntronLength;
        }

        public ConanParameter getMaxPaths() {
            return maxPaths;
        }

        public ConanParameter getOrdered() {
            return ordered;
        }

        public ConanParameter getMinTrimmedCoverage() {
            return minTrimmedCoverage;
        }

        public ConanParameter getMinIdentity() {
            return minIdentity;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.genomeDir,
                    this.genomeDB,
                    this.query,
                    this.output,
                    this.showAlignments,
                    this.outputFormat,
                    this.threads,
                    this.maxIntronLength,
                    this.maxTotalIntronLength,
                    this.maxPaths,
                    this.ordered,
                    this.minTrimmedCoverage,
                    this.minIdentity
            };
        }
    }

}
