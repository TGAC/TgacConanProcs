package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
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
public abstract class AbstractAssembler extends AbstractConanProcess implements AssemblerCreator, Assembler {

    protected AbstractAssembler(String executable, AbstractAssemblerArgs args, ProcessParams params) {
        super(executable, args, params);
    }

    public AbstractAssemblerArgs getArgs() {
        return (AbstractAssemblerArgs)this.getProcessArgs();
    }

    @Override
    public boolean makesUnitigs() {
        return true;
    }

    @Override
    public boolean makesContigs() {
        return true;
    }

    @Override
    public boolean makesScaffolds() {
        return true;
    }

    @Override
    public boolean usesOpenMpi() {
        return false;
    }

    @Override
    public boolean doesSubsampling() {
        return false;
    }

    @Override
    public boolean hasKParam() {
        return true;
    }

    @Override
    public boolean acceptsLibraries(List<Library> libraries) {
        return true;
    }

    public void initialise() throws IOException {
    }
}
