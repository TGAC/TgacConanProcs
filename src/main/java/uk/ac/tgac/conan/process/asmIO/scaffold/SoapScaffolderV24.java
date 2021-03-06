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
package uk.ac.tgac.conan.process.asmIO.scaffold;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asmIO.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 21/11/13
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
@MetaInfServices(AssemblyEnhancer.class)
public class SoapScaffolderV24 extends AbstractAssemblyEnhancer {

    private static Logger log = LoggerFactory.getLogger(SoapScaffolderV24.class);

    public static final String EXE = "SOAPdenovo-127mer";
    public static final String NAME = "SOAP_Scaffold_V2.4";
    public static final AssemblyEnhancerType TYPE = AssemblyEnhancerType.SCAFFOLDER;

    public SoapScaffolderV24() {
        this(null);
    }

    public SoapScaffolderV24(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public SoapScaffolderV24(ConanExecutorService ces, Args args) {
        super(NAME, TYPE, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    public File getPreparedInputFile() {
        return new File(this.getArgs().getOutputDir(), "input_wo-gaps.fa");
    }

    public File getSoapConfigFile() {
        return new File(this.getArgs().getOutputDir(), "soap_scaff.libs");
    }

    protected String createPrepareCommand() {

        Args args = this.getArgs();

        return "finalFusion " +
                " -s " + args.getConfigFile() +
                " -g " + args.getOutputPrefix() +
                " -p " + args.getThreads() +
                " -D" +
                " -K " + args.getKmer() +
                " -c " + this.getPreparedInputFile().getAbsolutePath();
    }

    protected String createMapCommand() {

        Args args = this.getArgs();

        return EXE + " map" +
                " -s " + args.getConfigFile() +
                " -g " + args.getOutputPrefix() +
                " -p " + args.getThreads();
    }

    protected String createScaffCommand() {

        Args args = this.getArgs();

        return EXE + " scaff" +
                " -g " + args.getOutputPrefix() +
                (args.isFillGaps() ? " -F" : "") +
                (args.isUnmaskContigs() ? " -u" : "") +
                (args.isRequireWeakConnection() ? " -w" : "") +
                " -G " + args.getGapLenDiff() +
                " -b " + args.getInsertSizeUpperBound() +
                " -B " + args.getBubbleCoverage() +
                " -N " + args.getGenomeSize() +
                " -p " + args.getThreads();
    }


    @Override
    public String getCommand() {

        List<String> commands = new ArrayList<String>();

        commands.add(this.createPrepareCommand());
        commands.add(this.createMapCommand());
        commands.add(this.createScaffCommand());

        // Join commands
        return StringUtils.join(commands, "; ");
    }

    @Override
    public void setup() throws IOException {

        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + ((Args)this.getProcessArgs()).getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);

        Args args = this.getArgs();

        // Create the SOAP lib configuration file from the library list
        if (args.getConfigFile() == null  && args.getLibraries() != null) {
            args.setConfigFile(this.getSoapConfigFile());
            args.createConfigFile(args.getLibraries(), args.getConfigFile());
        }
        else if (args.getConfigFile() == null && args.getLibraries() == null) {
            throw new IOException("Cannot run SOAP without libraries or config file");
        }
        else if (args.getConfigFile() != null && args.getLibraries() != null) {
            log.warn("SOAP denovo found both a config file and libraries.  Assuming config file was intended to be used.");
        }

        log.info("Created SOAP denovo library config file at: " + this.getSoapConfigFile().getAbsolutePath());

        // SOAP really hates having gaps in contigs, it generally tends to convert them to G's so we simply split the input
        // assembly around the gaps to remove this problem... note that this might potentially make the output less
        // contiguous than the input if things go badly!
        this.removeGaps(args.getInputAssembly(), this.getPreparedInputFile());

        log.info("Removed gaps from input assembly.  Gap free assembly is at: " + this.getPreparedInputFile().getAbsolutePath());
    }

    protected void split(PrintWriter writer, String header, String seq) {

        String[] splitRead=seq.split("N+");

        int i = 0;
        for(String chunk : splitRead) {
            writer.println(">" + header + "." + i++);
            writer.println(chunk);
        }
    }

    protected void removeGaps(File assemblyWithGaps, File assemblyWithoutGaps) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(assemblyWithGaps));
        PrintWriter contigWriter = new PrintWriter(new BufferedWriter(new FileWriter(assemblyWithoutGaps)));

        String currentId = "";
        StringBuilder currentContig = new StringBuilder();


        String line = null;
        while((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.startsWith(">")) {
                if (currentContig.length() > 0) {
                    split(contigWriter, currentId, currentContig.toString());
                }
                currentContig = new StringBuilder();
                currentId = line.substring(1);
            }
            else {
                currentContig.append(line);
            }
        }

        if (currentContig.length() > 0) {
            split(contigWriter, currentId, currentContig.toString());
        }

        if (reader != null) reader.close();
        if (contigWriter != null) contigWriter.close();
    }

    @Override
    public boolean isOperational(ExecutionContext executionContext) {

        String preCommand = "";

        if (executionContext.getExternalProcessConfiguration() != null) {
            String extPreCommand = executionContext.getExternalProcessConfiguration().getCommand(this.getName());

            if (extPreCommand != null && !extPreCommand.isEmpty()) {
                preCommand += extPreCommand + "; ";
            }
        }

        this.getConanProcessService().executableOnPath("finalFusion", preCommand, executionContext);

        return super.isOperational(executionContext);
    }

    @MetaInfServices(AssemblyEnhancerArgs.class)
    public static class Args extends AbstractAssemblyEnhancerArgs {

        public static final int DEFAULT_THREADS = 8;
        public static final int DEFAULT_MEMORY = 0;

        public static final int DEFAULT_KMER = 23;
        public static final int DEFAULT_KMER_FREQ_CUTOFF = 0;
        public static final boolean DEFAULT_FILL_GAPS = false;
        public static final boolean DEFAULT_UNMASK_CONTIGS = false;
        public static final boolean DEFAULT_REQUIRE_WEAK_CONNECTION = false;
        public static final int DEFAULT_GAP_LEN_DIFF = 50;
        //public static final int DEFAULT_MIN_CONTIG_LENGTH = DEFAULT_KMER * 2;
        //public static final int DEFAULT_MIN_CONTIG_COVERAGE = ??
        //public static final int DEFAULT_MAX_CONTIG_COVERAGE = ??
        public static final double DEFAULT_INSERT_SIZE_UPPER_BOUND = 1.5;
        public static final double DEFAULT_BUBBLE_COVERAGE = 0.6;
        public static final int DEFAULT_GENOME_SIZE = 0;

        private File configFile;
        private String outputPrefix;
        private int kmer;
        private int kmerFreqCutoff;
        private boolean fillGaps;
        private boolean unmaskContigs;
        private boolean requireWeakConnection;
        private int gapLenDiff;
        //private int minContigLength;
        //private int minContigCoverage;
        //private int maxContigCoverage;
        private double insertSizeUpperBound;
        private double bubbleCoverage;
        private int genomeSize;


        public Args() {

            super(new Params(), NAME, TYPE);
            this.configFile = null;
            this.outputPrefix = "soap";
            this.kmer = DEFAULT_KMER;
            this.kmerFreqCutoff = DEFAULT_KMER_FREQ_CUTOFF;
            this.fillGaps = DEFAULT_FILL_GAPS;
            this.unmaskContigs = DEFAULT_UNMASK_CONTIGS;
            this.requireWeakConnection = DEFAULT_REQUIRE_WEAK_CONNECTION;
            this.gapLenDiff = DEFAULT_GAP_LEN_DIFF;
            this.insertSizeUpperBound = DEFAULT_INSERT_SIZE_UPPER_BOUND;
            this.bubbleCoverage = DEFAULT_BUBBLE_COVERAGE;
            this.genomeSize = DEFAULT_GENOME_SIZE;
        }

        protected Params getParams() {
            return (Params)this.params;
        }

        public File getConfigFile() {
            return configFile;
        }

        public void setConfigFile(File configFile) {
            this.configFile = configFile;
        }

        public int getKmer() {
            return kmer;
        }

        public void setKmer(int kmer) {
            this.kmer = kmer;
        }

        public int getKmerFreqCutoff() {
            return kmerFreqCutoff;
        }

        public void setKmerFreqCutoff(int kmerFreqCutoff) {
            this.kmerFreqCutoff = kmerFreqCutoff;
        }

        public boolean isFillGaps() {
            return fillGaps;
        }

        public void setFillGaps(boolean fillGaps) {
            this.fillGaps = fillGaps;
        }

        public boolean isUnmaskContigs() {
            return unmaskContigs;
        }

        public void setUnmaskContigs(boolean unmaskContigs) {
            this.unmaskContigs = unmaskContigs;
        }

        public boolean isRequireWeakConnection() {
            return requireWeakConnection;
        }

        public void setRequireWeakConnection(boolean requireWeakConnection) {
            this.requireWeakConnection = requireWeakConnection;
        }

        public int getGapLenDiff() {
            return gapLenDiff;
        }

        public void setGapLenDiff(int gapLenDiff) {
            this.gapLenDiff = gapLenDiff;
        }

        public double getInsertSizeUpperBound() {
            return insertSizeUpperBound;
        }

        public void setInsertSizeUpperBound(double insertSizeUpperBound) {
            this.insertSizeUpperBound = insertSizeUpperBound;
        }

        public double getBubbleCoverage() {
            return bubbleCoverage;
        }

        public void setBubbleCoverage(double bubbleCoverage) {
            this.bubbleCoverage = bubbleCoverage;
        }

        public int getGenomeSize() {
            return genomeSize;
        }

        public void setGenomeSize(int genomeSize) {
            this.genomeSize = genomeSize;
        }

        @Override
        public File getOutputFile() {
            return new File(this.getOutputDir(), this.getOutputPrefix() + ".scafSeq");
        }

        public static void createConfigFile(List<Library> libs, File outputLibFile) throws IOException {

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
                if (lib.isPairedEnd()) {
                    sj.add(lib.getFile1Type() == SeqFile.FileType.FASTQ, "q1=", lib.getFile1().getAbsolutePath());
                    sj.add(lib.getFile2Type() == SeqFile.FileType.FASTQ, "q2=", lib.getFile2().getAbsolutePath());
                    sj.add(lib.getFile1Type() == SeqFile.FileType.FASTA, "f1=", lib.getFile1().getAbsolutePath());
                    sj.add(lib.getFile2Type() == SeqFile.FileType.FASTA, "f2=", lib.getFile2().getAbsolutePath());
                }
                else {
                    sj.add(lib.getFile1Type() == SeqFile.FileType.FASTQ, "q=", lib.getFile1().getAbsolutePath());
                    sj.add(lib.getFile1Type() == SeqFile.FileType.FASTA, "f=", lib.getFile1().getAbsolutePath());
                }

                lines.add(sj.toString() + "\n");

            }

            FileUtils.writeLines(outputLibFile, lines);
        }


        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

            this.kmer = cmdLine.hasOption(params.getKmer().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getKmer().getShortName())) :
                    this.kmer;

            this.kmerFreqCutoff = cmdLine.hasOption(params.getKmerFreqCutoff().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getKmerFreqCutoff().getShortName())) :
                    this.kmerFreqCutoff;

            this.fillGaps = cmdLine.hasOption(params.getFillGaps().getShortName()) ?
                    cmdLine.hasOption(params.getFillGaps().getShortName()) :
                    this.fillGaps;

            this.unmaskContigs = cmdLine.hasOption(params.getUnmaskContigs().getShortName()) ?
                    cmdLine.hasOption(params.getUnmaskContigs().getShortName()) :
                    this.unmaskContigs;

            this.requireWeakConnection = cmdLine.hasOption(params.getRequireWeakConnection().getShortName()) ?
                    cmdLine.hasOption(params.getRequireWeakConnection().getShortName()) :
                    this.requireWeakConnection;

            this.gapLenDiff = cmdLine.hasOption(params.getGapLenDiff().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getGapLenDiff().getShortName())) :
                    this.gapLenDiff;

            this.insertSizeUpperBound = cmdLine.hasOption(params.getInsertSizeUpperBound().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getInsertSizeUpperBound().getShortName())) :
                    this.insertSizeUpperBound;

            this.bubbleCoverage = cmdLine.hasOption(params.getBubbleCoverage().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getBubbleCoverage().getShortName())) :
                    this.bubbleCoverage;

            this.genomeSize = cmdLine.hasOption(params.getGenomeSize().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getGenomeSize().getShortName())) :
                    this.genomeSize;
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            // **** Main args ****

            if (this.configFile != null)
                pvp.put(params.getConfigFile(), this.configFile.getAbsolutePath());

            //if (this.getInputAssembly() != null)
            //    pvp.put(params.getContigsFile(), this.getInputAssembly().getAbsolutePath());

            if (this.getOutputPrefix() != null)
                pvp.put(params.getOutputPrefix(), this.getOutputPrefix());

            if (this.kmer != DEFAULT_KMER)
                pvp.put(params.getKmer(), Integer.toString(this.kmer));

            if (this.getThreads() != DEFAULT_THREADS)
                pvp.put(params.getCpus(), Integer.toString(this.getThreads()));

            if (this.getMemory() != DEFAULT_MEMORY)
                pvp.put(params.getMemoryGb(), Integer.toString(this.getMemory() / 1000));

            if (this.kmerFreqCutoff != DEFAULT_KMER_FREQ_CUTOFF)
                pvp.put(params.getKmerFreqCutoff(), Integer.toString(this.kmerFreqCutoff));

            if (this.fillGaps)
                pvp.put(params.getFillGaps(), Boolean.toString(this.fillGaps));

            if (this.unmaskContigs)
                pvp.put(params.getUnmaskContigs(), Boolean.toString(this.unmaskContigs));

            if (this.requireWeakConnection)
                pvp.put(params.getRequireWeakConnection(), Boolean.toString(this.requireWeakConnection));

            if (this.gapLenDiff != DEFAULT_GAP_LEN_DIFF)
                pvp.put(params.getGapLenDiff(), Integer.toString(this.gapLenDiff));

            if (this.insertSizeUpperBound != DEFAULT_INSERT_SIZE_UPPER_BOUND)
                pvp.put(params.getInsertSizeUpperBound(), Double.toString(this.insertSizeUpperBound));

            if (this.bubbleCoverage != DEFAULT_BUBBLE_COVERAGE)
                pvp.put(params.getBubbleCoverage(), Double.toString(this.bubbleCoverage));

            if (this.genomeSize != DEFAULT_GENOME_SIZE)
                pvp.put(params.getGenomeSize(), Integer.toString(this.genomeSize));

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            // **** Main args ****

            if (param.equals(params.getConfigFile())) {
                this.configFile = new File(value);
            }
            //else if (param.equals(params.())) {
            //    this.setInputAssembly(new File(value));
            //}
            else if (param.equals(params.getOutputPrefix())) {
                this.setOutputPrefix(value);
            }
            else if (param.equals(params.getKmer())) {
                this.kmer = Integer.parseInt(value);
            }
            else if (param.equals(params.getCpus())) {
                this.setThreads(Integer.parseInt(value));
            }
            else if (param.equals(params.getMemoryGb())) {
                this.setMemory(Integer.parseInt(value));
            }
            else if (param.equals(params.getKmerFreqCutoff())) {
                this.kmerFreqCutoff = Integer.parseInt(value);
            }
            else if (param.equals(params.getFillGaps())) {
                this.fillGaps = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getUnmaskContigs())) {
                this.unmaskContigs = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getRequireWeakConnection())) {
                this.requireWeakConnection = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getGapLenDiff())) {
                this.gapLenDiff = Integer.parseInt(value);
            }
            else if (param.equals(params.getGapLenDiff())) {
                this.gapLenDiff = Integer.parseInt(value);
            }
            else if (param.equals(params.getInsertSizeUpperBound())) {
                this.insertSizeUpperBound = Double.parseDouble(value);
            }
            else if (param.equals(params.getBubbleCoverage())) {
                this.bubbleCoverage = Double.parseDouble(value);
            }
            else if (param.equals(params.getGenomeSize())) {
                this.genomeSize = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter configFile;
        private ConanParameter outputPrefix;
        private ConanParameter kmer;
        private ConanParameter cpus;
        private ConanParameter memoryGb;
        private ConanParameter kmerFreqCutoff;
        private ConanParameter fillGaps;
        private ConanParameter unmaskContigs;
        private ConanParameter requireWeakConnection;
        private ConanParameter gapLenDiff;
        private ConanParameter insertSizeUpperBound;
        private ConanParameter bubbleCoverage;
        private ConanParameter genomeSize;

        public Params() {

            this.configFile = new PathParameter(
                    "s",
                    "The config file of reads",
                    false);

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .description("outputGraph: prefix of output graph file name")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.kmer = new NumericParameter(
                    "K",
                    "kmer(min 13, max 127): kmer size, [23]",
                    true);

            this.cpus = new NumericParameter(
                    "p",
                    "n_cpu: number of cpu for use, [8]",
                    true);

            this.memoryGb = new NumericParameter(
                    "a",
                    "initMemoryAssumption: memory assumption initialized to avoid further reallocation, unit GB, [0]",
                    true);

            this.kmerFreqCutoff = new NumericParameter(
                    "d",
                    "KmerFreqCutoff: kmers with frequency no larger than KmerFreqCutoff will be deleted, [0]",
                    true);

            this.fillGaps = new FlagParameter(
                    "F",
                    "fill gaps in scaffold, [No]");

            this.unmaskContigs = new FlagParameter(
                    "u",
                    "un-mask contigs with high/low coverage before scaffolding, [mask]");

            this.requireWeakConnection = new FlagParameter(
                    "w",
                    "keep contigs weakly connected to other contigs in scaffold, [NO]");

            this.gapLenDiff = new NumericParameter(
                    "G",
                    "gapLenDiff: allowed length difference between estimated and filled gap, [50]",
                    true);

            this.insertSizeUpperBound = new NumericParameter(
                    "b",
                    "insertSizeUpperBound: (b*avg_ins) will be used as upper bound of insert size for large insert size ( > 1000) when handling pair-end connections between contigs if b is set to larger than 1, [1.5]",
                    true);

            this.bubbleCoverage = new NumericParameter(
                    "B",
                    "bubbleCoverage: remove contig with lower cvoerage in bubble structure if both contigs' coverage are smaller than bubbleCoverage*avgCvg, [0.6]",
                    true);

            this.genomeSize = new NumericParameter(
                    "N",
                    "genomeSize: genome size for statistics, [0]",
                    true);

        }


        public ConanParameter getConfigFile() {
            return configFile;
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        public ConanParameter getCpus() {
            return cpus;
        }

        public ConanParameter getMemoryGb() {
            return memoryGb;
        }

        public ConanParameter getKmerFreqCutoff() {
            return kmerFreqCutoff;
        }

        public ConanParameter getFillGaps() {
            return fillGaps;
        }

        public ConanParameter getUnmaskContigs() {
            return unmaskContigs;
        }

        public ConanParameter getRequireWeakConnection() {
            return requireWeakConnection;
        }

        public ConanParameter getGapLenDiff() {
            return gapLenDiff;
        }

        public ConanParameter getInsertSizeUpperBound() {
            return insertSizeUpperBound;
        }

        public ConanParameter getBubbleCoverage() {
            return bubbleCoverage;
        }

        public ConanParameter getGenomeSize() {
            return genomeSize;
        }


        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.configFile,
                    this.outputPrefix,
                    this.kmer,
                    this.cpus,
                    this.memoryGb,
                    this.kmerFreqCutoff,
                    this.fillGaps,
                    this.unmaskContigs,
                    this.requireWeakConnection,
                    this.gapLenDiff,
                    this.insertSizeUpperBound,
                    this.bubbleCoverage,
                    this.genomeSize
            };
        }

    }

}
