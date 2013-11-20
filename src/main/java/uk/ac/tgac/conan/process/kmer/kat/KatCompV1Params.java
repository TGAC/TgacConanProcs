package uk.ac.tgac.conan.process.kmer.kat;

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
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatCompV1Params implements ProcessParams {

    private ConanParameter jellyfishHash1;
    private ConanParameter jellyfishHash2;
    private ConanParameter outputPrefix;
    private ConanParameter threads;
    private ConanParameter memoryMb;

    public KatCompV1Params() {

        this.jellyfishHash1 = new PathParameter(
                "input1",
                "First jellyfish hash",
                false);

        this.jellyfishHash2 = new PathParameter(
                "input2",
                "Second jellyfish hash",
                false);

        this.outputPrefix = new DefaultConanParameter(
                "o",
                "Output prefix",
                false,
                true,
                true);

        this.threads = new NumericParameter(
                "t",
                "Number of threads (1)",
                true);

        this.memoryMb = new NumericParameter(
                "memory",
                "Amount of memory to request from the scheduler",
                true);
    }

    public ConanParameter getJellyfishHash1() {
        return jellyfishHash1;
    }

    public ConanParameter getJellyfishHash2() {
        return jellyfishHash2;
    }

    public ConanParameter getOutputPrefix() {
        return outputPrefix;
    }

    public ConanParameter getThreads() {
        return threads;
    }

    public ConanParameter getMemoryMb() {
        return memoryMb;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<>(Arrays.asList(
                new ConanParameter[]{
                        this.jellyfishHash1,
                        this.jellyfishHash2,
                        this.outputPrefix,
                        this.threads,
                        this.memoryMb
                }
        ));
    }
}
