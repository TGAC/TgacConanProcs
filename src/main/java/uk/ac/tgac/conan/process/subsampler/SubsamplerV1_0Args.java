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
package uk.ac.tgac.conan.process.subsampler;

import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class SubsamplerV1_0Args implements ProcessArgs {

    private SubsamplerV1_0Params params = new SubsamplerV1_0Params();


    private boolean append;
    private File inputFile;
    private File outputFile;
    private File logFile;
    private int maxReadLength;
    private int maxNameLength;
    private double probability;
    private long seed;

    public SubsamplerV1_0Args() {
        this.append = false;
        this.inputFile = null;
        this.outputFile = null;
        this.logFile = null;
        this.maxReadLength = 2000;
        this.maxNameLength = 1000;
        this.probability = 0.0;
        this.seed = 0;
    }


    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public int getMaxReadLength() {
        return maxReadLength;
    }

    public void setMaxReadLength(int maxReadLength) {
        this.maxReadLength = maxReadLength;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<ConanParameter, String> getArgMap() {

        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();

        if (this.append) {
            pvp.put(params.getAppend(), Boolean.toString(this.append));
        }

        if (this.inputFile != null) {
            pvp.put(params.getInputFile(), this.inputFile.getAbsolutePath());
        }

        if (this.outputFile != null) {
            pvp.put(params.getOutputFile(), this.outputFile.getAbsolutePath());
        }

        if (this.logFile != null) {
            pvp.put(params.getLogFile(), this.logFile.getAbsolutePath());
        }

        if (this.maxReadLength != 2000 && this.maxReadLength > 0) {
            pvp.put(params.getMaxReadLength(), Integer.toString(this.maxReadLength));
        }

        if (this.maxNameLength != 1000 && this.maxNameLength > 0) {
            pvp.put(params.getMaxNameLength(), Integer.toString(this.maxNameLength));
        }

        if (this.probability > 0.0 && this.probability < 1.0) {
            pvp.put(params.getProbability(), Double.toString(this.probability));
        }

        if (this.seed > 0) {
            pvp.put(params.getSeed(), Long.toString(this.seed));
        }

        return pvp;
    }

    @Override
    public void setFromArgMap(Map<ConanParameter, String> pvp) {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();
            String val = entry.getValue();

            if (param.equals(this.params.getAppend().getName())) {
                this.append = Boolean.parseBoolean(val);
            }
            else if (param.equals(this.params.getInputFile().getName())) {
                this.inputFile = new File(val);
            }
            else if (param.equals(this.params.getOutputFile().getName())) {
                this.outputFile = new File(val);
            }
            else if (param.equals(this.params.getLogFile().getName())) {
                this.logFile = new File(val);
            }
            else if (param.equals(this.params.getMaxReadLength().getName())) {
                this.maxReadLength = Integer.parseInt(val);
            }
            else if (param.equals(this.params.getMaxNameLength().getName())) {
                this.maxNameLength = Integer.parseInt(val);
            }
            else if (param.equals(this.params.getProbability().getName())) {
                this.probability = Double.parseDouble(val);
            }
            else if (param.equals(this.params.getSeed().getName())) {
                this.seed = Long.parseLong(val);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }
}
