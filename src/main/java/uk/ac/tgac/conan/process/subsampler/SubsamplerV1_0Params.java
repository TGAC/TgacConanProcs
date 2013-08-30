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

import uk.ac.ebi.fgpt.conan.core.param.FlagParameter;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsamplerV1_0Params implements ProcessParams {

    private ConanParameter append;
    private ConanParameter inputFile;
    private ConanParameter outputFile;
    private ConanParameter logFile;
    private ConanParameter maxReadLength;
    private ConanParameter maxNameLength;
    private ConanParameter probability;
    private ConanParameter seed;


    public SubsamplerV1_0Params() {

        this.append = new FlagParameter(
                "a",
                "Append to an existing file");

        this.inputFile = new PathParameter(
                "i",
                "Input from a file. Default stdin.",
                true);

        this.outputFile = new PathParameter(
                "o",
                "Output to a file. Default stdout.",
                true);

        this.logFile = new PathParameter(
                "L",
                "Logfile. In addition to stderr, the log is printed to this file.",
                true);

        this.maxReadLength = new NumericParameter(
                "l",
                "Maximum read length. Default 2000",
                true);

        this.maxNameLength = new NumericParameter(
                "n",
                "Maximum name length. Default 1000",
                true);

        this.probability = new NumericParameter(
                "p",
                "Probability to pick a read. Default 0.01 (1% of the reads will be picked)",
                true);

        this.seed = new NumericParameter(
                "s",
                "Seed for the random number generator. The defualt is a timestamp. Useful if you want to get the same output on different runs.",
                true);
    }

    public ConanParameter getAppend() {
        return append;
    }

    public ConanParameter getInputFile() {
        return inputFile;
    }

    public ConanParameter getOutputFile() {
        return outputFile;
    }

    public ConanParameter getLogFile() {
        return logFile;
    }

    public ConanParameter getMaxReadLength() {
        return maxReadLength;
    }

    public ConanParameter getMaxNameLength() {
        return maxNameLength;
    }

    public ConanParameter getProbability() {
        return probability;
    }

    public ConanParameter getSeed() {
        return seed;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<ConanParameter>(Arrays.asList(
                new ConanParameter[]{
                        this.append,
                        this.inputFile,
                        this.outputFile,
                        this.logFile,
                        this.maxReadLength,
                        this.maxNameLength,
                        this.probability,
                        this.seed
                }
        ));
    }
}
