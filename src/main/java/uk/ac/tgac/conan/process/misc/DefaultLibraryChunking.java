package uk.ac.tgac.conan.process.misc;

import org.apache.commons.io.FilenameUtils;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionResult;
import uk.ac.ebi.fgpt.conan.model.context.ExitStatus;
import uk.ac.ebi.fgpt.conan.model.context.MultiWaitResult;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 28/01/14
 * Time: 13:27
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLibraryChunking extends AbstractConanProcess implements LibraryChunking {

    private ConanExecutorService conanExecutorService;

    public DefaultLibraryChunking(ConanExecutorService conanExecutorService) {
        this.conanExecutorService = conanExecutorService;
    }

    @Override
    public int chunkLibrary(Library library, File chunkDir, String chunkPrefix, String chunkSuffix, String jobPrefix, final int seqsPerChunk, boolean runParallel)
            throws IOException, InterruptedException, ProcessExecutionException, ConanParameterException {

        if (library.isPairedEnd()) {
            ExecutionResult result1 = this.chunkFile(library.getFile1(), chunkDir, chunkPrefix, chunkSuffix, jobPrefix, "_R1", seqsPerChunk, runParallel);
            ExecutionResult result2 = this.chunkFile(library.getFile2(), chunkDir, chunkPrefix, chunkSuffix, jobPrefix, "_R2", seqsPerChunk, runParallel);

            // Wait for chunking to finish if running in parallel
            if (this.conanExecutorService.usingScheduler() && runParallel) {
                List<ExecutionResult> results = new ArrayList<>();
                results.add(result1);
                results.add(result2);

                MultiWaitResult mwResults = this.conanExecutorService.executeScheduledWait(
                        results,
                        jobPrefix + "-chunk*",
                        ExitStatus.Type.COMPLETED_ANY,
                        jobPrefix + "-wait-chunk",
                        chunkDir);
            }

            final int chunksForFile1 = this.findNbChunkFiles(chunkDir, "_R1");
            final int chunksForFile2 = this.findNbChunkFiles(chunkDir, "_R2");

            if (chunksForFile1 != chunksForFile2)
                throw new IOException("Creates different number of chunks for R1 and R2 of library: " + library.getName());

            return chunksForFile1;
        }
        else {
            ExecutionResult result = this.chunkFile(library.getFile1(), chunkDir, chunkPrefix, chunkSuffix, jobPrefix, "_SE", seqsPerChunk, false);

            return this.findNbChunkFiles(chunkDir, "_SE");
        }
    }

    private int findNbChunkFiles(File dir, final String pattern) {
        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(pattern);
            }
        });

        return files.length;
    }

    private ExecutionResult chunkFile(File file, File chunkDir, String chunkPrefix, String chunkSuffix, String jobPrefix, String fileType, int seqsPerChunk, boolean runParallel)
            throws ProcessExecutionException, InterruptedException, ConanParameterException {

        boolean gzippedInput = FilenameUtils.getExtension(file.getName()).equals("gz");


        String outputPrefix = chunkDir.getAbsolutePath() + "/" + chunkPrefix + fileType + "-";
        String outputSuffix = chunkSuffix;
        String inputPath = gzippedInput ? "-" : file.getAbsolutePath();
        String decompressCommand = gzippedInput ? "zcat " + file.getAbsolutePath() + " | " : "";
        final int lines = seqsPerChunk * 4;

        String command = decompressCommand + "split -d --lines=" + lines + " --additional-suffix=" + outputSuffix +
                " " + inputPath + " " + outputPrefix;

        return this.conanExecutorService.executeProcess(command, chunkDir, jobPrefix + "-chunk-" + fileType, 1, 0, runParallel);
    }

    @Override
    public String getName() {
        return "LibraryChunking";
    }
}
