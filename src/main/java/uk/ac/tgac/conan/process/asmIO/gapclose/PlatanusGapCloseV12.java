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
package uk.ac.tgac.conan.process.asmIO.gapclose;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asmIO.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maplesod on 14/06/14.
 */
@MetaInfServices(AssemblyEnhancer.class)
public class PlatanusGapCloseV12 extends AbstractAssemblyEnhancer {

    public static final String NAME = "Platanus_Gapclose_V1.2";
    public static final String EXE = "platanus";
    public static final String MODE = "gap_close";
    public static final AssemblyEnhancerType TYPE = AssemblyEnhancerType.GAP_CLOSER;

    public PlatanusGapCloseV12() {
        this(null);
    }

    public PlatanusGapCloseV12(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public PlatanusGapCloseV12(ConanExecutorService ces, Args args) {
        super(NAME, TYPE, EXE, args, new Params(), ces);
        this.setMode(MODE);
    }

    @Override
    public void setup() throws IOException {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @MetaInfServices(AssemblyEnhancerArgs.class)
    public static class Args extends AbstractAssemblyEnhancerArgs {

        public static final int DEFAULT_MAPPING_SEED_LENGTH = 32;
        public static final int DEFAULT_MAPPING_SEED_LENGTH_OLC = 32;
        public static final int DEFAULT_MAX_READS_IN_GAPS = 5000;
        public static final int DEFAULT_MIN_OVERLAP_LENGTH_OLC = 32;
        public static final int DEFAULT_MIN_OVERLAP_LENGTH_DBG = 32;
        public static final int DEFAULT_MAX_EDIT_DISTANCE_OLC = 1;
        public static final double DEFAULT_MAX_ERROR_RATE_DBG = 0.05;
        public static final double DEFAULT_MIN_CONSENSUS_RATE_OLC = 0.66;
        public static final double DEFAULT_MIN_CONSENSUS_RATE_SR = 0.9;

        private int mappingSeedLength;
        private int mappingSeedLengthOLC;
        private int maxReadsInGaps;
        private int minOverlapLengthOLC;
        private int minOverlapLengthDBG;
        private int maxEditDistanceOLC;
        private double maxErrorRateDBG;
        private double minConsensusRateOLC;
        private double minConsensusRateSR;
        private boolean gapCloseOnce;


        public Args() {

            super(new Params(), NAME, TYPE);

            this.mappingSeedLength = DEFAULT_MAPPING_SEED_LENGTH;
            this.mappingSeedLengthOLC = DEFAULT_MAPPING_SEED_LENGTH_OLC;
            this.maxReadsInGaps = DEFAULT_MAX_READS_IN_GAPS;
            this.minOverlapLengthOLC = DEFAULT_MIN_OVERLAP_LENGTH_OLC;
            this.minOverlapLengthDBG = DEFAULT_MIN_OVERLAP_LENGTH_DBG;
            this.maxEditDistanceOLC = DEFAULT_MAX_EDIT_DISTANCE_OLC;
            this.maxErrorRateDBG = DEFAULT_MAX_ERROR_RATE_DBG;
            this.minConsensusRateOLC = DEFAULT_MIN_CONSENSUS_RATE_OLC;
            this.minConsensusRateSR = DEFAULT_MIN_CONSENSUS_RATE_SR;
            this.gapCloseOnce = false;

        }

        protected Params getParams() {
            return (Params) this.params;
        }

        @Override
        public File getOutputFile() {
            return new File(this.getOutputDir(), this.getOutputPrefix() + "_scaffold.fa");
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;

            if (param.equals(params.getOutputPrefix())) {

                File op = new File(value).getAbsoluteFile();
                this.setOutputDir(op.getParentFile());
                this.setOutputPrefix(op.getName());
            }
            else if (param.equals(params.getScaffoldFile())) {
                this.setInputAssembly(new File(value));
            }
            else if (param.equals(params.getThreads())) {
                this.setThreads(Integer.parseInt(value));
            }
            else if (param.equals(params.getMappingSeedLength())) {
                this.mappingSeedLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMappingSeedLengthOLC())) {
                this.mappingSeedLengthOLC = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxReadsInGaps())) {
                this.maxReadsInGaps = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinOverlapLengthOLC())) {
                this.minOverlapLengthOLC = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinOverlapLengthDBG())) {
                this.minOverlapLengthDBG = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxEditDistanceOLC())) {
                this.maxEditDistanceOLC = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxErrorRateDBG())) {
                this.maxErrorRateDBG = Double.parseDouble(value);
            }
            else if (param.equals(params.getMinConsensusRateOLC())) {
                this.minConsensusRateOLC = Double.parseDouble(value);
            }
            else if (param.equals(params.getMinConsensusRateSR())) {
                this.minConsensusRateSR = Double.parseDouble(value);
            }
            else if (param.equals(params.getGapCloseOnce())) {
                this.gapCloseOnce = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getThreads())) {
                this.setThreads(Integer.parseInt(value));
            }
            //else if (param.equals(params.getInput())) {
                //this.setThreads(Integer.parseInt(value));
            //}
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public void parse(String args) throws IOException {

        }

        @Override
        public ParamMap getArgMap() {

            Params params = (Params)this.params;
            ParamMap pvp = new DefaultParamMap();

            if (this.getOutputPrefix() != null) {
                pvp.put(params.getOutputPrefix(), this.getOutputPrefix());
            }

            if (this.getInputAssembly() != null) {
                pvp.put(params.getScaffoldFile(), this.getInputAssembly().getAbsolutePath());
            }

            if (this.mappingSeedLength != DEFAULT_MAPPING_SEED_LENGTH) {
                pvp.put(params.getMappingSeedLength(), Integer.toString(this.mappingSeedLength));
            }

            if (this.mappingSeedLengthOLC != DEFAULT_MAPPING_SEED_LENGTH_OLC) {
                pvp.put(params.getMappingSeedLengthOLC(), Integer.toString(this.mappingSeedLengthOLC));
            }

            if (this.maxReadsInGaps != DEFAULT_MAX_READS_IN_GAPS) {
                pvp.put(params.getMaxReadsInGaps(), Integer.toString(this.maxReadsInGaps));
            }

            if (this.minOverlapLengthOLC != DEFAULT_MIN_OVERLAP_LENGTH_OLC) {
                pvp.put(params.getMinOverlapLengthOLC(), Integer.toString(this.minOverlapLengthOLC));
            }

            if (this.minOverlapLengthDBG != DEFAULT_MIN_OVERLAP_LENGTH_DBG) {
                pvp.put(params.getMinOverlapLengthDBG(), Integer.toString(this.minOverlapLengthDBG));
            }

            if (this.maxEditDistanceOLC != DEFAULT_MAX_EDIT_DISTANCE_OLC) {
                pvp.put(params.getMaxEditDistanceOLC(), Integer.toString(this.maxEditDistanceOLC));
            }

            if (this.maxErrorRateDBG != DEFAULT_MAX_ERROR_RATE_DBG) {
                pvp.put(params.getMaxErrorRateDBG(), Double.toString(this.maxErrorRateDBG));
            }

            if (this.minConsensusRateOLC != DEFAULT_MIN_CONSENSUS_RATE_OLC) {
                pvp.put(params.getMinOverlapLengthOLC(), Double.toString(this.minConsensusRateOLC));
            }

            if (this.minConsensusRateSR != DEFAULT_MIN_CONSENSUS_RATE_SR) {
                pvp.put(params.getMinConsensusRateSR(), Double.toString(this.minConsensusRateSR));
            }

            if (this.gapCloseOnce) {
                pvp.put(params.getGapCloseOnce(), Boolean.toString(this.gapCloseOnce));
            }

            if (this.getThreads() != DEFAULT_THREADS) {
                pvp.put(params.getThreads(), Integer.toString(this.getThreads()));
            }

            if (this.getLibraries() != null && !this.getLibraries().isEmpty()) {
                pvp.put(params.getInput(), new InputLibsArg(this.getLibraries()).toString());
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter outputPrefix;
        private ConanParameter scaffoldFile;
        private ConanParameter input;
        private ConanParameter mappingSeedLength;
        private ConanParameter mappingSeedLengthOLC;
        private ConanParameter maxReadsInGaps;
        private ConanParameter minOverlapLengthOLC;
        private ConanParameter minOverlapLengthDBG;
        private ConanParameter maxEditDistanceOLC;
        private ConanParameter maxErrorRateDBG;
        private ConanParameter minConsensusRateOLC;
        private ConanParameter minConsensusRateSR;
        private ConanParameter gapCloseOnce;
        private ConanParameter threads;

        public Params() {

            super();

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .description("prefix of output file (default out, length <= 200)")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.scaffoldFile = new ParameterBuilder()
                    .shortName("c")
                    .description("scaffold_file (fasta format)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.input = new ParameterBuilder()
                    .isOption(false)
                    .description("input to platanus gap_close")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.mappingSeedLength = new ParameterBuilder()
                    .shortName("s")
                    .description("mapping seed length (default 32)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.mappingSeedLengthOLC = new ParameterBuilder()
                    .shortName("k")
                    .description("mapping seed length in overlpap-layout-consensus (default 32)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxReadsInGaps = new ParameterBuilder()
                    .shortName("d")
                    .description("maximum the number of read in gaps exec closing gap (default 5000)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minOverlapLengthOLC = new ParameterBuilder()
                    .shortName("vo")
                    .description("minimum overlap length among each read in OLC gap closing (default 32)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minOverlapLengthDBG = new ParameterBuilder()
                    .shortName("vd")
                    .description("minimum overlap length between contig and edge seq in De Bruijn gap closing (default 32)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxEditDistanceOLC = new ParameterBuilder()
                    .shortName("eo")
                    .description("maximum edit distance of overlap in OLC gap closing (identity, default 1)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxErrorRateDBG = new ParameterBuilder()
                    .shortName("ed")
                    .description("maximum error rate among gap edge seq in De Bruijn gap closing (identity, default 0.05)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.minConsensusRateOLC = new ParameterBuilder()
                    .shortName("ro")
                    .description("minimum consensus rate in OLC gap closing (identity, default 0.66)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.minConsensusRateSR = new ParameterBuilder()
                    .shortName("rs")
                    .description("minimum consensus rate in Single Read gap closing (identity, default 0.9)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.gapCloseOnce = new ParameterBuilder()
                    .shortName("a")
                    .description("do gap close only one time using all libraries")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .description("number of threads (default 1)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getScaffoldFile() {
            return scaffoldFile;
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getMappingSeedLength() {
            return mappingSeedLength;
        }

        public ConanParameter getMappingSeedLengthOLC() {
            return mappingSeedLengthOLC;
        }

        public ConanParameter getMaxReadsInGaps() {
            return maxReadsInGaps;
        }

        public ConanParameter getMinOverlapLengthOLC() {
            return minOverlapLengthOLC;
        }

        public ConanParameter getMinOverlapLengthDBG() {
            return minOverlapLengthDBG;
        }

        public ConanParameter getMaxEditDistanceOLC() {
            return maxEditDistanceOLC;
        }

        public ConanParameter getMaxErrorRateDBG() {
            return maxErrorRateDBG;
        }

        public ConanParameter getMinConsensusRateOLC() {
            return minConsensusRateOLC;
        }

        public ConanParameter getMinConsensusRateSR() {
            return minConsensusRateSR;
        }

        public ConanParameter getGapCloseOnce() {
            return gapCloseOnce;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.outputPrefix,
                    this.scaffoldFile,
                    this.input,
                    this.mappingSeedLength,
                    this.mappingSeedLengthOLC,
                    this.maxReadsInGaps,
                    this.minOverlapLengthOLC,
                    this.minOverlapLengthDBG,
                    this.maxEditDistanceOLC,
                    this.maxErrorRateDBG,
                    this.minConsensusRateOLC,
                    this.minConsensusRateSR,
                    this.gapCloseOnce,
                    this.threads
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

        @Override
        public String toString() {

            List<String> libStrings = new ArrayList<>();

            int index = 1;
            int nbSingleEndLibs = 0;
            for (Library lib : this.libs) {

                if (lib.getSeqOrientation() == Library.SeqOrientation.FORWARD_REVERSE) {
                    libStrings.add(
                            "-IP" + index + " " + lib.getFile1().getAbsolutePath() + " " + lib.getFile2().getAbsolutePath()
                    );
                }
                else if (lib.getSeqOrientation() == Library.SeqOrientation.REVERSE_FORWARD) {
                    final String mpStart = "--OP" + index;
                    libStrings.add(
                            "-OP" + index + " " + lib.getFile1().getAbsolutePath() + " " + lib.getFile2().getAbsolutePath()
                    );
                }
                else {
                    throw new IllegalArgumentException("Unknown library type detected \"" + lib.getType() + "\" for: " + lib.getName());
                }

                index++;
            }

            return StringUtils.join(libStrings, " ").trim();
        }

    }
}