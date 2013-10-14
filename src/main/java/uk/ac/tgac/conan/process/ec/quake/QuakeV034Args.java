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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.core.param.FilePair;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorArgs;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorPairedEndArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 09:49
 */
public class QuakeV034Args extends ErrorCorrectorPairedEndArgs {

    private static Logger log = LoggerFactory.getLogger(QuakeV034Args.class);

    private QuakeV034Params params = new QuakeV034Params();

    private File readsListFile;

    public QuakeV034Args() {
        this.readsListFile = null;
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
    public ErrorCorrectorArgs copy() {
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
    public Map<ConanParameter, String> getArgMap() {
        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();


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
    public void setFromArgMap(Map<ConanParameter, String> pvp) {

        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();
            String val = entry.getValue();

            if (param.equals(this.params.getReadsListFile().getName())) {
                this.readsListFile = new File(val);
            }
            else if (param.equals(this.params.getKmer().getName())) {
                this.setKmer(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getMinLength().getName())) {
                this.setMinLength(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getProcesses().getName())) {
                this.setThreads(Integer.parseInt(val));
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }
}
