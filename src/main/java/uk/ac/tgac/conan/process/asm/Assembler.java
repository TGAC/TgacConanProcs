package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.model.ConanProcess;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Defines a number of behaviours that all assembly tools should implement
 */
public interface Assembler extends ConanProcess {

    /**
     * Return the process args as an AbstractAssemblerArgs type (all Assemblers should support AbstractAssemblerArgs).
     * @return
     */
    AbstractAssemblerArgs getArgs();

    /**
     * Whether this assembler produces unitigs or not
     * @return
     */
    boolean makesUnitigs();

    /**
     * Whether this assembler produces contigs or not
     * @return
     */
    boolean makesContigs();

    /**
     * Whether this assembler produces scaffolds or not
     * @return
     */
    boolean makesScaffolds();

    /**
     * Returns the unitigs file produced by this assembler, if present, otherwise returns null
     * @return
     */
    File getUnitigsFile();

    /**
     * Returns the contigs file produced by this assembler, if present, otherwise returns null
     * @return
     */
    File getContigsFile();

    /**
     * Returns the scaffolds file produced by this assembler, if present, otherwise returns null
     * @return
     */
    File getScaffoldsFile();

    /**
     * Some assemblers use openmpi, this method returns true if this assembler uses openmpi
     * @return
     */
    boolean usesOpenMpi();

    /**
     * Returns true if this assembler can do its own subsampling to manage coverage of input
     * @return
     */
    boolean doesSubsampling();

    /**
     * Returns true if this assembler allows the user to modify the 'K' i.e. "kmer" length
     * @return
     */
    boolean hasKParam();

    /**
     * Given a list of libraries, this method checks to see if this assembler can process them correctly
     * @param libraries List of libraries to check
     * @return True if this assembler thinks it can process the provided libraries, false otherwise
     */
    boolean acceptsLibraries(List<Library> libraries);

    /**
     * This can be used to do any setup work between running the constructor and executing the process.  For example,
     * it can be used to add any pre or post commands that are necessary for this assembler.
     * @throws IOException
     */
    void initialise() throws IOException;
}