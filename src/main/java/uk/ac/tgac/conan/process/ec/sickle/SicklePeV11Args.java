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

import uk.ac.ebi.fgpt.conan.core.param.FilePair;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorArgs;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorPairedEndArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SicklePeV11Args extends ErrorCorrectorPairedEndArgs {

    private SicklePeV11Params params = new SicklePeV11Params();

    private FilePair outputFilePair;
    private File seOutFile;
    private boolean discardN;
    private SickleV11QualityTypeParameter.SickleQualityTypeOptions qualType;


    public SicklePeV11Args() {
        super();

        this.outputFilePair = null;
        this.seOutFile = null;
        this.discardN = false;
        this.qualType = SickleV11QualityTypeParameter.SickleQualityTypeOptions.SANGER;
    }

    @Override
    public ErrorCorrectorArgs copy() {
        return null;
    }

    @Override
    public void setFromLibrary(Library library, List<File> files) {

        this.setPairedEndInputFiles(new FilePair(
                files.get(0),
                files.get(1)));

        this.outputFilePair = new FilePair(
                new File(this.getOutputDir(), library.getName() + "_1.sickle.fastq"),
                new File(this.getOutputDir(), library.getName() + "_2.sickle.fastq"));

        this.seOutFile = new File(this.getOutputDir(), library.getName() + "_se.sickle.fastq");

        this.qualType = library.getPhred() == Library.Phred.PHRED_64 ?
                        SickleV11QualityTypeParameter.SickleQualityTypeOptions.ILLUMINA :
                        SickleV11QualityTypeParameter.SickleQualityTypeOptions.SANGER;
    }

    @Override
    public FilePair getPairedEndCorrectedFiles() {
        return this.outputFilePair;
    }

    @Override
    public List<File> getSingleEndCorrectedFiles() {
        List<File> seOutFiles = new ArrayList<File>();
        seOutFiles.add(this.seOutFile);

        return seOutFiles;
    }

    @Override
    public FilePair getPairedEndErrorFiles() {
        return null;
    }

    @Override
    public List<File> getSingleEndErrorFiles() {
        return null;
    }

    @Override
    public List<File> getCorrectedFiles() {

        List<File> correctedFiles = new ArrayList<>();

        correctedFiles.add(this.outputFilePair.getFile1());
        correctedFiles.add(this.outputFilePair.getFile2());
        correctedFiles.add(this.seOutFile);

        return correctedFiles;
    }

    public boolean isDiscardN() {
        return discardN;
    }

    public void setDiscardN(boolean discardN) {
        this.discardN = discardN;
    }

    public SickleV11QualityTypeParameter.SickleQualityTypeOptions getQualType() {
        return this.qualType;
    }

    public void setQualType(SickleV11QualityTypeParameter.SickleQualityTypeOptions qualType) {
        this.qualType = qualType;
    }

    public FilePair getOutputFilePair() {
        return outputFilePair;
    }

    public void setOutputFilePair(FilePair outputFilePair) {
        this.outputFilePair = outputFilePair;
    }

    public File getSeOutFile() {
        return seOutFile;
    }

    public void setSeOutFile(File seOutFile) {
        this.seOutFile = seOutFile;
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

        if (this.getQualType() != null) {
            pvp.put(params.getQualityType(), "--" + params.getQualityType() + "=" + this.getQualType().toString().toLowerCase());
        }

        if (this.getPairedEndInputFiles() != null) {
            pvp.put(params.getPeFile1(), "--" + params.getPeFile1().getName() + "=" + this.getPairedEndInputFiles().getFile1().getAbsolutePath());
            pvp.put(params.getPeFile2(), "--" + params.getPeFile2().getName() + "=" + this.getPairedEndInputFiles().getFile2().getAbsolutePath());
        }

        if (this.getPairedEndCorrectedFiles() != null) {
            pvp.put(params.getOutputPe1(), "--" + params.getOutputPe1().getName() + "=" + this.getPairedEndCorrectedFiles().getFile1().getAbsolutePath());
            pvp.put(params.getOutputPe2(), "--" + params.getOutputPe2().getName() + "=" + this.getPairedEndCorrectedFiles().getFile2().getAbsolutePath());
        }

        if (this.getSingleEndCorrectedFiles() != null && this.getSingleEndCorrectedFiles().size() > 0) {
            pvp.put(params.getOutputSingles(), "--" + params.getOutputSingles().getName() + "=" + this.getSingleEndCorrectedFiles().get(0).getAbsolutePath());
        }

        return pvp;
    }

    @Override
    public void setFromArgMap(Map<ConanParameter, String> pvp) {

        // If file pairs don't exist then create them here
        if (this.getPairedEndInputFiles() == null) {
            this.setPairedEndInputFiles(new FilePair());
        }

        if (this.outputFilePair == null) {
            this.outputFilePair = new FilePair();
        }

        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();


            if (param.equals(this.params.getPeFile1().getName())) {
                this.getPairedEndInputFiles().setFile1(new File(entry.getValue()));
            } else if (param.equals(this.params.getPeFile2().getName())) {
                this.getPairedEndInputFiles().setFile2(new File(entry.getValue()));
            } else if (param.equals(this.params.getOutputPe1().getName())) {
                this.outputFilePair.setFile1(new File(entry.getValue()));
            } else if (param.equals(this.params.getOutputPe2().getName())) {
                this.outputFilePair.setFile2(new File(entry.getValue()));
            } else if (param.equals(this.params.getOutputSingles().getName())) {
                this.seOutFile = new File(entry.getValue());
            } else if (param.equals(this.params.getDiscardN().getName())) {
                this.discardN = Boolean.parseBoolean(entry.getValue());
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
