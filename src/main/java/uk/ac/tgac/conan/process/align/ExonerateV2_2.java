package uk.ac.tgac.conan.process.align;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 15/01/14
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class ExonerateV2_2 extends AbstractConanProcess {

    public static final String EXE = "exonerate";

    public ExonerateV2_2() {
        this(new Args());
    }

    public ExonerateV2_2(Args args) {
        super(EXE, args, new Params());
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }


    @Override
    public String getName() {
        return "Exonerate_V2.2.X";
    }

    public static class Args extends AbstractProcessArgs {

        private Params params = new Params();

        private File query;
        private File target;
        private String model;
        private int score;
        private double percent;
        private boolean showAlignment;
        private boolean showSugar;
        private boolean showCigar;
        private boolean showVulgar;
        private boolean showQueryGff;
        private boolean showTargetGff;
        private String ryo;

        public Args() {
            this.query = null;
            this.target = null;
            this.model = "";
            this.score = 100;
            this.percent = 0.0;
            this.showAlignment = true;
            this.showSugar = false;
            this.showCigar = false;
            this.showVulgar = true;
            this.showQueryGff = false;
            this.showTargetGff = false;
            this.ryo = "";
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

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
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

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {
            if (param.equals(this.params.getQuery())) {
                this.query = new File(value);
            }
            else if (param.equals(this.params.getTarget())) {
                this.target = new File(value);
            }
            else if (param.equals(this.params.getModel())) {
                this.model = value;
            }
            else if (param.equals(this.params.getScore())) {
                this.score = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getPercent())) {
                this.percent = Double.parseDouble(value);
            }
            else if (param.equals(this.params.getShowAlignment())) {
                this.showAlignment = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getShowSugar())) {
                this.showSugar = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getShowCigar())) {
                this.showCigar = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getShowVulgar())) {
                this.showVulgar = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getShowQueryGff())) {
                this.showQueryGff = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getShowTargetGff())) {
                this.showTargetGff = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getRyo())) {
                this.ryo = value;
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            ParamMap pvp = new DefaultParamMap();

            if (this.query != null) {
                pvp.put(params.getQuery(), this.query.getAbsolutePath());
            }

            if (this.target != null) {
                pvp.put(params.getTarget(), this.target.getAbsolutePath());
            }

            if (this.model != null && !this.model.isEmpty()) {
                pvp.put(params.getModel(), this.model);
            }

            if (this.score != 100) {
                pvp.put(params.getScore(), Integer.toString(this.score));
            }

            if (this.percent != 0.0) {
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

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter query;
        private ConanParameter target;
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
        }


        public ConanParameter getQuery() {
            return query;
        }

        public ConanParameter getTarget() {
            return target;
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

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.query,
                    this.target,
                    this.model,
                    this.score,
                    this.percent,
                    this.showAlignment,
                    this.showSugar,
                    this.showCigar,
                    this.showVulgar,
                    this.showQueryGff,
                    this.showTargetGff,
                    this.ryo
            };
        }
    }
}
