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
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;

import java.util.Arrays;
import java.util.List;

public class SicklePeV11Params extends SickleV11Params {

    private ConanParameter peFile1;
    private ConanParameter peFile2;
    private ConanParameter outputPe1;
    private ConanParameter outputPe2;
    private ConanParameter outputSingles;


    public SicklePeV11Params() {

        super();

        this.peFile1 = new ParameterBuilder()
                .longName("pe-file1")
                .description("Input paired-end fastq file 1 (required, must have same number of records as pe2)")
                .isOptional(false)
                .argValidator(ArgValidator.PATH)
                .create();

        this.peFile2 = new ParameterBuilder()
                .longName("pe-file2")
                .description("Input paired-end fastq file 2 (required, must have same number of records as pe1)")
                .isOptional(false)
                .argValidator(ArgValidator.PATH)
                .create();

        this.outputPe1 = new ParameterBuilder()
                .longName("output-pe1")
                .description("Output trimmed fastq file 1 (required)")
                .isOptional(false)
                .argValidator(ArgValidator.PATH)
                .create();

        this.outputPe2 = new ParameterBuilder()
                .longName("output-pe2")
                .description("Output trimmed fastq file 2 (required)")
                .isOptional(false)
                .argValidator(ArgValidator.PATH)
                .create();

        this.outputSingles = new ParameterBuilder()
                .longName("output-single")
                .description("Output trimmed singles fastq file (required)")
                .isOptional(false)
                .argValidator(ArgValidator.PATH)
                .create();
    }

    public ConanParameter getPeFile1() {
        return peFile1;
    }

    public ConanParameter getPeFile2() {
        return peFile2;
    }

    public ConanParameter getOutputPe1() {
        return outputPe1;
    }

    public ConanParameter getOutputPe2() {
        return outputPe2;
    }

    public ConanParameter getOutputSingles() {
        return outputSingles;
    }


    @Override
    public List<ConanParameter> getConanParameters() {

        List<ConanParameter> params = super.getConanParameters();

        params.addAll(Arrays.asList(this.peFile1,
                this.peFile2,
                this.outputPe1,
                this.outputPe2,
                this.outputSingles));

        return params;
    }
}
