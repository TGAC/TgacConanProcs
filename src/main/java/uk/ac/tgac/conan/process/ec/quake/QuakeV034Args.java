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
package uk.ac.tgac.conan.process.ec.quake;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorArgs;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorPairedEndArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 09:49
 */
@MetaInfServices(uk.ac.tgac.conan.process.ec.ErrorCorrectorArgsCreator.class)
public class QuakeV034Args extends AbstractErrorCorrectorPairedEndArgs {

    private static Logger log = LoggerFactory.getLogger(QuakeV034Args.class);

    private File readsListFile;

    public QuakeV034Args() {

        super(new QuakeV034Params());
        this.readsListFile = null;
    }

    public QuakeV034Params getParams() {
        return (QuakeV034Params)this.params;
    }


    public File getReadsListFile() {
        return readsListFile;
    }

    public void setReadsListFile(File readsListFile) {
        this.readsListFile = readsListFile;
    }


    protected FilePair loadInputFilePaths() throws IOException {
        if (this.readsListFile == null || !this.readsListFile.exists())
            return null;

        List<String> fileLines = FileUtils.readLines(this.readsListFile);

        // This wrapper only processes a single library, so ignore all but the first line.
        if (fileLines.size() == 0)
            return null;

        String[] pairedEndFilePaths = fileLines.get(0).split(" ");

        if (pairedEndFilePaths.length != 2)
            return null;

        FilePair inputFiles = new FilePair(new File(pairedEndFilePaths[0].trim()), new File(pairedEndFilePaths[1].trim()));

        return inputFiles;
    }

    @Override
    public FilePair getPairedEndCorrectedFiles() {

        try {
            FilePair inputFiles =  this.loadInputFilePaths();

            String ext1 = FilenameUtils.getExtension(inputFiles.getFile1().getName());
            String ext2 = FilenameUtils.getExtension(inputFiles.getFile2().getName());

            String base1 = FilenameUtils.getBaseName(inputFiles.getFile1().getName());
            String base2 = FilenameUtils.getBaseName(inputFiles.getFile2().getName());

            String corOut1FileName = base1 + ".cor." + ext1;
            String corOut2FileName = base2 + ".cor." + ext2;

            File corOut1 = new File(this.getOutputDir(), corOut1FileName);
            File corOut2 = new File(this.getOutputDir(), corOut2FileName);

            return new FilePair(corOut1, corOut2);
        }
        catch(IOException ioe) {
            return null;
        }
    }

    @Override
    public List<File> getSingleEndCorrectedFiles() {
        try {
            FilePair inputFiles =  this.loadInputFilePaths();

            String ext1 = FilenameUtils.getExtension(inputFiles.getFile1().getName());
            String ext2 = FilenameUtils.getExtension(inputFiles.getFile2().getName());

            String base1 = FilenameUtils.getBaseName(inputFiles.getFile1().getName());
            String base2 = FilenameUtils.getBaseName(inputFiles.getFile2().getName());

            String seCorOut1FileName = base1 + ".cor_single." + ext1;
            String seCorOut2FileName = base2 + ".cor_single." + ext2;

            File seCorOut1 = new File(this.getOutputDir(), seCorOut1FileName);
            File seCorOut2 = new File(this.getOutputDir(), seCorOut2FileName);

            List<File> seCorfiles = new ArrayList<File>();
            seCorfiles.add(seCorOut1);
            seCorfiles.add(seCorOut2);

            return seCorfiles;
        }
        catch(IOException ioe) {
            return null;
        }
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
    public AbstractErrorCorrectorArgs copy() {
        return null;
    }

    @Override
    public List<File> getCorrectedFiles() {
        List<File> correctedFiles = new ArrayList<>();

        FilePair fp = getPairedEndCorrectedFiles();

        correctedFiles.add(fp.getFile1());
        correctedFiles.add(fp.getFile2());

        return correctedFiles;
    }

    @Override
    public void setFromLibrary(Library lib, List<File> files) {

        String fp1 = files.get(0).getAbsolutePath();
        String fp2 = files.get(1).getAbsolutePath();

        String outLine = fp1 + " " + fp2;

        List<String> outLines = new ArrayList<String>();
        outLines.add(outLine);

        File inputFileList = new File(this.getOutputDir(), "readsListFile.lst");
        this.readsListFile = inputFileList;

        try {
            FileUtils.writeLines(inputFileList, outLines);
        }
        catch(IOException ioe) {
            log.error("Failed to create readsListFile.lst for Quake");
        }
    }

    @Override
    public void parse(String args) {

    }

    @Override
    public ParamMap getArgMap() {

        QuakeV034Params params = this.getParams();

        ParamMap pvp = new DefaultParamMap();

        if (this.getMinLength() != -1) {
            pvp.put(params.getMinLength(), String.valueOf(this.getMinLength()));
        }

        pvp.put(params.getKmer(), String.valueOf(this.getKmer()));
        pvp.put(params.getProcesses(), String.valueOf(this.getThreads()));

        if (this.getReadsListFile() != null) {
            pvp.put(params.getReadsListFile(), this.getReadsListFile().getAbsolutePath());
        }

        return pvp;
    }


    @Override
    protected void setOptionFromMapEntry(ConanParameter param, String value) {

        QuakeV034Params params = this.getParams();

        if (param.equals(params.getReadsListFile())) {
            this.readsListFile = new File(value);
        }
        else if (param.equals(params.getKmer())) {
            this.setKmer(Integer.parseInt(value));
        }
        else if (param.equals(params.getMinLength())) {
            this.setMinLength(Integer.parseInt(value));
        }
        else if (param.equals(params.getProcesses())) {
            this.setThreads(Integer.parseInt(value));
        }
        else {
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

        QuakeV034Args qargs = new QuakeV034Args();

        qargs.setOutputDir(outputDir);
        //qargs..setOutputPrefix(outputPrefix);
        qargs.setFromLibrary(lib);
        qargs.setThreads(threads);
        qargs.setMemoryGb(memory);
        qargs.setKmer(kmer);
        qargs.setMinLength(minLength);
        qargs.setQualityThreshold(minQual);

        return qargs;
    }

    @Override
    public String getName() {
        return QuakeV034Process.NAME;
    }
}
