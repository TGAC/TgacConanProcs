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
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorArgs;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorSingleEndArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@MetaInfServices(uk.ac.tgac.conan.process.ec.ErrorCorrectorArgsCreator.class)
public class SickleSeV11Args extends AbstractErrorCorrectorSingleEndArgs {


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
    public AbstractErrorCorrectorArgs copy() {
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

        if (this.qualType != null) {
            pvp.put(params.getQualityType(), this.qualType.toString().toLowerCase());
        }

        if (this.getSingleEndInputFile() != null) {
            pvp.put(params.getSeFile(),this.getSingleEndInputFile().getAbsolutePath());
        }

        if (this.getCorrectedFile() != null) {
            pvp.put(params.getOutputFile(), this.getCorrectedFile().getPath());
        }

        return pvp;
    }

    @Override
    protected void setOptionFromMapEntry(ConanParameter param, String value) {
        if (param.equals(this.params.getSeFile())) {
            this.setSingleEndInputFile(new File(value));
        } else if (param.equals(this.params.getOutputFile())) {
            this.outputFile = new File(value);
        } else if (param.equals(this.params.getDiscardN())) {
            this.discardN = Boolean.parseBoolean(value);
        } else if (param.equals(this.params.getQualityType())) {
            this.qualType = SickleV11QualityTypeParameter.SickleQualityTypeOptions.valueOf(value.toUpperCase());
        } else if (param.equals(this.params.getLengthThreshold())) {
            this.setMinLength(Integer.parseInt(value));
        } else if (param.equals(this.params.getQualityThreshold())) {
            this.setQualityThreshold(Integer.parseInt(value));
        } else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }

    @Override
    protected void setArgFromMapEntry(ConanParameter param, String value) {
        //To change body of implemented methods use File | Settings | File Templates.
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
