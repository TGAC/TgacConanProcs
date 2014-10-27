package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.ConanProcess;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
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
    AbstractAssemblerArgs getAssemblerArgs();

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
     * Whether this assembler produces a bubble file
     * @return
     */
    boolean makesBubbles();

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
     * Returns the bubble file produced by this asssembler, if present, otherwise returns null
     * @return
     */
    File getBubbleFile();

    /**
     * Returns the most contiguous assembly produced by the assembler.  Typically, this is done using the precedence:
     * Scaffolds > Contigs > Unitigs.
     * @return
     */
    File getBestAssembly();

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
     * Returns the type of assembler that this is
     * @return
     */
    Type getType();

    /**
     * Given a list of libraries, this method checks to see if this assembler can process them correctly
     * @param libraries List of libraries to check
     * @return True if this assembler thinks it can process the provided libraries, false otherwise
     */
    boolean acceptsLibraries(List<Library> libraries);

    /**
     * Sets the list of libraries as input for this assembler
     * @param libraries List of libraries to assemble
     */
    void setLibraries(List<Library> libraries);

    /**
     * This can be used to do any setup work between running the constructor and executing the process.  For example,
     * it can be used to add any pre or post commands that are necessary for this assembler.
     * @throws IOException
     */
    void setup() throws IOException;

    /**
     * This can be used to do any setup work between running the constructor and executing the process.  For example,
     * it can be used to add any pre or post commands that are necessary for this assembler.
     * @param
     * @param
     * @throws IOException
     */
    void initialise(AbstractProcessArgs args, ConanExecutorService ces);

    void initialise(ConanExecutorService ces);


    public enum Type {
        DE_BRUIJN,
        DE_BRUIJN_OPTIMISER,
        DE_BRUIJN_AUTO,
        OVERLAP_LAYOUT_CONSENSUS;
    }
}