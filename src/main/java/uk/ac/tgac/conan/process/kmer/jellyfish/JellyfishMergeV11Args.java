package uk.ac.tgac.conan.process.kmer.jellyfish;

import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishMergeV11Args implements ProcessArgs {

    private JellyfishMergeV11Params params = new JellyfishMergeV11Params();

    private List<File> inputFiles;
    private File outputFile;
    private long bufferSize;

    public JellyfishMergeV11Args() {
        this.inputFiles = new ArrayList<>();
        this.outputFile = null;
        this.bufferSize = 10000000;
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<File> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public long getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(long bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void parse(String args) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ParamMap getArgMap() {

        ParamMap pvp = new DefaultParamMap();

        if (this.inputFiles != null && !this.inputFiles.isEmpty()) {

            List<String> paths = new ArrayList<>();
            for(File f : this.inputFiles) {
                paths.add(f.getAbsolutePath());
            }

            pvp.put(params.getInputFiles(), StringUtils.join(paths, " "));
        }

        pvp.put(params.getBufferSize(), Long.toString(this.bufferSize));

        if (this.outputFile != null) {
            pvp.put(params.getOutputFile(), this.outputFile.getAbsolutePath());
        }

        return pvp;
    }

    @Override
    public void setFromArgMap(ParamMap pvp) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
