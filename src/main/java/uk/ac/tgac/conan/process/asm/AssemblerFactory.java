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
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * User: maplesod
 * Date: 30/01/13
 * Time: 18:49
 */
public class AssemblerFactory {

    public static Assembler createGenericAssembler(String toolName) {
        return createGenericAssembler(toolName, null);
    }

    public static Assembler createGenericAssembler(String toolName, ConanExecutorService ces) {

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

    protected static AssemblerArgs createProcessArgs(String toolName) {

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

}
