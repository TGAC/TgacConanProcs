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
import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;

public class SickleV11QualityTypeParameter extends DefaultConanParameter {

    private static final long serialVersionUID = 3065149558945750682L;

    public static enum SickleQualityTypeOptions {

        ILLUMINA,
        PHRED,
        SANGER
    }

    public SickleV11QualityTypeParameter() {
        super();
        this.name = "qual-type";
        this.longName = "qual-type";
        this.description = "Type of quality values (illumina, phred, sanger) (required)";
        this.paramType = ParamType.OPTION;
        this.isOptional = false;
        this.isFlag = false;
        this.argValidator = ArgValidator.OFF;
    }

    @Override
    public boolean validateParameterValue(String value) {
        try {
            SickleQualityTypeOptions.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
