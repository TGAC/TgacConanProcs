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
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 15:00
 */
public abstract class SickleV11Params implements ProcessParams {

    private ConanParameter qualityThreshold;
    private ConanParameter lengthThreshold;
    private ConanParameter discardN;
    private ConanParameter qualityType;

    public SickleV11Params() {

        this.qualityThreshold = new ParameterBuilder()
                .longName("qual-threshold")
                .description("Threshold for trimming based on average quality in a window. Default 20.")
                .isOptional(true)
                .argValidator(ArgValidator.DIGITS)
                .create();

        this.lengthThreshold = new ParameterBuilder()
                .longName("length-threshold")
                .description("Threshold to keep a read based on length after trimming. Default 20.")
                .isOptional(true)
                .argValidator(ArgValidator.DIGITS)
                .create();

        this.discardN = new ParameterBuilder()
                .longName("discard-n")
                .description("Discard sequences with any Ns in them.")
                .isFlag(true)
                .isOptional(true)
                .argValidator(ArgValidator.OFF)
                .create();

        this.qualityType = new SickleV11QualityTypeParameter();
    }

    public ConanParameter getQualityThreshold() {
        return qualityThreshold;
    }

    public ConanParameter getLengthThreshold() {
        return lengthThreshold;
    }

    public ConanParameter getDiscardN() {
        return discardN;
    }

    public ConanParameter getQualityType() {
        return qualityType;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<ConanParameter>(Arrays.asList(
                new ConanParameter[]{
                        this.qualityThreshold,
                        this.lengthThreshold,
                        this.discardN,
                        this.qualityType
                }
        ));
    }
}
