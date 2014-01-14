package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.FlagParameter;
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
 * Date: 11/11/13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishCountV11Params implements ProcessParams {

    private ConanParameter inputFile;
    private ConanParameter merLength;
    private ConanParameter hashSize;
    private ConanParameter threads;
    private ConanParameter memoryMb;
    private ConanParameter outputPrefix;
    private ConanParameter counterLength;
    private ConanParameter bothStrands;
    private ConanParameter lowerCount;
    private ConanParameter upperCount;

    public JellyfishCountV11Params() {
        super();

        this.inputFile = new ParameterBuilder()
                .isOption(false)
                .isOptional(false)
                .argIndex(0)
                .description("Input file (supports globbing)")
                .argValidator(ArgValidator.PATH)
                .create();

        this.merLength = new NumericParameter(
                "m",
                "Length of mer",
                false);

        this.hashSize = new NumericParameter(
                "s",
                "Hash size",
                false);

        this.threads = new NumericParameter(
                "t",
                "Number of threads (1)",
                true);

        this.memoryMb = new NumericParameter(
                "memory",
                "Amount of memory to request from the scheduler",
                true);

        this.outputPrefix = new ParameterBuilder()
                .shortName("o")
                .longName("output")
                .description("Output prefix (mer_counts)")
                .argValidator(ArgValidator.PATH)
                .create();

        this.counterLength = new NumericParameter(
                "c",
                "Length of counting field (7)",
                true);

        this.bothStrands = new FlagParameter(
                "C",
                "Count both strand, canonical representation (false)");

        this.lowerCount = new NumericParameter(
                "L",
                "Don't output k-mer with count < lower-count",
                true);

        this.upperCount = new NumericParameter(
                "U",
                "Don't output k-mer with count > upper-count",
                true);
    }

    public ConanParameter getInputFile() {
        return inputFile;
    }

    public ConanParameter getMerLength() {
        return merLength;
    }

    public ConanParameter getHashSize() {
        return hashSize;
    }

    public ConanParameter getThreads() {
        return threads;
    }

    public ConanParameter getMemoryMb() {
        return memoryMb;
    }

    public ConanParameter getOutputPrefix() {
        return outputPrefix;
    }

    public ConanParameter getCounterLength() {
        return counterLength;
    }

    public ConanParameter getBothStrands() {
        return bothStrands;
    }

    public ConanParameter getLowerCount() {
        return lowerCount;
    }

    public ConanParameter getUpperCount() {
        return upperCount;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<>(Arrays.asList(
                new ConanParameter[]{
                        this.inputFile,
                        this.merLength,
                        this.hashSize,
                        this.threads,
                        this.memoryMb,
                        this.outputPrefix,
                        this.counterLength,
                        this.bothStrands,
                        this.lowerCount,
                        this.upperCount
                }
        ));
    }
}
