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
package uk.ac.tgac.conan.process.ec;

import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractErrorCorrectorArgs extends AbstractProcessArgs implements ErrorCorrectorArgsCreator {

    public static final int DEFAULT_MIN_LENGTH = 60;

    private int minLength;
    private int qualityThreshold;
    private int kmer;
    private int threads;
    private int memoryGb;

    private File outputDir;

    public AbstractErrorCorrectorArgs() {

        this.minLength = DEFAULT_MIN_LENGTH;
        this.qualityThreshold = 30;
        this.kmer = 17;
        this.threads = 8;
        this.memoryGb = 20;
        this.outputDir = new File(".");
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

    public int getKmer() {
        return kmer;
    }

    public void setKmer(int kmer) {
        this.kmer = kmer;
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

    public abstract AbstractErrorCorrectorArgs copy();

    public abstract List<File> getCorrectedFiles();

    /**
     * Configures error corrector args using a Library object
     * @param lib
     */
    public void setFromLibrary(Library lib) {

        if (lib == null)
            return;

        List<File> files = new ArrayList<>();
        if (lib.isPairedEnd()) {
            files.add(lib.getFile1());
            files.add(lib.getFile2());
        }
        else {
            files.add(lib.getFile1());
        }

        this.setFromLibrary(lib, files);
    }

    /**
     * Configures error corrector args using a library, and with supplied alternate input files
     * @param lib
     * @param files Alternative input files
     */
    public abstract void setFromLibrary(Library lib, List<File> files);
}
