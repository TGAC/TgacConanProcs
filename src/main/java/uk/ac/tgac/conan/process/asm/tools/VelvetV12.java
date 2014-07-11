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

import org.apache.commons.cli.*;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.IOException;

/**
 * User: maplesod
 * Date: 13/03/13
 * Time: 15:49
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class VelvetV12 extends AbstractAssembler {

    public static final String NAME = "Velvet_V1.2";
    public static final String EXE = "velveth-127";

    public VelvetV12() {
        this(null);
    }
    public VelvetV12(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }
    public VelvetV12(ConanExecutorService conanExecutorService, AbstractProcessArgs args) {
        super(NAME, EXE, args, new Params(), conanExecutorService);
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
    public File getContigsFile() {
        return new File(this.getArgs().getOutputDir(), "contigs.fa");
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
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN;
    }

    protected String createLibString(Library lib, boolean additionalLib) {

        StringBuilder sb = new StringBuilder();

        if (lib.getFile1Type() == SeqFile.FileType.FASTQ) {
            sb.append("-fastq");
        }
        else if (lib.getFile1Type() == SeqFile.FileType.FASTA) {
            sb.append("-fasta");
        }
        else {
            throw new IllegalArgumentException("Unknown library file type: " + lib.getFile1Type());
        }

        sb.append(" ");

        if (lib.getReadLength() < 1000) {
            if (additionalLib) {
                sb.append(lib.isPairedEnd() ? "-shortPaired" : "-short");
            }
            else {
                sb.append(lib.isPairedEnd() ? "-shortPaired2" : "-short2");
            }
        }
        else {
            sb.append(lib.isPairedEnd() ? "-longPaired" : "-long");
        }

        sb.append(" ");

        if (lib.isPairedEnd()) {
            if (lib.getFiles().size() == 1) {
                sb.append("-interleaved");
            } else if (lib.getFiles().size() == 2) {
                sb.append("-separate");
            }
            else {
                throw new IllegalArgumentException("Incorrectly constructed library");
            }
        }

        sb.append(" ");

        for(File f : lib.getFiles()) {
            sb.append(f.getAbsolutePath()).append(" ");
        }

        return sb.toString();
    }

    @Override
    public String getCommand() {

        Args args = this.getArgs();

        StringBuilder libString = new StringBuilder();

        for(int i = 0; i < args.getLibs().size(); i++) {
            libString.append(this.createLibString(args.getLibs().get(i), i == 0)).append(" ");
        }

        final String velvetHCmd = "velveth-127 " + args.getOutputDir().getAbsolutePath() + " " + args.getK() + " -create_binary " + libString.toString();

        StringBuilder insString = new StringBuilder();

        for(int i = 0; i < args.getLibs().size(); i++) {
            insString.append("-ins_length" + (i != 0 ? i : "") + " " + args.getLibs().get(i).getAverageInsertSize()).append(" ");
        }

        final String velvetGCmd = "velvetg-127 " + args.getOutputDir().getAbsolutePath() + " -cov_cutoff auto " + insString.toString() + " -exp_cov auto";

        return velvetHCmd + "; " + velvetGCmd;
    }

    @MetaInfServices(DeBruijnArgs.class)
    public static class Args extends AbstractAssemblerArgs implements DeBruijnArgs {

        public static final int DEFAULT_K = 61;
        public static final int DEFAULT_COVERAGE_CUTOFF = 0;
        private int k;
        private int coverageCutoff;

        public Args() {
            super(new Params(), NAME);

            this.k = DEFAULT_K;
            this.coverageCutoff = DEFAULT_COVERAGE_CUTOFF;
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


        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

        }

        @Override
        public ParamMap getArgMap() {

            return null;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String val) {

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public GenericDeBruijnArgs getDeBruijnArgs() {

            GenericDeBruijnArgs args = new GenericDeBruijnArgs();

            args.setOutputDir(this.outputDir);
            args.setLibraries(this.libs);
            args.setThreads(this.threads);
            args.setMemory(this.getMaxMemUsageMB());

            args.setK(this.k);

            return args;
        }

        @Override
        public void setDeBruijnArgs(GenericDeBruijnArgs args) {

            this.outputDir = args.getOutputDir();
            this.libs = args.getLibraries();
            this.threads = args.getThreads();
            this.maxMemUsageMB = args.getMemory();

            this.k = args.getK();
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter hashLength;
        private ConanParameter covCutoff;
        private ConanParameter readTracking;
        private ConanParameter minContigLength;
        private ConanParameter exportToAmos;
        private ConanParameter expCoverage;
        private ConanParameter longCovCutoff;
        private ConanParameter scaffolding;
        private ConanParameter maxBranchLength;
        private ConanParameter maxDivergence;
        private ConanParameter maxGapCount;
        private ConanParameter minPairCount;
        private ConanParameter maxCoverage;
        private ConanParameter coverageMask;
        private ConanParameter longMultCutoff;
        private ConanParameter exportUnusedReads;
        private ConanParameter exportAlignments;
        private ConanParameter exportFiltered;
        private ConanParameter clean;
        private ConanParameter veryClean;
        private ConanParameter pairedExpFraction;
        private ConanParameter shortMatePaired;
        private ConanParameter conserveLong;

        public Params() {

            this.hashLength = new ParameterBuilder()
                    .shortName("hash_length")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.covCutoff = new ParameterBuilder()
                    .shortName("cov_cutoff")
                    .description("removal of low coverage nodes AFTER tour bus or allow the system to infer it")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.readTracking = new ParameterBuilder()
                    .shortName("read_trkg")
                    .create();

            this.minContigLength = new ParameterBuilder()
                    .shortName("min_contig_lgth")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.exportToAmos = new ParameterBuilder()
                    .shortName("amos_file")
                    .create();

            this.expCoverage = new ParameterBuilder()
                    .shortName("exp_cov")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.longCovCutoff = new ParameterBuilder()
                    .shortName("long_cov_cutoff")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.scaffolding = new ParameterBuilder()
                    .shortName("scaffolding")
                    .create();

            this.maxBranchLength = new ParameterBuilder()
                    .shortName("max_branch_length")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxDivergence = new ParameterBuilder()
                    .shortName("max_divergence")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.maxGapCount = new ParameterBuilder()
                    .shortName("max_gap_count")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minPairCount = new ParameterBuilder()
                    .shortName("min_pair_count")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxCoverage = new ParameterBuilder()
                    .shortName("max_coverage")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.coverageMask = new ParameterBuilder()
                    .shortName("coverage_mask")
                    .create();

            this.longMultCutoff = new ParameterBuilder()
                    .shortName("long_mult_cutoff")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.exportUnusedReads = new ParameterBuilder()
                    .shortName("unused_reads")
                    .create();

            this.exportAlignments = new ParameterBuilder()
                    .shortName("alignments")
                    .create();

            this.exportFiltered = new ParameterBuilder()
                    .shortName("exportFiltered")
                    .create();

            this.clean = new ParameterBuilder()
                    .shortName("clean")
                    .create();

            this.veryClean = new ParameterBuilder()
                    .shortName("very_clean")
                    .create();

            this.pairedExpFraction = new ParameterBuilder()
                    .shortName("paired_exp_fraction")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.shortMatePaired = new ParameterBuilder()
                    .shortName("shortMatePaired")
                    .create();

            this.conserveLong = new ParameterBuilder()
                    .shortName("conserveLong")
                    .create();
        }

        public ConanParameter getHashLength() {
            return hashLength;
        }

        public ConanParameter getCovCutoff() {
            return covCutoff;
        }

        public ConanParameter getReadTracking() {
            return readTracking;
        }

        public ConanParameter getMinContigLength() {
            return minContigLength;
        }

        public ConanParameter getExportToAmos() {
            return exportToAmos;
        }

        public ConanParameter getExpCoverage() {
            return expCoverage;
        }

        public ConanParameter getLongCovCutoff() {
            return longCovCutoff;
        }

        public ConanParameter getScaffolding() {
            return scaffolding;
        }

        public ConanParameter getMaxBranchLength() {
            return maxBranchLength;
        }

        public ConanParameter getMaxDivergence() {
            return maxDivergence;
        }

        public ConanParameter getMaxGapCount() {
            return maxGapCount;
        }

        public ConanParameter getMinPairCount() {
            return minPairCount;
        }

        public ConanParameter getMaxCoverage() {
            return maxCoverage;
        }

        public ConanParameter getCoverageMask() {
            return coverageMask;
        }

        public ConanParameter getLongMultCutoff() {
            return longMultCutoff;
        }

        public ConanParameter getExportUnusedReads() {
            return exportUnusedReads;
        }

        public ConanParameter getExportAlignments() {
            return exportAlignments;
        }

        public ConanParameter getExportFiltered() {
            return exportFiltered;
        }

        public ConanParameter getClean() {
            return clean;
        }

        public ConanParameter getVeryClean() {
            return veryClean;
        }

        public ConanParameter getPairedExpFraction() {
            return pairedExpFraction;
        }

        public ConanParameter getShortMatePaired() {
            return shortMatePaired;
        }

        public ConanParameter getConserveLong() {
            return conserveLong;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.hashLength,
                    this.covCutoff,
                    this.readTracking,
                    this.minContigLength,
                    this.exportToAmos,
                    this.expCoverage,
                    this.longCovCutoff,
                    this.scaffolding,
                    this.maxBranchLength,
                    this.maxDivergence,
                    this.maxGapCount,
                    this.minPairCount,
                    this.maxCoverage,
                    this.longMultCutoff,
                    this.exportUnusedReads,
                    this.exportAlignments,
                    this.exportFiltered,
                    this.clean,
                    this.veryClean,
                    this.pairedExpFraction,
                    this.shortMatePaired,
                    this.conserveLong
            };
        }
    }
}
