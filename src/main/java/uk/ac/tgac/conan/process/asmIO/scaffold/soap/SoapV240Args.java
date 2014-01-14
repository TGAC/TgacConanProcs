package uk.ac.tgac.conan.process.asmIO.scaffold.soap;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOArgs;
import uk.ac.tgac.conan.process.asmIO.scaffold.sspace.SSpaceBasicV2Process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 21/11/13
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
@MetaInfServices(uk.ac.tgac.conan.process.asmIO.AssemblyIOArgsCreator.class)
public class SoapV240Args extends AbstractAssemblyIOArgs {

    private SoapV240Params params = new SoapV240Params();

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


    public SoapV240Args() {
        this.configFile = null;
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

    @Override
    public AbstractAssemblyIOArgs create(File inputFile, File outputDir, String outputPrefix, List<Library> libs,
                                         int threads, int memory, String otherArgs) throws IOException {

        SoapV240Args args = new SoapV240Args();
        args.setInputFile(inputFile);
        args.setOutputDir(outputDir);
        args.setOutputPrefix(outputPrefix);
        args.setLibraries(libs);
        args.setThreads(threads);
        args.setMemory(memory);
        args.parse(otherArgs);

        return args;
    }

    @Override
    public String getName() {
        return SoapV240Process.NAME;
    }

    @Override
    public String getAssemblyIOProcessType() {
        return SoapV240Process.TYPE;
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

    public Options createOptions() {

        // create Options object
        Options options = new Options();

        options.addOption(new Option(params.getKmer().getShortName(), true, params.getKmer().getDescription()));
        options.addOption(new Option(params.getKmerFreqCutoff().getShortName(), true, params.getKmerFreqCutoff().getDescription()));
        options.addOption(new Option(params.getFillGaps().getShortName(), true, params.getFillGaps().getDescription()));
        options.addOption(new Option(params.getUnmaskContigs().getShortName(), true, params.getUnmaskContigs().getDescription()));
        options.addOption(new Option(params.getRequireWeakConnection().getShortName(), true, params.getRequireWeakConnection().getDescription()));
        options.addOption(new Option(params.getGapLenDiff().getShortName(), true, params.getGapLenDiff().getDescription()));
        options.addOption(new Option(params.getInsertSizeUpperBound().getShortName(), true, params.getInsertSizeUpperBound().getDescription()));
        options.addOption(new Option(params.getBubbleCoverage().getShortName(), true, params.getBubbleCoverage().getDescription()));
        options.addOption(new Option(params.getGenomeSize().getShortName(), true, params.getGenomeSize().getDescription()));

        return options;
    }


    @Override
    public void parse(String args) throws IOException {
        String[] splitArgs = new String(SSpaceBasicV2Process.EXE + " " + args).split(" ");
        CommandLine cmdLine = null;
        try {
            cmdLine = new PosixParser().parse(createOptions(), splitArgs);
        } catch (ParseException e) {
            throw new IOException(e);
        }

        if (cmdLine == null)
            return;

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

        ParamMap pvp = new DefaultParamMap();

        // **** Main args ****

        if (this.configFile != null)
            pvp.put(params.getConfigFile(), this.configFile.getAbsolutePath());

        //if (this.getInputFile() != null)
        //    pvp.put(params.getContigsFile(), this.getInputFile().getAbsolutePath());

        if (this.getOutputPrefix() != null)
            pvp.put(params.getOutputPrefix(), this.getOutputPrefix());

        if (this.kmer != DEFAULT_KMER)
            pvp.put(params.getKmer(), Integer.toString(this.kmer));

        if (this.getThreads() != DEFAULT_THREADS)
            pvp.put(params.getCpus(), Integer.toString(this.getThreads()));

        if (this.getMemory() != DEFAULT_MEMORY)
            pvp.put(params.getMemoryGb(), Integer.toString(this.getMemory()));

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
    public void setFromArgMap(ParamMap pvp) throws IOException {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            ConanParameter param = entry.getKey();

            // **** Main args ****

            if (param.equals(this.params.getConfigFile())) {
                this.configFile = new File(entry.getValue());
            }
            //else if (param.equals(this.params.())) {
            //    this.setInputFile(new File(entry.getValue()));
            //}
            else if (param.equals(this.params.getOutputPrefix())) {
                this.setOutputPrefix(entry.getValue());
            }
            else if (param.equals(this.params.getKmer())) {
                this.kmer = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getCpus())) {
                this.setThreads(Integer.parseInt(entry.getValue()));
            }
            else if (param.equals(this.params.getMemoryGb())) {
                this.setMemory(Integer.parseInt(entry.getValue()));
            }
            else if (param.equals(this.params.getKmerFreqCutoff())) {
                this.kmerFreqCutoff = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getFillGaps())) {
                this.fillGaps = Boolean.parseBoolean(entry.getValue());
            }
            else if (param.equals(this.params.getUnmaskContigs())) {
                this.unmaskContigs = Boolean.parseBoolean(entry.getValue());
            }
            else if (param.equals(this.params.getRequireWeakConnection())) {
                this.requireWeakConnection = Boolean.parseBoolean(entry.getValue());
            }
            else if (param.equals(this.params.getGapLenDiff())) {
                this.gapLenDiff = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getGapLenDiff())) {
                this.gapLenDiff = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getInsertSizeUpperBound())) {
                this.insertSizeUpperBound = Double.parseDouble(entry.getValue());
            }
            else if (param.equals(this.params.getBubbleCoverage())) {
                this.bubbleCoverage = Double.parseDouble(entry.getValue());
            }
            else if (param.equals(this.params.getGenomeSize())) {
                this.genomeSize = Integer.parseInt(entry.getValue());
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }
}
