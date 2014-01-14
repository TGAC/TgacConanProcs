package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 11/11/13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishStatsV11Args extends AbstractProcessArgs {

    private JellyfishStatsV11Params params = new JellyfishStatsV11Params();

    private File input;
    private File output;
    private long lowerCount;
    private long upperCount;

    public JellyfishStatsV11Args() {
        this.input = null;
        this.output = null;
        this.lowerCount = 0;
        this.upperCount = Long.MAX_VALUE;
    }

    public File getInput() {
        return input;
    }

    public void setInput(File input) {
        this.input = input;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public long getLowerCount() {
        return lowerCount;
    }

    public void setLowerCount(long lowerCount) {
        this.lowerCount = lowerCount;
    }

    public long getUpperCount() {
        return upperCount;
    }

    public void setUpperCount(long upperCount) {
        this.upperCount = upperCount;
    }

    @Override
    public void parse(String args) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ParamMap getArgMap() {
        ParamMap pvp = new DefaultParamMap();

        if (this.input != null) {
            pvp.put(params.getInput(), this.input.getAbsolutePath());
        }

        if (this.output != null) {
            pvp.put(params.getOutput(), this.output.getAbsolutePath());
        }

        pvp.put(params.getLowerCount(), Long.toString(this.lowerCount));
        pvp.put(params.getUpperCount(), Long.toString(this.upperCount));

        return pvp;
    }


    @Override
    protected void setOptionFromMapEntry(ConanParameter param, String value) {

        if (param.equals(this.params.getOutput())) {
            this.output = new File(value);
        } else if (param.equals(this.params.getLowerCount())) {
            this.lowerCount = Long.parseLong(value);
        } else if (param.equals(this.params.getUpperCount())) {
            this.upperCount = Long.parseLong(value);
        } else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }

    @Override
    protected void setArgFromMapEntry(ConanParameter param, String value) {
        if (param.equals(this.params.getInput())) {
            this.input = new File(value);
        } else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }

}
