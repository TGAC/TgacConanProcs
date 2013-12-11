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

import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asmIO.AssemblyIOArgsCreator;
import uk.ac.tgac.conan.process.asmIO.AssemblyIOCreator;
import uk.ac.tgac.conan.process.ec.musket.MusketV106Process;
import uk.ac.tgac.conan.process.ec.quake.QuakeV034Process;
import uk.ac.tgac.conan.process.ec.sickle.SickleV11Process;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ServiceLoader;

/**
 * User: maplesod
 * Date: 30/01/13
 * Time: 18:42
 */
public class ErrorCorrectorFactory {


    public static AbstractErrorCorrector create(String toolName, ConanProcessService conanProcessService) {
        return create(toolName, new File("."), null, 1, 0, 17, 8, 20, conanProcessService);
    }


    public static AbstractErrorCorrector create(String toolName,
                                                   File outputDir,
                                                   Library lib,
                                                   int threads,
                                                   int memory,
                                                   int kmer,
                                                   int minLength,
                                                   int minQual,
                                                   ConanProcessService conanProcessService) {

        AbstractErrorCorrectorArgs actualArgs = null;

        ServiceLoader<ErrorCorrectorArgsCreator> foundECArgsClasses = ServiceLoader.load(ErrorCorrectorArgsCreator.class);

        for(ErrorCorrectorArgsCreator ecArgsClass : foundECArgsClasses) {

            if (ecArgsClass.getName().equalsIgnoreCase(toolName.trim())) {

                if (lib == null || lib.isPairedEnd() == ecArgsClass.isPairedEnd()) {
                    actualArgs = ecArgsClass.create(outputDir, lib, threads, memory, kmer, minLength, minQual);
                    break;
                }
            }
        }

        if (actualArgs == null)
            return null;

        ServiceLoader<ErrorCorrectorCreator> procLoader = ServiceLoader.load(ErrorCorrectorCreator.class);

        for(ErrorCorrectorCreator ecProc : procLoader) {

            if (ecProc.getName().equalsIgnoreCase(toolName.trim())) {
                AbstractErrorCorrector aec = ecProc.create(actualArgs);
                aec.setConanProcessService(conanProcessService);
                return aec;
            }
        }

        return null;
    }
}
