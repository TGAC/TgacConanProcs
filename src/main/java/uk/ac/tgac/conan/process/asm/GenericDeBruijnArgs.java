package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * Created by maplesod on 18/06/14.
 */
public class GenericDeBruijnArgs extends GenericAssemblerArgs {

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
