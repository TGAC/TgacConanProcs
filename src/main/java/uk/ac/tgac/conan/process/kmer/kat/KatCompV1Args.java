package uk.ac.tgac.conan.process.kmer.kat;

import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;
import uk.ac.tgac.conan.process.kmer.jellyfish.JellyfishCountV11Params;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatCompV1Args implements ProcessArgs {

    private KatCompV1Params params = new KatCompV1Params();

    private File jellyfishHash1;
    private File jellyfishHash2;
    private String outputPrefix;
    private int threads;
    private int memoryMb;

    public KatCompV1Args() {
        this.jellyfishHash1 = null;
        this.jellyfishHash2 = null;
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
    public Map<ConanParameter, String> getArgMap() {
        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();

        if (this.jellyfishHash1 != null) {
            pvp.put(params.getJellyfishHash1(), this.jellyfishHash1.getAbsolutePath());
        }

        if (this.jellyfishHash2 != null) {
            pvp.put(params.getJellyfishHash2(), this.jellyfishHash2.getAbsolutePath());
        }

        if (this.outputPrefix != null && !this.outputPrefix.isEmpty()) {
            pvp.put(params.getOutputPrefix(), this.outputPrefix);
        }

        pvp.put(params.getThreads(), Integer.toString(this.threads));
        pvp.put(params.getMemoryMb(), Integer.toString(this.memoryMb));

        return pvp;
    }

    @Override
    public void setFromArgMap(Map<ConanParameter, String> pvp) throws IOException {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();

            if (param.equals(this.params.getJellyfishHash1().getName())) {
                this.jellyfishHash1 = new File(entry.getValue());
            } else if (param.equals(this.params.getJellyfishHash2().getName())) {
                this.jellyfishHash2 = new File(entry.getValue());
            } else if (param.equals(this.params.getThreads().getName())) {
                this.threads = Integer.parseInt(entry.getValue());
            } else if (param.equals(this.params.getMemoryMb().getName())) {
                this.memoryMb = Integer.parseInt(entry.getValue());
            } else if (param.equals(this.params.getOutputPrefix().getName())) {
                this.outputPrefix = entry.getValue();
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }
}
