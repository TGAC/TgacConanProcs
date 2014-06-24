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
package uk.ac.tgac.conan.process.asmIO.scaffold;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.process.asmIO.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by maplesod on 14/06/14.
 */
@MetaInfServices(AssemblyEnhancer.class)
public class PlatanusScaffoldV12 extends AbstractAssemblyEnhancer {

    public static final String NAME = "Platanus_Scaffold_V1.2";
    public static final String EXE = "platanus";
    public static final String MODE = "scaffold";
    public static final AssemblyEnhancerType TYPE = AssemblyEnhancerType.SCAFFOLDER;

    public PlatanusScaffoldV12() {
        this(null);
    }

    public PlatanusScaffoldV12(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public PlatanusScaffoldV12(ConanExecutorService ces, Args args) {

        super(NAME, TYPE, EXE, args, new Params(), ces);
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @MetaInfServices(AssemblyEnhancerArgs.class)
    public static class Args extends AbstractAssemblyEnhancerArgs {

        public static final int DEFAULT_MAPPING_SEED_LENGTH = 32;
        public static final int DEFAULT_MIN_OVERLAP_LENGTH = 32;
        public static final int DEFAULT_MIN_LINKS = 3;
        public static final double DEFAULT_MAX_DIFF_BUBBLE_CRUSH = 0.1;

        private File bubbleFile;
        private int mappingSeedLength;
        private int minOverlapLength;
        private int minLinks;
        private double maxDiffBubbleCrush;

        public Args() {

            super(new Params(), NAME, TYPE);

            this.bubbleFile = null;
            this.mappingSeedLength = DEFAULT_MAPPING_SEED_LENGTH;
            this.minOverlapLength = DEFAULT_MIN_OVERLAP_LENGTH;
            this.minLinks = DEFAULT_MIN_LINKS;
            this.maxDiffBubbleCrush = DEFAULT_MAX_DIFF_BUBBLE_CRUSH;
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
            else if (param.equals(params.getContigFile())) {
                this.setInputFile(new File(value));
            }
            else if (param.equals(params.getBubbleFile())) {
                this.bubbleFile = new File(value);
            }
            else if (param.equals(params.getThreads())) {
                this.setThreads(Integer.parseInt(value));
            }
            else if (param.equals(params.getMappingSeedLength())) {
                this.mappingSeedLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinOverlapLength())) {
                this.minOverlapLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinLinks())) {
                this.minLinks = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxDiffBubbleCrush())) {
                this.maxDiffBubbleCrush = Double.parseDouble(value);
            }
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
                pvp.put(params.getOutputPrefix(), new File(this.getOutputDir(), this.getOutputPrefix()).getAbsolutePath());
            }

            if (this.getInputFile() != null) {
                pvp.put(params.getContigFile(), this.getInputFile().getAbsolutePath());
            }

            if (this.bubbleFile != null) {
                pvp.put(params.getBubbleFile(), this.bubbleFile.getAbsolutePath());
            }

            if (this.mappingSeedLength != DEFAULT_MAPPING_SEED_LENGTH) {
                pvp.put(params.getMappingSeedLength(), Integer.toString(this.mappingSeedLength));
            }

            if (this.getThreads() != DEFAULT_THREADS) {
                pvp.put(params.getThreads(), Integer.toString(this.getThreads()));
            }

            if (this.minOverlapLength != DEFAULT_MIN_OVERLAP_LENGTH) {
                pvp.put(params.getMinOverlapLength(), Integer.toString(this.minOverlapLength));
            }

            if (this.minLinks != DEFAULT_MIN_LINKS) {
                pvp.put(params.getMinOverlapLength(), Integer.toString(this.minLinks));
            }

            if (this.maxDiffBubbleCrush != DEFAULT_MAX_DIFF_BUBBLE_CRUSH) {
                pvp.put(params.getMaxDiffBubbleCrush(), Double.toString(this.maxDiffBubbleCrush));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter outputPrefix;
        private ConanParameter contigFile;
        private ConanParameter bubbleFile;
        private ConanParameter mappingSeedLength;
        private ConanParameter minOverlapLength;
        private ConanParameter minLinks;
        private ConanParameter maxDiffBubbleCrush;
        private ConanParameter threads;

        public Params() {

            super();

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .description("prefix of output file (default out, length <= 200)")
                    .create();

            this.contigFile = new ParameterBuilder()
                    .shortName("c")
                    .description("contig_file (fasta format)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.bubbleFile = new ParameterBuilder()
                    .shortName("b")
                    .description("bubble_seq_file (fasta format)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.mappingSeedLength = new ParameterBuilder()
                    .shortName("s")
                    .description("mapping seed length (default 32)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minOverlapLength = new ParameterBuilder()
                    .shortName("v")
                    .description("minimum overlap length (default 32)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minLinks = new ParameterBuilder()
                    .shortName("l")
                    .description("minimum number of link (default 3)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.maxDiffBubbleCrush = new ParameterBuilder()
                    .shortName("u")
                    .description("maximum difference for bubble crush (identity, default 0.1)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .description("number of threads (<= 1, default 1)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getContigFile() {
            return contigFile;
        }

        public ConanParameter getBubbleFile() {
            return bubbleFile;
        }

        public ConanParameter getMappingSeedLength() {
            return mappingSeedLength;
        }

        public ConanParameter getMinOverlapLength() {
            return minOverlapLength;
        }

        public ConanParameter getMinLinks() {
            return minLinks;
        }

        public ConanParameter getMaxDiffBubbleCrush() {
            return maxDiffBubbleCrush;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {

            return new ConanParameter[] {
                    this.outputPrefix,
                    this.contigFile,
                    this.bubbleFile,
                    this.mappingSeedLength,
                    this.minOverlapLength,
                    this.minLinks,
                    this.maxDiffBubbleCrush,
                    this.threads
            };
        }
    }
}