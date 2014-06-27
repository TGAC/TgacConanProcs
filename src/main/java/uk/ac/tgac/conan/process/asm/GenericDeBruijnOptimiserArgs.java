package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

/**
 * Created by maplesod on 18/06/14.
 */
public abstract class GenericDeBruijnOptimiserArgs extends AbstractAssemblerArgs {

    private KmerRange kmerRange;

    protected GenericDeBruijnOptimiserArgs(ProcessParams params, String name) {
        super(params, name);
    }

    @Override
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN_OPTIMISER;
    }

    public KmerRange getKmerRange() {
        return kmerRange;
    }

    public void setKmerRange(KmerRange kmerRange) {
        this.kmerRange = kmerRange;
    }

}
