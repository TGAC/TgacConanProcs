package uk.ac.tgac.conan.process.align;

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
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 15/01/14
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class SamtoolsViewV0_1 extends AbstractConanProcess {

    public static final String EXE = "exonerate";

    public SamtoolsViewV0_1(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public SamtoolsViewV0_1(ConanExecutorService conanExecutorService, Args args) {
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
        return "Samtools_View_V0.1.X";
    }

    public static class Args extends AbstractProcessArgs {


        public Args() {

            super(new Params());

        }

        public Params getParams() {
            return (Params)this.params;
        }


        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void setRedirectFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {


        public Params() {

        }



        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {

            };
        }
    }
}
