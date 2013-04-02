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
package uk.ac.tgac.conan.process.qt;

import uk.ac.ebi.fgpt.conan.core.param.FilePair;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;

public interface QualityTrimmerArgs extends ProcessArgs {

    boolean isSingleEndArgs();

    FilePair getPairedEndInputFiles();

    void setPairedEndInputFiles(FilePair pairedEndInputFiles);

    FilePair getPairedEndOutputFiles();

    void setPairedEndOutputFiles(FilePair pairedEndOutputFiles);

    File getSingleEndInputFile();

    void setSingleEndInputFile(File singleEndInputFile);

    File getSingleEndOutputFile();

    void setSingleEndOutputFile(File singleEndOutputFile);

    int getQualityThreshold();

    void setQualityThreshold(int qualityThreshold);

    int getMinLength();

    void setMinLength(int minLength);
}
