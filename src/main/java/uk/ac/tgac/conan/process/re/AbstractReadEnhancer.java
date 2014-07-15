package uk.ac.tgac.conan.process.re;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 09/12/13
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractReadEnhancer extends AbstractConanProcess implements ReadEnhancer {

    protected String name;

    protected AbstractReadEnhancer(String name, String executable, AbstractProcessArgs args, ProcessParams params, ConanExecutorService ces) {
        super(executable, args, params, ces);
        this.name = name;
    }

    @Override
    public AbstractReadEnhancerArgs getReadEnchancerArgs() {
        return (AbstractReadEnhancerArgs)this.getProcessArgs();
    }

    @Override
    public void setup() throws IOException {
    }

    @Override
    public void initialise(ConanExecutorService ces) {
        this.conanExecutorService = ces;
    }

    @Override
    public void initialise(ReadEnhancerArgs args, ConanExecutorService ces) {
        this.setProcessArgs((ProcessArgs)args);
        this.conanExecutorService = ces;
    }

    @Override
    public Library getOutputLibrary(Library inputLibrary) {

        Library modLib = inputLibrary.copy();

        List<File> files = this.getEnhancedFiles();

        if (modLib.isPairedEnd()) {
            if (files.size() < 2 || files.size() > 3) {
                throw new IllegalArgumentException("Paired end library: " + modLib.getName() + " from " + this.name + " does not have two or three files");
            }

            modLib.setFiles(files.get(0), files.get(1));
        }
        else {
            if (files.size() != 1) {
                throw new IllegalArgumentException("Single end library: " + modLib.getName() + " from " + this.name + " does not have one file");
            }

            modLib.setFiles(files.get(0), null);
        }

        return modLib;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProcessName() {
        return name;
    }

    @Override
    public AbstractConanProcess toConanProcess() {
        return this;
    }
}
