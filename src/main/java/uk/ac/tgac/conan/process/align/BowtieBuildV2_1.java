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
 * Creates Bowtie2 index files.
 */
public class BowtieBuildV2_1 extends AbstractConanProcess {

    public static final String EXE = "bowtie2-build";

    public BowtieBuildV2_1(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public BowtieBuildV2_1(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Bowtie2-Build_V2.1.X";
    }


    /**
     * Bowtie Arguments
     */
    public static class Args extends AbstractProcessArgs {

        private File[] referenceIn;
        private String baseName;

        public Args() {

            super(new Params());

            this.referenceIn = null;
            this.baseName = "";
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File[] getReferenceIn() {
            return referenceIn;
        }

        public String getReferenceAsString() {

            if (this.referenceIn == null)
                return null;

            if (this.referenceIn.length == 0)
                return "";

            StringBuilder sb = new StringBuilder();

            sb.append(this.referenceIn[0].getAbsolutePath());

            for(int i = 1; i < this.referenceIn.length; i++) {
                sb.append(",").append(this.referenceIn[i].getAbsolutePath());
            }

            return sb.toString();
        }

        public void setReferenceIn(File[] referenceIn) {
            this.referenceIn = referenceIn;
        }

        public String getBaseName() {
            return baseName;
        }

        public void setBaseName(String baseName) {
            this.baseName = baseName;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.referenceIn != null) {
                pvp.put(params.getReferenceIn(), this.getReferenceAsString());
            }

            if (this.baseName != null && !this.getBaseName().isEmpty()) {
                pvp.put(params.getBaseName(), this.baseName);
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getReferenceIn())) {

                String[] parts = value.split(",");

                File[] files = new File[parts.length];
                for(int i = 0; i < parts.length; i++) {
                    files[i] = new File(parts[i]);
                }

                this.referenceIn = files;
            }
            else if (param.equals(params.getBaseName())) {
                this.baseName = value;
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    /**
     * Bowtie Params
     */
    public static class Params extends AbstractProcessParams {

        private ConanParameter referenceIn;
        private ConanParameter baseName;

        public Params() {

            this.referenceIn = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .description("A comma-separated list of FASTA files containing the reference sequences to be aligned to.  " +
                            "E.g., <reference_in> might be chr1.fa,chr2.fa,chrX.fa,chrY.fa.")
                    .argIndex(0)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.baseName = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .description("The basename of the index files to write. By default, bowtie2-build writes files named NAME.1.bt2, " +
                            "NAME.2.bt2, NAME.3.bt2, NAME.4.bt2, NAME.rev.1.bt2, and NAME.rev.2.bt2, where NAME is <bt2_base>.")
                    .argIndex(1)
                    .argValidator(ArgValidator.PATH)
                    .create();
        }


        public ConanParameter getReferenceIn() {
            return referenceIn;
        }

        public ConanParameter getBaseName() {
            return baseName;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.referenceIn,
                    this.baseName
            };
        }

    }
}
