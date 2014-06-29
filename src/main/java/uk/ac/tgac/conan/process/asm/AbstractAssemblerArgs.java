package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dan on 29/06/14.
 */
public abstract class AbstractAssemblerArgs extends AbstractProcessArgs implements AssemblerArgs {

    private String processName;
    protected File outputDir;
    protected List<Library> libs;
    protected int threads;
    protected int maxMemUsageMB;

    protected AbstractAssemblerArgs(ProcessParams params, String processName) {

        super(params);

        this.processName = processName;
        this.outputDir = new File("");
        this.libs = new ArrayList<>();
        this.threads = 1;
        this.maxMemUsageMB = 0;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public List<Library> getLibs() {
        return libs;
    }

    public void setLibs(List<Library> libs) {
        this.libs = libs;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getMaxMemUsageMB() {
        return maxMemUsageMB;
    }

    public void setMaxMemUsageMB(int maxMemUsageMB) {
        this.maxMemUsageMB = maxMemUsageMB;
    }

    @Override
    public String getProcessName() {
        return processName;
    }

    @Override
    public AbstractProcessArgs toConanArgs() {
        return this;
    }
}
