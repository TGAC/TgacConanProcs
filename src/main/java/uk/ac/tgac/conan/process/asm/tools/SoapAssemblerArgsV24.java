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
package uk.ac.tgac.conan.process.asm.tools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.FlagParameter;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 16/04/13
 * Time: 15:57
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class SoapAssemblerArgsV24 extends AbstractAssembler {

    private static Logger log = LoggerFactory.getLogger(SoapAssemblerArgsV24.class);


    public static final String EXE = "SOAPdenovo-127mer";
    public static final String NAME = "SOAP_Assemble_V2.4";

    public SoapAssemblerArgsV24() {
        this(null);
    }

    public SoapAssemblerArgsV24(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public SoapAssemblerArgsV24(ConanExecutorService ces, Args args) {
        super(NAME, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public boolean makesUnitigs() {
        return false;
    }

    @Override
    public boolean makesContigs() {
        return true;
    }

    @Override
    public boolean makesScaffolds() {
        return false;
    }

    @Override
    public boolean makesBubbles() {
        return false;
    }

    @Override
    public File getUnitigsFile() {
        return null;
    }

    @Override
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN;
    }

    @Override
    public File getContigsFile() {
        return new File(this.getArgs().getOutputDir(), "soap.contig");
    }

    @Override
    public File getScaffoldsFile() {
        return null;
    }

    @Override
    public File getBubbleFile() {
        return null;
    }

    @Override
    public void setLibraries(List<Library> libraries) {
        this.getArgs().setLibs(libraries);
    }

    protected String createPreGraphCommand() {

        Args args = this.getArgs();

        return EXE + " pregraph" +
                " -s " + args.getConfigFile() +
                " -o soap" +
                " -K " + args.getK() +
                " -p " + args.getThreads() +
                " -a " + args.getMemoryAssumptionGB() +
                (args.resolveRepeats ? " -R" : "") +
                " -d " + args.getCoverageCutoff();
    }

    protected String createContigCommand() {

        Args args = this.getArgs();

        return EXE + " contig" +
                " -g soap" +
                (args.resolveRepeats ? " -R" : "") //+
                //" -M ?" +
                //" -D ?" +
                //" -e ?"
        ;


    }


    @Override
    public String getCommand() {

        List<String> commands = new ArrayList<String>();

        commands.add(this.createPreGraphCommand());
        commands.add(this.createContigCommand());

        // Join commands
        return StringUtils.join(commands, "; ");
    }


    @Override
    public void setup() throws IOException {

        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        // Create the SOAP lib configuration file from the library list
        Args args = this.getArgs();

        this.addPreCommand("cd " + args.getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);


        if (args.getConfigFile() == null && args.getLibs() != null) {

            File configFile = new File(args.getOutputDir(), "soap.libs");
            log.debug("Config file not defined but libraries available.  Creating config file from libraries at " + configFile.getAbsolutePath());
            args.setConfigFile(configFile);
            args.createLibraryConfigFile(args.getLibs(), configFile);
        }
        else if (args.getConfigFile() == null && args.getLibs() == null) {
            throw new IOException("Cannot run SOAP without libraries or config file");
        }
        else if (args.getConfigFile() != null && args.getLibs() != null) {
            log.warn("SOAP denovo found both a config file and libraries.  Assuming config file was intended to be used.");
        }
    }

    @MetaInfServices(DeBruijnArgs.class)
    public static class Args extends AbstractAssemblerArgs implements DeBruijnArgs {

        private static Logger log = LoggerFactory.getLogger(SoapAssemblerArgsV24.class);

        public static final int DEFAULT_K = 61;
        public static final int DEFAULT_COVERAGE_CUTOFF = 0;
        public static final int DEFAULT_THREADS = 1;

        private int k;
        private int coverageCutoff;
        private boolean resolveRepeats;
        private boolean fillGaps;
        private String outputPrefix;
        private File configFile;


        public Args() {
            super(new Params(), NAME);

            this.k = DEFAULT_K;
            this.coverageCutoff = DEFAULT_COVERAGE_CUTOFF;
            this.resolveRepeats = true;
            this.fillGaps = false;
            this.configFile = null;
            this.outputPrefix = "soap";
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public int getK() {
            return k;
        }

        public void setK(int k) {
            this.k = k;
        }

        public int getCoverageCutoff() {
            return coverageCutoff;
        }

        public void setCoverageCutoff(int coverageCutoff) {
            this.coverageCutoff = coverageCutoff;
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

        public int getMemoryAssumptionGB() {
            return this.maxMemUsageMB / 1000;
        }

        @Override
        public void parse(String args) {

        }

        @Override
        public ParamMap getArgMap() {

            Params params = (Params)this.params;
            ParamMap pvp = new DefaultParamMap();

            if (this.outputPrefix != null) {
                pvp.put(params.getPrefix(), this.outputPrefix);
            }

            if (this.getK() > 0) {
                pvp.put(params.getKmer(), Integer.toString(this.getK()));
            }

            if (this.getCoverageCutoff() > 0) {
                pvp.put(params.getKmerFreqCutoff(), Integer.toString(this.getCoverageCutoff()));
            }

            if (this.getThreads() > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.getThreads()));
            }

            if (this.getMemoryAssumptionGB() > 0) {
                pvp.put(params.getMemory(), Integer.toString(this.getMemoryAssumptionGB()));
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
        protected void setOptionFromMapEntry(ConanParameter param, String value) {
            
            Params params = (Params)this.params;
            
            if (param.equals(params.getPrefix())) {
                this.outputPrefix = value;
            }
            else if (param.equals(params.getKmer())) {
                this.setK(Integer.parseInt(value));
            }
            else if (param.equals(params.getKmerFreqCutoff())) {
                this.setCoverageCutoff(Integer.parseInt(value));
            }
            else if (param.equals(params.getThreads())) {
                this.setThreads(Integer.parseInt(value));
            }
            else if (param.equals(params.getMemory())) {
                this.maxMemUsageMB = Integer.parseInt(value) * 1000;
            }
            else if (param.equals(params.getFillGaps())) {
                this.setFillGaps(Boolean.parseBoolean(value));
            }
            else if (param.equals(params.getResolveRepeats())) {
                this.setResolveRepeats(Boolean.parseBoolean(value));
            }
            else if (param.equals(params.getConfigFile())) {
                this.setConfigFile(new File(value));
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        public void createLibraryConfigFile(List<Library> libraries, File configFile) throws IOException {

            StringJoiner sj = new StringJoiner("\n");

            log.debug("Creating SOAP config file from " + libraries.size() + " libraries.  Writing config file to: " + configFile.getAbsolutePath() + ". Note that not all libraries maybe used, depending on library 'usage' paramter.");

            int rank = 1;
            for (Library lib : libraries) {

                log.debug("Adding library " + lib.getName() + " to config file");

                sj.add("[LIB]");
                sj.add("max_rd_len=" + lib.getReadLength());
                sj.add("avg_ins=", lib.getAverageInsertSize());
                sj.add(lib.getSeqOrientation() != null, "reverse_seq=", lib.getSeqOrientation() == Library.SeqOrientation.FORWARD_REVERSE ? "0" : "1");
                sj.add("asm_flags=3");
                sj.add("rank=" + rank);
                sj.add(lib.getFile1Type() == SeqFile.FileType.FASTQ || lib.getFile1Type() == SeqFile.FileType.UNKNOWN,
                        "q1=", lib.getFile1().getAbsolutePath() );
                sj.add(lib.getFile2Type() == SeqFile.FileType.FASTQ || lib.getFile2Type() == SeqFile.FileType.UNKNOWN,
                        "q2=", lib.getFile2().getAbsolutePath());
                //sj.add(lib.getSeFile() != null && lib.getSeFile().getFileType() == SeqFile.FileType.FASTQ, "q=", lib.getSeFile().getFilePath());
                sj.add(lib.getFile1Type() == SeqFile.FileType.FASTA, "f1=", lib.getFile1().getAbsolutePath());
                sj.add(lib.getFile2Type() == SeqFile.FileType.FASTA, "f2=", lib.getFile2().getAbsolutePath());
                //sj.add(lib.getSeFile() != null && lib.getSeFile().getFileType() == SeqFile.FileType.FASTA, "f=", lib.getSeFile().getFilePath());

                rank++;
            }

            String fileContents = sj.toString();

            FileUtils.writeStringToFile(configFile, fileContents);

            log.debug("Config file created: " + (configFile.exists() ? "true" : "false"));
        }

        @Override
        public GenericDeBruijnArgs getDeBruijnArgs() {

            GenericDeBruijnArgs args = new GenericDeBruijnArgs();

            args.setK(this.k);
            args.setThreads(this.threads);
            args.setMemory(this.maxMemUsageMB);
            args.setLibraries(this.libs);
            args.setOutputDir(this.outputDir);

            return args;
        }

        @Override
        public void setDeBruijnArgs(GenericDeBruijnArgs args) {

            this.outputDir = args.getOutputDir();
            this.libs = args.getLibraries();
            this.k = args.getK();
            this.threads = args.getThreads();
            this.maxMemUsageMB = args.getMemory();
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter configFile;
        private ConanParameter prefix;
        private ConanParameter kmer;
        private ConanParameter resolveRepeats;
        private ConanParameter kmerFreqCutoff;
        private ConanParameter threads;
        private ConanParameter memory;
        private ConanParameter fillGaps;


        public Params() {

            this.configFile = new PathParameter(
                    "s",
                    "configFile: the config file of solexa reads",
                    false);

            this.prefix = new PathParameter(
                    "o",
                    "outputGraph: prefix of output graph file name",
                    false);

            this.kmer = new NumericParameter(
                    "K",
                    "kmer(min 13, max 63/127): kmer size, [23]",
                    false);

            this.threads = new NumericParameter(
                    "p",
                    "n_cpu: number of cpu for use, [8]",
                    true);

            this.memory = new NumericParameter(
                    "a",
                    "initMemoryAssumption: memory assumption initialized to avoid further reallocation, unit G, [0]",
                    true);

            this.kmerFreqCutoff = new NumericParameter(
                    "d",
                    "KmerFreqCutoff: kmers with frequency no larger than KmerFreqCutoff will be deleted, [0]",
                    true);

            this.resolveRepeats = new FlagParameter(
                    "R",
                    "resolve repeats by reads, [NO]");

            this.fillGaps = new FlagParameter(
                    "F",
                    "fill gaps in scaffold, [NO]");
        }

        public ConanParameter getConfigFile() {
            return configFile;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        public ConanParameter getPrefix() {
            return prefix;
        }

        public ConanParameter getResolveRepeats() {
            return resolveRepeats;
        }

        public ConanParameter getKmerFreqCutoff() {
            return kmerFreqCutoff;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMemory() {
            return memory;
        }

        public ConanParameter getFillGaps() {
            return fillGaps;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                        this.configFile,
                        this.kmer,
                        this.prefix,
                        this.resolveRepeats,
                        this.kmerFreqCutoff,
                        this.threads,
                        this.memory,
                        this.fillGaps
            };
        }
    }
}
