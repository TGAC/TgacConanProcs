package uk.ac.tgac.conan.process.asm.tools;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.*;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by maplesod on 18/02/15.
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class MaSuRCAV23 extends AbstractAssembler {

    public static final String NAME = "MaSuRCA_V2.3";
    public static final String EXE = "masurca";

    public MaSuRCAV23() {
        this(null);
    }
    public MaSuRCAV23(ConanExecutorService ces) {
        this(ces, new Args());
    }
    public MaSuRCAV23(ConanExecutorService ces, AbstractProcessArgs args) {
        super(NAME, EXE, args, new Params(), ces);

        this.setFormat(CommandLineFormat.KEY_VALUE_PAIR);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    protected void generateConfigFile() throws IOException {

        StringJoiner sj = new StringJoiner("\n");

        sj.add("DATA");
/*        PE= pe 180 20  /FULL_PATH/frag_1.fastq  /FULL_PATH/frag_2.fastq
        JUMP= sh 3600 200  /FULL_PATH/short_1.fastq  /FULL_PATH/short_2.fastq
        OTHER=/FULL_PATH/file.frg
  */
        sj.add("END");
        sj.add("PARAMETERS");
/*        #this is k-mer size for deBruijn graph values between 25 and 101 are supported, auto will compute the optimal size based on the read data and GC content
                GRAPH_KMER_SIZE = auto
        #set this to 1 for Illumina-only assemblies and to 0 if you have 1x or more long (Sanger, 454) reads, you can also set this to 0 for large data sets with high jumping clone coverage, e.g. >50x
                USE_LINKING_MATES = 0
        #this parameter is useful if you have too many jumping library mates. Typically set it to 60 for bacteria and 300 for the other organisms
                LIMIT_JUMP_COVERAGE = 300
        #these are the additional parameters to Celera Assembler.  do not worry about performance, number or processors or batch sizes -- these are computed automatically.
        #set cgwErrorRate=0.25 for bacteria and 0.1<=cgwErrorRate<=0.15 for other organisms.
        CA_PARAMETERS = cgwErrorRate=0.15 ovlMemory=4GB*/
/*
        #minimum count k-mers used in error correction 1 means all k-mers are used.  one can increase to 2 if coverage >100
        KMER_COUNT_THRESHOLD = 1
        #auto-detected number of cpus to use
                NUM_THREADS = 16
        #this is mandatory jellyfish hash size -- a safe value is estimated_genome_size*estimated_coverage
        JF_SIZE = 200000000
        #this specifies if we do (1) or do not (0) want to trim long runs of homopolymers (e.g. GGGGGGGG) from 3' read ends, use it for high GC genomes
        DO_HOMOPOLYMER_TRIM = 0
*/
        sj.add("END");


        FileUtils.writeStringToFile(this.getArgs().getConfigurationFile(), sj.toString());
    }

    @Override
    public boolean makesUnitigs() {
        return false;
    }

    @Override
    public boolean makesContigs() {
        return false;
    }

    @Override
    public boolean makesScaffolds() {
        return true;
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
    public File getContigsFile() {
        return null;
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
    public File getBestAssembly() {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @MetaInfServices(DeBruijnOptimiserArgs.class)
    public static class Args extends AbstractAssemblerArgs implements DeBruijnOptimiserArgs {


        public Args() {
            super(new Params(), NAME);
        }

        public Params getParams() {
            return (Params)this.params;
        }


        public File getConfigurationFile() {
            return new File(this.outputDir, "config.txt");
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

        }

        @Override
        public GenericDeBruijnOptimiserArgs getDeBruijnOptimiserArgs() {
            return null;
        }

        @Override
        public void setDeBruijnOptimiserArgs(GenericDeBruijnOptimiserArgs args) {

        }

        @Override
        public ParamMap getArgMap() {
            return null;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter outputDir;
        private ConanParameter threads;
        private ConanParameter kmer;
        private ConanParameter useLinkingMates;
        private ConanParameter limitJumpCoverage;
        private ConanParameter caParameters;
        private ConanParameter kmerCountThreshold;
        private ConanParameter doHomopolymerTrim;

        public Params() {

            this.outputDir = new ParameterBuilder()
                    .shortName("o")
                    .description("Specify the output directory. Required option.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.threads = new ParameterBuilder()
                    .longName("NUM_THREADS")
                    .description("Number of threads. The default value is 16.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.kmer = new ParameterBuilder()
                    .longName("GRAPH_KMER_SIZE")
                    .description("this is k-mer size for deBruijn graph values between 25 and 101 are supported, auto will compute the optimal size based on the read data and GC content")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.useLinkingMates = new ParameterBuilder()
                    .longName("USE_LINKING_MATES")
                    .description("#set this to 1 for Illumina-only assemblies and to 0 if you have 1x or more long (Sanger, 454) reads, you can also set this to 0 for large data sets with high jumping clone coverage, e.g. >50x")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.limitJumpCoverage = new ParameterBuilder()
                    .longName("LIMIT_JUMP_COVERAGE")
                    .description("this parameter is useful if you have too many jumping library mates. Typically set it to 60 for bacteria and 300 for the other organisms")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.caParameters = new ParameterBuilder()
                    .longName("CA_PARAMETERS")
                    .description("these are the additional parameters to Celera Assembler.  do not worry about performance, number or processors or batch sizes -- these are computed automatically. \n" +
                            "#set cgwErrorRate=0.25 for bacteria and 0.1<=cgwErrorRate<=0.15 for other organisms.")
                    .create();

            this.kmerCountThreshold = new ParameterBuilder()
                    .longName("KMER_COUNT_THRESHOLD")
                    .description("minimum count k-mers used in error correction 1 means all k-mers are used.  one can increase to 2 if coverage >100")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.doHomopolymerTrim = new ParameterBuilder()
                    .longName("DO_HOMOPOLYMER_TRIM")
                    .description("this specifies if we do (1) or do not (0) want to trim long runs of homopolymers (e.g. GGGGGGGG) from 3' read ends, use it for high GC genomes")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        public ConanParameter getUseLinkingMates() {
            return useLinkingMates;
        }

        public ConanParameter getLimitJumpCoverage() {
            return limitJumpCoverage;
        }

        public ConanParameter getCaParameters() {
            return caParameters;
        }

        public ConanParameter getKmerCountThreshold() {
            return kmerCountThreshold;
        }

        public ConanParameter getDoHomopolymerTrim() {
            return doHomopolymerTrim;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.outputDir,
                    this.threads,
                    this.kmer,
                    this.useLinkingMates,
                    this.limitJumpCoverage,
                    this.caParameters,
                    this.kmerCountThreshold,
                    this.doHomopolymerTrim
            };
        }
    }
}
