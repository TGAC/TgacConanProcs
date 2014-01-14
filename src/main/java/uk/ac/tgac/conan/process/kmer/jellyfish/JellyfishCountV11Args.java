package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 11/11/13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishCountV11Args extends AbstractProcessArgs {

    private JellyfishCountV11Params params = new JellyfishCountV11Params();

    private String inputFile;
    private int merLength;
    private long hashSize;
    private int threads;
    private int memoryMb;
    private String outputPrefix;
    private int counterLength;
    private boolean bothStrands;
    private long lowerCount;
    private long upperCount;

    public JellyfishCountV11Args() {
        this.inputFile = null;
        this.merLength = -1;
        this.hashSize = -1;
        this.threads = 1;
        this.memoryMb = 0;
        this.outputPrefix = "";
        this.counterLength = 7;
        this.bothStrands = false;
        this.lowerCount = 0;
        this.upperCount = Long.MAX_VALUE;
    }


    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public int getMerLength() {
        return merLength;
    }

    public void setMerLength(int merLength) {
        this.merLength = merLength;
    }

    public long getHashSize() {
        return hashSize;
    }

    public void setHashSize(long hashSize) {
        this.hashSize = hashSize;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getMemoryMb() {
        return memoryMb;
    }

    public void setMemoryMb(int memoryMb) {
        this.memoryMb = memoryMb;
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(String outputPrefix) {
        this.outputPrefix = outputPrefix;
    }

    public int getCounterLength() {
        return counterLength;
    }

    public void setCounterLength(int counterLength) {
        this.counterLength = counterLength;
    }

    public boolean isBothStrands() {
        return bothStrands;
    }

    public void setBothStrands(boolean bothStrands) {
        this.bothStrands = bothStrands;
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

        if (this.inputFile != null && !this.inputFile.isEmpty()) {
            pvp.put(params.getInputFile(), this.inputFile);
        }

        pvp.put(params.getMerLength(), Integer.toString(this.merLength));
        pvp.put(params.getHashSize(), Long.toString(this.hashSize));
        pvp.put(params.getThreads(), Integer.toString(this.threads));
        pvp.put(params.getMemoryMb(), Integer.toString(this.memoryMb));

        if (this.outputPrefix != null && !this.outputPrefix.isEmpty()) {
            pvp.put(params.getOutputPrefix(), this.outputPrefix);
        }

        pvp.put(params.getCounterLength(), Integer.toString(this.counterLength));
        pvp.put(params.getBothStrands(), Boolean.toString(this.bothStrands));
        pvp.put(params.getLowerCount(), Long.toString(this.lowerCount));

        if (this.upperCount != Long.MAX_VALUE) {
            pvp.put(params.getUpperCount(), Long.toString(this.upperCount));
        }

        return pvp;
    }

    @Override
    protected void setOptionFromMapEntry(ConanParameter param, String value) {

        if (param.equals(this.params.getMerLength())) {
            this.merLength = Integer.parseInt(value);
        } else if (param.equals(this.params.getHashSize())) {
            this.hashSize = Long.parseLong(value);
        } else if (param.equals(this.params.getThreads())) {
            this.threads = Integer.parseInt(value);
        } else if (param.equals(this.params.getMemoryMb())) {
            this.memoryMb = Integer.parseInt(value);
        } else if (param.equals(this.params.getOutputPrefix())) {
            this.outputPrefix = value;
        } else if (param.equals(this.params.getCounterLength())) {
            this.counterLength = Integer.parseInt(value);
        } else if (param.equals(this.params.getBothStrands())) {
            this.bothStrands = Boolean.parseBoolean(value);
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

        if (param.equals(this.params.getInputFile())) {
            this.inputFile = value;
        } else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }

}
