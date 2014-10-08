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

import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;
import java.util.ServiceLoader;

/**
 * User: maplesod
 * Date: 30/01/13
 * Time: 18:42
 */
public class ReadEnhancerFactory {


    public static ReadEnhancer create(String toolName, ConanExecutorService ces) {

        GenericReadEnhancerArgs genericArgs = new GenericReadEnhancerArgs();
        genericArgs.setInput(null);
        genericArgs.setOutputDir(new File(""));
        genericArgs.setThreads(1);
        genericArgs.setMemoryGb(0);
        return create(toolName, genericArgs, ces);
    }


    public static ReadEnhancer create(String toolName,
                                      GenericReadEnhancerArgs genericArgs,
                                                   ConanExecutorService ces) {

        ReadEnhancerArgs actualArgs = null;

        ServiceLoader<ReadEnhancerArgs> foundECArgsClasses = ServiceLoader.load(ReadEnhancerArgs.class);

        for(ReadEnhancerArgs reArgsClass : foundECArgsClasses) {

            if (reArgsClass.getProcessName().equalsIgnoreCase(toolName.trim())) {
                reArgsClass.setReadEnhancerArgs(genericArgs);
                actualArgs = reArgsClass;
                break;
            }
        }

        if (actualArgs == null)
            return null;

        ServiceLoader<ReadEnhancer> procLoader = ServiceLoader.load(ReadEnhancer.class);

        for(ReadEnhancer reClass : procLoader) {

            if (reClass.getProcessName().equalsIgnoreCase(toolName.trim())) {
                reClass.initialise(actualArgs, ces);
                return reClass;
            }
        }

        return null;
    }
}
