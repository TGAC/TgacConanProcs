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

import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.util.List;
import java.util.ServiceLoader;

/**
 * User: maplesod
 * Date: 20/08/13
 * Time: 21:32
 */
public class AssemblyIOFactory {


    public static AbstractAssemblyIOProcess create(String toolName,
                                                   File inputFile,
                                                   File outputDir,
                                                   String outputPrefix,
                                                   List<Library> libs,
                                                   int threads,
                                                   int memory,
                                                   String otherArgs) {

        AbstractAssemblyIOArgs actualArgs = null;

        ServiceLoader<AssemblyIOArgsCreator> argsLoader = ServiceLoader.load(AssemblyIOArgsCreator.class);

        for(AssemblyIOArgsCreator aioArgs : argsLoader) {

            if (aioArgs.getName().equalsIgnoreCase(toolName.trim())) {
                actualArgs = aioArgs.create(inputFile, outputDir, outputPrefix, libs, threads, memory, otherArgs);
                break;
            }
        }

        if (actualArgs == null)
            return null;

        ServiceLoader<AssemblyIOCreator> procLoader = ServiceLoader.load(AssemblyIOCreator.class);

        for(AssemblyIOCreator aioProc : procLoader) {

            if (aioProc.getName().equalsIgnoreCase(toolName.trim())) {
                return aioProc.create(actualArgs);
            }
        }

        return null;
    }

}
