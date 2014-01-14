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

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorArgs;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorPairedEndArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MetaInfServices(uk.ac.tgac.conan.process.ec.ErrorCorrectorArgsCreator.class)
public class SicklePeV11Args extends AbstractErrorCorrectorPairedEndArgs {

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
    public AbstractErrorCorrectorArgs copy() {
        return null;
    }

    @Override
    public void setFromLibrary(Library library, List<File> files) {

        this.setPairedEndInputFiles(new FilePair(
                files.get(0),
                files.get(1)));

        this.outputFilePair = new FilePair(
                new File(this.getOutputDir(), library + "_1.sickle.fastq"),
                new File(this.getOutputDir(), library + "_2.sickle.fastq"));

        this.seOutFile = new File(this.getOutputDir(), library + "_se.sickle.fastq");

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
    public ParamMap getArgMap() {

        ParamMap pvp = new DefaultParamMap();

        if (this.getQualityThreshold() != 20) {
            pvp.put(params.getQualityThreshold(), String.valueOf(this.getQualityThreshold()));
        }

        if (this.getMinLength() != 20) {
            pvp.put(params.getLengthThreshold(), String.valueOf(this.getMinLength()));
        }

        if (this.discardN) {
            pvp.put(params.getDiscardN(), Boolean.toString(true));
        }

        if (this.getQualType() != null) {
            pvp.put(params.getQualityType(), this.getQualType().toString().toLowerCase());
        }

        if (this.getPairedEndInputFiles() != null) {
            pvp.put(params.getPeFile1(), this.getPairedEndInputFiles().getFile1().getAbsolutePath());
            pvp.put(params.getPeFile2(), this.getPairedEndInputFiles().getFile2().getAbsolutePath());
        }

        if (this.getPairedEndCorrectedFiles() != null) {
            pvp.put(params.getOutputPe1(), this.getPairedEndCorrectedFiles().getFile1().getAbsolutePath());
            pvp.put(params.getOutputPe2(), this.getPairedEndCorrectedFiles().getFile2().getAbsolutePath());
        }

        if (this.getSingleEndCorrectedFiles() != null && this.getSingleEndCorrectedFiles().size() > 0) {
            pvp.put(params.getOutputSingles(), this.getSingleEndCorrectedFiles().get(0).getAbsolutePath());
        }

        return pvp;
    }


    @Override
    protected void setOptionFromMapEntry(ConanParameter param, String value) {
        if (param.equals(this.params.getPeFile1())) {
            this.getPairedEndInputFiles().setFile1(new File(value));
        } else if (param.equals(this.params.getPeFile2())) {
            this.getPairedEndInputFiles().setFile2(new File(value));
        } else if (param.equals(this.params.getOutputPe1())) {
            this.outputFilePair.setFile1(new File(value));
        } else if (param.equals(this.params.getOutputPe2())) {
            this.outputFilePair.setFile2(new File(value));
        } else if (param.equals(this.params.getOutputSingles())) {
            this.seOutFile = new File(value);
        } else if (param.equals(this.params.getDiscardN())) {
            this.discardN = Boolean.parseBoolean(value);
        } else if (param.equals(this.params.getLengthThreshold())) {
            this.setMinLength(Integer.parseInt(value.trim()));
        } else if (param.equals(this.params.getQualityThreshold())) {
            this.setQualityThreshold(Integer.parseInt(value.trim()));
        } else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }

    @Override
    protected void setArgFromMapEntry(ConanParameter param, String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
    @Override
    public void setFromArgMap(ParamMap pvp) throws IOException, ConanParameterException {

        // If file pairs don't exist then create them here
        if (this.getPairedEndInputFiles() == null) {
            this.setPairedEndInputFiles(new FilePair());
        }

        if (this.outputFilePair == null) {
            this.outputFilePair = new FilePair();
        }
        
        super.setFromArgMap(pvp);
    }

    @Override
    public AbstractErrorCorrectorArgs create(File outputDir, Library lib, int threads,
                                             int memory, int kmer,
                                             int minLength,
                                             int minQual) {

        SicklePeV11Args sargs = new SicklePeV11Args();

        sargs.setOutputDir(outputDir);
        //qargs..setOutputPrefix(outputPrefix);

        sargs.setFromLibrary(lib);
        sargs.setThreads(threads);
        sargs.setMemoryGb(memory);
        sargs.setKmer(kmer);
        sargs.setMinLength(minLength);
        sargs.setQualityThreshold(minQual);

        return sargs;
    }

    @Override
    public String getName() {
        return SickleV11Process.NAME;
    }
}
