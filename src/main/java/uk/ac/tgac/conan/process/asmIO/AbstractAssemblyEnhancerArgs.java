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
package uk.ac.tgac.conan.process.asmIO;

import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: maplesod
 * Date: 28/02/13
 * Time: 11:55
 */
public abstract class AbstractAssemblyEnhancerArgs extends AbstractProcessArgs implements AssemblyEnhancerArgs {

    private String name;
    private AssemblyEnhancerType type;
    private File inputFile;
    private File outputDir;
    private String outputPrefix;
    private List<Library> libraries;
    private int threads;
    private int memory;

    protected AbstractAssemblyEnhancerArgs(ProcessParams params, String name, AssemblyEnhancerType type) {

        super(params);
        this.name = name;
        this.type = type;
        this.inputFile = null;
        this.outputDir = null;
        this.outputPrefix = "AMP";
        this.libraries = null;
        this.threads = 1;
        this.memory = 0;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(String outputPrefix) {
        this.outputPrefix = outputPrefix;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public abstract File getOutputFile();

    @Override
    public void initialise(File inputFile, File outputDir, String outputPrefix, List<Library> libs,
                                                   int threads, int memory, String otherArgs) throws IOException {

        this.setInputFile(inputFile);
        this.setOutputDir(outputDir);
        this.setOutputPrefix(outputPrefix);
        this.setLibraries(libs);
        this.setThreads(threads);
        this.setMemory(memory);
        this.parse(otherArgs);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public AssemblyEnhancerType getAssemblyEnhancerType() {
        return this.type;
    }

}
