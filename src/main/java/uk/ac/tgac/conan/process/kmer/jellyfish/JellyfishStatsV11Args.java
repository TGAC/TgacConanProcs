package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 11/11/13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishStatsV11Args implements ProcessArgs {

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
    public Map<ConanParameter, String> getArgMap() {
        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();

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
    public void setFromArgMap(Map<ConanParameter, String> pvp) throws IOException {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();

            if (param.equals(this.params.getInput().getName())) {
                this.input = new File(entry.getValue());
            } else if (param.equals(this.params.getOutput().getName())) {
                this.output = new File(entry.getValue());
            } else if (param.equals(this.params.getLowerCount().getName())) {
                this.lowerCount = Long.parseLong(entry.getValue());
            } else if (param.equals(this.params.getUpperCount().getName())) {
                this.upperCount = Long.parseLong(entry.getValue());
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }
}
