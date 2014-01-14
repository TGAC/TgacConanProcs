package uk.ac.tgac.conan.process.kmer.kat;

import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatCompV1Args extends AbstractProcessArgs {

    private KatCompV1Params params = new KatCompV1Params();

    private File jellyfishHash1;
    private File jellyfishHash2;
    private File jellyfishHash3;
    private String outputPrefix;
    private int threads;
    private int memoryMb;

    public KatCompV1Args() {
        this.jellyfishHash1 = null;
        this.jellyfishHash2 = null;
        this.jellyfishHash3 = null;
        this.outputPrefix = "";
        this.threads = 1;
        this.memoryMb = 0;
    }

    public File getJellyfishHash1() {
        return jellyfishHash1;
    }

    public void setJellyfishHash1(File jellyfishHash1) {
        this.jellyfishHash1 = jellyfishHash1;
    }

    public File getJellyfishHash2() {
        return jellyfishHash2;
    }

    public void setJellyfishHash2(File jellyfishHash2) {
        this.jellyfishHash2 = jellyfishHash2;
    }

    public File getJellyfishHash3() {
        return jellyfishHash3;
    }

    public void setJellyfishHash3(File jellyfishHash3) {
        this.jellyfishHash3 = jellyfishHash3;
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(String outputPrefix) {
        this.outputPrefix = outputPrefix;
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

    @Override
    public void parse(String args) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ParamMap getArgMap() {
        ParamMap pvp = new DefaultParamMap();

        if (this.jellyfishHash1 != null) {
            pvp.put(params.getJellyfishHash1(), this.jellyfishHash1.getAbsolutePath());
        }

        if (this.jellyfishHash2 != null) {
            pvp.put(params.getJellyfishHash2(), this.jellyfishHash2.getAbsolutePath());
        }

        if (this.jellyfishHash3 != null) {
            pvp.put(params.getJellyfishHash3(), this.jellyfishHash3.getAbsolutePath());
        }

        if (this.outputPrefix != null && !this.outputPrefix.isEmpty()) {
            pvp.put(params.getOutputPrefix(), this.outputPrefix);
        }

        pvp.put(params.getThreads(), Integer.toString(this.threads));
        pvp.put(params.getMemoryMb(), Integer.toString(this.memoryMb));

        return pvp;
    }



    @Override
    protected void setOptionFromMapEntry(ConanParameter param, String value) {

        if (param.equals(this.params.getThreads())) {
            this.threads = Integer.parseInt(value);
        } else if (param.equals(this.params.getMemoryMb())) {
            this.memoryMb = Integer.parseInt(value);
        } else if (param.equals(this.params.getOutputPrefix())) {
            this.outputPrefix = value;
        } else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }

    }

    @Override
    protected void setArgFromMapEntry(ConanParameter param, String value) {

        if (param.equals(this.params.getJellyfishHash1())) {
            this.jellyfishHash1 = new File(value);
        } else if (param.equals(this.params.getJellyfishHash2())) {
            this.jellyfishHash2 = new File(value);
        } else if (param.equals(this.params.getJellyfishHash3())) {
            this.jellyfishHash3 = new File(value);
        } else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }

    }

}
