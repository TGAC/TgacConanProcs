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

import uk.ac.tgac.conan.process.ec.sickle.SickleV11Process;

/**
 * User: maplesod
 * Date: 30/01/13
 * Time: 18:42
 */
public enum ErrorCorrectorFactory {

    SICKLE_SE_V1_1 {
        @Override
        public String getToolName() {
            return "SICKLE";
        }

        @Override
        public boolean isPairedEnd() {
            return false;
        }

        @Override
        public ErrorCorrector create() {
            return new SickleV11Process(SickleV11Process.JobType.SINGLE_END);
        }

        @Override
        public ErrorCorrector create(ErrorCorrectorArgs args) {
            return new SickleV11Process(SickleV11Process.JobType.SINGLE_END, args);
        }

    },
    SICKLE_PE_V1_1 {
        @Override
        public String getToolName() {
            return "SICKLE";
        }

        @Override
        public boolean isPairedEnd() {
            return true;
        }

        @Override
        public ErrorCorrector create() {
            return new SickleV11Process(SickleV11Process.JobType.PAIRED_END);
        }

        @Override
        public ErrorCorrector create(ErrorCorrectorArgs args) {
            return new SickleV11Process(SickleV11Process.JobType.PAIRED_END, args);
        }
    };

    public abstract String getToolName();

    public abstract boolean isPairedEnd();

    public abstract ErrorCorrector create();
    public abstract ErrorCorrector create(ErrorCorrectorArgs args);



    public static String defaultQTName() {
        return SICKLE_PE_V1_1.toString();
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
