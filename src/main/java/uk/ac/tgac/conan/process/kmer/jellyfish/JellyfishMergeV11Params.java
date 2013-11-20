package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishMergeV11Params implements ProcessParams {

    private ConanParameter inputFiles;
    private ConanParameter outputFile;
    private ConanParameter bufferSize;

    public JellyfishMergeV11Params() {

        this.inputFiles = new DefaultConanParameter(
                "inputs",
                "List of input files (supports globbing)",
                false,
                false,
                false);

        this.outputFile = new PathParameter(
                "o",
                "Output file",
                true);

        this.bufferSize = new NumericParameter(
                "s",
                "Length in bytes of input buffer (1000000)",
                true);

    }

    public ConanParameter getInputFiles() {
        return inputFiles;
    }

    public ConanParameter getOutputFile() {
        return outputFile;
    }

    public ConanParameter getBufferSize() {
        return bufferSize;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<>(Arrays.asList(
                new ConanParameter[]{
                        this.inputFiles,
                        this.outputFile,
                        this.bufferSize
                }
        ));
    }
}
