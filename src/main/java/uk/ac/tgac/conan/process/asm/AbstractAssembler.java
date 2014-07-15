package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 10/12/13
 * Time: 12:27
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAssembler extends AbstractConanProcess implements Assembler {

    protected String name;

    protected AbstractAssembler(String name, String executable, AbstractProcessArgs args, ProcessParams params, ConanExecutorService conanExecutorService) {
        super(executable, args, params, conanExecutorService);
        this.name = name;
    }

    @Override
    public AbstractAssemblerArgs getAssemblerArgs() {
        return (AbstractAssemblerArgs)this.getProcessArgs();
    }

    /**
     * Assume concrete assembler does not use open MPI.  Will need to override if it does.
     * @return
     */
    @Override
    public boolean usesOpenMpi() {
        return false;
    }

    /**
     * Assume concrete assembler does not have subsampling functionality itself.  Will need to override if it does.
     * @return
     */
    @Override
    public boolean doesSubsampling() {
        return false;
    }

    /**
     * Assume concrete assembler does accept libraries.  If the wrapper has been designed correctly then it will.
     * @param libraries List of libraries to check
     * @return
     */
    @Override
    public boolean acceptsLibraries(List<Library> libraries) {
        return true;
    }

    @Override
    public void setup() throws IOException {
    }

    @Override
    public void initialise(ConanExecutorService ces) {
        this.conanExecutorService = ces;
    }

    @Override
    public void initialise(AbstractProcessArgs args, ConanExecutorService ces) {
        this.setProcessArgs(args);
        this.conanExecutorService = ces;
    }

    @Override
    public void setLibraries(List<Library> libraries) {
        this.getAssemblerArgs().setLibs(libraries);
    }

    @Override
    public String getName() {
        return name;
    }

}
