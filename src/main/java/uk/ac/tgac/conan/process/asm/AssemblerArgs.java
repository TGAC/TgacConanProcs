package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.Organism;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 10/12/13
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */
public interface AssemblerArgs extends ProcessArgs {

    /**
     * Creates an instance of an AbstractAssembler using Basic args
     *
     * @return
     */
    void initialise(
            List<Library> libs,
            File outputDir,
            int threads,
            int memory,
            Organism organism);

    /**
     * The type of assembler supported by these args.
     * @return
     */
    Assembler.Type getType();

    /**
     * Gets the name of this process
     * @return
     */
    String getName();
}
