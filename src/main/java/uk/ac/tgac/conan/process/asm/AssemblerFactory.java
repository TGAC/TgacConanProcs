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

import java.io.IOException;
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

}
