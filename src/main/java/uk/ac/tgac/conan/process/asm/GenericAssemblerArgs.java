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
package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.Organism;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class GenericAssemblerArgs {

    private int memory;
    private int threads;
    private File outputDir;
    private List<Library> libraries;
    private Organism organism;
    private int desiredCoverage;

    public GenericAssemblerArgs() {
        this.memory = 0;
        this.threads = 1;
        this.outputDir = new File(".");
        this.libraries = new ArrayList<>();
        this.organism = null;
        this.desiredCoverage = -1;
    }

    public GenericAssemblerArgs(GenericAssemblerArgs args) {
        this.memory = args.getMemory();
        this.threads = args.getThreads();
        this.outputDir = new File(args.getOutputDir().getPath());
        this.libraries = new ArrayList<>(args.getLibraries());
        this.organism = args.getOrganism();
        this.desiredCoverage = args.getDesiredCoverage();
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getDesiredCoverage() {
        return desiredCoverage;
    }

    public void setDesiredCoverage(int desiredCoverage) {
        this.desiredCoverage = desiredCoverage;
    }

    public AssemblerArgs createProcessArgs(String toolName) {

        ServiceLoader<DeBruijnArgs> dbgArgs = ServiceLoader.load(DeBruijnArgs.class);
        ServiceLoader<DeBruijnOptimiserArgs> dbgOptArgs = ServiceLoader.load(DeBruijnOptimiserArgs.class);
        ServiceLoader<DeBruijnAutoArgs> dbgAutoArgs = ServiceLoader.load(DeBruijnAutoArgs.class);

        List<AssemblerArgs> argClasses = new ArrayList<>();
        for(DeBruijnArgs args : dbgArgs) {
            argClasses.add(args);
        }

        for(DeBruijnOptimiserArgs args : dbgOptArgs) {
            argClasses.add(args);
        }

        for(DeBruijnAutoArgs args : dbgAutoArgs) {
            argClasses.add(args);
        }

        for(AssemblerArgs args : argClasses) {
            if (args.getProcessName().equalsIgnoreCase(toolName)) {
                return args;
            }
        }

        return null;
    }

    public Assembler createAssembler(String toolName) {
        return this.createAssembler(toolName, null);
    }

    public Assembler createAssembler(String toolName, ConanExecutorService ces) {

        AssemblerArgs args = createProcessArgs(toolName);

        if (args == null)
            return null;

        ServiceLoader<Assembler> procLoader = ServiceLoader.load(Assembler.class);

        for(Assembler assembler : procLoader) {
            if (assembler.getName().equalsIgnoreCase(toolName.trim())) {
                assembler.initialise(args.toConanArgs(), ces);
                return assembler;
            }
        }

        return null;
    }

    protected Assembler createAssembler(String toolName, AbstractProcessArgs args, ConanExecutorService ces) {

        if (args == null)
            throw new IllegalArgumentException("Provided assembler args are null");

        ServiceLoader<Assembler> procLoader = ServiceLoader.load(Assembler.class);

        for(Assembler assembler : procLoader) {
            if (assembler.getName().equalsIgnoreCase(toolName.trim())) {
                assembler.initialise(args, ces);
                return assembler;
            }
        }

        throw new IllegalArgumentException("Could not find the requested assembler: " + toolName);
    }
}
