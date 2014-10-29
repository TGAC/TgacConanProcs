package uk.ac.tgac.conan.process.misc;

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
 * Created by maplesod on 07/10/14.
 */
public class FastXRC_V0013 extends AbstractConanProcess {

    public static final String NAME = "FastXRC_V0013";
    public static final String EXE = "fastx_reverse_complement";

    public FastXRC_V0013() {
        this(null);
    }

    public FastXRC_V0013(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public FastXRC_V0013(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static class Args extends AbstractProcessArgs {

        private File in;
        private File out;
        private boolean compressOut;
        private int qualityOffset;

        public Args() {
            super(new Params());

            this.in = null;
            this.out = null;
            this.compressOut = false;
            this.qualityOffset = 0;
        }

        public File getIn() {
            return in;
        }

        public void setIn(File in) {
            this.in = in;
        }

        public File getOut() {
            return out;
        }

        public void setOut(File out) {
            this.out = out;
        }

        public boolean isCompressOut() {
            return compressOut;
        }

        public void setCompressOut(boolean compressOut) {
            this.compressOut = compressOut;
        }

        public int getQualityOffset() {
            return qualityOffset;
        }

        public void setQualityOffset(int qualityOffset) {
            this.qualityOffset = qualityOffset;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;

            if (param.equals(params.getIn())) {
                this.in = new File(value);
            }
            else if (param.equals(params.getOut())) {

                this.out = new File(value);
            }
            else if (param.equals(params.getCompressOut())) {
                this.compressOut = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getQualityOffset())) {
                this.qualityOffset = Integer.parseInt(value);
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

            Params params = (Params)this.params;

            ParamMap pvp = new DefaultParamMap();

            if (this.in != null) {
                pvp.put(params.getIn(), this.in.getAbsolutePath());
            }

            if (this.out != null) {
                pvp.put(params.getOut(), this.out.getAbsolutePath());
            }

            if (this.compressOut) {
                pvp.put(params.getCompressOut(), Boolean.toString(this.compressOut));
            }

            if (this.qualityOffset != 0) {
                pvp.put(params.getQualityOffset(), Integer.toString(this.qualityOffset));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter in;
        private ConanParameter out;
        private ConanParameter compressOut;
        private ConanParameter qualityOffset;

        public Params() {

            this.in = new ParameterBuilder()
                    .isOptional(false)
                    .shortName("i")
                    .description("FASTA/Q input file. default is STDIN")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.out = new ParameterBuilder()
                    .isOptional(false)
                    .shortName("o")
                    .description("FASTA/Q output file. default is STDOUT.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.compressOut = new ParameterBuilder()
                    .shortName("z")
                    .description("Compress output with GZIP.")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.qualityOffset = new ParameterBuilder()
                    .shortName("Q")
                    .description("Quality offset to use (most likely 33 or 64)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getIn() {
            return in;
        }

        public ConanParameter getOut() {
            return out;
        }

        public ConanParameter getCompressOut() {
            return compressOut;
        }

        public ConanParameter getQualityOffset() {
            return qualityOffset;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.in,
                    this.out,
                    this.compressOut,
                    this.qualityOffset
            };
        }
    }
}
