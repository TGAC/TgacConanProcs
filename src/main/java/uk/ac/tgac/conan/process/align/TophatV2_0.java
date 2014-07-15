package uk.ac.tgac.conan.process.align;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.util.PathUtils;

import java.io.File;
import java.io.IOException;

/**
 * Aligns RNA reads against a genome to identify exon-exon splice junctions.  Built on top of bowtie.
 */
public class TophatV2_0 extends AbstractConanProcess {

    public static final String EXE = "tophat2";

    public TophatV2_0() {
        this(new Args());
    }

    public TophatV2_0(Args args) {
        super(EXE, args, new Params());
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Tophat2_V2.0.X";
    }


    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_READ_MISMATCHES = 2;
        public static final int DEFAULT_MIN_INTRON_LENGTH = 50;
        public static final int DEFAULT_MAX_INTRON_LENGTH = 500000;
        private static final boolean DEFAULT_MICRO_EXON_SEARCH = false;
        private static final int DEFAULT_MATE_INNER_DIST = 50;
        private static final int DEFAULT_MATE_STD_DEV = 20;

        public static enum LibraryType {

            UNSTRANDED {
                @Override
                public Library.Strandedness toStrandedness() {
                    return Library.Strandedness.UNSTRANDED;
                }

                @Override
                public boolean acceptsStrandedness(Library.Strandedness strandedness) {
                    return strandedness == Library.Strandedness.UNSTRANDED;
                }
            },
            FIRSTSTRAND {
                @Override
                public Library.Strandedness toStrandedness() {
                    return Library.Strandedness.FIRST_STRAND;
                }

                @Override
                public boolean acceptsStrandedness(Library.Strandedness strandedness) {
                    return strandedness == Library.Strandedness.FIRST_STRAND;
                }
            },
            SECONDSTRAND {
                @Override
                public Library.Strandedness toStrandedness() {
                    return Library.Strandedness.SECOND_STRAND;
                }

                @Override
                public boolean acceptsStrandedness(Library.Strandedness strandedness) {
                    return strandedness == Library.Strandedness.SECOND_STRAND;
                }
            };

            public String toArgString() {
                return "fr-" + this.name().toLowerCase();
            }

            public static LibraryType fromArgString(String value) {
                return LibraryType.valueOf(value.substring(3).toUpperCase());
            }

            public abstract Library.Strandedness toStrandedness();
            public abstract boolean acceptsStrandedness(Library.Strandedness strandedness);

            public static LibraryType fromStrandedNess(Library.Strandedness strandedness) {

                for(LibraryType type : LibraryType.values()) {
                    if (type.acceptsStrandedness(strandedness)) {
                        return type;
                    }
                }

                return null;
            }
        }

        private File genomeIndexBase;
        private File gtf;
        private File transcriptomeIndexBase;
        private File[] leftReads;
        private File[] rightReads;
        private File[] singleEndReads;
        private File outputDir;
        private int threads;
        private int readMismatches;
        private int mateInnerDist;
        private int mateStdDev;
        private int minIntronLength;
        private int maxIntronLength;
        private boolean microExonSearch;
        private LibraryType libraryType;


        public Args() {

            super(new Params());

            this.genomeIndexBase = null;
            this.gtf = null;
            this.transcriptomeIndexBase = null;
            this.leftReads = null;
            this.rightReads = null;
            this.singleEndReads = null;
            this.outputDir = null;
            this.threads = 1;
            this.readMismatches = DEFAULT_READ_MISMATCHES;
            this.mateInnerDist = DEFAULT_MATE_INNER_DIST;
            this.mateStdDev = DEFAULT_MATE_STD_DEV;
            this.minIntronLength = DEFAULT_MIN_INTRON_LENGTH;
            this.maxIntronLength = DEFAULT_MAX_INTRON_LENGTH;
            this.microExonSearch = DEFAULT_MICRO_EXON_SEARCH;
            this.libraryType = null;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getGenomeIndexBase() {
            return genomeIndexBase;
        }

        public void setGenomeIndexBase(File genomeIndexBase) {
            this.genomeIndexBase = genomeIndexBase;
        }

        public File getGtf() {
            return gtf;
        }

        public void setGtf(File gtf) {
            this.gtf = gtf;
        }

        public File getTranscriptomeIndexBase() {
            return transcriptomeIndexBase;
        }

        public void setTranscriptomeIndexBase(File transcriptomeIndexBase) {
            this.transcriptomeIndexBase = transcriptomeIndexBase;
        }

        public File[] getLeftReads() {
            return leftReads;
        }

        public void setLeftReads(File[] leftReads) {
            this.leftReads = leftReads;
        }

        public File[] getRightReads() {
            return rightReads;
        }

        public void setRightReads(File[] rightReads) {
            this.rightReads = rightReads;
        }

        public File[] getSingleEndReads() {
            return singleEndReads;
        }

        public void setSingleEndReads(File[] singleEndReads) {
            this.singleEndReads = singleEndReads;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public int getReadMismatches() {
            return readMismatches;
        }

        public void setReadMismatches(int readMismatches) {
            this.readMismatches = readMismatches;
        }

        public int getMateInnerDist() {
            return mateInnerDist;
        }

        public void setMateInnerDist(int mateInnerDist) {
            this.mateInnerDist = mateInnerDist;
        }

        public int getMateStdDev() {
            return mateStdDev;
        }

        public void setMateStdDev(int mateStdDev) {
            this.mateStdDev = mateStdDev;
        }

        public int getMinIntronLength() {
            return minIntronLength;
        }

        public void setMinIntronLength(int minIntronLength) {
            this.minIntronLength = minIntronLength;
        }

        public int getMaxIntronLength() {
            return maxIntronLength;
        }

        public void setMaxIntronLength(int maxIntronLength) {
            this.maxIntronLength = maxIntronLength;
        }

        public boolean isMicroExonSearch() {
            return microExonSearch;
        }

        public void setMicroExonSearch(boolean microExonSearch) {
            this.microExonSearch = microExonSearch;
        }

        public LibraryType getLibraryType() {
            return libraryType;
        }

        public void setLibraryType(LibraryType libraryType) {
            this.libraryType = libraryType;
        }

        public void setLibraryType(Library.Strandedness strandedness) {

            LibraryType type = LibraryType.fromStrandedNess(strandedness);

            if (type == null)
                throw new IllegalArgumentException("Unknown strandedness specified");

            this.libraryType = type;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getGtf())) {
                this.gtf = new File(value);
            }
            else if (param.equals(params.getTranscriptomeIndexBase())) {
                this.transcriptomeIndexBase = new File(value);
            }
            else if (param.equals(params.getOutputDir())) {
                this.outputDir = new File(value);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getReadMismatches())) {
                this.readMismatches = Integer.parseInt(value);
            }
            else if (param.equals(params.getMateInnerDist())) {
                this.mateInnerDist = Integer.parseInt(value);
            }
            else if (param.equals(params.getMateStdDev())) {
                this.mateStdDev = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinIntronLength())) {
                this.minIntronLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxIntronLength())) {
                this.maxIntronLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMicroExonSearch())) {
                this.microExonSearch = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getLibraryType())) {
                this.libraryType = LibraryType.fromArgString(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getGenomeIndexBase())) {
                this.genomeIndexBase = new File(value);
            }
            else if (param.equals(params.getLeftReads())) {
                this.leftReads = PathUtils.splitPaths(value, ",");
            }
            else if (param.equals(params.getRightReads())) {
                this.rightReads = PathUtils.splitPaths(value, ",");
            }
            else if (param.equals(params.getSingleEndReads())) {
                this.singleEndReads = PathUtils.splitPaths(value, ",");
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.genomeIndexBase != null) {
                pvp.put(params.getGenomeIndexBase(), this.genomeIndexBase.getAbsolutePath());
            }

            if (this.leftReads != null && this.leftReads.length > 0) {
                pvp.put(params.getLeftReads(), PathUtils.joinAbsolutePaths(this.leftReads, ","));
            }

            if (this.rightReads != null && this.rightReads.length > 0) {
                pvp.put(params.getRightReads(), PathUtils.joinAbsolutePaths(this.rightReads, ","));
            }

            if (this.singleEndReads != null && this.singleEndReads.length > 0) {
                pvp.put(params.getSingleEndReads(), PathUtils.joinAbsolutePaths(this.singleEndReads, ","));
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.readMismatches != DEFAULT_READ_MISMATCHES) {
                pvp.put(params.getReadMismatches(), Integer.toString(this.readMismatches));
            }

            if (this.mateInnerDist != DEFAULT_MATE_INNER_DIST) {
                pvp.put(params.getMateInnerDist(), Integer.toString(this.mateInnerDist));
            }

            if (this.mateStdDev != DEFAULT_MATE_STD_DEV) {
                pvp.put(params.getMateStdDev(), Integer.toString(this.mateStdDev));
            }

            if (this.minIntronLength != DEFAULT_MIN_INTRON_LENGTH) {
                pvp.put(params.getMinIntronLength(), Integer.toString(this.minIntronLength));
            }

            if (this.maxIntronLength != DEFAULT_MAX_INTRON_LENGTH) {
                pvp.put(params.getMaxIntronLength(), Integer.toString(this.maxIntronLength));
            }

            if (this.microExonSearch != DEFAULT_MICRO_EXON_SEARCH) {
                pvp.put(params.getMicroExonSearch(), Boolean.toString(this.microExonSearch));
            }

            if (this.libraryType != null) {
                pvp.put(params.getLibraryType(), this.libraryType.toArgString());
            }

            return pvp;
        }

    }


    public static class Params extends AbstractProcessParams {

        private ConanParameter genomeIndexBase;
        private ConanParameter gtf;
        private ConanParameter transcriptomeIndexBase;
        private ConanParameter leftReads;
        private ConanParameter rightReads;
        private ConanParameter singleEndReads;
        private ConanParameter outputDir;
        private ConanParameter threads;
        private ConanParameter readMismatches;
        private ConanParameter mateInnerDist;
        private ConanParameter mateStdDev;
        private ConanParameter minIntronLength;
        private ConanParameter maxIntronLength;
        private ConanParameter microExonSearch;
        private ConanParameter libraryType;


        public Params() {

            this.genomeIndexBase = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description(
                            "The basename of the genome index to be searched. The basename is the name of any of the index " +
                                    "files up to but not including the first period. Bowtie first looks in the current directory for " +
                                    "the index files, then looks in the indexes subdirectory under the directory where the " +
                                    "currently-running bowtie executable is located, then looks in the directory specified in the " +
                                    "BOWTIE_INDEXES  (or BOWTIE2_INDEXES) environment variable.\n" +
                                    "Please note that it is highly recommended that a FASTA file with the sequence(s) the genome " +
                                    "being indexed be present in the same directory with the Bowtie index files and having the name " +
                                    "<genome_index_base>.fa. If not present, TopHat will automatically rebuild this FASTA file from " +
                                    "the Bowtie index files.")
                    .create();

            this.gtf = new ParameterBuilder()
                    .shortName("G")
                    .longName("GTF")
                    .description(
                            "Supply TopHat with a set of gene model annotations and/or known transcripts, as a GTF 2.2 or " +
                            "GFF3 formatted file. If this option is provided, TopHat will first extract the transcript " +
                            "sequences and use Bowtie to align reads to this virtual transcriptome first. Only the reads " +
                            "that do not fully map to the transcriptome will then be mapped on the genome. The reads that " +
                            "did map on the transcriptome will be converted to genomic mappings (spliced as needed) and " +
                            "merged with the novel mappings and junctions in the final tophat output.\n" +
                            "Please note that the values in the first column of the provided GTF/GFF file (column which " +
                            "indicates the chromosome or contig on which the feature is located), must match the name of " +
                            "the reference sequence in the Bowtie index you are using with TopHat. You can get a list of " +
                            "the sequence names in a Bowtie index by typing:\n" +
                            "bowtie-inspect --names your_index\n" +
                            "So before using a known annotation file with this option please make sure that the 1st column " +
                            "in the annotation file uses the exact same chromosome/contig names (case sensitive) as shown " +
                            "by the bowtie-inspect command above.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.transcriptomeIndexBase = new ParameterBuilder()
                    .longName("transcriptome-index")
                    .description(
                            "When providing TopHat with a known transcript file (-G/--GTF option above), a transcriptome " +
                            "sequence file is built and a Bowtie index has to be created for it in order to align the " +
                            "reads to the known transcripts. Creating this Bowtie index can be time consuming and " +
                            "in many cases the same transcriptome data is being used for aligning multiple samples with " +
                            "TopHat. A transcriptome index and the associated data files (the original GFF file) can be " +
                            "thus reused for multiple TopHat runs with this option, so these files are only created for " +
                            "the first run with a given set of transcripts. If multiple TopHat runs are planned with the " +
                            "same transcriptome data, TopHat should be first run with the -G/--GTF option together with " +
                            "the --transcriptome-index option pointing to a directory and a name prefix which will indicate " +
                            "where the transcriptome data files will be stored. Then subsequent TopHat runs using the same " +
                            "--transcriptome-index option value will directly use the transcriptome data created in the " +
                            "first run (no -G option needed after the first run).\n" +
                            "Please note that starting with version 2.0.10 TopHat can be invoked with just the -G/--GTF " +
                            "and --transcriptome-index options but without providing any input reads (the <genome_index_base> " +
                            "argument is still required). This is a special usage directing TopHat to only build the " +
                            "transcriptome index data files for the given annotation and then exit.\n" +
                            "Note: Only after the transcriptome files are built with one of the methods above, by a single " +
                            "TopHat process, it is safe to run multiple TopHat processes simultaneously making use of the " +
                            "same pre-built transcriptome index data.\n" +
                            "For example, in order to just prepare the transcriptome index files for a specific annotation, " +
                            "an initial, single TopHat run could be invoked like this:\n" +
                            " tophat -G known_genes.gtf --transcriptome-index=transcriptome_data/known hg19\n" +
                            "In this example TopHat will create the transcriptome_data directory in the current directory " +
                            "(if it doesn't exist already) containing files known.gff, known.fa, known.fa.tlst, known.fa.ver " +
                            "and the known.* Bowtie index files. Then for subsequent TopHat runs with the same genome and " +
                            "known transcripts but different reads, TopHat will no longer spend time building the " +
                            "transcriptome index because it can use the previously built transcriptome index files, so the " +
                            "-G option is no longer needed (however using it again will not force TopHat to rebuild the " +
                            "transcriptome index files if they are already present and with the matching version)\n" +
                            " tophat -o out_sample1 -p4 --transcriptome-index=transcriptome_data/known hg19 sample1_1.fq.z " +
                            " sample1_2.fq.z & tophat -o out_sample2 -p4 --transcriptome-index=transcriptome_data/known " +
                            " hg19 sample2_1.fq.z sample2_2.fq.z & ")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.leftReads = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(1)
                    .description(
                            "A comma-separated list of files containing reads in FASTQ or FASTA format. When running TopHat " +
                                    "with paired-end reads, this should be the *_1 (\"left\") set of files.)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.rightReads = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(true)
                    .argIndex(2)
                    .description(
                            "A comma-separated list of files containing reads in FASTA or FASTA format. Only used when " +
                                    "running TopHat with paired end reads, and contains the *_2 (\"right\") set of files. The *_2 " +
                                    "files MUST appear in the same order as the *_1 files.")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.singleEndReads = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(true)
                    .argIndex(3)
                    .description(
                            "A comma-separated list of files containing reads in FASTA or FASTA format. This is not an " +
                                    "explicit option ")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.outputDir = new ParameterBuilder()
                    .shortName("o")
                    .longName("output-dir")
                    .description(
                            "Sets the name of the directory in which TopHat will write all of its output. The default is " +
                                    "\"./tophat_out")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("p")
                    .longName("num-threads")
                    .description("Use this many threads to align reads. The default is 1.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.readMismatches = new ParameterBuilder()
                    .shortName("N")
                    .longName("read-mismatches")
                    .description("Final read alignments having more than these many mismatches are discarded. The default is 2.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.mateInnerDist = new ParameterBuilder()
                    .shortName("r")
                    .longName("mate-inner-dist")
                    .description("This is the expected (mean) inner distance between mate pairs. For, example, for paired " +
                            "end runs with fragments selected at 300bp, where each end is 50bp, you should set -r to be 200. The default is 50bp.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.mateStdDev = new ParameterBuilder()
                    .longName("mate-std-dev")
                    .description("The standard deviation for the distribution on inner distances between mate pairs. The default is 20bp.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minIntronLength = new ParameterBuilder()
                    .shortName("i")
                    .longName("min-intron-length")
                    .description("The minimum intron length. TopHat will ignore donor/acceptor pairs closer than this " +
                            "many bases apart. The default is 70.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxIntronLength = new ParameterBuilder()
                    .shortName("I")
                    .longName("max-intron-length")
                    .description("The maximum intron length. When searching for junctions ab initio, TopHat will ignore " +
                            "donor/acceptor pairs farther than this many bases apart, except when such a pair is supported by a split segment alignment of a long read. The default is 500000.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.microExonSearch = new ParameterBuilder()
                    .longName("microexon-search")
                    .isFlag(true)
                    .description("With this option, the pipeline will attempt to find alignments incident to micro-exons. Works only for reads 50bp or longer.")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.libraryType = new ParameterBuilder()
                    .longName("library-type")
                    .description("The default is unstranded (fr-unstranded). If either fr-firststrand or fr-secondstrand " +
                            "is specified, every read alignment will have an XS attribute tag as explained below. " +
                            "Consider supplying library type options below to select the correct RNA-seq protocol.")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

        }

        public ConanParameter getGenomeIndexBase() {
            return genomeIndexBase;
        }

        public ConanParameter getGtf() {
            return gtf;
        }

        public ConanParameter getTranscriptomeIndexBase() {
            return transcriptomeIndexBase;
        }

        public ConanParameter getLeftReads() {
            return leftReads;
        }

        public ConanParameter getRightReads() {
            return rightReads;
        }

        public ConanParameter getSingleEndReads() {
            return singleEndReads;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getReadMismatches() {
            return readMismatches;
        }

        public ConanParameter getMateInnerDist() {
            return mateInnerDist;
        }

        public ConanParameter getMateStdDev() {
            return mateStdDev;
        }

        public ConanParameter getMinIntronLength() {
            return minIntronLength;
        }

        public ConanParameter getMaxIntronLength() {
            return maxIntronLength;
        }

        public ConanParameter getMicroExonSearch() {
            return microExonSearch;
        }

        public ConanParameter getLibraryType() {
            return libraryType;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.genomeIndexBase,
                    this.gtf,
                    this.transcriptomeIndexBase,
                    this.leftReads,
                    this.rightReads,
                    this.singleEndReads,
                    this.outputDir,
                    this.threads,
                    this.readMismatches,
                    this.mateInnerDist,
                    this.mateStdDev,
                    this.minIntronLength,
                    this.maxIntronLength,
                    this.microExonSearch,
                    this.libraryType
            };
        }

    }
}
