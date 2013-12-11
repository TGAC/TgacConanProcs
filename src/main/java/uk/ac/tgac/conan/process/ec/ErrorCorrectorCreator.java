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

import uk.ac.ebi.fgpt.conan.model.ConanProcess;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOArgs;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOProcess;

/**
 * An Error Corrector is a process that attempts to either discard, correct or otherwise improve NGS datasets
 */
public interface ErrorCorrectorCreator extends ConanProcess {


    /**
     * Creates an instance of an AbstractAssemblyIOProcess using Basic args
     * @param args
     * @return
     */
    AbstractErrorCorrector create(AbstractErrorCorrectorArgs args);

    /**
     * Gets the name of this process
     * @return
     */
    String getName();

    /**
     * Returns the process args for this error corrector type case as AbstractErrorCorrectorArgs
     * @return
     */
    AbstractErrorCorrectorArgs getArgs();

    /**
     * This can be used to do any setup work between running the constructor and executing the process.  For example,
     * it can be used to add any pre or post commands that are necessary for this error correction process
     */
    void initialise();
}
