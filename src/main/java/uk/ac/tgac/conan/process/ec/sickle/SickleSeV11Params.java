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
package uk.ac.tgac.conan.process.ec.sickle;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;

import java.util.Arrays;
import java.util.List;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 15:04
 */
public class SickleSeV11Params extends SickleV11Params {

    private ConanParameter inputFile;
    private ConanParameter outputFile;

    public SickleSeV11Params() {

        super();

        this.inputFile = new ParameterBuilder()
                .longName("fastq-file")
                .description("Input fastq file (required)")
                .isOptional(false)
                .argValidator(ArgValidator.PATH)
                .create();

        this.outputFile = new ParameterBuilder()
                .longName("output-file")
                .description("Output trimmed fastq file (required)")
                .isOptional(false)
                .argValidator(ArgValidator.PATH)
                .create();
    }


    public ConanParameter getSeFile() {
        return inputFile;
    }

    public ConanParameter getOutputFile() {
        return outputFile;
    }

    @Override
    public List<ConanParameter> getConanParameters() {

        List<ConanParameter> params = super.getConanParameters();

        params.addAll(Arrays.asList(this.inputFile,
                this.outputFile));

        return params;
    }
}
