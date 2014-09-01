package uk.ac.tgac.conan.process.rnaasm;

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
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 30/01/14
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class BedtoolsWindowV2_17 extends AbstractConanProcess {

    public static final String EXE = "bedtools";
    public static final String MODE = "window";

    public BedtoolsWindowV2_17(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public BedtoolsWindowV2_17(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "BedTools_Window_V2.17";
    }


    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_WINDOW_SIZE = 1000;

        private File inputA;
        private File inputB;
        private File output;
        private int windowSize;
        private boolean reportNbOverlaps;

        public Args() {
            super(new Params());

            this.inputA = null;
            this.inputB = null;
            this.output = null;
            this.windowSize = DEFAULT_WINDOW_SIZE;
            this.reportNbOverlaps = false;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getInputA() {
            return inputA;
        }

        public void setInputA(File inputA) {
            this.inputA = inputA;
        }

        public File getInputB() {
            return inputB;
        }

        public void setInputB(File inputB) {
            this.inputB = inputB;
        }

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        public int getWindowSize() {
            return windowSize;
        }

        public void setWindowSize(int windowSize) {
            this.windowSize = windowSize;
        }

        public boolean isReportNbOverlaps() {
            return reportNbOverlaps;
        }

        public void setReportNbOverlaps(boolean reportNbOverlaps) {
            this.reportNbOverlaps = reportNbOverlaps;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {
            Params params = this.getParams();

            if (param.equals(params.getInputA())) {
                this.inputA = new File(value);
            }
            else if (param.equals(params.getInputB())) {
                this.inputB = new File(value);
            }
            else if (param.equals(params.getOutput())) {
                this.output = new File(value);
            }
            else if (param.equals(params.getWindowSize())) {
                this.windowSize = Integer.parseInt(value);
            }
            else if (param.equals(params.getReportNbOverlaps())) {
                this.reportNbOverlaps = Boolean.parseBoolean(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {
            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.inputA != null) {
                pvp.put(params.getInputA(), this.inputA.getAbsolutePath());
            }

            if (this.inputB != null) {
                pvp.put(params.getInputB(), this.inputB.getAbsolutePath());
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            if (this.windowSize >= 0 && this.windowSize != DEFAULT_WINDOW_SIZE) {
                pvp.put(params.getWindowSize(), Integer.toString(this.windowSize));
            }

            if (this.reportNbOverlaps) {
                pvp.put(params.getReportNbOverlaps(), Boolean.toString(this.reportNbOverlaps));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter inputA;
        private ConanParameter inputB;
        private ConanParameter output;
        private ConanParameter windowSize;
        private ConanParameter reportNbOverlaps;

        public Params() {

            this.inputA = new ParameterBuilder()
                    .shortName("a")
                    .isOptional(false)
                    .description("Input bed/gff/vcf file 1")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.inputB = new ParameterBuilder()
                    .shortName("b")
                    .isOptional(false)
                    .description("Input bed/gff/vcf file 2")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .isOptional(false)
                    .description("Output bed/gff/vcf file")
                    .argValidator(ArgValidator.PATH)
                    .type(DefaultConanParameter.ParamType.REDIRECTION)
                    .create();

            this.windowSize = new ParameterBuilder()
                    .shortName("w")
                    .description("Base pairs added upstream and downstream of each entry in A when searching for overlaps in B.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.reportNbOverlaps = new ParameterBuilder()
                    .shortName("c")
                    .description("For each entry in A, report the number of overlaps with B.")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        public ConanParameter getInputA() {
            return inputA;
        }

        public ConanParameter getInputB() {
            return inputB;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getWindowSize() {
            return windowSize;
        }

        public ConanParameter getReportNbOverlaps() {
            return reportNbOverlaps;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.inputA,
                    this.inputB,
                    this.output,
                    this.windowSize,
                    this.reportNbOverlaps
            };
        }
    }
}
