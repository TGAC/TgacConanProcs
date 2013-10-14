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

import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorArgs;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorSingleEndArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SickleSeV11Args extends ErrorCorrectorSingleEndArgs {


    private SickleSeV11Params params = new SickleSeV11Params();

    private File outputFile;
    private boolean discardN;
    private SickleV11QualityTypeParameter.SickleQualityTypeOptions qualType;


    public SickleSeV11Args() {
        super();

        this.outputFile = null;
        this.discardN = false;
        this.qualType = SickleV11QualityTypeParameter.SickleQualityTypeOptions.SANGER;
    }

    @Override
    public ErrorCorrectorArgs copy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<File> getCorrectedFiles() {

        List<File> correctedFiles = new ArrayList<>();

        correctedFiles.add(this.outputFile);

        return correctedFiles;
    }

    @Override
    public void setFromLibrary(Library library, List<File> files) {
        this.setSingleEndInputFile(files.get(0));
        this.outputFile = new File(this.getOutputDir(), library.getName() + ".qt.fastq");
        this.qualType = library.getPhred() == Library.Phred.PHRED_64 ?
                SickleV11QualityTypeParameter.SickleQualityTypeOptions.ILLUMINA :
                SickleV11QualityTypeParameter.SickleQualityTypeOptions.SANGER;
    }

    @Override
    public Library createOutputLibrary() {

        Library lib = new Library();

        //lib.

        return lib;
    }

    @Override
    public File getCorrectedFile() {
        return this.outputFile;
    }

    @Override
    public File getErrorFile() {
        return null;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public boolean isDiscardN() {
        return discardN;
    }

    public void setDiscardN(boolean discardN) {
        this.discardN = discardN;
    }

    public SickleV11QualityTypeParameter.SickleQualityTypeOptions getQualType() {
        return qualType;
    }

    public void setQualType(SickleV11QualityTypeParameter.SickleQualityTypeOptions qualType) {
        this.qualType = qualType;
    }



    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<ConanParameter, String> getArgMap() {

        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();

        if (this.getQualityThreshold() != 20) {
            pvp.put(params.getQualityThreshold(), "--" + params.getQualityThreshold().getName() + "=" + String.valueOf(this.getQualityThreshold()));
        }

        if (this.getMinLength() != 20) {
            pvp.put(params.getLengthThreshold(), "--" + params.getLengthThreshold().getName() + "=" + String.valueOf(this.getMinLength()));
        }

        if (this.discardN) {
            pvp.put(params.getDiscardN(), "--" + params.getDiscardN().getName());
        }

        if (this.qualType != null) {
            pvp.put(params.getQualityType(), "--" + params.getQualityType() + "=" + this.qualType.toString().toLowerCase());
        }

        if (this.getSingleEndInputFile() != null) {
            pvp.put(params.getSeFile(), "--" + params.getSeFile().getName() + "=" + this.getSingleEndInputFile().getAbsolutePath());
        }

        if (this.getCorrectedFile() != null) {
            pvp.put(params.getOutputFile(), "--" + params.getOutputFile().getName() + "=" + this.getCorrectedFile().getPath());
        }

        return pvp;
    }

    @Override
    public void setFromArgMap(Map<ConanParameter, String> pvp) {

        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();

            if (param.equals(this.params.getSeFile().getName())) {
                this.setSingleEndInputFile(new File(entry.getValue()));
            } else if (param.equals(this.params.getOutputFile().getName())) {
                this.outputFile = new File(entry.getValue());
            } else if (param.equals(this.params.getDiscardN().getName())) {
                this.discardN = Boolean.parseBoolean(entry.getValue());
            } else if (param.equals(this.params.getQualityType().getName())) {
                this.qualType = SickleV11QualityTypeParameter.SickleQualityTypeOptions.valueOf(entry.getValue().trim().toUpperCase());
            } else if (param.equals(this.params.getLengthThreshold().getName())) {
                this.setMinLength(Integer.parseInt(entry.getValue().trim()));
            } else if (param.equals(this.params.getQualityThreshold().getName())) {
                this.setQualityThreshold(Integer.parseInt(entry.getValue().trim()));
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }

        }
    }

}
