package uk.ac.tgac.conan.process.align.tophat;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 13/01/14
 * Time: 13:44
 * To change this template use File | Settings | File Templates.
 */
public class TophatV2_0Params extends AbstractProcessParams {

    private ConanParameter genomeIndexBase;
    private ConanParameter leftReads;
    private ConanParameter rightReads;
    private ConanParameter outputDir;
    private ConanParameter threads;

    public TophatV2_0Params() {

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
    }

    public ConanParameter getGenomeIndexBase() {
        return genomeIndexBase;
    }

    public ConanParameter getLeftReads() {
        return leftReads;
    }

    public ConanParameter getRightReads() {
        return rightReads;
    }

    public ConanParameter getOutputDir() {
        return outputDir;
    }

    public ConanParameter getThreads() {
        return threads;
    }

    @Override
    public ConanParameter[] getConanParametersAsArray() {
        return new ConanParameter[]{
                this.genomeIndexBase,
                this.leftReads,
                this.rightReads,
                this.outputDir,
                this.threads
        };
    }

}
