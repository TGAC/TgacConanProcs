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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;
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
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 13/03/13
 * Time: 15:49
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class PlatanusAssembleV12 extends AbstractAssembler {

    public static final String NAME = "Platanus_Assemble_V1.2";
    public static final String EXE = "platanus";
    public static final String MODE = "assemble";

    public PlatanusAssembleV12() {
        this(null);
    }
    public PlatanusAssembleV12(ConanExecutorService ces) {
        this(ces, new Args());
    }
    public PlatanusAssembleV12(ConanExecutorService ces, Args args) {
        super(NAME, EXE, args, new Params(), ces);
        this.setMode(MODE);
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
        return true;
    }

    @Override
    public File getUnitigsFile() {
        return null;
    }

    @Override
    public File getContigsFile() {
        return new File(this.getArgs().getOutputDir(), this.getArgs().getOutputPrefix() + "_contig.fa");
    }

    @Override
    public File getScaffoldsFile() {
        return null;
    }

    @Override
    public File getBubbleFile() {
        return new File(this.getArgs().getOutputDir(), this.getArgs().getOutputPrefix() + "_contigBubble.fa");
    }

    @Override
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN_OPTIMISER;
    }

    @Override
    public void setup() throws IOException {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @MetaInfServices(DeBruijnOptimiserArgs.class)
    public static class Args extends AbstractAssemblerArgs implements DeBruijnOptimiserArgs {

        public static final String DEFAULT_OUTPUT_PREFIX = "platanus";
        public static final int DEFAULT_INITIAL_K = 32;
        public static final int DEFAULT_K_STEP = 10;
        public static final int DEFAULT_INITIAL_K_CUTOFF = 0;
        public static final int DEFAULT_MIN_K_COVERAGE = 2;
        public static final double DEFAULT_K_EXT_LEVEL = 10.0;
        public static final double DEFAULT_MAX_DIFF_BUBBLE_CRUSH = 0.1;
        public static final double DEFAULT_MAX_DIFF_BRANCH_CUT = 0.5;
        public static final int DEFAULT_THREADS = 1;
        public static final int DEFAULT_MEM_LIMIT = 16;

        private String outputPrefix;
        private int initialK;
        private int kStep;
        private int initialKCutoff;
        private int minKCoverage;
        private double kExtensionLevel;
        private double maxDiffBubbleCrush;
        private double maxDiffBranchCut;

        public Args() {
            super(new Params(), NAME);

            this.outputPrefix = DEFAULT_OUTPUT_PREFIX;
            this.initialK = DEFAULT_INITIAL_K;
            this.kStep = DEFAULT_K_STEP;
            this.initialKCutoff = DEFAULT_INITIAL_K_CUTOFF;
            this.minKCoverage = DEFAULT_MIN_K_COVERAGE;
            this.kExtensionLevel = DEFAULT_K_EXT_LEVEL;
            this.maxDiffBubbleCrush = DEFAULT_MAX_DIFF_BUBBLE_CRUSH;
            this.maxDiffBranchCut = DEFAULT_MAX_DIFF_BRANCH_CUT;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public String getOutputPrefix() {
            return outputPrefix;
        }

        public void setOutputPrefix(String outputPrefix) {
            this.outputPrefix = outputPrefix;
        }

        public int getInitialK() {
            return initialK;
        }

        public void setInitialK(int initialK) {
            this.initialK = initialK;
        }

        public int getkStep() {
            return kStep;
        }

        public void setkStep(int kStep) {
            this.kStep = kStep;
        }

        public int getInitialKCutoff() {
            return initialKCutoff;
        }

        public void setInitialKCutoff(int initialKCutoff) {
            this.initialKCutoff = initialKCutoff;
        }

        public int getMinKCoverage() {
            return minKCoverage;
        }

        public void setMinKCoverage(int minKCoverage) {
            this.minKCoverage = minKCoverage;
        }

        public double getkExtensionLevel() {
            return kExtensionLevel;
        }

        public void setkExtensionLevel(double kExtensionLevel) {
            this.kExtensionLevel = kExtensionLevel;
        }

        public double getMaxDiffBubbleCrush() {
            return maxDiffBubbleCrush;
        }

        public void setMaxDiffBubbleCrush(double maxDiffBubbleCrush) {
            this.maxDiffBubbleCrush = maxDiffBubbleCrush;
        }

        public double getMaxDiffBranchCut() {
            return maxDiffBranchCut;
        }

        public void setMaxDiffBranchCut(double maxDiffBranchCut) {
            this.maxDiffBranchCut = maxDiffBranchCut;
        }

        public int getMemLimitGB() {
            return this.maxMemUsageMB / 1000;
        }


        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

            this.initialK = cmdLine.hasOption(params.getInitialK().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getInitialK().getShortName())) :
                    this.initialK;

            this.kStep = cmdLine.hasOption(params.getkStep().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getkStep().getShortName())) :
                    this.kStep;

            this.initialKCutoff = cmdLine.hasOption(params.getInitialKCutoff().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getInitialKCutoff().getShortName())) :
                    this.initialKCutoff;

            this.kExtensionLevel = cmdLine.hasOption(params.getkExtensionLevel().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getkExtensionLevel().getShortName())) :
                    this.kExtensionLevel;

            this.minKCoverage = cmdLine.hasOption(params.getMinKCoverage().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinKCoverage().getShortName())) :
                    this.minKCoverage;

            this.maxDiffBubbleCrush = cmdLine.hasOption(params.getMaxDiffBubbleCrush().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxDiffBubbleCrush().getShortName())) :
                    this.maxDiffBubbleCrush;

            this.maxDiffBranchCut = cmdLine.hasOption(params.getMaxDiffBranchCut().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxDiffBranchCut().getShortName())) :
                    this.maxDiffBranchCut;
        }

        @Override
        public ParamMap getArgMap() {

            Params params = (Params)this.params;
            ParamMap pvp = new DefaultParamMap();

            if (this.outputPrefix != null) {
                pvp.put(params.getOutputPrefix(), this.outputPrefix);
            }

            if (this.initialK != DEFAULT_INITIAL_K) {
                pvp.put(params.getInitialK(), Integer.toString(this.initialK));
            }

            if (this.kStep != DEFAULT_K_STEP) {
                pvp.put(params.getkStep(), Integer.toString(this.kStep));
            }

            if (this.initialKCutoff != DEFAULT_INITIAL_K_CUTOFF) {
                pvp.put(params.getInitialKCutoff(), Integer.toString(this.initialKCutoff));
            }

            if (this.threads != DEFAULT_THREADS) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.getMemLimitGB() != DEFAULT_MEM_LIMIT) {
                pvp.put(params.getMemoryLimit(), Integer.toString(this.getMemLimitGB()));
            }

            if (this.minKCoverage != DEFAULT_MIN_K_COVERAGE) {
                pvp.put(params.getMinKCoverage(), Integer.toString(this.minKCoverage));
            }

            if (this.kExtensionLevel != DEFAULT_K_EXT_LEVEL) {
                pvp.put(params.getkExtensionLevel(), Double.toString(this.kExtensionLevel));
            }

            if (this.maxDiffBubbleCrush != DEFAULT_MAX_DIFF_BUBBLE_CRUSH) {
                pvp.put(params.getMaxDiffBubbleCrush(), Double.toString(this.maxDiffBubbleCrush));
            }

            if (this.maxDiffBranchCut != DEFAULT_MAX_DIFF_BRANCH_CUT) {
                pvp.put(params.getMaxDiffBranchCut(), Double.toString(this.getMaxDiffBranchCut()));
            }

            if (this.libs != null) {
                pvp.put(params.getFiles(), this.createFileString());
            }

            return pvp;
        }

        protected String createFileString() {

            List<String> libStrings = new ArrayList<>();
            for(Library lib : this.getLibs()) {
                if (lib.isPairedEnd()) {
                    libStrings.add(lib.getFile1().getAbsolutePath() + " " + lib.getFile2().getAbsolutePath());
                }
                else {
                    libStrings.add(lib.getFile1().getAbsolutePath());
                }
            }

            return StringUtils.join(libStrings, " ");
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;

            if (param.equals(params.getOutputPrefix())) {
                this.outputPrefix = value;
            }
            else if (param.equals(params.getInitialK())) {
                this.setInitialK(Integer.parseInt(value));
            }
            else if (param.equals(params.getkStep())) {
                this.setkStep(Integer.parseInt(value));
            }
            else if (param.equals(params.getThreads())) {
                this.setThreads(Integer.parseInt(value));
            }
            else if (param.equals(params.getMemoryLimit())) {
                this.maxMemUsageMB = Integer.parseInt(value) * 1000;
            }
            else if (param.equals(params.getInitialKCutoff())) {
                this.setInitialKCutoff(Integer.parseInt(value));
            }
            else if (param.equals(params.getMinKCoverage())) {
                this.setMinKCoverage(Integer.parseInt(value));
            }
            else if (param.equals(params.getkExtensionLevel())) {
                this.setkExtensionLevel(Double.parseDouble(value));
            }
            else if (param.equals(params.getMaxDiffBubbleCrush())) {
                this.setMaxDiffBubbleCrush(Double.parseDouble(value));
            }
            else if (param.equals(params.getMaxDiffBranchCut())) {
                this.setMaxDiffBranchCut(Double.parseDouble(value));
            }
            else if (param.equals(params.getFiles())) {
                //this.libs = value.split(" ");
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public void setFromArgMap(ParamMap pvp) throws IOException, ConanParameterException {

        }

        @Override
        public GenericDeBruijnOptimiserArgs getDeBruijnOptimiserArgs() {

            GenericDeBruijnOptimiserArgs args = new GenericDeBruijnOptimiserArgs();

            args.setOutputDir(this.outputDir);
            args.setLibraries(this.libs);
            args.setThreads(this.threads);
            args.setMemory(this.maxMemUsageMB);
            args.setKmerRange(new KmerRange(this.initialK, KmerRange.getLastKmerFromLibs(this.libs), this.kStep));

            return args;
        }

        @Override
        public void setDeBruijnOptimiserArgs(GenericDeBruijnOptimiserArgs args) {

            this.outputDir = args.getOutputDir();
            this.libs = args.getLibraries();
            this.threads = args.getThreads();
            this.maxMemUsageMB = args.getMemory();
            this.initialK = args.getKmerRange().getFirstKmer();
            this.kStep = args.getKmerRange().getStepSize();
        }

        @Override
        public String getProcessName() {
            return NAME;
        }

        @Override
        public AbstractProcessArgs toConanArgs() {
            return this;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter outputPrefix;
        private ConanParameter files;
        private ConanParameter initialK;
        private ConanParameter kStep;
        private ConanParameter initialKCutoff;
        private ConanParameter minKCoverage;
        private ConanParameter kExtensionLevel;
        private ConanParameter maxDiffBubbleCrush;
        private ConanParameter maxDiffBranchCut;
        private ConanParameter threads;
        private ConanParameter memoryLimit;

        public Params() {

            super();

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .description("prefix of output files (default out, length <= 200)")
                    .create();

            this.files = new ParameterBuilder()
                    .shortName("f")
                    .description("reads file (fasta or fastq, number <= 100)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.initialK = new ParameterBuilder()
                    .shortName("k")
                    .description("initial k-mer size (default 32)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.kStep = new ParameterBuilder()
                    .shortName("s")
                    .description("step size of k-mer extension (>= 1, default 10)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.initialKCutoff = new ParameterBuilder()
                    .shortName("n")
                    .description("initial k-mer coverage cutoff (default 0, 0 means auto)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minKCoverage = new ParameterBuilder()
                    .shortName("c")
                    .description("minimun k-mer coverage (default 2)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.kExtensionLevel = new ParameterBuilder()
                    .shortName("a")
                    .description("k-mer extension safety level (default 10.0)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.maxDiffBubbleCrush = new ParameterBuilder()
                    .shortName("u")
                    .description("maximum difference for bubble crush (identity, default 0.1)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.maxDiffBranchCut = new ParameterBuilder()
                    .shortName("d")
                    .description("maximum difference for branch cutting (coverage ratio, default 0.5)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .description("number of threads (<= 100, default 1)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.memoryLimit = new ParameterBuilder()
                    .shortName("m")
                    .description("memory limit for making kmer distribution (GB, >=1, default 16)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getFiles() {
            return files;
        }

        public ConanParameter getInitialK() {
            return initialK;
        }

        public ConanParameter getkStep() {
            return kStep;
        }

        public ConanParameter getInitialKCutoff() {
            return initialKCutoff;
        }

        public ConanParameter getMinKCoverage() {
            return minKCoverage;
        }

        public ConanParameter getkExtensionLevel() {
            return kExtensionLevel;
        }

        public ConanParameter getMaxDiffBubbleCrush() {
            return maxDiffBubbleCrush;
        }

        public ConanParameter getMaxDiffBranchCut() {
            return maxDiffBranchCut;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMemoryLimit() {
            return memoryLimit;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {

            return new ConanParameter[] {
                    this.outputPrefix,
                    this.files,
                    this.initialK,
                    this.kStep,
                    this.initialKCutoff,
                    this.minKCoverage,
                    this.kExtensionLevel,
                    this.maxDiffBubbleCrush,
                    this.maxDiffBranchCut,
                    this.threads,
                    this.memoryLimit
            };
        }
    }
}
