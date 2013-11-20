package uk.ac.tgac.conan.process.kmer.jellyfish;

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
 * Date: 11/11/13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishStatsV11Params implements ProcessParams {

    private ConanParameter input;
    private ConanParameter output;
    private ConanParameter lowerCount;
    private ConanParameter upperCount;

    public JellyfishStatsV11Params() {
        super();

        this.input = new PathParameter(
                "input",
                "Input file",
                false);

        this.output = new PathParameter(
                "o",
                "Output file",
                false);

        this.lowerCount = new NumericParameter(
                "L",
                "Don't consider k-mer with count < lower-count",
                true);

        this.upperCount = new NumericParameter(
                "U",
                "Don't consider k-mer with count > upper-count",
                true);
    }

    public ConanParameter getInput() {
        return input;
    }

    public ConanParameter getOutput() {
        return output;
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
                        this.input,
                        this.output,
                        this.lowerCount,
                        this.upperCount
                }
        ));
    }
}
