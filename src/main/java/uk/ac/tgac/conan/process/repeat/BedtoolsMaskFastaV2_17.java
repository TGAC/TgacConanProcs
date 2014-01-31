package uk.ac.tgac.conan.process.repeat;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 30/01/14
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class BedtoolsMaskFastaV2_17 extends AbstractConanProcess {

    public static final String EXE = "bedtools";
    public static final String MODE = "maskfasta";

    public BedtoolsMaskFastaV2_17() {
        this(new Args());
    }

    public BedtoolsMaskFastaV2_17(Args args) {
        super(EXE, args, new Params());
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "BedTools_MaskFasta_V2.17";
    }


    public static class Args extends AbstractProcessArgs {

        private File input;
        private File output;
        private File bed;
        private boolean softMask;

        public Args() {
            super(new Params());

            this.input = null;
            this.output = null;
            this.bed = null;
            this.softMask = false;
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

        public File getBed() {
            return bed;
        }

        public void setBed(File bed) {
            this.bed = bed;
        }

        public boolean isSoftMask() {
            return softMask;
        }

        public void setSoftMask(boolean softMask) {
            this.softMask = softMask;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {
            Params params = this.getParams();

            if (param.equals(params.getInput())) {
                this.input = new File(value);
            }
            else if (param.equals(params.getOutput())) {
                this.output = new File(value);
            }
            else if (param.equals(params.getBed())) {
                this.bed = new File(value);
            }
            else if (param.equals(params.getSoftMask())) {
                this.softMask = Boolean.parseBoolean(value);
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

            if (this.bed != null) {
                pvp.put(params.getBed(), this.bed.getAbsolutePath());
            }

            if (this.softMask) {
                pvp.put(params.getSoftMask(), Boolean.toString(this.softMask));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter input;
        private ConanParameter output;
        private ConanParameter bed;
        private ConanParameter softMask;

        public Params() {

            this.input = new ParameterBuilder()
                    .shortName("fi")
                    .isOptional(false)
                    .description("Input FASTA file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .shortName("fo")
                    .isOptional(false)
                    .description("Output FASTA file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.bed = new ParameterBuilder()
                    .shortName("bed")
                    .isOptional(false)
                    .description("BED/GFF/VCF file of ranges to mask in -fi")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.softMask = new ParameterBuilder()
                    .shortName("soft")
                    .isFlag(true)
                    .description("Enforce \"soft\" masking.  That is, instead of masking with Ns, mask with lower-case bases.")
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getBed() {
            return bed;
        }

        public ConanParameter getSoftMask() {
            return softMask;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.input,
                    this.output,
                    this.bed,
                    this.softMask
            };
        }
    }
}
