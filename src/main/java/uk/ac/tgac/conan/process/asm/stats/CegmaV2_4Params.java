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
public class CegmaV2_4Params implements ProcessParams {

    private ConanParameter genomeFile;
    private ConanParameter outputPrefix;
    private ConanParameter threads;

    public CegmaV2_4Params() {

        this.genomeFile = new PathParameter(
                "genome",
                "fasta file of the query sequence",
                false
        );

        this.outputPrefix = new PathParameter(
                "output",
                "ouput file prefix",
                false
        );


        this.threads = new NumericParameter(
                "threads",
                "Specify number of processor threads to use",
                true
        );

    }

    public ConanParameter getGenomeFile() {
        return genomeFile;
    }

    public ConanParameter getOutputPrefix() {
        return outputPrefix;
    }

    public ConanParameter getThreads() {
        return threads;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<ConanParameter>(Arrays.asList(
                new ConanParameter[]{
                        this.genomeFile,
                        this.outputPrefix,
                        this.threads
                }
        ));
    }
}
