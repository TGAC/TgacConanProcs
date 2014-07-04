package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.util.ServiceLoader;

/**
 * Created by maplesod on 18/06/14.
 */
public class GenericDeBruijnOptimiserArgs extends GenericAssemblerArgs {

    private KmerRange kmerRange;

    public GenericDeBruijnOptimiserArgs() {
        super();

        this.kmerRange = new KmerRange();
    }

    public KmerRange getKmerRange() {
        return kmerRange;
    }

    public void setKmerRange(KmerRange kmerRange) {
        this.kmerRange = kmerRange;
    }

    @Override
    public DeBruijnOptimiserArgs createProcessArgs(String toolName) {
        ServiceLoader<DeBruijnOptimiserArgs> argLoader = ServiceLoader.load(DeBruijnOptimiserArgs.class);

        for(DeBruijnOptimiserArgs args : argLoader) {
            if (args.getProcessName().equalsIgnoreCase(toolName.trim())) {
                args.setDeBruijnOptimiserArgs(this);
                return args;
            }
        }

        throw new IllegalArgumentException("Could not find the requested assembler: " + toolName);
    }

    @Override
    public Assembler createAssembler(String toolName, ConanExecutorService ces) {

        DeBruijnOptimiserArgs args = this.createProcessArgs(toolName);

        if (args == null)
            throw new IllegalArgumentException("Provided assembler args are null");

        Assembler asm = createAssembler(toolName, args.toConanArgs(), ces);

        if (asm.getType() != Assembler.Type.DE_BRUIJN_OPTIMISER) {
            throw new IllegalArgumentException("Assembler \"" + toolName + "\" is not a De Bruijn graph assembler that optimises K value ranges");
        }

        return asm;
    }
}
