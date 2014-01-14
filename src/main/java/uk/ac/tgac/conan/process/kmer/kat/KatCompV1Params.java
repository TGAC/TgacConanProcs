package uk.ac.tgac.conan.process.kmer.kat;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
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
    private ConanParameter jellyfishHash3;
    private ConanParameter outputPrefix;
    private ConanParameter threads;
    private ConanParameter memoryMb;

    public KatCompV1Params() {

        this.jellyfishHash1 = new ParameterBuilder()
                .isOption(false)
                .isOptional(false)
                .argIndex(0)
                .description("First jellyfish hash")
                .argValidator(ArgValidator.PATH)
                .create();

        this.jellyfishHash2 = new ParameterBuilder()
                .isOption(false)
                .isOptional(false)
                .argIndex(1)
                .description("Second jellyfish hash")
                .argValidator(ArgValidator.PATH)
                .create();

        this.jellyfishHash3 = new ParameterBuilder()
                .isOption(false)
                .isOptional(true)
                .argIndex(2)
                .description("Third jellyfish hash")
                .argValidator(ArgValidator.PATH)
                .create();

        this.outputPrefix = new ParameterBuilder()
                .shortName("o")
                .longName("output")
                .description("Output prefix")
                .argValidator(ArgValidator.PATH)
                .create();

        this.threads = new ParameterBuilder()
                .shortName("t")
                .longName("threads")
                .description("Number of threads (1)")
                .argValidator(ArgValidator.DIGITS)
                .isOptional(true)
                .create();

        this.memoryMb = new ParameterBuilder()
                .longName("memory")
                .description("Amount of memory to request from the scheduler")
                .isOptional(true)
                .argValidator(ArgValidator.DIGITS)
                .create();
    }

    public ConanParameter getJellyfishHash1() {
        return jellyfishHash1;
    }

    public ConanParameter getJellyfishHash2() {
        return jellyfishHash2;
    }

    public ConanParameter getJellyfishHash3() {
        return jellyfishHash3;
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
                        this.jellyfishHash3,
                        this.outputPrefix,
                        this.threads,
                        this.memoryMb
                }
        ));
    }
}
