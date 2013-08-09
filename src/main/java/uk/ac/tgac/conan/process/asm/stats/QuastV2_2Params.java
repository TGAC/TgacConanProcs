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

import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
import uk.ac.ebi.fgpt.conan.core.param.FlagParameter;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
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

        this.inputFiles = new DefaultConanParameter(
                "input",
                "A list of space separated input files to compare for contiguity",
                false
        );

        this.inputFiles = new DefaultConanParameter(
                "input",
                "Names of assemblies to use in reports, comma-separated. If contain spaces, use quotes",
                false
        );

        this.outputDir = new PathParameter(
                "output",
                "The output directory",
                true
        );

        this.threads = new NumericParameter(
                "threads",
                "Maximum number of threads [default: number of CPUs]",
                true
        );

        this.estimatedGenomeSize = new NumericParameter(
                "est-ref-size",
                "Estimated reference size (for computing NGx metrics without a reference)",
                true
        );

        this.scaffolds = new FlagParameter(
                "scaffolds",
                "Assemblies are scaffolds, split them and add contigs to the comparison"
        );
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
