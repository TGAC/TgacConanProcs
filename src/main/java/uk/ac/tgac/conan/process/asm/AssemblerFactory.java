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
import java.io.IOException;
import java.util.List;
import java.util.ServiceLoader;

/**
 * User: maplesod
 * Date: 30/01/13
 * Time: 18:49
 */
public class AssemblerFactory {

    public static Assembler createGenericAssembler(String toolName) throws IOException {
        return createGenericAssembler(toolName, null);
    }

    public static Assembler createGenericAssembler(String toolName, ConanExecutorService ces) throws IOException {
        AssemblerArgs actualArgs = null;

        ServiceLoader<AssemblerArgs> argLoader = ServiceLoader.load(AssemblerArgs.class);

        for(AssemblerArgs args : argLoader) {
            if (args.getProcessName().equalsIgnoreCase(toolName.trim())) {
                actualArgs = args;
                break;
            }
        }

        if (actualArgs == null)
            return null;

        ServiceLoader<Assembler> procLoader = ServiceLoader.load(Assembler.class);

        for(Assembler assembler : procLoader) {
            if (assembler.getName().equalsIgnoreCase(toolName.trim())) {
                assembler.initialise(actualArgs.toConanArgs(), ces);
                return assembler;
            }
        }

        return null;
    }

    public static Assembler createDeBruijnAssembler(String toolName,
                            ConanExecutorService ces,
                            GenericDeBruijnArgs dbgArgs) throws IOException {

        DeBruijnArgs args = createDeBruijnArgs(toolName, dbgArgs);

        if (args == null)
            throw new IllegalArgumentException("Provided assembler args are null");

        Assembler asm = createAssembler(toolName, args.toConanArgs(), ces);

        if (asm.getType() != Assembler.Type.DE_BRUIJN) {
            throw new IllegalArgumentException("Assembler \"" + toolName + "\" is not a De Bruijn graph assembler that allows you to specify a specific K value");
        }

        return asm;
    }

    public static Assembler createDeBruijnOptimiserAssembler(String toolName,
                                                         ConanExecutorService ces,
                                                         GenericDeBruijnOptimiserArgs dbgOptArgs) throws IOException {

        DeBruijnOptimiserArgs args = createDeBruijnOptimiserArgs(toolName, dbgOptArgs);

        if (args == null)
            throw new IllegalArgumentException("Provided assembler args are null");

        Assembler asm = createAssembler(toolName, args.toConanArgs(), ces);

        if (asm.getType() != Assembler.Type.DE_BRUIJN_OPTIMISER) {
            throw new IllegalArgumentException("Assembler \"" + toolName + "\" is not a De Bruijn graph assembler that optimises K value ranges");
        }

        return asm;
    }


    public static Assembler createDeBruijnFixedAssembler(String toolName,
                                                    List<Library> libs,
                                                    File outputDir,
                                                    int threads,
                                                    int memory,
                                                    Organism organism,
                                                    ConanExecutorService ces) throws IOException {

        AssemblerArgs args = createDeBruijnArgs(toolName);
        args.initialise(libs, outputDir, threads, memory, organism);

        if (args.getType() != Assembler.Type.DE_BRUIJN_AUTO) {
            throw new IllegalArgumentException("Assembler \"" + toolName + "\" is not a De Bruijn graph assembler that handles K values automatically");
        }

        GenericDeBruijnAutoArgs dbgArgs = (GenericDeBruijnAutoArgs)args;

        Assembler assembler = createAssembler(toolName, dbgArgs, ces);

        if (assembler.getType() != Assembler.Type.DE_BRUIJN_AUTO) {
            throw new IllegalArgumentException("Assembler \"" + toolName + "\" is not a De Bruijn graph assembler that handles K values automatically");
        }

        return assembler;
    }

    protected static DeBruijnArgs createDeBruijnArgs(String toolName, GenericDeBruijnArgs other) {

        ServiceLoader<DeBruijnArgs> argLoader = ServiceLoader.load(DeBruijnArgs.class);

        for(DeBruijnArgs args : argLoader) {
            if (args.getProcessName().equalsIgnoreCase(toolName.trim())) {
                args.setDeBruijnArgs(other);
                return args;
            }
        }

        throw new IllegalArgumentException("Could not find the requested assembler: " + toolName);
    }

    protected static DeBruijnOptimiserArgs createDeBruijnOptimiserArgs(String toolName, GenericDeBruijnOptimiserArgs other) {
        ServiceLoader<DeBruijnOptimiserArgs> argLoader = ServiceLoader.load(DeBruijnOptimiserArgs.class);

        for(DeBruijnOptimiserArgs args : argLoader) {
            if (args.getProcessName().equalsIgnoreCase(toolName.trim())) {
                args.setDeBruijnOptimiserArgs(other);
                return args;
            }
        }

        throw new IllegalArgumentException("Could not find the requested assembler: " + toolName);
    }


    protected static Assembler createAssembler(String toolName, AbstractProcessArgs args, ConanExecutorService ces)
            throws IOException {

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
