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
package uk.ac.tgac.conan.process.asmIO.degap.gapcloser;

import org.apache.commons.io.FileUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@MetaInfServices(uk.ac.tgac.conan.process.asmIO.AssemblyIOArgsCreator.class)
public class GapCloserV112Args extends AbstractAssemblyIOArgs {

    private GapCloserV112Params params = new GapCloserV112Params();

    public static int DEFAULT_OVERLAP = 25;
    public static int DEFAULT_READ_LENGTH = 100;

    // GapCloser vars
    private File libraryFile;
    private File outputFile;
    private int overlap = DEFAULT_OVERLAP;
    private int maxReadLength = DEFAULT_READ_LENGTH;

    public GapCloserV112Args() {
        this.libraryFile = null;
        this.outputFile = null;
        this.overlap = DEFAULT_OVERLAP;
        this.maxReadLength = DEFAULT_READ_LENGTH;
    }

    @Override
    public File getOutputFile() {
        return this.outputFile;
    }

    @Override
    public void setOutputPrefix(String outputPrefix) {

        if (this.getOutputDir() != null) {
            this.setOutputFile(new File(this.getOutputDir(), outputPrefix + ".fa"));
        }
        else {
            this.setOutputFile(new File(outputPrefix + ".fa"));
        }

        super.setOutputPrefix(outputPrefix);
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getLibraryFile() {
        return libraryFile;
    }

    public void setLibraryFile(File libraryFile) {
        this.libraryFile = libraryFile;
    }

    public Integer getOverlap() {
        return overlap;
    }

    public void setOverlap(Integer overlap) {
        this.overlap = overlap;
    }

    public int getMaxReadLength() {
        return maxReadLength;
    }

    public void setMaxReadLength(int maxReadLength) {
        this.maxReadLength = maxReadLength;
    }

    public static void createLibraryFile(List<Library> libs, File outputLibFile) throws IOException {

        List<String> lines = new ArrayList<String>();

        int rank = 1;
        for (Library lib : libs) {

            StringJoiner sj = new StringJoiner("\n");

            sj.add("[LIB]");
            sj.add("max_rd_len=" + lib.getReadLength());
            sj.add("avg_ins=" + lib.getAverageInsertSize());
            sj.add(lib.getSeqOrientation() != null, "reverse_seq=", lib.getSeqOrientation() == Library.SeqOrientation.FORWARD_REVERSE ? "0" : "1");
            sj.add("asm_flags=3");
            sj.add("rank=" + rank);
            sj.add(lib.getFile1Type() == SeqFile.FileType.FASTQ, "q1=", lib.getFile1().getAbsolutePath());
            sj.add(lib.getFile2Type() == SeqFile.FileType.FASTQ, "q2=", lib.getFile2().getAbsolutePath());
            //sj.add(lib.getSeFile() != null && lib.getSeFile().getFileType() == SeqFile.FileType.FASTQ, "q=", lib.getSeFile().getFilePath());
            sj.add(lib.getFile1Type() == SeqFile.FileType.FASTA, "f1=", lib.getFile1().getAbsolutePath());
            sj.add(lib.getFile2Type() == SeqFile.FileType.FASTA, "f2=", lib.getFile2().getAbsolutePath());
            //sj.add(lib.getSeFile() != null && lib.getSeFile().getFileType() == SeqFile.FileType.FASTA, "f=", lib.getSeFile().getFilePath());

            lines.add(sj.toString() + "\n");

        }

        FileUtils.writeLines(outputLibFile, lines);
    }


    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<ConanParameter, String> getArgMap() {

        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();

        if (this.getInputFile() != null)
            pvp.put(params.getInputScaffoldFile(), this.getInputFile().getAbsolutePath());

        if (this.libraryFile != null)
            pvp.put(params.getLibraryFile(), this.libraryFile.getAbsolutePath());

        if (this.getOutputFile() != null)
            pvp.put(params.getOutputFile(), this.getOutputFile().getAbsolutePath());

        if (this.overlap != DEFAULT_OVERLAP)
            pvp.put(params.getOverlap(), Integer.toString(this.overlap));

        if (this.maxReadLength != DEFAULT_READ_LENGTH)
            pvp.put(params.getMaxReadLength(), Integer.toString(this.maxReadLength));

        if (this.getThreads() > 1)
            pvp.put(params.getThreads(), Integer.toString(this.getThreads()));

        return pvp;
    }

    @Override
    public void setFromArgMap(Map<ConanParameter, String> pvp) {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();

            if (param.equals(this.params.getLibraryFile().getName())) {
                this.libraryFile = new File(entry.getValue());
            }
            else if (param.equals(this.params.getOverlap().getName())) {
                this.overlap = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getMaxReadLength().getName())) {
                this.maxReadLength = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getThreads().getName())) {
                this.setThreads(Integer.parseInt(entry.getValue()));
            }
            else if (param.equals(this.params.getInputScaffoldFile().getName())) {
                this.setInputFile(new File(entry.getValue()));
            }
            else if (param.equals(this.params.getOutputFile().getName())) {
                this.setOutputFile(new File(entry.getValue()));
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    @Override
    public AbstractAssemblyIOArgs create(File inputFile, File outputDir, String outputPrefix, List<Library> libs,
                                         int threads, int memory, String otherArgs) {

        GapCloserV112Args newArgs = new GapCloserV112Args();
        newArgs.setInputFile(inputFile);
        newArgs.setOutputDir(outputDir);
        newArgs.setOutputPrefix(outputPrefix);
        newArgs.setLibraries(libs);
        newArgs.setThreads(threads);
        newArgs.setMemory(memory);
        newArgs.parse(otherArgs);

        return newArgs;
    }

    @Override
    public String getName() {
        return GapCloserV112Process.NAME;
    }

    @Override
    public String getAssemblyIOProcessType() {
        return GapCloserV112Process.TYPE;
    }
}
