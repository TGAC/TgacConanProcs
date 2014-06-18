package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.Organism;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by maplesod on 18/06/14.
 */
public enum AssemblerType {

    DE_BRUIJN {

        public Assembler create(String toolName,
                                List<Library> libs,
                                File outputDir,
                                int threads,
                                int memory,
                                Organism organism,
                                ConanExecutorService ces,
                                int k,
                                int coverageCutoff
                                ) throws IOException {

            AssemblerArgs args = this.createAssemblerArgs(toolName, libs, outputDir, threads, memory, organism);
            args.initialise(libs, outputDir, threads, memory, organism);

            if (args.getType() != DE_BRUIJN) {
                throw new IllegalArgumentException("Assembler \"" + toolName + "\" is not a De Bruijn graph assembler that allows you to specify a specific K value");
            }

            DeBruijnAssemblerArgs dbgArgs = (DeBruijnAssemblerArgs)args;
            dbgArgs.setK(k);
            dbgArgs.setCoverageCutoff(coverageCutoff);

            return this.createAssembler(toolName, dbgArgs, ces);
        }
    },
    DE_BRUIJN_OPTIMISER {


    },
    DE_BRUIJN_FIXED,
    OVERLAP_LAYOUT_CONSENSUS;


    protected AssemblerArgs createAssemblerArgs(String toolName, List<Library> libs, File outputDir, int threads, int memory, Organism organism) {
        AssemblerArgs actualArgs = null;

        ServiceLoader<AssemblerArgs> argLoader = ServiceLoader.load(AssemblerArgs.class);

        for(AssemblerArgs args : argLoader) {
            if (args.getName().equalsIgnoreCase(toolName.trim())) {
                if (args.getType() != DE_BRUIJN) {
                    throw new IllegalArgumentException("Found assembler \"" + toolName + "\" but this assembler is not a De Bruijn graph assembler");
                }

                return args;
            }
        }

        throw new IllegalArgumentException("Could not find the requested assembler: " + toolName);
    }

    protected Assembler createAssembler(String toolName, AssemblerArgs args, ConanExecutorService ces)
            throws IOException {

        if (args == null)
            throw new IllegalArgumentException("Provided assembler args are null");

        ServiceLoader<Assembler> procLoader = ServiceLoader.load(Assembler.class);

        for(Assembler assembler : procLoader) {
            if (assembler.getName().equalsIgnoreCase(toolName.trim())) {
                assembler.initialise(args, ces);
                return assembler;
            }
        }

        throw new IllegalArgumentException("Could not find the requested assembler: " + toolName);
    }
}
