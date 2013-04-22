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
package uk.ac.tgac.conan.process.ec.quake;

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
 * Time: 09:49
 */
public class QuakeV034Params implements ProcessParams {

    private ConanParameter readsListFile;
    private ConanParameter kmer;
    private ConanParameter processes;
    private ConanParameter qualityValue;
    private ConanParameter minLength;

    public QuakeV034Params() {

        this.readsListFile = new PathParameter(
                "f",
                "File containing fastq file names, one per line or two per line for paired end reads.",
                false);

        this.kmer = new NumericParameter(
                "k",
                "Size of k-mers to correct",
                true);

        this.processes = new NumericParameter(
                "p",
                "Number of processes [default: 4]",
                true);

        this.qualityValue = new NumericParameter(
                "q",
                "Quality value ascii scale, generally 64 or 33. If not specified, it will guess.",
                true);

        this.minLength = new NumericParameter(
                "l",
                "Return only reads corrected and/or trimmed to <min_read> bp",
                true);
    }

    public ConanParameter getReadsListFile() {
        return readsListFile;
    }

    public ConanParameter getKmer() {
        return kmer;
    }

    public ConanParameter getProcesses() {
        return processes;
    }

    public ConanParameter getQualityValue() {
        return qualityValue;
    }

    public ConanParameter getMinLength() {
        return minLength;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<ConanParameter>(Arrays.asList(
                new ConanParameter[]{
                        this.readsListFile,
                        this.kmer,
                        this.processes,
                        this.qualityValue,
                        this.minLength
                }
        ));
    }
}
