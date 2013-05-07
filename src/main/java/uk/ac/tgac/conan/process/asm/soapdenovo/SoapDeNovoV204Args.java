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
package uk.ac.tgac.conan.process.asm.soapdenovo;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.AssemblerArgs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 16/04/13
 * Time: 15:57
 */
public class SoapDeNovoV204Args extends AssemblerArgs {

    private static Logger log = LoggerFactory.getLogger(SoapDeNovoV204Process.class);

    private SoapDeNovoV204Params params = new SoapDeNovoV204Params();

    private int memory;
    private boolean resolveRepeats;
    private boolean fillGaps;
    private String outputPrefix;
    private File configFile;


    public SoapDeNovoV204Args() {
        super();

        this.memory = 0;
        this.resolveRepeats = false;
        this.fillGaps = false;
        this.configFile = null;

        StringJoiner nameJoiner = new StringJoiner("-");
        nameJoiner.add("soap-2.04");
        nameJoiner.add(this.getKmer() != 0 && this.getKmer() != DEFAULT_KMER, "", "k" + Integer.toString(this.getKmer()));
        nameJoiner.add(this.getCoverageCutoff() != 0, "", "cc" + Integer.toString(this.getCoverageCutoff()));

        this.outputPrefix = nameJoiner.toString();
    }


    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public boolean isResolveRepeats() {
        return resolveRepeats;
    }

    public void setResolveRepeats(boolean resolveRepeats) {
        this.resolveRepeats = resolveRepeats;
    }

    public boolean isFillGaps() {
        return fillGaps;
    }

    public void setFillGaps(boolean fillGaps) {
        this.fillGaps = fillGaps;
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(String outputPrefix) {
        this.outputPrefix = outputPrefix;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    @Override
    public AssemblerArgs copy() {
        return null;
    }

    @Override
    public void parse(String args) {

    }

    @Override
    public Map<ConanParameter, String> getArgMap() {

        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();

        if (this.outputPrefix != null) {
            pvp.put(params.getPrefix(), this.outputPrefix);
        }

        if (this.getKmer() > 0) {
            pvp.put(params.getKmer(), Integer.toString(this.getKmer()));
        }

        if (this.getCoverageCutoff() > 0) {
            pvp.put(params.getKmerFreqCutoff(), Integer.toString(this.getCoverageCutoff()));
        }

        if (this.getThreads() > 1) {
            pvp.put(params.getThreads(), Integer.toString(this.getThreads()));
        }

        if (this.memory > 0) {
            pvp.put(params.getMemory(), Integer.toString(this.memory));
        }

        if (this.resolveRepeats) {
            pvp.put(params.getResolveRepeats(), Boolean.toString(this.resolveRepeats));
        }

        if (this.fillGaps) {
            pvp.put(params.getFillGaps(), Boolean.toString(this.fillGaps));
        }

        if (this.configFile != null) {
            pvp.put(params.getConfigFile(), this.configFile.getAbsolutePath());
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

            if (param.equals(this.params.getPrefix().getName())) {
                this.outputPrefix = val;
            }
            else if (param.equals(this.params.getKmer().getName())) {
                this.setKmer(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getKmerFreqCutoff().getName())) {
                this.setCoverageCutoff(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getThreads().getName())) {
                this.setThreads(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getMemory().getName())) {
                this.setMemory(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getFillGaps().getName())) {
                this.setFillGaps(Boolean.parseBoolean(val));
            }
            else if (param.equals(this.params.getResolveRepeats().getName())) {
                this.setResolveRepeats(Boolean.parseBoolean(val));
            }
            else if (param.equals(this.params.getConfigFile().getName())) {
                this.setConfigFile(new File(val));
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    public void createLibraryConfigFile(List<Library> libraries, File configFile) throws IOException {

        StringJoiner sj = new StringJoiner("\n");

        log.debug("Creating SOAP config file from " + libraries.size() + " libraries.  Writing config file to: " + configFile.getAbsolutePath() + ". Note that not all libraries maybe used, depending on library 'usage' paramter.");

        for (Library lib : libraries) {

            // Will need to do something a little cleverer here eventually
            if (lib.testUsage(Library.Usage.ASSEMBLING) ||
                    lib.testUsage(Library.Usage.SCAFFOLDING) ||
                    lib.testUsage(Library.Usage.GAP_CLOSING)) {

                log.debug("Adding library " + lib.getName() + " to config file");

                sj.add("[LIB]");
                sj.add(lib.getReadLength() != null, "max_rd_len=", Integer.toString(lib.getReadLength()));
                sj.add(lib.getAverageInsertSize() != null, "avg_ins=", Integer.toString(lib.getAverageInsertSize()));
                sj.add(lib.getSeqOrientation() != null, "reverse_seq=", lib.getSeqOrientation() == Library.SeqOrientation.FORWARD_REVERSE ? "0" : "1");
                sj.add("asm_flags=3");
                sj.add(lib.getIndex() != null, "rank=", Integer.toString(lib.getIndex()));
                sj.add( lib.getFilePaired1() != null &&
                        (lib.getFilePaired1().getFileType() == SeqFile.FileType.FASTQ ||
                         lib.getFilePaired1().getFileType() == SeqFile.FileType.UNKNOWN),
                        "q1=", lib.getFilePaired1().getFilePath());
                sj.add(lib.getFilePaired2() != null &&
                        (lib.getFilePaired2().getFileType() == SeqFile.FileType.FASTQ ||
                         lib.getFilePaired2().getFileType() == SeqFile.FileType.UNKNOWN),
                        "q2=", lib.getFilePaired2().getFilePath());
                //sj.add(lib.getSeFile() != null && lib.getSeFile().getFileType() == SeqFile.FileType.FASTQ, "q=", lib.getSeFile().getFilePath());
                sj.add(lib.getFilePaired1() != null && lib.getFilePaired1().getFileType() == SeqFile.FileType.FASTA, "f1=", lib.getFilePaired1().getFilePath());
                sj.add(lib.getFilePaired2() != null && lib.getFilePaired2().getFileType() == SeqFile.FileType.FASTA, "f2=", lib.getFilePaired2().getFilePath());
                //sj.add(lib.getSeFile() != null && lib.getSeFile().getFileType() == SeqFile.FileType.FASTA, "f=", lib.getSeFile().getFilePath());

            }

        }

        String fileContents = sj.toString();

        FileUtils.writeStringToFile(configFile, fileContents);

        log.debug("Config file created: " + (configFile.exists() ? "true" : "false"));
    }

}
