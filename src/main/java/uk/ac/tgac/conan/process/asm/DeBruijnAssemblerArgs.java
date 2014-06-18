package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

/**
 * Created by maplesod on 18/06/14.
 */
public abstract class DeBruijnAssemblerArgs extends AbstractAssemblerArgs {

    public static final int DEFAULT_K = 55;
    public static final int DEFAULT_COVERAGE_CUTOFF = 0;

    private int k;
    private int coverageCutoff;

    protected DeBruijnAssemblerArgs(ProcessParams params, String name) {
        super(params, name);

        this.k = DEFAULT_K;
        this.coverageCutoff = DEFAULT_COVERAGE_CUTOFF;
    }

    @Override
    public AssemblerType getType() {
        return AssemblerType.DE_BRUIJN;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getCoverageCutoff() {
        return coverageCutoff;
    }

    public void setCoverageCutoff(int coverageCutoff) {
        this.coverageCutoff = coverageCutoff;
    }
}
