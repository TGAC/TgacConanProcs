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
package uk.ac.tgac.conan.process.asm.stats;

import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:30
 */
public class QuastV2_2Params implements ProcessParams {

    private ConanParameter inputFiles;
    private ConanParameter labels;
    private ConanParameter outputDir;
    private ConanParameter threads;
    private ConanParameter estimatedGenomeSize;
    private ConanParameter scaffolds;

    public QuastV2_2Params() {

        this.inputFiles = new ParameterBuilder()
                .isOption(false)
                .isOptional(false)
                .longName("input")
                .description("A list of space separated input files to compare for contiguity")
                .argIndex(0)
                .argValidator(ArgValidator.OFF)
                .create();

        this.labels = new ParameterBuilder()
                .shortName("l")
                .longName("labels")
                .description("Names of assemblies to use in reports, comma-separated. If contain spaces, use quotes")
                .create();

        this.outputDir = new ParameterBuilder()
                .longName("output")
                .description("The output directory")
                .isOptional(true)
                .argValidator(ArgValidator.PATH)
                .create();

        this.threads = new ParameterBuilder()
                .longName("threads")
                .description("Maximum number of threads [default: number of CPUs]")
                .isOptional(true)
                .argValidator(ArgValidator.DIGITS)
                .create();

        this.estimatedGenomeSize = new ParameterBuilder()
                .longName("est-ref-size")
                .description("Estimated reference size (for computing NGx metrics without a reference)")
                .isOptional(true)
                .argValidator(ArgValidator.DIGITS)
                .create();

        this.scaffolds = new ParameterBuilder()
                .longName("scaffolds")
                .description("Assemblies are scaffolds, split them and add contigs to the comparison")
                .isFlag(true)
                .isOptional(true)
                .argValidator(ArgValidator.OFF)
                .create();
    }

    public ConanParameter getInputFiles() {
        return inputFiles;
    }

    public ConanParameter getOutputDir() {
        return outputDir;
    }

    public ConanParameter getThreads() {
        return threads;
    }

    public ConanParameter getLabels() {
        return labels;
    }

    public ConanParameter getEstimatedGenomeSize() {
        return estimatedGenomeSize;
    }

    public ConanParameter getScaffolds() {
        return scaffolds;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<ConanParameter>(Arrays.asList(
                new ConanParameter[]{
                        this.inputFiles,
                        this.labels,
                        this.outputDir,
                        this.threads,
                        this.estimatedGenomeSize,
                        this.scaffolds
                }
        ));
    }
}
