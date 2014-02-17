package uk.ac.tgac.conan.process.asmIO.scaffold.soap;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOArgs;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOProcess;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 21/11/13
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
@MetaInfServices(uk.ac.tgac.conan.process.asmIO.AssemblyIOCreator.class)
public class SoapV240Process extends AbstractAssemblyIOProcess {


    public static final String EXE = "soap2 scaff";
    public static final String NAME = "SOAP2_v240";
    public static final String TYPE = "scaffolder";

    public SoapV240Process() {
        this(new SoapV240Args());
    }

    public SoapV240Process(SoapV240Args args) {
        super(EXE, args, new SoapV240Params());
    }

    @Override
    public AbstractAssemblyIOProcess create(AbstractAssemblyIOArgs args) {
        return new SoapV240Process((SoapV240Args)args);
    }

    @Override
    public String getAssemblyIOProcessType() {
        return TYPE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean execute(ExecutionContext executionContext) throws ProcessExecutionException, InterruptedException {

        SoapV240Args args = (SoapV240Args)this.getProcessArgs();

        // Create the SSPACE lib configuration file from the library list
        try {
            if (args.getConfigFile() == null) {
                args.setConfigFile(new File(args.getOutputDir(), "soap_gc.libs"));
            }

            args.createConfigFile(args.getLibraries(), args.getConfigFile());
        }
        catch(IOException ioe) {
            throw new ProcessExecutionException(-1, ioe);
        }

        ExecutionContext executionContextCopy = executionContext.copy();

        if (executionContextCopy.usingScheduler()) {
            executionContextCopy.getScheduler().getArgs().setMonitorFile(new File(args.getOutputDir(), args.getOutputFile().getName() + ".scheduler.log"));
        }



        return super.execute(executionContextCopy);
    }

}
