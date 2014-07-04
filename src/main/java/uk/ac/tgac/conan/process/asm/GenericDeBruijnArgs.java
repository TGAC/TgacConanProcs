package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.util.ServiceLoader;

/**
 * Created by maplesod on 18/06/14.
 */
public class GenericDeBruijnArgs extends GenericAssemblerArgs {

    public static final int DEFAULT_K = 55;

    private int k;

    public GenericDeBruijnArgs() {

        super();

        this.k = DEFAULT_K;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    public DeBruijnArgs createProcessArgs(String toolName) {

        ServiceLoader<DeBruijnArgs> argLoader = ServiceLoader.load(DeBruijnArgs.class);

        for(DeBruijnArgs args : argLoader) {
            if (args.getProcessName().equalsIgnoreCase(toolName.trim())) {
                args.setDeBruijnArgs(this);
                return args;
            }
        }

        throw new IllegalArgumentException("Could not find the requested assembler: " + toolName);
    }

    @Override
    public Assembler createAssembler(String toolName, ConanExecutorService ces) {

        DeBruijnArgs args = this.createProcessArgs(toolName);

        if (args == null)
            throw new IllegalArgumentException("Provided assembler args are null");

        Assembler asm = createAssembler(toolName, args.toConanArgs(), ces);

        if (asm.getType() != Assembler.Type.DE_BRUIJN) {
            throw new IllegalArgumentException("Assembler \"" + toolName + "\" is not a De Bruijn graph assembler that allows you to specify a specific K value");
        }

        return asm;
    }
}
