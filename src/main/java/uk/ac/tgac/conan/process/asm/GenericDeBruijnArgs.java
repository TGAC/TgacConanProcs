package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

/**
 * Created by maplesod on 18/06/14.
 */
public class GenericDeBruijnArgs extends AbstractAssemblerArgs {

    public static final int DEFAULT_K = 55;
    public static final int DEFAULT_COVERAGE_CUTOFF = 0;

    private int k;
    private int coverageCutoff;

    public GenericDeBruijnArgs() {

        super();

        this.k = DEFAULT_K;
        this.coverageCutoff = DEFAULT_COVERAGE_CUTOFF;
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
