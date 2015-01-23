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
package uk.ac.tgac.conan.process.asm.stats;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 12/08/13
 * Time: 09:54
 */
public class KmerGenieV16 extends AbstractConanProcess {

    public static final String EXE = "kmergenie";

    public KmerGenieV16() {
        this(null);
    }

    public KmerGenieV16(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public KmerGenieV16(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);

        if (args.inputFiles.size() > 1) {

            StringBuilder sb = new StringBuilder();

            int i = 0;
            for(File input : args.inputFiles) {

                if (i > 0) {
                    sb.append(" ");
                }

                sb.append(input.getAbsolutePath());
                i++;
            }

            this.addPreCommand("cat " + sb.toString() + " > " + args.getDefaultInputFile());
            args.setReadFile(args.getDefaultInputFile());
        }
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    public int getBestKmer() throws IOException {
        return this.getArgs().getBestKmer();
    }

    @Override
    public String getName() {
        return "KmerGenie_V1.6";
    }

    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_LARGEST_K = 121;
        public static final int DEFAULT_SMALLEST_K = 15;
        public static final int DEFAULT_INTERVAL = 10;

        private List<File> inputFiles;
        private File readFile;
        private File outputDir;
        private String outputPrefix;
        private int threads;
        private boolean diploid;
        private boolean onePass;
        private int largestK;
        private int smallestK;
        private int interval;
        private int samplingVal;
        private File outputFile;
        private File logFile;

        public Args() {

            super(new Params());

            this.inputFiles = new ArrayList<>();
            this.readFile = null;
            this.outputDir = null;
            this.outputPrefix = "kmergenie_out";
            this.threads = 1;
            this.diploid = false;
            this.onePass = false;
            this.largestK = DEFAULT_LARGEST_K;
            this.smallestK = DEFAULT_SMALLEST_K;
            this.interval = DEFAULT_INTERVAL;
            this.samplingVal = 0;
            this.outputFile = null;
            this.logFile = null;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public static int getBestKmer(File outputFile) throws IOException {
            if (outputFile != null && outputFile.exists()) {

                List<String> lines = FileUtils.readLines(outputFile);

                if (lines != null && !lines.isEmpty()) {

                    String lastLine = lines.get(lines.size() - 1);

                    String[] words = lastLine.split(" ");

                    String lastWord = words[words.length - 1];

                    int bestKmer = Integer.parseInt(lastWord);

                    return bestKmer;
                }
            }

            return 0;
        }

        public int getBestKmer() throws IOException {
            return getBestKmer(this.outputFile);
        }

        public List<File> getInputFiles() {
            return inputFiles;
        }

        public void setInputFiles(List<File> inputFiles) {
            this.inputFiles = inputFiles;
        }

        public File getDefaultInputFile() {
            return new File(this.outputDir, "input.fastq");
        }

        public File getReadFile() {
            return readFile;
        }

        public void setReadFile(File readFile) {
            this.readFile = readFile;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public String getOutputPrefix() {
            return outputPrefix;
        }

        public void setOutputPrefix(String outputPrefix) {
            this.outputPrefix = outputPrefix;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public boolean isDiploid() {
            return diploid;
        }

        public void setDiploid(boolean diploid) {
            this.diploid = diploid;
        }

        public boolean isOnePass() {
            return onePass;
        }

        public void setOnePass(boolean onePass) {
            this.onePass = onePass;
        }

        public int getLargestK() {
            return largestK;
        }

        public void setLargestK(int largestK) {
            this.largestK = largestK;
        }

        public int getSmallestK() {
            return smallestK;
        }

        public void setSmallestK(int smallestK) {
            this.smallestK = smallestK;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public int getSamplingVal() {
            return samplingVal;
        }

        public void setSamplingVal(int samplingVal) {
            this.samplingVal = samplingVal;
        }

        public File getOutputFile() {
            return outputFile;
        }

        public void setOutputFile(File outputFile) {
            this.outputFile = outputFile;
        }

        public File getLogFile() {
            return logFile;
        }

        public void setLogFile(File logFile) {
            this.logFile = logFile;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            if (this.readFile != null) {
                pvp.put(params.getReadFile(), this.readFile.getAbsolutePath());
            }

            if (this.outputPrefix != null && this.outputDir != null) {
                pvp.put(params.getOutputPrefix(), this.outputDir.getAbsolutePath() + "/" + this.outputPrefix);
            }

            if (this.threads > 0) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.diploid) {
                pvp.put(params.getDiploid(), Boolean.toString(this.diploid));
            }

            if (this.onePass) {
                pvp.put(params.getOnePass(), Boolean.toString(this.onePass));
            }

            if (this.largestK != DEFAULT_LARGEST_K && this.largestK > 0) {
                pvp.put(params.getLargestK(), Integer.toString(this.largestK));
            }

            if (this.smallestK != DEFAULT_SMALLEST_K && this.smallestK > 0) {
                pvp.put(params.getSmallestK(), Integer.toString(this.smallestK));
            }

            if (this.interval != DEFAULT_INTERVAL && this.interval > 0) {
                pvp.put(params.getInterval(), Integer.toString(this.interval));
            }

            if (this.samplingVal > 0) {
                pvp.put(params.getSamplingVal(), Integer.toString(this.samplingVal));
            }

            if (this.outputFile != null) {
                pvp.put(params.getOutputFile(), this.outputFile.getAbsolutePath());
            }

            if (this.logFile != null) {
                pvp.put(params.getLogFile(), this.logFile.getAbsolutePath());
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutputPrefix())) {
                this.outputDir = new File(value).getParentFile();
                this.outputPrefix = new File(value).getName();
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getDiploid())) {
                this.diploid = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getOnePass())) {
                this.onePass = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getLargestK())) {
                this.largestK = Integer.parseInt(value);
            }
            else if (param.equals(params.getSmallestK())) {
                this.smallestK = Integer.parseInt(value);
            }
            else if (param.equals(params.getInterval())) {
                this.interval = Integer.parseInt(value);
            }
            else if (param.equals(params.getSamplingVal())) {
                this.samplingVal = Integer.parseInt(value);
            }
            else if (param.equals(params.getOutputFile())) {
                this.outputFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getReadFile())) {
                this.readFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown arg found: " + param);
            }
        }

        @Override
        protected void setStdOutRedirectFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutputFile())) {
                this.outputFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setStdErrRedirectFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getLogFile())) {
                this.logFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter readFile;
        private ConanParameter outputPrefix;
        private ConanParameter threads;
        private ConanParameter diploid;
        private ConanParameter onePass;
        private ConanParameter largestK;
        private ConanParameter smallestK;
        private ConanParameter interval;
        private ConanParameter samplingVal;
        private ConanParameter outputFile;
        private ConanParameter logFile;

        public Params() {

            this.readFile = new ParameterBuilder()
                    .description("fastq file containing reads to analyse")
                    .isOptional(false)
                    .isOption(false)
                    .argIndex(0)
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .description("prefix of the output files (default: histograms)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .description("number of threads (default: number of cores minus one)")
                    .isOptional(false)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.diploid = new ParameterBuilder()
                    .longName("diploid")
                    .description("use the diploid model")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.onePass = new ParameterBuilder()
                    .longName("one-pass")
                    .description("skip the second pass")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.largestK = new ParameterBuilder()
                    .shortName("k")
                    .description("largest k-mer size to consider (default: 121)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.smallestK = new ParameterBuilder()
                    .shortName("l")
                    .description("smallest k-mer size to consider (default: 15)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.interval = new ParameterBuilder()
                    .shortName("s")
                    .description("interval between consecutive kmer sizes (default: 10)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.samplingVal = new ParameterBuilder()
                    .shortName("e")
                    .description("k-mer sampling value (default: auto-detected to use ~200 MB memory/thread)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.outputFile = new ParameterBuilder()
                    .isOptional(false)
                    .type(DefaultConanParameter.ParamType.STDOUT_REDIRECTION)
                    .description("The standard output")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.logFile = new ParameterBuilder()
                    .isOptional(false)
                    .type(DefaultConanParameter.ParamType.STDERR_REDIRECTION)
                    .description("The standard error")
                    .argValidator(ArgValidator.PATH)
                    .create();

        }

        public ConanParameter getReadFile() {
            return readFile;
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getDiploid() {
            return diploid;
        }

        public ConanParameter getOnePass() {
            return onePass;
        }

        public ConanParameter getLargestK() {
            return largestK;
        }

        public ConanParameter getSmallestK() {
            return smallestK;
        }

        public ConanParameter getInterval() {
            return interval;
        }

        public ConanParameter getSamplingVal() {
            return samplingVal;
        }

        public ConanParameter getOutputFile() {
            return outputFile;
        }

        public ConanParameter getLogFile() {
            return logFile;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.readFile,
                    this.outputPrefix,
                    this.threads,
                    this.diploid,
                    this.onePass,
                    this.largestK,
                    this.smallestK,
                    this.interval,
                    this.samplingVal,
                    this.outputFile,
                    this.logFile
            };
        }
    }

}
