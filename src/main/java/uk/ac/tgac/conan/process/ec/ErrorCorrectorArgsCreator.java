package uk.ac.tgac.conan.process.ec;

import uk.ac.tgac.conan.core.data.Library;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 09/12/13
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public interface ErrorCorrectorArgsCreator {

    /**
     * Creates an instance of an AbstractAssemblyIOProcess using Basic args
     * @return
     */
    AbstractErrorCorrectorArgs create(File outputDir, Library lib, int threads,
                                  int memory, int kmer,
                                  int minLength,
                                  int minQual);

    /**
     * Returns true if this class supports paired end input
     * @return
     */
    boolean isPairedEnd();

    /**
     * Gets the name of this process
     * @return
     */
    String getName();
}
