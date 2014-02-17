package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
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
 * Date: 11/11/13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishStatsV11 extends AbstractConanProcess {

    public static final String EXE = "jellyfish";
    public static final String MODE = "stats";


    public JellyfishStatsV11() {
        this(new Args());
    }

    public JellyfishStatsV11(Args args) {
        super(EXE, args, new Params());
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Jellyfish_Stats_V1.1";
    }

    public static class Args extends AbstractProcessArgs {

        private File input;
        private File output;
        private long lowerCount;
        private long upperCount;

        public Args() {

            super(new Params());

            this.input = null;
            this.output = null;
            this.lowerCount = 0;
            this.upperCount = Long.MAX_VALUE;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getInput() {
            return input;
        }

        public void setInput(File input) {
            this.input = input;
        }

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        public long getLowerCount() {
            return lowerCount;
        }

        public void setLowerCount(long lowerCount) {
            this.lowerCount = lowerCount;
        }

        public long getUpperCount() {
            return upperCount;
        }

        public void setUpperCount(long upperCount) {
            this.upperCount = upperCount;
        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.input != null) {
                pvp.put(params.getInput(), this.input.getAbsolutePath());
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            pvp.put(params.getLowerCount(), Long.toString(this.lowerCount));
            pvp.put(params.getUpperCount(), Long.toString(this.upperCount));

            return pvp;
        }


        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutput())) {
                this.output = new File(value);
            } else if (param.equals(params.getLowerCount())) {
                this.lowerCount = Long.parseLong(value);
            } else if (param.equals(params.getUpperCount())) {
                this.upperCount = Long.parseLong(value);
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getInput())) {
                this.input = new File(value);
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter input;
        private ConanParameter output;
        private ConanParameter lowerCount;
        private ConanParameter upperCount;

        public Params() {
            super();

            this.input = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("Input file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .shortName("o")
                    .longName("output")
                    .description("Output file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.lowerCount = new NumericParameter(
                    "L",
                    "Don't consider k-mer with count < lower-count",
                    true);

            this.upperCount = new NumericParameter(
                    "U",
                    "Don't consider k-mer with count > upper-count",
                    true);
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getLowerCount() {
            return lowerCount;
        }

        public ConanParameter getUpperCount() {
            return upperCount;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.input,
                    this.output,
                    this.lowerCount,
                    this.upperCount
            };
        }
    }

}
