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

import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Uses ServiceLoader and SPI to create a specific AssemblyIOProcess based on a tool name and a set of common properties
 */
public class AssemblyEnhancerFactory {


    public static AssemblyEnhancer create(String toolName,
                                                   File inputFile,
                                                   File outputDir,
                                                   String outputPrefix,
                                                   List<Library> libs,
                                                   int threads,
                                                   int memory,
                                                   String otherArgs,
                                                   ConanExecutorService ces) throws IOException {

        AssemblyEnhancerArgs actualArgs = null;

        ServiceLoader<AssemblyEnhancerArgs> foundAsmIOArgsClasses = ServiceLoader.load(AssemblyEnhancerArgs.class);

        for(AssemblyEnhancerArgs args : foundAsmIOArgsClasses) {

            if (args.getName().equalsIgnoreCase(toolName.trim())) {
                args.initialise(inputFile, outputDir, outputPrefix, libs, threads, memory, otherArgs);
                actualArgs = args;
                break;
            }
        }

        if (actualArgs == null)
            return null;

        ServiceLoader<AssemblyEnhancer> procLoader = ServiceLoader.load(AssemblyEnhancer.class);

        for(AssemblyEnhancer proc : procLoader) {

            if (proc.getName().equalsIgnoreCase(toolName.trim())) {
                proc.initialise(actualArgs, ces);
                return proc;
            }
        }

        return null;
    }

}
