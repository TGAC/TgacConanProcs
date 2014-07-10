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
package uk.ac.tgac.conan.process.re;

import uk.ac.tgac.conan.core.data.Library;

import java.io.File;

public class GenericReadEnhancerArgs {

    public static final int DEFAULT_MIN_LENGTH = 60;

    private int minLength;
    private int qualityThreshold;
    private int threads;
    private int memoryGb;
    private File outputDir;
    private Library input;

    public GenericReadEnhancerArgs() {

        this.minLength = DEFAULT_MIN_LENGTH;
        this.qualityThreshold = 30;
        this.threads = 8;
        this.memoryGb = 20;
        this.outputDir = new File("");
        this.input = null;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getQualityThreshold() {
        return qualityThreshold;
    }

    public void setQualityThreshold(int qualityThreshold) {
        this.qualityThreshold = qualityThreshold;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getMemoryGb() {
        return memoryGb;
    }

    public void setMemoryGb(int memoryGb) {
        this.memoryGb = memoryGb;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public Library getInput() {
        return input;
    }

    public void setInput(Library input) {
        this.input = input;
    }
}
