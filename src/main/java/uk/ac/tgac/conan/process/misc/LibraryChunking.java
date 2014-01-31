package uk.ac.tgac.conan.process.misc;

import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 28/01/14
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public interface LibraryChunking {

    public int chunkLibrary(Library library, File chunkDir, String chunkPrefix, String chunkSuffix, String jobPrefix, final int seqsPerChunk, boolean runParallel)
            throws IOException, InterruptedException, ProcessExecutionException, ConanParameterException;
}
