package uk.ac.tgac.conan.process.asmIO.scaffold.soap;

import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
import uk.ac.ebi.fgpt.conan.core.param.FlagParameter;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 21/11/13
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class SoapV240Params implements ProcessParams {

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

    public SoapV240Params() {

        this.configFile = new PathParameter(
                "s",
                "The config file of reads",
                false);

        this.outputPrefix = new DefaultConanParameter(
                "o",
                "outputGraph: prefix of output graph file name",
                false,
                false,
                true);

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
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<ConanParameter>(Arrays.asList(
                new ConanParameter[]{
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
                }
        ));
    }
}
