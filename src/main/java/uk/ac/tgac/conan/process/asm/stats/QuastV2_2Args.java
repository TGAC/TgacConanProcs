/**
 * RAMPART - Robust Automatic MultiPle AssembleR Toolkit
 * Copyright (C) 2013  Daniel Mapleson - TGAC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package uk.ac.tgac.conan.process.asm.stats;

import org.apache.commons.lang.StringUtils;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:30
 */
public class QuastV2_2Args implements ProcessArgs {

    private QuastV2_2Params params = new QuastV2_2Params();

    private List<File> inputFiles;
    private List<String> labels;
    private File outputDir;
    private int threads;
    private int estimatedGenomeSize;
    private boolean scaffolds;

    public QuastV2_2Args() {
        this.inputFiles = new ArrayList<File>();
        this.labels = new ArrayList<String>();
        this.outputDir = null;
        this.threads = 1;
        this.estimatedGenomeSize = -1;
        this.scaffolds = false;
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<File> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getEstimatedGenomeSize() {
        return estimatedGenomeSize;
    }

    public void setEstimatedGenomeSize(int estimatedGenomeSize) {
        this.estimatedGenomeSize = estimatedGenomeSize;
    }

    public boolean isScaffolds() {
        return scaffolds;
    }

    public void setScaffolds(boolean scaffolds) {
        this.scaffolds = scaffolds;
    }

    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ParamMap getArgMap() {

        ParamMap pvp = new DefaultParamMap();

        if (this.inputFiles != null && !this.inputFiles.isEmpty()) {
            pvp.put(params.getInputFiles(), this.getInputFilesAsString());
        }

        if (this.labels != null && !this.labels.isEmpty()) {
            pvp.put(params.getLabels(), StringUtils.join(this.labels, ", "));
        }

        if (this.outputDir != null) {
            pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
        }

        pvp.put(params.getThreads(), Integer.toString(this.threads));

        if (this.estimatedGenomeSize > -1) {
            pvp.put(params.getEstimatedGenomeSize(), Integer.toString(this.estimatedGenomeSize));
        }

        if (this.scaffolds) {
            pvp.put(params.getScaffolds(), Boolean.toString(this.scaffolds));
        }

        return pvp;
    }

    @Override
    public void setFromArgMap(ParamMap pvp) {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            ConanParameter param = entry.getKey();
            String val = entry.getValue();

            if (param.equals(this.params.getInputFiles())) {
                String[] paths = val.split(" ");
                for(String path : paths) {
                    this.inputFiles.add(new File(path.trim()));
                }
            }
            else if (param.equals(this.params.getLabels())) {
                String[] labelArray = val.split(",");
                for(String label : labelArray) {
                    this.labels.add(label.trim());
                }
            }
            else if (param.equals(this.params.getOutputDir())) {
                this.outputDir = new File(val);
            }
            else if (param.equals(this.params.getThreads())) {
                this.threads = Integer.parseInt(val);
            }
            else if (param.equals(this.params.getEstimatedGenomeSize())) {
                this.estimatedGenomeSize = Integer.parseInt(val);
            }
            else if (param.equals(this.params.getScaffolds())) {
                this.scaffolds = Boolean.parseBoolean(val);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    public String getInputFilesAsString() {
        List<String> paths = new ArrayList<String>();
        for(File file : this.inputFiles) {
            paths.add(file.getAbsolutePath());
        }

        return StringUtils.join(paths, " ");
    }
}
