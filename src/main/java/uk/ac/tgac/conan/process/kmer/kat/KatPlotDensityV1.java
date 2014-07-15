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
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/01/14
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class KatPlotDensityV1 extends AbstractConanProcess {

    public static final String EXE = "kat";
    public static final String MODE = "plot density";

    public KatPlotDensityV1() {
        this(new Args());
    }

    public KatPlotDensityV1(Args args) {
        super(EXE, args, new Params());
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "KAT_Plot_Density_V1.0";
    }

    public static class Args extends AbstractProcessArgs {

        private File input;
        private File output;

        public Args() {
            super(new Params());

            this.input = null;
            this.output = null;
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

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {
            Params params = this.getParams();

            if (param.equals(params.getOutput())) {
                this.output = new File(value);
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

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
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

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter input;
        private ConanParameter output;

        public Params() {
            this.input = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .argValidator(ArgValidator.PATH)
                    .description("Input matrix")
                    .create();

            this.output = new ParameterBuilder()
                    .shortName("o")
                    .longName("output")
                    .description("Output file (\"kat-plot-density.png\")")
                    .argValidator(ArgValidator.PATH)
                    .create();
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getOutput() {
            return output;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.input,
                    this.output
            };
        }
    }

}
