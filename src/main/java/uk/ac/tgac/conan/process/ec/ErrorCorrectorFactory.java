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
package uk.ac.tgac.conan.process.ec;

import uk.ac.tgac.conan.process.ec.musket.MusketV106Process;
import uk.ac.tgac.conan.process.ec.quake.QuakeV034Process;
import uk.ac.tgac.conan.process.ec.sickle.SickleV11Process;

/**
 * User: maplesod
 * Date: 30/01/13
 * Time: 18:42
 */
public enum ErrorCorrectorFactory {

    SICKLE_V1_1 {
        @Override
        public String getToolName() {
            return "SICKLE";
        }

        @Override
        public ErrorCorrector create() {
            return new SickleV11Process();
        }

        @Override
        public ErrorCorrector create(ErrorCorrectorArgs args) {
            return new SickleV11Process(args);
        }

    },
    QUAKE_V0_3_4 {
        @Override
        public String getToolName() {
            return "QUAKE";
        }

        @Override
        public ErrorCorrector create() {
            return new QuakeV034Process();
        }

        @Override
        public ErrorCorrector create(ErrorCorrectorArgs args) {
            return new QuakeV034Process(args);
        }
    },
    MUSKET_V1_0_6 {
        @Override
        public String getToolName() {
            return "MUSKET";
        }

        @Override
        public ErrorCorrector create() {
            return new MusketV106Process();
        }

        @Override
        public ErrorCorrector create(ErrorCorrectorArgs args) {
            return new MusketV106Process(args);
        }
    };

    public abstract String getToolName();

    public abstract ErrorCorrector create();
    public abstract ErrorCorrector create(ErrorCorrectorArgs args);



    public static String defaultQTName() {
        return SICKLE_V1_1.toString();
    }

    public static ErrorCorrector createQualityTrimmer() {

        return createQualityTrimmer("SICKLE");
    }


    public static ErrorCorrector createQualityTrimmer(String qtType) {

        ErrorCorrectorFactory qualityTrimmerType = ErrorCorrectorFactory.valueOf(qtType.toUpperCase());

        if (qualityTrimmerType == null) {
            qualityTrimmerType = findGeneric(qtType);
        }

        if (qualityTrimmerType != null) {
            return qualityTrimmerType.create();
        }

        return null;
    }

    protected static ErrorCorrectorFactory findGeneric(String qtType) {

        for (ErrorCorrectorFactory inst : ErrorCorrectorFactory.values()) {
            if (inst.getToolName().equalsIgnoreCase(qtType)) {
                return inst;
            }
        }

        return null;
    }
}
