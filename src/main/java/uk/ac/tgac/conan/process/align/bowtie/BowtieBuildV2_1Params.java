package uk.ac.tgac.conan.process.align.bowtie;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 13/01/14
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class BowtieBuildV2_1Params extends AbstractProcessParams {

    private ConanParameter referenceIn;
    private ConanParameter baseName;
    private ConanParameter threads;

    public BowtieBuildV2_1Params() {

        this.referenceIn = new ParameterBuilder()
                .isOption(false)
                .isOptional(false)
                .description("A comma-separated list of FASTA files containing the reference sequences to be aligned to.  " +
                             "E.g., <reference_in> might be chr1.fa,chr2.fa,chrX.fa,chrY.fa.")
                .argIndex(0)
                .create();

        this.baseName = new ParameterBuilder()
                .isOption(false)
                .isOptional(false)
                .description("The basename of the index files to write. By default, bowtie2-build writes files named NAME.1.bt2, " +
                             "NAME.2.bt2, NAME.3.bt2, NAME.4.bt2, NAME.rev.1.bt2, and NAME.rev.2.bt2, where NAME is <bt2_base>.")
                .argIndex(1)
                .create();

        this.threads = new ParameterBuilder()
                .shortName("p")
                .longName("threads")
                .description(
                "Launch NTHREADS parallel search threads (default: 1). Threads will run on separate processors/cores " +
                "and synchronize when parsing reads and outputting alignments. Searching for alignments is highly " +
                "parallel, and speedup is close to linear. Increasing -p increases Bowtie 2's memory footprint. E.g. " +
                "when aligning to a human genome index, increasing -p from 1 to 8 increases the memory footprint by a " +
                "few hundred megabytes. This option is only available if bowtie is linked with the pthreads library " +
                "(i.e. if BOWTIE_PTHREADS=0 is not specified at build time).")
                .argValidator(ArgValidator.DIGITS)
                .create();
    }


    public ConanParameter getReferenceIn() {
        return referenceIn;
    }

    public ConanParameter getBaseName() {
        return baseName;
    }

    public ConanParameter getThreads() {
        return threads;
    }


    @Override
    public ConanParameter[] getConanParametersAsArray() {
        return new ConanParameter[]{
                this.referenceIn,
                this.baseName,
                this.threads
        };
    }


}
