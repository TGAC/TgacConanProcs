package uk.ac.tgac.conan.process.re;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by maplesod on 10/07/14.
 */
public interface ReadEnhancer {
    /**
     * Return the process args as an AbstractReadEnhancerArgs type (all ReadEnchancers should support AbstractReadEnhancerArgs).
     * @return
     */
    AbstractReadEnhancerArgs getReadEnchancerArgs();

    /**
     * Gets the name of this process
     * @return
     */
    String getProcessName();

    /**
     * A list of all the corrected files.  Generally for single end data we expect a single file.  For paired end data
     * we expect the first two files to represent the enhanced reads, R1 and R2 respectively.  Optionally, there maybe
     * a third file representing corrected reads that are no longer paired.
     * @return A list of the files produced by this read enhancer
     */
    List<File> getEnhancedFiles();

    /**
     * Returns a library object that is constructed using the new enhanced files and an existing library
     * @return A library object representing the enhanced reads
     */
    Library getOutputLibrary(Library inputLibrary);

    /**
     * This can be used to do any setup work between running the constructor and executing the process.  For example,
     * it can be used to add any pre or post commands that are necessary for this read enchancer.
     * @throws IOException
     */
    void setup() throws IOException;

    /**
     * This can be used to do any setup work between running the constructor and executing the process.  For example,
     * it can be used to add any pre or post commands that are necessary for this read enchancer.
     * @param
     * @param
     * @throws IOException
     */
    void initialise(ReadEnhancerArgs args, ConanExecutorService ces);

    void initialise(ConanExecutorService ces);

    AbstractConanProcess toConanProcess();
}
