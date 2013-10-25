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

/**
 * All process args that relate to processes that modify an assembly should implement this interface.  It can be used by
 * SPI to automatically create an assembly IO process
 */
public interface AssemblyIOCreator {

    /**
     * Creates an instance of an AbstractAssemblyIOProcess using Basic args
     * @param args
     * @return
     */
    AbstractAssemblyIOProcess create(AbstractAssemblyIOArgs args);

    /**
     * Gets the name of this process
     * @return
     */
    String getName();

    /**
     * Gets the type of assembly IO process this is
     * @return
     */
    String getAssemblyIOProcessType();
}
