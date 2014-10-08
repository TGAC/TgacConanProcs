package uk.ac.tgac.conan.process.align;

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

import java.io.File;

/**
 * Protein 2 Protein BLAST search
 */
public class BlastPV2 extends AbstractConanProcess {

    private static final String NAME = "BlastP_V2";
    private static final String EXE = "blastp";

    public BlastPV2() {
        this(null);
    }

    public BlastPV2(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public BlastPV2(ConanExecutorService ces, AbstractProcessArgs args) {
        super(EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static class Args extends AbstractProcessArgs {

        public static final double DEFAULT_EVALUE = 10.0;
        public static final String DEFAULT_OUT_FMT = "0";
        public static final int DEFAULT_MAX_TARGET_SEQS = 500;
        public static final int DEFAULT_THREADS = 1;

        private File query;
        private File db;
        private File out;
        private double eValue;
        private String outFormat;
        private int maxTargetSeqs;
        private boolean ungapped;
        private int numThreads;

        public Args() {
            super(new Params());

            this.query = null;
            this.db = null;
            this.out = null;
            this.eValue = DEFAULT_EVALUE;
            this.outFormat = DEFAULT_OUT_FMT;
            this.maxTargetSeqs = DEFAULT_MAX_TARGET_SEQS;
            this.ungapped = false;
            this.numThreads = DEFAULT_THREADS;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getQuery() {
            return query;
        }

        public void setQuery(File query) {
            this.query = query;
        }

        public File getDb() {
            return db;
        }

        public void setDb(File db) {
            this.db = db;
        }

        public File getOut() {
            return out;
        }

        public void setOut(File out) {
            this.out = out;
        }

        public double geteValue() {
            return eValue;
        }

        public void seteValue(double eValue) {
            this.eValue = eValue;
        }

        public String getOutFormat() {
            return outFormat;
        }

        public void setOutFormat(String outFormat) {
            this.outFormat = outFormat;
        }

        public int getMaxTargetSeqs() {
            return maxTargetSeqs;
        }

        public void setMaxTargetSeqs(int maxTargetSeqs) {
            this.maxTargetSeqs = maxTargetSeqs;
        }

        public boolean isUngapped() {
            return ungapped;
        }

        public void setUngapped(boolean ungapped) {
            this.ungapped = ungapped;
        }

        public int getNumThreads() {
            return numThreads;
        }

        public void setNumThreads(int numThreads) {
            this.numThreads = numThreads;
        }


        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {
            Params params = this.getParams();

            if (param.equals(params.getQuery())) {
                this.query = new File(value);
            }
            else if (param.equals(params.getDb())) {
                this.db = new File(value);
            }
            else if (param.equals(params.getOut())) {
                this.out = new File(value);
            }
            else if (param.equals(params.geteValue())) {
                this.eValue = Double.parseDouble(value);
            }
            else if (param.equals(params.getOutFormat())) {
                this.outFormat = value;
            }
            else if (param.equals(params.getMaxTargetSeqs())) {
                this.maxTargetSeqs = Integer.parseInt(value);
            }
            else if (param.equals(params.getUngapped())) {
                this.ungapped = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getNumThreads())) {
                this.numThreads = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void parseCommandLine(CommandLine commandLine) {

        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            if (this.query != null) {
                pvp.put(params.getQuery(), this.query.getAbsolutePath());
            }

            if (this.db != null) {
                pvp.put(params.getDb(), this.db.getAbsolutePath());
            }

            if (this.out != null && !this.out.getName().trim().equalsIgnoreCase("-")) {
                pvp.put(params.getOut(), this.out.getAbsolutePath());
            }

            if (this.eValue != DEFAULT_EVALUE) {
                pvp.put(params.geteValue(), Double.toString(this.eValue));
            }

            if (!this.outFormat.equalsIgnoreCase(DEFAULT_OUT_FMT)) {
                pvp.put(params.getOutFormat(), this.outFormat);
            }

            if (this.maxTargetSeqs != DEFAULT_MAX_TARGET_SEQS && this.maxTargetSeqs > 0) {
                pvp.put(params.getMaxTargetSeqs(), Integer.toString(this.maxTargetSeqs));
            }

            if (this.numThreads != DEFAULT_THREADS && this.numThreads > 1) {
                pvp.put(params.getNumThreads(), Integer.toString(this.numThreads));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter query;
        private ConanParameter db;
        private ConanParameter out;
        private ConanParameter eValue;
        private ConanParameter outFormat;
        private ConanParameter maxTargetSeqs;
        private ConanParameter ungapped;
        private ConanParameter numThreads;

        public Params() {

            this.query = new ParameterBuilder()
                    .shortName("query")
                    .description("Input file name")
                    .isOptional(false)
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.db = new ParameterBuilder()
                    .shortName("db")
                    .description("BLAST database name")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.out = new ParameterBuilder()
                    .shortName("out")
                    .description("Output file name")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.eValue = new ParameterBuilder()
                    .shortName("evalue")
                    .description("Expectation value (E) threshold for saving hits")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.outFormat = new ParameterBuilder()
                    .shortName("outfmt")
                    .description("alignment view options")
                    .create();

            this.maxTargetSeqs = new ParameterBuilder()
                    .shortName("max_target_seqs")
                    .description("Maximum number of aligned sequences to keep")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.ungapped = new ParameterBuilder()
                    .shortName("ungapped")
                    .description("Perform ungapped alignment only?")
                    .isFlag(true)
                    .create();

            this.numThreads = new ParameterBuilder()
                    .shortName("num_threads")
                    .description("Number of threads (CPUs) to use in the BLAST search")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getQuery() {
            return query;
        }

        public ConanParameter getDb() {
            return db;
        }

        public ConanParameter getOut() {
            return out;
        }

        public ConanParameter geteValue() {
            return eValue;
        }

        public ConanParameter getOutFormat() {
            return outFormat;
        }

        public ConanParameter getMaxTargetSeqs() {
            return maxTargetSeqs;
        }

        public ConanParameter getUngapped() {
            return ungapped;
        }

        public ConanParameter getNumThreads() {
            return numThreads;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.query,
                    this.db,
                    this.out,
                    this.eValue,
                    this.outFormat,
                    this.maxTargetSeqs,
                    this.ungapped,
                    this.numThreads
            };
        }
    }
}
