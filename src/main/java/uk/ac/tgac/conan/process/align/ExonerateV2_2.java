package uk.ac.tgac.conan.process.align;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
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
public class ExonerateV2_2 extends AbstractConanProcess {

    public static final String EXE = "exonerate";

    public ExonerateV2_2(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public ExonerateV2_2(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
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
        return "Exonerate_V2.2.X";
    }

    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_SCORE = 100;
        public static final double DEFAULT_PERCENT = 0.0;
        public static final int DEFAULT_MAX_INTRON = 200000;


        public static enum Model {
            UNGAPPED,
            UNGAPPED_TRANS,
            AFFINE_GLOBAL,
            AFFINE_BESTFIT,
            AFFINE_LOCAL,
            AFFINE_OVERLAP,
            EST2GENOME,
            NER,
            PROTEIN2DNA,
            PROTEIN2DNA_BESTFIT,
            PROTEIN2GENOME,
            PROTEIN2GENOME_BESTFIT,
            CODING2CODING,
            CODING2GENOME,
            CDNA2GENOME,
            GENOME2GENOME;

            public String toArgString() {
                return this.toString().replace('_',':').toLowerCase();
            }

            public static Model fromArgString(String value) {
                return Model.valueOf(value.replace(':','_').toUpperCase());
            }
        }

        private File query;
        private File target;
        private File output;
        private Model model;
        private int score;
        private double percent;
        private boolean showAlignment;
        private boolean showSugar;
        private boolean showCigar;
        private boolean showVulgar;
        private boolean showQueryGff;
        private boolean showTargetGff;
        private String ryo;
        private int maxIntron;

        public Args() {

            super(new Params());

            this.query = null;
            this.target = null;
            this.output = null;
            this.model = null;
            this.score = DEFAULT_SCORE;
            this.percent = DEFAULT_PERCENT;
            this.showAlignment = true;
            this.showSugar = false;
            this.showCigar = false;
            this.showVulgar = true;
            this.showQueryGff = false;
            this.showTargetGff = false;
            this.ryo = "";
            this.maxIntron = DEFAULT_MAX_INTRON;
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

        public File getTarget() {
            return target;
        }

        public void setTarget(File target) {
            this.target = target;
        }

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public double getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }

        public boolean isShowAlignment() {
            return showAlignment;
        }

        public void setShowAlignment(boolean showAlignment) {
            this.showAlignment = showAlignment;
        }

        public boolean isShowSugar() {
            return showSugar;
        }

        public void setShowSugar(boolean showSugar) {
            this.showSugar = showSugar;
        }

        public boolean isShowCigar() {
            return showCigar;
        }

        public void setShowCigar(boolean showCigar) {
            this.showCigar = showCigar;
        }

        public boolean isShowVulgar() {
            return showVulgar;
        }

        public void setShowVulgar(boolean showVulgar) {
            this.showVulgar = showVulgar;
        }

        public boolean isShowQueryGff() {
            return showQueryGff;
        }

        public void setShowQueryGff(boolean showQueryGff) {
            this.showQueryGff = showQueryGff;
        }

        public boolean isShowTargetGff() {
            return showTargetGff;
        }

        public void setShowTargetGff(boolean showTargetGff) {
            this.showTargetGff = showTargetGff;
        }

        public String getRyo() {
            return ryo;
        }

        public void setRyo(String ryo) {
            this.ryo = ryo;
        }

        public int getMaxIntron() {
            return maxIntron;
        }

        public void setMaxIntron(int maxIntron) {
            this.maxIntron = maxIntron;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getQuery())) {
                this.query = new File(value);
            }
            else if (param.equals(params.getTarget())) {
                this.target = new File(value);
            }
            else if (param.equals(params.getModel())) {
                this.model = Model.fromArgString(value);
            }
            else if (param.equals(params.getScore())) {
                this.score = Integer.parseInt(value);
            }
            else if (param.equals(params.getPercent())) {
                this.percent = Double.parseDouble(value);
            }
            else if (param.equals(params.getShowAlignment())) {
                this.showAlignment = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getShowSugar())) {
                this.showSugar = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getShowCigar())) {
                this.showCigar = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getShowVulgar())) {
                this.showVulgar = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getShowQueryGff())) {
                this.showQueryGff = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getShowTargetGff())) {
                this.showTargetGff = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getRyo())) {
                this.ryo = value;
            }
            else if (param.equals(params.getMaxIntron())) {
                this.maxIntron = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

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

            if (this.query != null) {
                pvp.put(params.getQuery(), this.query.getAbsolutePath());
            }

            if (this.target != null) {
                pvp.put(params.getTarget(), this.target.getAbsolutePath());
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            if (this.model != null) {
                pvp.put(params.getModel(), this.model.toArgString());
            }

            if (this.score != DEFAULT_SCORE) {
                pvp.put(params.getScore(), Integer.toString(this.score));
            }

            if (this.percent != DEFAULT_PERCENT) {
                pvp.put(params.getPercent(), Double.toString(this.percent));
            }

            if (!this.showAlignment) {
                pvp.put(params.getShowAlignment(), Boolean.toString(this.showAlignment).toUpperCase());
            }

            if (this.showSugar) {
                pvp.put(params.getShowSugar(), Boolean.toString(this.showSugar).toUpperCase());
            }

            if (this.showCigar) {
                pvp.put(params.getShowCigar(), Boolean.toString(this.showCigar).toUpperCase());
            }

            if (!this.showVulgar) {
                pvp.put(params.getShowVulgar(), Boolean.toString(this.showVulgar).toUpperCase());
            }

            if (this.showQueryGff) {
                pvp.put(params.getShowQueryGff(), Boolean.toString(this.showQueryGff).toUpperCase());
            }

            if (this.showTargetGff) {
                pvp.put(params.getShowTargetGff(), Boolean.toString(this.showTargetGff).toUpperCase());
            }

            if (this.ryo != null && !this.ryo.isEmpty()) {
                pvp.put(params.getRyo(), this.ryo);
            }

            if (this.maxIntron != DEFAULT_MAX_INTRON) {
                pvp.put(params.getMaxIntron(), Integer.toString(this.maxIntron));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter query;
        private ConanParameter target;
        private ConanParameter output;
        private ConanParameter model;
        private ConanParameter score;
        private ConanParameter percent;
        private ConanParameter showAlignment;
        private ConanParameter showSugar;
        private ConanParameter showCigar;
        private ConanParameter showVulgar;
        private ConanParameter showQueryGff;
        private ConanParameter showTargetGff;
        private ConanParameter ryo;
        private ConanParameter maxIntron;


        public Params() {

            this.query = new ParameterBuilder()
                    .isOption(true)
                    .isOptional(false)
                    .shortName("q")
                    .longName("query")
                    .description("Specify query sequences as a fasta format file.  *** This argument is mandatory ***")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.target = new ParameterBuilder()
                    .isOption(true)
                    .isOptional(false)
                    .shortName("t")
                    .longName("target")
                    .description("Specify target sequences as a fasta format file.  *** This argument is mandatory ***")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .type(DefaultConanParameter.ParamType.REDIRECTION)
                    .isOptional(false)
                    .description("Redirected output to file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.model = new ParameterBuilder()
                    .shortName("m")
                    .longName("model")
                    .description("Specify alignment model type\n" +
                            "Supported types:\n" +
                            "    ungapped ungapped:trans\n" +
                            "    affine:global affine:bestfit affine:local affine:overlap\n" +
                            "    est2genome ner protein2dna protein2genome\n" +
                            "    protein2dna:bestfit protein2genome:bestfit\n" +
                            "    coding2coding coding2genome cdna2genome genome2genome")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.score = new ParameterBuilder()
                    .shortName("s")
                    .longName("score")
                    .description("Score threshold for gapped alignment")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.percent = new ParameterBuilder()
                    .longName("percent")
                    .description("Percent self-score threshold")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.showAlignment = new ParameterBuilder()
                    .isFlag(true)
                    .longName("showalignment")
                    .description("Include (human readable) alignment in results")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.showSugar = new ParameterBuilder()
                    .isFlag(true)
                    .longName("showsugar")
                    .description("Include 'sugar' format output in results")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.showCigar = new ParameterBuilder()
                    .isFlag(true)
                    .longName("showcigar")
                    .description("Include 'cigar' format output in results")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.showVulgar = new ParameterBuilder()
                    .isFlag(true)
                    .longName("showvulgar")
                    .description("Include 'vulgar' format output in results")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.showQueryGff = new ParameterBuilder()
                    .isFlag(true)
                    .longName("showquerygff")
                    .description("Include GFF output on query in results")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.showTargetGff = new ParameterBuilder()
                    .isFlag(true)
                    .longName("showtargetgff")
                    .description("Include GFF output on target in results")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.ryo = new ParameterBuilder()
                    .longName("ryo")
                    .description("Roll-your-own printf-esque output format")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.maxIntron = new ParameterBuilder()
                    .longName("maxintron")
                    .description("Maximum intron length")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }


        public ConanParameter getQuery() {
            return query;
        }

        public ConanParameter getTarget() {
            return target;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getModel() {
            return model;
        }

        public ConanParameter getScore() {
            return score;
        }

        public ConanParameter getPercent() {
            return percent;
        }

        public ConanParameter getShowAlignment() {
            return showAlignment;
        }

        public ConanParameter getShowSugar() {
            return showSugar;
        }

        public ConanParameter getShowCigar() {
            return showCigar;
        }

        public ConanParameter getShowVulgar() {
            return showVulgar;
        }

        public ConanParameter getShowQueryGff() {
            return showQueryGff;
        }

        public ConanParameter getShowTargetGff() {
            return showTargetGff;
        }

        public ConanParameter getRyo() {
            return ryo;
        }

        public ConanParameter getMaxIntron() {
            return maxIntron;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.query,
                    this.target,
                    this.output,
                    this.model,
                    this.score,
                    this.percent,
                    this.showAlignment,
                    this.showSugar,
                    this.showCigar,
                    this.showVulgar,
                    this.showQueryGff,
                    this.showTargetGff,
                    this.ryo,
                    this.maxIntron
            };
        }
    }
}
