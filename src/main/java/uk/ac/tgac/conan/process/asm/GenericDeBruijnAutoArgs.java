package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * Created by maplesod on 18/06/14.
 */
public class GenericDeBruijnAutoArgs extends AbstractAssemblerArgs {

    public GenericDeBruijnAutoArgs() {
        super();
    }

    @Override
    public DeBruijnAutoArgs createProcessArgs(String toolName) {
        ServiceLoader<DeBruijnAutoArgs> argLoader = ServiceLoader.load(DeBruijnAutoArgs.class);

        for(DeBruijnAutoArgs args : argLoader) {
            if (args.getProcessName().equalsIgnoreCase(toolName.trim())) {
                args.setDeBruijnAutoArgs(this);
                return args;
            }
        }

        throw new IllegalArgumentException("Could not find the requested assembler: " + toolName);
    }

    @Override
    public Assembler createAssembler(String toolName, ConanExecutorService ces) throws IOException {

        DeBruijnAutoArgs args = this.createProcessArgs(toolName);

        if (args == null)
            throw new IllegalArgumentException("Provided assembler args are null");

        Assembler assembler = this.createAssembler(toolName, ces);

        if (assembler.getType() != Assembler.Type.DE_BRUIJN_AUTO) {
            throw new IllegalArgumentException("Assembler \"" + toolName + "\" is not a De Bruijn graph assembler that handles K values automatically");
        }

        return assembler;
    }
}