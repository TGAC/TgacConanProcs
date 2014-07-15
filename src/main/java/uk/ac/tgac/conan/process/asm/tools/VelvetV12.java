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
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
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
    public static final String EXE = "velvetg-127";

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
    public void setup() throws IOException {
        Args args = this.getArgs();

        // This is a bit of a hack to get the output directory as the first argument before all the options.
        this.setMode(args.getOutputDir().getAbsolutePath());
    }

    @Override
    public String getCommand() throws ConanParameterException {

        Args args = this.getArgs();

        // Add options describing insert sizes here
        StringBuilder insString = new StringBuilder();

        for(int i = 0; i < args.getLibs().size(); i++) {
            insString.append("-ins_length" + (i != 0 ? i : "") + " " + args.getLibs().get(i).getAverageInsertSize()).append(" ");
        }

        StringBuilder libString = new StringBuilder();

        for(int i = 0; i < args.getLibs().size(); i++) {
            libString.append(this.createLibString(args.getLibs().get(i), i == 0)).append(" ");
        }

        final String velvetHCmd = "velveth-127 " + args.getOutputDir().getAbsolutePath() + " " + args.getHashLength() + " -create_binary " + libString.toString();

        return velvetHCmd.trim() + "; " + super.getCommand() + " " + insString.toString();
    }

    @MetaInfServices(DeBruijnArgs.class)
    public static class Args extends AbstractAssemblerArgs implements DeBruijnArgs {

        public static final int DEFAULT_K = 61;
        public static final int DEFAULT_COVERAGE_CUTOFF = 0;
        public static final int DEFAULT_MAX_BRANCH_LENGTH = 100;
        public static final double DEFAULT_MAX_DIVERGENCE = 0.2;
        public static final int DEFAULT_MAX_GAP_COUNT = 3;
        public static final int DEFAULT_MIN_PAIR_COUNT = 5;
        public static final int DEFAULT_COVERAGE_MASK = 1;
        private static final int DEFAULT_LONG_MULT_CUTOFF = 2;
        private static final double DEFAULT_PAIRED_EXP_FRACTION = 0.1;

        private int hashLength;
        private int covCutoff;
        private boolean readTracking;
        private int minContigLength;
        private boolean exportToAmos;
        private double expCoverage;
        private double longCovCutoff;
        private boolean scaffolding;
        private int maxBranchLength;
        private double maxDivergence;
        private int maxGapCount;
        private int minPairCount;
        private double maxCoverage;
        private int coverageMask;
        private int longMultCutoff;
        private boolean exportUnusedReads;
        private boolean exportAlignments;
        private boolean exportFiltered;
        private boolean clean;
        private boolean veryClean;
        private double pairedExpFraction;
        private boolean shortMatePaired;
        private boolean conserveLong;


        public Args() {
            super(new Params(), NAME);

            this.hashLength = DEFAULT_K;
            this.covCutoff = DEFAULT_COVERAGE_CUTOFF;
            this.readTracking = false;
            this.minContigLength = DEFAULT_K * 2;
            this.exportToAmos = false;
            this.expCoverage = 0.0;
            this.longCovCutoff = 0.0;
            this.scaffolding = true;
            this.maxBranchLength = DEFAULT_MAX_BRANCH_LENGTH;
            this.maxDivergence = DEFAULT_MAX_DIVERGENCE;
            this.maxGapCount = DEFAULT_MAX_GAP_COUNT;
            this.minPairCount = DEFAULT_MIN_PAIR_COUNT;
            this.maxCoverage = 0.0;
            this.coverageMask = DEFAULT_COVERAGE_MASK;
            this.longMultCutoff = DEFAULT_LONG_MULT_CUTOFF;
            this.exportUnusedReads = false;
            this.exportAlignments = false;
            this.exportFiltered = false;
            this.clean = false;
            this.veryClean = false;
            this.pairedExpFraction = DEFAULT_PAIRED_EXP_FRACTION;
            this.shortMatePaired = false;
            this.conserveLong = false;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public int getHashLength() {
            return hashLength;
        }

        public void setHashLength(int hashLength) {
            this.hashLength = hashLength;
        }

        public int getCovCutoff() {
            return covCutoff;
        }

        public void setCovCutoff(int covCutoff) {
            this.covCutoff = covCutoff;
        }

        public boolean isReadTracking() {
            return readTracking;
        }

        public void setReadTracking(boolean readTracking) {
            this.readTracking = readTracking;
        }

        public int getMinContigLength() {
            return minContigLength;
        }

        public void setMinContigLength(int minContigLength) {
            this.minContigLength = minContigLength;
        }

        public boolean isExportToAmos() {
            return exportToAmos;
        }

        public void setExportToAmos(boolean exportToAmos) {
            this.exportToAmos = exportToAmos;
        }

        public double getExpCoverage() {
            return expCoverage;
        }

        public void setExpCoverage(double expCoverage) {
            this.expCoverage = expCoverage;
        }

        public double getLongCovCutoff() {
            return longCovCutoff;
        }

        public void setLongCovCutoff(double longCovCutoff) {
            this.longCovCutoff = longCovCutoff;
        }

        public boolean isScaffolding() {
            return scaffolding;
        }

        public void setScaffolding(boolean scaffolding) {
            this.scaffolding = scaffolding;
        }

        public int getMaxBranchLength() {
            return maxBranchLength;
        }

        public void setMaxBranchLength(int maxBranchLength) {
            this.maxBranchLength = maxBranchLength;
        }

        public double getMaxDivergence() {
            return maxDivergence;
        }

        public void setMaxDivergence(double maxDivergence) {
            this.maxDivergence = maxDivergence;
        }

        public int getMaxGapCount() {
            return maxGapCount;
        }

        public void setMaxGapCount(int maxGapCount) {
            this.maxGapCount = maxGapCount;
        }

        public int getMinPairCount() {
            return minPairCount;
        }

        public void setMinPairCount(int minPairCount) {
            this.minPairCount = minPairCount;
        }

        public double getMaxCoverage() {
            return maxCoverage;
        }

        public void setMaxCoverage(double maxCoverage) {
            this.maxCoverage = maxCoverage;
        }

        public int getCoverageMask() {
            return coverageMask;
        }

        public void setCoverageMask(int coverageMask) {
            this.coverageMask = coverageMask;
        }

        public int getLongMultCutoff() {
            return longMultCutoff;
        }

        public void setLongMultCutoff(int longMultCutoff) {
            this.longMultCutoff = longMultCutoff;
        }

        public boolean isExportUnusedReads() {
            return exportUnusedReads;
        }

        public void setExportUnusedReads(boolean exportUnusedReads) {
            this.exportUnusedReads = exportUnusedReads;
        }

        public boolean isExportAlignments() {
            return exportAlignments;
        }

        public void setExportAlignments(boolean exportAlignments) {
            this.exportAlignments = exportAlignments;
        }

        public boolean isExportFiltered() {
            return exportFiltered;
        }

        public void setExportFiltered(boolean exportFiltered) {
            this.exportFiltered = exportFiltered;
        }

        public boolean isClean() {
            return clean;
        }

        public void setClean(boolean clean) {
            this.clean = clean;
        }

        public boolean isVeryClean() {
            return veryClean;
        }

        public void setVeryClean(boolean veryClean) {
            this.veryClean = veryClean;
        }

        public double getPairedExpFraction() {
            return pairedExpFraction;
        }

        public void setPairedExpFraction(double pairedExpFraction) {
            this.pairedExpFraction = pairedExpFraction;
        }

        public boolean isShortMatePaired() {
            return shortMatePaired;
        }

        public void setShortMatePaired(boolean shortMatePaired) {
            this.shortMatePaired = shortMatePaired;
        }

        public boolean isConserveLong() {
            return conserveLong;
        }

        public void setConserveLong(boolean conserveLong) {
            this.conserveLong = conserveLong;
        }

        boolean parseBoolean(String value) {
            if (value.trim().equalsIgnoreCase("yes")) {
                return true;
            }
            else if (value.trim().equalsIgnoreCase("no")) {
                return false;
            }
            else {
                throw new IllegalArgumentException("Cannot parse boolean value: " + value);
            }
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

            this.hashLength = cmdLine.hasOption(params.getHashLength().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getHashLength().getShortName())) :
                    this.hashLength;

            this.covCutoff = cmdLine.hasOption(params.getCovCutoff().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getCovCutoff().getShortName())) :
                    this.covCutoff;

            this.readTracking = cmdLine.hasOption(params.getReadTracking().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getReadTracking().getShortName())) :
                    this.readTracking;

            this.minContigLength = cmdLine.hasOption(params.getMinContigLength().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinContigLength().getShortName())) :
                    this.minContigLength;

            this.exportToAmos = cmdLine.hasOption(params.getExportToAmos().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getExportToAmos().getShortName())) :
                    this.exportToAmos;

            this.expCoverage = cmdLine.hasOption(params.getExpCoverage().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getExpCoverage().getShortName())) :
                    this.expCoverage;

            this.longCovCutoff = cmdLine.hasOption(params.getLongCovCutoff().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getLongCovCutoff().getShortName())) :
                    this.longCovCutoff;

            this.scaffolding = cmdLine.hasOption(params.getScaffolding().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getScaffolding().getShortName())) :
                    this.scaffolding;

            this.maxBranchLength = cmdLine.hasOption(params.getMaxBranchLength().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxBranchLength().getShortName())) :
                    this.maxBranchLength;

            this.maxDivergence = cmdLine.hasOption(params.getMaxDivergence().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getMaxDivergence().getShortName())) :
                    this.maxDivergence;

            this.maxGapCount = cmdLine.hasOption(params.getMaxGapCount().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxGapCount().getShortName())) :
                    this.maxGapCount;

            this.minPairCount = cmdLine.hasOption(params.getMinPairCount().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinPairCount().getShortName())) :
                    this.minPairCount;

            this.maxCoverage = cmdLine.hasOption(params.getMaxCoverage().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getMaxCoverage().getShortName())) :
                    this.maxCoverage;

            this.coverageMask = cmdLine.hasOption(params.getCoverageMask().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getCoverageMask().getShortName())) :
                    this.coverageMask;

            this.longMultCutoff = cmdLine.hasOption(params.getLongMultCutoff().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getLongMultCutoff().getShortName())) :
                    this.longMultCutoff;

            this.exportUnusedReads = cmdLine.hasOption(params.getExportUnusedReads().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getExportUnusedReads().getShortName())) :
                    this.exportUnusedReads;

            this.exportAlignments = cmdLine.hasOption(params.getExportAlignments().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getExportAlignments().getShortName())) :
                    this.exportAlignments;

            this.exportFiltered = cmdLine.hasOption(params.getExportFiltered().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getExportFiltered().getShortName())) :
                    this.exportFiltered;

            this.clean = cmdLine.hasOption(params.getClean().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getClean().getShortName())) :
                    this.clean;

            this.veryClean = cmdLine.hasOption(params.getVeryClean().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getVeryClean().getShortName())) :
                    this.veryClean;

            this.pairedExpFraction = cmdLine.hasOption(params.getPairedExpFraction().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getPairedExpFraction().getShortName())) :
                    this.pairedExpFraction;

            this.shortMatePaired = cmdLine.hasOption(params.getShortMatePaired().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getShortMatePaired().getShortName())) :
                    this.shortMatePaired;

            this.conserveLong = cmdLine.hasOption(params.getConserveLong().getShortName()) ?
                    parseBoolean(cmdLine.getOptionValue(params.getConserveLong().getShortName())) :
                    this.conserveLong;
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            pvp.put(params.getCovCutoff(),      this.covCutoff == DEFAULT_COVERAGE_CUTOFF ? "auto" : Integer.toString(this.covCutoff));
            pvp.put(params.getExpCoverage(),    this.expCoverage == 0.0 ? "auto" : Double.toString(this.expCoverage));

            if (this.readTracking)                                      pvp.put(params.getReadTracking(), "yes");
            if (this.minContigLength != this.hashLength * 2)            pvp.put(params.getMinContigLength(), Integer.toString(this.minContigLength));
            if (this.exportToAmos)                                      pvp.put(params.getExportToAmos(), "yes");
            if (this.longCovCutoff != 0.0)                              pvp.put(params.getLongCovCutoff(), Double.toString(this.longCovCutoff));
            if (!this.scaffolding)                                      pvp.put(params.getScaffolding(), "no");
            if (this.maxBranchLength != DEFAULT_MAX_BRANCH_LENGTH)      pvp.put(params.getMaxBranchLength(), Integer.toString(this.maxBranchLength));
            if (this.maxDivergence != DEFAULT_MAX_DIVERGENCE)           pvp.put(params.getMaxDivergence(), Double.toString(this.maxDivergence));
            if (this.maxGapCount != DEFAULT_MAX_GAP_COUNT)              pvp.put(params.getMaxGapCount(), Integer.toString(this.maxGapCount));
            if (this.minPairCount != DEFAULT_MIN_PAIR_COUNT)            pvp.put(params.getMinPairCount(), Integer.toString(this.minPairCount));
            if (this.maxCoverage != 0.0)                                pvp.put(params.getMaxCoverage(), Double.toString(this.maxCoverage));
            if (this.coverageMask != DEFAULT_COVERAGE_MASK)             pvp.put(params.getCoverageMask(), Integer.toString(this.coverageMask));
            if (this.longMultCutoff != DEFAULT_LONG_MULT_CUTOFF)        pvp.put(params.getLongMultCutoff(), Integer.toString(this.longMultCutoff));
            if (this.exportUnusedReads)                                 pvp.put(params.getExportUnusedReads(), "yes");
            if (this.exportAlignments)                                  pvp.put(params.getExportAlignments(), "yes");
            if (this.exportFiltered)                                    pvp.put(params.getExportFiltered(), "yes");
            if (this.clean)                                             pvp.put(params.getClean(), "yes");
            if (this.veryClean)                                         pvp.put(params.getVeryClean(), "yes");
            if (this.pairedExpFraction != DEFAULT_PAIRED_EXP_FRACTION)  pvp.put(params.getPairedExpFraction(), Double.toString(this.pairedExpFraction));
            if (this.shortMatePaired)                                   pvp.put(params.getShortMatePaired(), "yes");
            if (this.conserveLong)                                      pvp.put(params.getConserveLong(), "yes");

            return pvp;
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

            args.setK(this.hashLength);

            return args;
        }

        @Override
        public void setDeBruijnArgs(GenericDeBruijnArgs args) {

            this.outputDir = args.getOutputDir();
            this.libs = args.getLibraries();
            this.threads = args.getThreads();
            this.maxMemUsageMB = args.getMemory();

            this.hashLength = args.getK();
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
                    .argValidator(ArgValidator.OFF)
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
                    .argValidator(ArgValidator.OFF)
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
