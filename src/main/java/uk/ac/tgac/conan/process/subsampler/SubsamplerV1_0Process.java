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
package uk.ac.tgac.conan.process.subsampler;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;

/**
 * User: maplesod
 * Date: 16/01/13
 * Time: 16:13
 */
public class SubsamplerV1_0Process extends AbstractConanProcess {

    public static final String EXE = "subsampler";

    public SubsamplerV1_0Process() {
        this(new SubsamplerV1_0Args());
    }

    public SubsamplerV1_0Process(SubsamplerV1_0Args args) {
        super(EXE, args, new SubsamplerV1_0Params());
    }

    @Override
    public String getName() {
        return "Subsampler_V1.0";
    }
}
