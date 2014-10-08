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
package uk.ac.tgac.conan.process.re.tools;

import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.process.re.AbstractReadEnhancer;
import uk.ac.tgac.conan.process.re.AbstractReadEnhancerArgs;
import uk.ac.tgac.conan.process.re.ReadEnhancer;
import uk.ac.tgac.conan.process.re.ReadEnhancerArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 02/05/13
 * Time: 15:31
 */
@MetaInfServices(ReadEnhancer.class)
public class MusketV10 extends AbstractReadEnhancer {

    public static final String NAME = "Musket_V1.0";
    public static final String EXE = "musket";

    public MusketV10() {
        this(null);
    }

    public MusketV10(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public MusketV10(ConanExecutorService ces, Args args) {
        super(NAME, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getReadEnchancerArgs();
    }

    @Override
    public List<File> getEnhancedFiles() {

        List<File> enhancedFiles = new ArrayList<>();

        Args args = this.getArgs();

        enhancedFiles.add(new File(args.getOutputDir(), args.outputPrefix + ".0"));
        enhancedFiles.add(new File(args.getOutputDir(), args.outputPrefix + ".1"));

        return enhancedFiles;
    }

    @Override
    public void setup() {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }


    @MetaInfServices(ReadEnhancerArgs.class)
    public static class Args extends AbstractReadEnhancerArgs {

        public static final int DEFAULT_MAX_ERR = 4;
        public static final int DEFAULT_MAX_ITER = 2;


        private String outputPrefix;
        private int kmer;
        private long totalKmers;
        private int maxTrim;
        private boolean inOrder;
        private boolean multiK;
        private int maxErr;
        private int maxIter;

        public Args() {
            super(new Params(), NAME);

            this.threads = 2;
            this.outputPrefix = "output";
            this.kmer = 0;
            this.totalKmers = 0;
            this.maxTrim = 0;
            this.inOrder = true;
            this.multiK = false;
            this.maxErr = 4;
            this.maxIter = 2;
            this.maxTrim = 0;
        }

        public int getKmer() {
            return kmer;
        }

        public void setKmer(int kmer) {
            this.kmer = kmer;
        }

        public String getOutputPrefix() {
            return outputPrefix;
        }

        public void setOutputPrefix(String outputPrefix) {
            this.outputPrefix = outputPrefix;
        }

        public long getTotalKmers() {

            // This is a bit of a fudge.  Dividing by 20 is just a arbitrary number used to move the theoretical number of
            // kmers closer to something that might be seen in reality.  This number isn't critical however, but if it is
            // wildly wrong it might effect Musket's memory usage and runtime performance.
            return this.totalKmers == 0 ?
                    (long)Math.pow(4, this.kmer) / 20 :
                    totalKmers;
        }

        public void setTotalKmers(long totalKmers) {
            this.totalKmers = totalKmers;
        }

        public int getMaxTrim() {
            return maxTrim;
        }

        public void setMaxTrim(int maxTrim) {
            this.maxTrim = maxTrim;
        }

        public boolean isInOrder() {
            return inOrder;
        }

        public void setInOrder(boolean inOrder) {
            this.inOrder = inOrder;
        }

        public boolean isMultiK() {
            return multiK;
        }

        public void setMultiK(boolean multiK) {
            this.multiK = multiK;
        }

        public int getMaxErr() {
            return maxErr;
        }

        public void setMaxErr(int maxErr) {
            this.maxErr = maxErr;
        }

        public int getMaxIter() {
            return maxIter;
        }

        public void setMaxIter(int maxIter) {
            this.maxIter = maxIter;
        }

        /**
         * Must have a minimum of 2 threads for musket.
         * @param threads
         */
        @Override
        public void setThreads(int threads) {
            super.setThreads(threads < 2 ? 2 : threads);
        }


        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = (Params)this.params;

            if (cmdLine.hasOption(params.getKmer().getShortName())) {
                String[] parts = cmdLine.getOptionValue(params.getKmer().getShortName()).split(" ");

                if (parts.length != 2) {
                    throw new IllegalArgumentException("Parameter/Arg map does not contain a valid kmer value pair for musket.  Musket requires two value, the first being the kmer value, the second being the estimate number of distinct kmers (4^k).");
                }

                this.kmer = Integer.parseInt(parts[0]);
                this.setTotalKmers(Long.parseLong(parts[1]));
            }

            this.threads = cmdLine.hasOption(params.getThreads().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getThreads().getShortName())) :
                    this.threads;

            this.maxTrim = cmdLine.hasOption(params.getMaxTrim().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxTrim().getShortName())) :
                    this.maxTrim;

            this.inOrder = cmdLine.hasOption(params.getInOrder().getShortName());

            this.multiK = cmdLine.hasOption(params.getMultiK().getShortName()) ?
                    Boolean.parseBoolean(cmdLine.getOptionValue(params.getMultiK().getShortName())) :
                    this.multiK;

            this.maxErr = cmdLine.hasOption(params.getMaxErr().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxErr().getShortName())) :
                    this.maxErr;

            this.maxIter = cmdLine.hasOption(params.getMaxIter().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMaxIter().getShortName())) :
                    this.maxIter;
        }

        @Override
        public ParamMap getArgMap() {

            Params params = (Params)this.params;

            ParamMap pvp = new DefaultParamMap();

            pvp.put(params.getKmer(), String.valueOf(this.kmer) + " " + String.valueOf(this.getTotalKmers()));
            pvp.put(params.getOutputPrefix(), this.getOutputPrefix());
            pvp.put(params.getThreads(), String.valueOf(this.getThreads()));

            if (this.maxTrim != 0) {
                pvp.put(params.getMaxTrim(), String.valueOf(this.maxTrim));
            }

            if (this.inOrder) {
                pvp.put(params.getInOrder(), String.valueOf(this.inOrder));
            }

            if (this.multiK) {
                pvp.put(params.getMultiK(), String.valueOf(this.multiK));
            }

            if (this.maxErr != DEFAULT_MAX_ERR) {
                pvp.put(params.getMaxErr(), String.valueOf(this.maxErr));
            }

            if (this.maxIter != DEFAULT_MAX_ITER) {
                pvp.put(params.getMaxIter(), String.valueOf(this.maxIter));
            }

            if (this.input != null) {
                pvp.put(params.getReadFiles(),
                        this.input.getFile1().getAbsolutePath() +
                        (this.input.isPairedEnd() ? " " + this.input.getFile2().getAbsolutePath() : ""));
            }

            return pvp;
        }


        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;

            if (param.equals(params.getKmer())) {

                String[] parts = value.split(" ");

                if (parts.length != 2) {
                    throw new IllegalArgumentException("Parameter/Arg map does not contain a valid kmer value pair for musket.  Musket requires two value, the first being the kmer value, the second being the estimate number of distinct kmers (4^k).");
                }

                this.kmer = Integer.parseInt(parts[0]);
                this.setTotalKmers(Long.parseLong(parts[1]));
            }
            else if (param.equals(params.getMaxTrim())) {
                this.setMaxTrim(Integer.parseInt(value));
            }
            else if (param.equals(params.getInOrder())) {

                this.setInOrder(value == null || value.isEmpty() || Boolean.parseBoolean(value));
            }
            else if (param.equals(params.getMultiK())) {
                this.setMultiK(value == null || value.isEmpty() || Boolean.parseBoolean(value));
            }
            else if (param.equals(params.getThreads())) {
                this.setThreads(Integer.parseInt(value));
            }
            else if (param.equals(params.getMaxErr())) {
                this.setMaxErr(Integer.parseInt(value));
            }
            else if (param.equals(params.getMaxIter())) {
                this.setMaxIter(Integer.parseInt(value));
            }
            else if (param.equals(params.getOutputPrefix())) {
                this.setOutputPrefix(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;

            if (param.equals(params.getReadFiles())) {

                // Assumes there are no space in the path!!!
                String[] parts = value.split(" ");

                if (parts.length == 1) {
                    this.input.setFiles(parts[0], null);
                }
                else if (parts.length == 2) {
                    this.input.setFiles(parts[0], parts[1]);
                }
                else {
                    throw new IllegalArgumentException("Parameter/Arg map does not contain two file valid paths.  Note that each path must contain no spaces!");
                }

            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter kmer;
        private ConanParameter outputPrefix;
        private ConanParameter threads;
        private ConanParameter maxTrim;
        private ConanParameter inOrder;
        private ConanParameter multiK;
        private ConanParameter maxErr;
        private ConanParameter maxIter;
        private ConanParameter readFiles;

        public Params() {

            this.kmer = new ParameterBuilder()
                    .isOption(true)
                    .isOptional(false)
                    .shortName("k")
                    .description("(specify two paramters: k-mer size and estimated total number of k-mers for this k-mer size) The " +
                            "second values  specifies the (possible) number of " +
                            "distinct k-mers in the input datasets (e.g. estimated number of k-mers: 67108864, 134217728, " +
                            "268435456 or 536870912). This second value does not affect the correctness of the program, " +
                            "but balances the memory consumption between Bloom filters and hash tables, thus affecting the " +
                            "overall memory footprint of the program. Please feel free to specify this value.")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.outputPrefix = new PathParameter(
                    "omulti",
                    "When this option is used, each input file will have its own output file. For each input file, all " +
                            "corrected reads in the file will be ouput to a file with prefix #str (the value of this " +
                            "option). The first input file has an output file name #str.0, the second #str.1, and so on. " +
                            "For example, the command \"musket -omutli myout infile_1.fa infile_2 fa infile2_1.fa " +
                            "infile2_2.fa\" will create 4 output files with names \"myout.0\", \"myout.1\", \"myout.2\" " +
                            "and \"myout.3\". File \"myout.0\" corresponds to file \"infile_1.fa\" and \"myout.1\" " +
                            "corresponds to \"infile_2.fa\", and so on",
                    true);

            this.threads = new NumericParameter(
                    "p",
                    "number of threads [>=2]",
                    true
            );

            this.maxTrim = new NumericParameter(
                    "maxtrim",
                    "maximal number of bases that can be trimmed, default 0",
                    true
            );

            this.inOrder = new FlagParameter(
                    "inorder",
                    "keep sequences outputed in the same order with the input"
            );

            this.multiK = new FlagParameter(
                    "multik",
                    "enable the use of multiple k-mer sizes"
            );

            this.maxErr = new NumericParameter(
                    "maxerr",
                    "maximal number of mutations in any region of length #k, default 4",
                    true
            );

            this.maxIter = new NumericParameter(
                    "maxiter",
                    "maximal number of correcting iterations per k-mer size, default 2",
                    true
            );

            this.readFiles = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .longName("input")
                    .description("The input files to error correct separated by spaces")
                    .argIndex(0)
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.kmer,
                    this.outputPrefix,
                    this.threads,
                    this.maxTrim,
                    this.inOrder,
                    this.multiK,
                    this.maxErr,
                    this.maxIter,
                    this.readFiles
            };
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMaxTrim() {
            return maxTrim;
        }

        public ConanParameter getInOrder() {
            return inOrder;
        }

        public ConanParameter getMultiK() {
            return multiK;
        }

        public ConanParameter getMaxErr() {
            return maxErr;
        }

        public ConanParameter getMaxIter() {
            return maxIter;
        }

        public ConanParameter getReadFiles() {
            return readFiles;
        }

    }
}
