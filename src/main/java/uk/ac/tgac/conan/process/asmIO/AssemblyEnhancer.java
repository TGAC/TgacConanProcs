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

import uk.ac.ebi.fgpt.conan.model.ConanProcess;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.process.asm.AssemblerArgs;

import java.io.File;
import java.io.IOException;

/**
 * All process args that relate to processes that modify an assembly should implement this interface.  It can be used by
 * SPI to automatically create an assembly IO process
 */
public interface AssemblyEnhancer extends ConanProcess {

    /**
    * This can be used to do any setup work between running the constructor and executing the process.  For example,
    * it can be used to add any pre or post commands that are necessary for this assembly enhancer.
    * @param
    * @param
    * @throws IOException
    */
    void initialise(AssemblyEnhancerArgs args, ConanExecutorService ces) throws IOException;

    /**
     * This can be used to do any setup work between running the constructor and executing the process.  For example,
     * it can be used to add any pre or post commands that are necessary for this assembly enhancer.
     * @throws java.io.IOException
     */
    void setup() throws IOException;

    /**
     * Gets the name of this process
     * @return
     */
    String getName();

    /**
     * Gets the type of assembly enhancer process this is
     * @return
     */
    AssemblyEnhancerType getAssemblyEnhancerType();

    /**
     * Returns the arguments associated to this assembly enhancer
     * @return
     */
    AssemblyEnhancerArgs getAssemblyEnhancerArgs();

    /**
     * Gets the output directory for this assembly enhancer
     * @return
     */
    File getOutputDir();

    /**
     * Gets the output file for this assembly enhancer
     * @return
     */
    File getOutputFile();
}
