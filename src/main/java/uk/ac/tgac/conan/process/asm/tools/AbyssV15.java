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
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * User: maplesod
 * Date: 07/01/13
 * Time: 12:12
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class AbyssV15 extends AbstractAssembler {

    public static final String EXE = "abyss-pe";
    public static final String NAME = "Abyss_V1.5";

    public AbyssV15() {
        this(null);
    }

    public AbyssV15(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public AbyssV15(ConanExecutorService ces, AbstractProcessArgs args) {
        super(NAME, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public boolean makesUnitigs() {
        return true;
    }


    /**
     * Abyss only produces contigs if at least one paired end library is provided
     * @return
     */
    @Override
    public boolean makesContigs() {
        return containsPairedEndLib();
    }

    /**
     * Abyss only produces scaffolds if at least one paired end library is provided
     * @return
     */
    @Override
    public boolean makesScaffolds() {
        return containsPairedEndLib();
    }

    @Override
    public boolean makesBubbles() {
        return true;
    }

    protected boolean containsPairedEndLib() {
        for(Library lib : this.getArgs().getLibs()) {
            if (lib.isPairedEnd()) {
                return true;
            }
        }

        return false;
    }

    public File findUnitigsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-unitigs.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    public File findContigsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-contigs.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    public File findScaffoldsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-scaffolds.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    @Override
    public File getUnitigsFile() {
        return new File(this.getArgs().getOutputDir(), this.getArgs().getName() + "-unitigs.fa");
    }

    @Override
    public File getContigsFile() {
        return new File(this.getArgs().getOutputDir(), this.getArgs().getName() + "-contigs.fa");
    }

    @Override
    public File getScaffoldsFile() {
        return new File(this.getArgs().getOutputDir(), this.getArgs().getName() + "-scaffolds.fa");
    }

    @Override
    public File getBubbleFile() {
        return new File(this.getArgs().getOutputDir(), this.getArgs().getName() + "-bubbles.fa");
    }

    @Override
    public boolean usesOpenMpi() {
        return true;
    }

    @Override
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN;
    }

    @Override
    public void setup() throws IOException {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @Override
    public String getCommand() throws ConanParameterException {
        return super.getCommand(CommandLineFormat.KEY_VALUE_PAIR);
    }


    @MetaInfServices(DeBruijnArgs.class)
    public static class Args extends AbstractAssemblerArgs implements DeBruijnArgs {

        public static final String DEFAULT_NAME = "abyss";
        public static final int DEFAULT_K = 61;
        public static final int DEFAULT_THREADS = 1;
        public static final int DEFAULT_MAX_BUBBLE_BRANCHES = 2;
        public static final int DEFAULT_MAX_BUBBLE_LENGTH = 10000;
        public static final double DEFAULT_MIN_MEAN_UNITIG_KCVG = 0.0;
        public static final int DEFAULT_ALLOWABLE_DIST_ERR = 6;
        public static final double DEFAULT_MIN_EROSION = 0.0;
        public static final int DEFAULT_MIN_EROSION_PER_STRAND = 1;
        public static final int DEFAULT_MIN_ALIGNMENT_LENGTH = 0;
        public static final int DEFAULT_MIN_UNITIG_OVERLAP = 30;
        public static final int DEFAULT_MIN_NB_PAIRS_FOR_CONTIGS = 10;
        public static final double DEFAULT_MIN_BUBBLE_ID = 0.9;
        public static final int DEFAULT_MIN_BASE_QUALITY = 3;
        public static final int DEFAULT_MIN_UNITIG_SIZE_FOR_CONTIGS = 200;


        private String name;
        private int k;
        private int maxBubbleBranches;
        private int maxBubbleLength;
        private double minMeanUnitigKmerCoverage;
        private int allowableDistanceError;
        private double minErosion;
        private int minErosionPerStrand;
        private int minAlignmentLength;
        private int minUnitigOverlap;
        private int minNbPairsForContigs;
        private int minNbPairsForScaffolds;
        private double minBubbleId;
        private int minBaseQuality;
        private int minUnitigSizeForContigs;
        private int minContigSizeForScaffolds;
        private int minTipSize;



        public Args() {
            super(new Params(), NAME);

            this.name = DEFAULT_NAME;
            this.k = DEFAULT_K;
            this.maxBubbleBranches = DEFAULT_MAX_BUBBLE_BRANCHES;
            this.maxBubbleLength = DEFAULT_MAX_BUBBLE_LENGTH;
            this.minMeanUnitigKmerCoverage = DEFAULT_MIN_MEAN_UNITIG_KCVG;
            this.allowableDistanceError = DEFAULT_ALLOWABLE_DIST_ERR;
            this.minErosion = DEFAULT_MIN_EROSION;
            this.minErosionPerStrand = DEFAULT_MIN_EROSION_PER_STRAND;
            this.minAlignmentLength = DEFAULT_MIN_ALIGNMENT_LENGTH;
            this.minUnitigOverlap = DEFAULT_MIN_UNITIG_OVERLAP;
            this.minNbPairsForContigs = DEFAULT_MIN_NB_PAIRS_FOR_CONTIGS;
            this.minNbPairsForScaffolds = 0;
            this.minBubbleId = DEFAULT_MIN_BUBBLE_ID;
            this.minBaseQuality = DEFAULT_MIN_BASE_QUALITY;
            this.minUnitigSizeForContigs = DEFAULT_MIN_UNITIG_SIZE_FOR_CONTIGS;
            this.minContigSizeForScaffolds = 0;
            this.minTipSize = 0;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getK() {
            return k;
        }

        public void setK(int k) {
            this.k = k;
        }


        public int getMaxBubbleBranches() {
            return maxBubbleBranches;
        }

        public void setMaxBubbleBranches(int maxBubbleBranches) {
            this.maxBubbleBranches = maxBubbleBranches;
        }

        public int getMaxBubbleLength() {
            return maxBubbleLength;
        }

        public void setMaxBubbleLength(int maxBubbleLength) {
            this.maxBubbleLength = maxBubbleLength;
        }

        public double getMinMeanUnitigKmerCoverage() {
            return minMeanUnitigKmerCoverage;
        }

        public void setMinMeanUnitigKmerCoverage(double minMeanUnitigKmerCoverage) {
            this.minMeanUnitigKmerCoverage = minMeanUnitigKmerCoverage;
        }

        public int getAllowableDistanceError() {
            return allowableDistanceError;
        }

        public void setAllowableDistanceError(int allowableDistanceError) {
            this.allowableDistanceError = allowableDistanceError;
        }

        public double getMinErosion() {
            return minErosion;
        }

        public void setMinErosion(double minErosion) {
            this.minErosion = minErosion;
        }

        public int getMinErosionPerStrand() {
            return minErosionPerStrand;
        }

        public void setMinErosionPerStrand(int minErosionPerStrand) {
            this.minErosionPerStrand = minErosionPerStrand;
        }

        public int getMinAlignmentLength() {
            return minAlignmentLength;
        }

        public void setMinAlignmentLength(int minAlignmentLength) {
            this.minAlignmentLength = minAlignmentLength;
        }

        public int getMinUnitigOverlap() {
            return minUnitigOverlap;
        }

        public void setMinUnitigOverlap(int minUnitigOverlap) {
            this.minUnitigOverlap = minUnitigOverlap;
        }

        public int getMinNbPairsForContigs() {
            return minNbPairsForContigs;
        }

        public void setMinNbPairsForContigs(int minNbPairsForContigs) {
            this.minNbPairsForContigs = minNbPairsForContigs;
        }

        public int getMinNbPairsForScaffolds() {
            return minNbPairsForScaffolds;
        }

        public void setMinNbPairsForScaffolds(int minNbPairsForScaffolds) {
            this.minNbPairsForScaffolds = minNbPairsForScaffolds;
        }

        public double getMinBubbleId() {
            return minBubbleId;
        }

        public void setMinBubbleId(double minBubbleId) {
            this.minBubbleId = minBubbleId;
        }

        public int getMinBaseQuality() {
            return minBaseQuality;
        }

        public void setMinBaseQuality(int minBaseQuality) {
            this.minBaseQuality = minBaseQuality;
        }

        public int getMinUnitigSizeForContigs() {
            return minUnitigSizeForContigs;
        }

        public void setMinUnitigSizeForContigs(int minUnitigSizeForContigs) {
            this.minUnitigSizeForContigs = minUnitigSizeForContigs;
        }

        public int getMinContigSizeForScaffolds() {
            return minContigSizeForScaffolds;
        }

        public void setMinContigSizeForScaffolds(int minContigSizeForScaffolds) {
            this.minContigSizeForScaffolds = minContigSizeForScaffolds;
        }

        public int getMinTipSize() {
            return minTipSize;
        }

        public void setMinTipSize(int minTipSize) {
            this.minTipSize = minTipSize;
        }

        public Options createOptions() {

            Params params = this.getParams();

            // create Options object
            Options options = new Options();

            /*options.addOption(new Option(params.getName().getShortName(), true, params.getName().getDescription()));
            options.addOption(new Option(params.getKmer().getShortName(), true, params.getKmer().getDescription()));
            options.addOption(new Option(params.getThreads().getShortName(), true, params.getThreads().getDescription()));*/
            options.addOption(new Option(params.getMaxBubbleBranches().getShortName(), true, params.getMaxBubbleBranches().getDescription()));
            options.addOption(new Option(params.getMaxBubbleLength().getShortName(), true, params.getMaxBubbleLength().getDescription()));
            options.addOption(new Option(params.getMinMeanUnitigKmerCoverage().getShortName(), true, params.getMinMeanUnitigKmerCoverage().getDescription()));
            options.addOption(new Option(params.getAllowableDistanceError().getShortName(), true, params.getAllowableDistanceError().getDescription()));
            options.addOption(new Option(params.getMinErosion().getShortName(), true, params.getMinErosion().getDescription()));
            options.addOption(new Option(params.getMinErosionPerStrand().getShortName(), true, params.getMinErosionPerStrand().getDescription()));
            options.addOption(new Option(params.getMinAlignmentLength().getShortName(), true, params.getMinAlignmentLength().getDescription()));
            options.addOption(new Option(params.getMinUnitigOverlap().getShortName(), true, params.getMinUnitigOverlap().getDescription()));
            options.addOption(new Option(params.getMinNbPairsForContigs().getShortName(), true, params.getMinNbPairsForContigs().getDescription()));
            options.addOption(new Option(params.getMinNbPairsForScaffolds().getShortName(), true, params.getMinNbPairsForScaffolds().getDescription()));
            options.addOption(new Option(params.getMinBubbleId().getShortName(), true, params.getMinBubbleId().getDescription()));
            options.addOption(new Option(params.getMinBaseQuality().getShortName(), true, params.getMinBaseQuality().getDescription()));
            options.addOption(new Option(params.getMinUnitigSizeForContigs().getShortName(), true, params.getMinUnitigSizeForContigs().getDescription()));
            options.addOption(new Option(params.getMinContigSizeForScaffolds().getShortName(), true, params.getMinContigSizeForScaffolds().getDescription()));
            options.addOption(new Option(params.getMinTipSize().getShortName(), true, params.getMinTipSize().getDescription()));

            return options;
        }

        @Override
        public void parse(String args) throws IOException {
            Params params = this.getParams();

            String[] splitArgs = new String(AbyssV15.EXE + " " + args.trim()).split(" ");
            CommandLine cmdLine = null;
            try {
                cmdLine = new PosixParser().parse(createOptions(), splitArgs);
            } catch (ParseException e) {
                throw new IOException(e);
            }

            if (cmdLine == null)
                return;

            this.maxBubbleBranches = cmdLine.hasOption(params.getMaxBubbleBranches().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxBubbleBranches().getShortName())) :
                    this.maxBubbleBranches;

            this.maxBubbleLength = cmdLine.hasOption(params.getMaxBubbleLength().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxBubbleLength().getShortName())) :
                    this.maxBubbleLength;

            this.minMeanUnitigKmerCoverage = cmdLine.hasOption(params.getMinMeanUnitigKmerCoverage().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getMinMeanUnitigKmerCoverage().getShortName())) :
                    this.minMeanUnitigKmerCoverage;

            this.allowableDistanceError = cmdLine.hasOption(params.getAllowableDistanceError().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getAllowableDistanceError().getShortName())) :
                    this.allowableDistanceError;

            this.minErosion = cmdLine.hasOption(params.getMinErosion().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getMinErosion().getShortName())) :
                    this.minErosion;

            this.minErosionPerStrand = cmdLine.hasOption(params.getMinErosionPerStrand().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinErosionPerStrand().getShortName())) :
                    this.minErosionPerStrand;

            this.minAlignmentLength = cmdLine.hasOption(params.getMinAlignmentLength().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinAlignmentLength().getShortName())) :
                    this.minAlignmentLength;

            this.minUnitigOverlap = cmdLine.hasOption(params.getMinUnitigOverlap().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinUnitigOverlap().getShortName())) :
                    this.minUnitigOverlap;

            this.minNbPairsForContigs = cmdLine.hasOption(params.getMinNbPairsForContigs().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinNbPairsForContigs().getShortName())) :
                    this.minNbPairsForContigs;

            this.minNbPairsForScaffolds = cmdLine.hasOption(params.getMinNbPairsForScaffolds().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinNbPairsForScaffolds().getShortName())) :
                    this.minNbPairsForScaffolds;

            this.minBubbleId = cmdLine.hasOption(params.getMinBubbleId().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getMinBubbleId().getShortName())) :
                    this.minBubbleId;

            this.minBaseQuality = cmdLine.hasOption(params.getMinBaseQuality().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinBaseQuality().getShortName())) :
                    this.minBaseQuality;

            this.minUnitigSizeForContigs = cmdLine.hasOption(params.getMinUnitigSizeForContigs().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinUnitigSizeForContigs().getShortName())) :
                    this.minUnitigSizeForContigs;

            this.minContigSizeForScaffolds = cmdLine.hasOption(params.getMinContigSizeForScaffolds().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinContigSizeForScaffolds().getShortName())) :
                    this.minContigSizeForScaffolds;

            this.minTipSize = cmdLine.hasOption(params.getMinTipSize().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinTipSize().getShortName())) :
                    this.minTipSize;
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            if (this.name != null) {
                pvp.put(params.getName(), this.name);
            }

            if (this.k > 11) {
                pvp.put(params.getKmer(), Integer.toString(this.k));
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.getLibs() != null && !this.getLibs().isEmpty()) {
                pvp.put(params.getLibs(), new InputLibsArg(this.getLibs()).toString());
            }

            if (this.maxBubbleBranches != DEFAULT_MAX_BUBBLE_BRANCHES) {
                pvp.put(params.getMaxBubbleBranches(), Integer.toString(this.maxBubbleBranches));
            }

            if (this.maxBubbleLength != DEFAULT_MAX_BUBBLE_LENGTH) {
                pvp.put(params.getMaxBubbleLength(), Integer.toString(this.maxBubbleLength));
            }
            if (this.minMeanUnitigKmerCoverage != DEFAULT_MIN_MEAN_UNITIG_KCVG) {
                pvp.put(params.getMinMeanUnitigKmerCoverage(), Double.toString(this.minMeanUnitigKmerCoverage));
            }

            if (this.allowableDistanceError != DEFAULT_ALLOWABLE_DIST_ERR) {
                pvp.put(params.getAllowableDistanceError(), Integer.toString(this.allowableDistanceError));
            }

            if (this.minErosion != DEFAULT_MIN_EROSION) {
                pvp.put(params.getMinErosion(), Double.toString(this.minErosion));
            }

            if (this.minErosionPerStrand != DEFAULT_MIN_EROSION_PER_STRAND) {
                pvp.put(params.getMinErosionPerStrand(), Integer.toString(this.minErosionPerStrand));
            }

            if (this.minAlignmentLength != DEFAULT_MIN_ALIGNMENT_LENGTH) {
                pvp.put(params.getMinAlignmentLength(), Integer.toString(this.minAlignmentLength));
            }

            if (this.minUnitigOverlap != DEFAULT_MIN_UNITIG_OVERLAP) {
                pvp.put(params.getMinUnitigOverlap(), Integer.toString(this.minUnitigOverlap));
            }

            if (this.minNbPairsForContigs != DEFAULT_MIN_NB_PAIRS_FOR_CONTIGS) {
                pvp.put(params.getMinNbPairsForContigs(), Integer.toString(this.minNbPairsForContigs));
            }

            if (this.minNbPairsForScaffolds != 0 && this.minNbPairsForScaffolds != this.minNbPairsForContigs) {
                pvp.put(params.getMinNbPairsForScaffolds(), Integer.toString(this.minNbPairsForScaffolds));
            }

            if (this.minBubbleId != DEFAULT_MIN_BUBBLE_ID) {
                pvp.put(params.getMinBubbleId(), Double.toString(this.minBubbleId));
            }

            if (this.minBaseQuality != DEFAULT_MIN_BASE_QUALITY) {
                pvp.put(params.getMinBaseQuality(), Integer.toString(this.minBaseQuality));
            }

            if (this.minUnitigSizeForContigs != DEFAULT_MIN_UNITIG_SIZE_FOR_CONTIGS) {
                pvp.put(params.getMinUnitigSizeForContigs(), Integer.toString(this.minUnitigSizeForContigs));
            }

            if (this.minContigSizeForScaffolds != 0 && this.minContigSizeForScaffolds != this.minUnitigSizeForContigs) {
                pvp.put(params.getMinContigSizeForScaffolds(), Integer.toString(this.minContigSizeForScaffolds));
            }

            if (this.minTipSize != 0) {
                pvp.put(params.getMinTipSize(), Integer.toString(this.minTipSize));
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String val) {

            Params params = this.getParams();

            if (param.equals(params.getName())) {
                this.name = val;
            }
            else if (param.equals(params.getKmer())) {
                this.k = Integer.parseInt(val);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(val);
            }
            else if (param.equals(params.getLibs())) {
                this.setLibs(InputLibsArg.parse(val).getLibs());
            }
            else if (param.equals(params.getMaxBubbleBranches())) {
                this.maxBubbleBranches = Integer.parseInt(val);
            }
            else if (param.equals(params.getMaxBubbleLength())) {
                this.maxBubbleLength = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinMeanUnitigKmerCoverage())) {
                this.minMeanUnitigKmerCoverage = Double.parseDouble(val);
            }
            else if (param.equals(params.getAllowableDistanceError())) {
                this.allowableDistanceError = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinErosion())) {
                this.minErosion = Double.parseDouble(val);
            }
            else if (param.equals(params.getMinErosionPerStrand())) {
                this.minErosionPerStrand = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinAlignmentLength())) {
                this.minAlignmentLength = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinUnitigOverlap())) {
                this.minUnitigOverlap = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinNbPairsForContigs())) {
                this.minNbPairsForContigs = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinNbPairsForScaffolds())) {
                this.minNbPairsForScaffolds = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinBubbleId())) {
                this.minBubbleId = Double.parseDouble(val);
            }
            else if (param.equals(params.getMinBaseQuality())) {
                this.minBaseQuality = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinUnitigSizeForContigs())) {
                this.minUnitigSizeForContigs = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinContigSizeForScaffolds())) {
                this.minContigSizeForScaffolds = Integer.parseInt(val);
            }
            else if (param.equals(params.getMinTipSize())) {
                this.minTipSize = Integer.parseInt(val);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public GenericDeBruijnArgs getDeBruijnArgs() {

            GenericDeBruijnArgs args = new GenericDeBruijnArgs();

            args.setThreads(this.threads);
            args.setLibraries(this.libs);
            args.setOutputDir(this.outputDir);
            args.setMemory(this.maxMemUsageMB);

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

        private ConanParameter libs;
        private ConanParameter kmer;
        private ConanParameter threads;
        private ConanParameter name;
        private ConanParameter maxBubbleBranches;
        private ConanParameter maxBubbleLength;
        private ConanParameter minMeanUnitigKmerCoverage;
        private ConanParameter allowableDistanceError;
        private ConanParameter minErosion;
        private ConanParameter minErosionPerStrand;
        private ConanParameter minAlignmentLength;
        private ConanParameter minUnitigOverlap;
        private ConanParameter minNbPairsForContigs;
        private ConanParameter minNbPairsForScaffolds;
        private ConanParameter minBubbleId;
        private ConanParameter minBaseQuality;
        private ConanParameter minUnitigSizeForContigs;
        private ConanParameter minContigSizeForScaffolds;
        private ConanParameter minTipSize;

        public Params() {

            this.libs = new InputLibsParameter();

            this.kmer = new NumericParameter(
                    "k",
                    "k-mer size",
                    false);

            this.threads = new NumericParameter(
                    "np",
                    "the number of processes of an MPI assembly",
                    true);

            this.name = new ParameterBuilder()
                    .longName("name")
                    .description("The name of this assembly. The resulting contigs will be stored in ${name}-contigs.fa")
                    .isOptional(false)
                    .create();

            this.maxBubbleBranches = new ParameterBuilder()
                    .shortName("a")
                    .description("maximum number of branches of a bubble [2]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxBubbleLength = new ParameterBuilder()
                    .shortName("b")
                    .description("maximum length of a bubble (bp) [10000]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minMeanUnitigKmerCoverage = new ParameterBuilder()
                    .shortName("c")
                    .description("minimum mean k-mer coverage of a unitig [sqrt(median)]")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.allowableDistanceError = new ParameterBuilder()
                    .shortName("d")
                    .description("allowable error of a distance estimate (bp) [6]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minErosion = new ParameterBuilder()
                    .shortName("e")
                    .description(" minimum erosion k-mer coverage [sqrt(median)]")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.minErosionPerStrand = new ParameterBuilder()
                    .shortName("E")
                    .description("minimum erosion k-mer coverage per strand [1]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minAlignmentLength = new ParameterBuilder()
                    .shortName("l")
                    .description("minimum alignment length of a read (bp) [k]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minUnitigOverlap = new ParameterBuilder()
                    .shortName("m")
                    .description("minimum overlap of two unitigs (bp) [30]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minNbPairsForContigs = new ParameterBuilder()
                    .shortName("n")
                    .description("minimum number of pairs required for building contigs [10]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minNbPairsForScaffolds = new ParameterBuilder()
                    .shortName("N")
                    .description("minimum number of pairs required for building scaffolds [n]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minBubbleId = new ParameterBuilder()
                    .shortName("p")
                    .description("minimum sequence identity of a bubble [0.9]")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.minBaseQuality = new ParameterBuilder()
                    .shortName("q")
                    .description("minimum base quality [3] (Trim bases from the ends of reads whose quality is less q.)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minUnitigSizeForContigs = new ParameterBuilder()
                    .shortName("s")
                    .description("minimum unitig size required for building contigs (bp) [200].  The seed length should " +
                                    "be at least twice the value of k. If more sequence  is  assembled  than  the  expected " +
                                    "genome  size, try increasing s.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minContigSizeForScaffolds = new ParameterBuilder()
                    .shortName("S")
                    .description(" minimum contig size required for building scaffolds (bp) [s]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minTipSize = new ParameterBuilder()
                    .shortName("t")
                    .description(" minimum tip size (bp) [2k]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getLibs() {
            return libs;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getName() {
            return name;
        }

        public ConanParameter getMaxBubbleBranches() {
            return maxBubbleBranches;
        }

        public ConanParameter getMaxBubbleLength() {
            return maxBubbleLength;
        }

        public ConanParameter getMinMeanUnitigKmerCoverage() {
            return minMeanUnitigKmerCoverage;
        }

        public ConanParameter getAllowableDistanceError() {
            return allowableDistanceError;
        }

        public ConanParameter getMinErosion() {
            return minErosion;
        }

        public ConanParameter getMinErosionPerStrand() {
            return minErosionPerStrand;
        }

        public ConanParameter getMinAlignmentLength() {
            return minAlignmentLength;
        }

        public ConanParameter getMinUnitigOverlap() {
            return minUnitigOverlap;
        }

        public ConanParameter getMinNbPairsForContigs() {
            return minNbPairsForContigs;
        }

        public ConanParameter getMinNbPairsForScaffolds() {
            return minNbPairsForScaffolds;
        }

        public ConanParameter getMinBubbleId() {
            return minBubbleId;
        }

        public ConanParameter getMinBaseQuality() {
            return minBaseQuality;
        }

        public ConanParameter getMinUnitigSizeForContigs() {
            return minUnitigSizeForContigs;
        }

        public ConanParameter getMinContigSizeForScaffolds() {
            return minContigSizeForScaffolds;
        }

        public ConanParameter getMinTipSize() {
            return minTipSize;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.libs,
                    this.kmer,
                    this.threads,
                    this.name,
                    this.maxBubbleBranches,
                    this.maxBubbleLength,
                    this.minMeanUnitigKmerCoverage,
                    this.allowableDistanceError,
                    this.minErosion,
                    this.minErosionPerStrand,
                    this.minAlignmentLength,
                    this.minUnitigOverlap,
                    this.minNbPairsForContigs,
                    this.minNbPairsForScaffolds,
                    this.minBubbleId,
                    this.minBaseQuality,
                    this.minUnitigSizeForContigs,
                    this.minContigSizeForScaffolds,
                    this.minTipSize
            };
        }

    }

    public static class InputLibsArg {

        private List<Library> libs;

        public InputLibsArg() {
            this(new ArrayList<Library>());
        }

        public InputLibsArg(List<Library> libs) {
            this.libs = libs;
        }


        public List<Library> getLibs() {
            return libs;
        }

        public void setLibs(List<Library> libs) {
            this.libs = libs;
        }

        protected String joinPairedLibs(Map<String, FilePair> libs) {
            List<String> list = new ArrayList<String>();

            for (Map.Entry<String, FilePair> pp : libs.entrySet()) {
                list.add(pp.getKey() + "='" + pp.getValue().toString() + "'");
            }

            return StringUtils.join(list, " ");
        }

        public Map<String, FilePair> getPairedLibs(Library.Type type) {

            Map<String, FilePair> peLibs = new HashMap<String, FilePair>();

            for (Library lib : this.libs) {
                if (lib.getType() == type) {
                    peLibs.put(lib.getName(), new FilePair(lib.getFile1(), lib.getFile2()));
                }
            }

            return peLibs;
        }

        public Set<File> getSingleEndLibs() {
            Set<File> seLibs = new HashSet<File>();

            for (Library lib : this.libs) {
                if (lib.getType() == Library.Type.SE) {
                    seLibs.add(lib.getFile1());
                }
            }

            return seLibs;
        }


        @Override
        public String toString() {

            Map<String, FilePair> peLibs = getPairedLibs(Library.Type.PAIRED_END);
            Map<String, FilePair> opeLibs = getPairedLibs(Library.Type.OVERLAPPING_PAIRED_END);
            Map<String, FilePair> mpLibs = getPairedLibs(Library.Type.MATE_PAIR);
            Set<File> seLibs = getSingleEndLibs();

            StringBuilder sb = new StringBuilder();

            if (peLibs.size() > 0 || opeLibs.size() > 0) {

                sb.append("'");

                String peKeys = StringUtils.join(peLibs.keySet(), " ");
                String opeKeys = StringUtils.join(opeLibs.keySet(), " ");
                String keys = peKeys + " " + opeKeys;
                sb.append(keys.trim());

                sb.append("'");

                sb.append(" ");

                String peVals = joinPairedLibs(peLibs);
                String opeVals = joinPairedLibs(opeLibs);
                String vals = peVals + " " + opeVals;

                sb.append(vals.trim());

                sb.append(" ");
            }

            if (mpLibs != null && mpLibs.size() > 0) {

                sb.append("mp='");
                sb.append(StringUtils.join(mpLibs.keySet(), " "));
                sb.append("'");

                sb.append(" ");

                sb.append(joinPairedLibs(mpLibs));

                sb.append(" ");
            }

            if (seLibs != null && seLibs.size() > 0) {

                sb.append("se='");
                sb.append(StringUtils.join(seLibs, " "));
                sb.append("'");
            }

            return sb.toString().trim();
        }

        public static InputLibsArg parse(String libs) {

            InputLibsArg libsArg = new InputLibsArg();

            // Get PE libs
            List<String> peLibs = getAbyssArgs(libs, "lib");

            // Get MP libs
            List<String> mpLibs = getAbyssArgs(libs, "mp");

            // Get SE libs
            List<String> seLibs = getAbyssArgs(libs, "se");

            // Convert all string to actual library objects and add to libsArg.
            List<Library> allLibs = new ArrayList<Library>();

            for(String peLib : peLibs) {
                List<String> peLibPaths = getAbyssArgs(libs, peLib);

                if (peLibPaths.size() == 2) {
                    allLibs.add(createNewPELibrary(peLibPaths.get(0), peLibPaths.get(1), Library.Type.PE));
                }
                else {
                    throw new IllegalArgumentException("Paired end library does not contain two file paths");
                }
            }

            for(String mpLib : mpLibs) {
                List<String> mpLibPaths = getAbyssArgs(libs, mpLib);

                if (mpLibPaths.size() == 2) {
                    allLibs.add(createNewPELibrary(mpLibPaths.get(0), mpLibPaths.get(1), Library.Type.MP));
                }
                else {
                    throw new IllegalArgumentException("Paired end library does not contain two file paths");
                }
            }

            for(String seLib : seLibs) {

                allLibs.add(createNewSELibrary(seLib));
            }

            libsArg.setLibs(allLibs);

            return libsArg;
        }

        protected static Library createNewPELibrary(String libPath1, String libPath2, Library.Type type) {

            List<SeqFile> files = new ArrayList<SeqFile>();
            files.add(new SeqFile(libPath1));
            files.add(new SeqFile(libPath2));

            Library lib = new Library();

            lib.setType(type);
            lib.setFiles(files);
            lib.setSeqOrientation(type == Library.Type.PE ? Library.SeqOrientation.FR : Library.SeqOrientation.RF);

            return lib;
        }

        protected static Library createNewSELibrary(String libPath) {

            List<SeqFile> files = new ArrayList<SeqFile>();
            files.add(new SeqFile(libPath));

            Library lib = new Library();

            lib.setType(Library.Type.SE);
            lib.setFiles(files);

            return lib;
        }

        protected static List<String> getAbyssArgs(String string, String header) {

            int indexStart = string.indexOf("header");

            if (indexStart < 0) {
                return null;
            }

            String subStr = string.substring(indexStart);

            int indexStartArgs = subStr.indexOf("\"");

            if (indexStartArgs < 0) {
                return null;
            }

            String subStrArgs = string.substring(indexStartArgs);

            int indexEndArgs = subStrArgs.indexOf("\"");

            if (indexEndArgs < 0) {
                return null;
            }

            String args = subStrArgs.substring(0, indexEndArgs).trim();

            String[] argArray = args.split(" ");

            List<String> argList = new ArrayList<String>();

            for(String arg : argArray) {

                String argTrimmed = arg.trim();

                if (!arg.isEmpty()) {
                    argList.add(argTrimmed);
                }
            }

            return argList;
        }

    }


    public static class InputLibsParameter extends DefaultConanParameter {

        private static final long serialVersionUID = 4497529578973609010L;

        public InputLibsParameter() {
            super();

            this.name = "lib";
            this.description = "Required.  The input libraries to assemble with abyss.  Can include paired end and single end.  Will run paired end assemblies in parallel.";
            this.paramType = ParamType.OPTION;
            this.isOptional = false;
            this.isFlag = false;
            this.argValidator = ArgValidator.OFF;
        }
    }

}
