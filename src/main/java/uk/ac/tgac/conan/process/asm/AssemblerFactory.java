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

    public static Assembler create(String toolName) throws IOException {
        return create(toolName, null);
    }

    public static Assembler create(String toolName, ConanExecutorService ces) throws IOException {
        return create(toolName, 61, null, null, 1, 0, -1, null, ces);
    }

    public static Assembler create(String toolName,
                                                   int k,
                                                   List<Library> libs,
                                                   File outputDir,
                                                   int threads,
                                                   int memory,
                                                   int coverage,
                                                   Organism organism,
                                                   ConanExecutorService ces) throws IOException {

        AssemblerArgs actualArgs = null;

        ServiceLoader<AssemblerArgs> argLoader = ServiceLoader.load(AssemblerArgs.class);

        for(AssemblerArgs args : argLoader) {
            if (args.getName().equalsIgnoreCase(toolName.trim())) {
                args.initialise(libs, outputDir, threads, memory, organism);
                actualArgs = args;
                break;
            }
        }

        if (actualArgs == null)
            return null;

        ServiceLoader<Assembler> procLoader = ServiceLoader.load(Assembler.class);

        for(Assembler assembler : procLoader) {
            if (assembler.getName().equalsIgnoreCase(toolName.trim())) {
                assembler.initialise(actualArgs, ces);
                return assembler;
            }
        }

        return null;
    }

}
