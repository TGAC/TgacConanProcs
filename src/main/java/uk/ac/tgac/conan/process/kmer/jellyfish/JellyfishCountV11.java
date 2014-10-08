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
package uk.ac.tgac.conan.process.kmer.jellyfish;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 26/03/13
 * Time: 17:58
 */
public class JellyfishCountV11 extends AbstractConanProcess {

    public static final String EXE = "jellyfish";
    public static final String MODE = "count";

    public JellyfishCountV11() {
        this(null);
    }

    public JellyfishCountV11(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public JellyfishCountV11(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    public Params getParams() {
        return (Params)this.getProcessParams();
    }

    @Override
    public String getCommand() throws ConanParameterException {

        List<ConanParameter> optionException = new ArrayList<>();
        optionException.add(this.getParams().getMemoryMb());

        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" ").append(this.getMode()).append(" ");
        sb.append(this.getArgs().getArgMap().buildOptionString(CommandLineFormat.POSIX, optionException));
        sb.append(" ");
        sb.append(this.getArgs().getArgMap().buildArgString());

        return sb.toString();
    }

    @Override
    public String getName() {
        return "Jellyfish_Count_V1.1";
    }

    public static class Args extends AbstractProcessArgs {

        private String inputFile;
        private int merLength;
        private long hashSize;
        private int threads;
        private int memoryMb;
        private String outputPrefix;
        private int counterLength;
        private boolean bothStrands;
        private long lowerCount;
        private long upperCount;

        public Args() {

            super(new Params());

            this.inputFile = null;
            this.merLength = -1;
            this.hashSize = -1;
            this.threads = 1;
            this.memoryMb = 0;
            this.outputPrefix = "";
            this.counterLength = 7;
            this.bothStrands = false;
            this.lowerCount = 0;
            this.upperCount = Long.MAX_VALUE;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public String getInputFile() {
            return inputFile;
        }

        public void setInputFile(String inputFile) {
            this.inputFile = inputFile;
        }

        public int getMerLength() {
            return merLength;
        }

        public void setMerLength(int merLength) {
            this.merLength = merLength;
        }

        public long getHashSize() {
            return hashSize;
        }

        public void setHashSize(long hashSize) {
            this.hashSize = hashSize;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public int getMemoryMb() {
            return memoryMb;
        }

        public void setMemoryMb(int memoryMb) {
            this.memoryMb = memoryMb;
        }

        public String getOutputPrefix() {
            return outputPrefix;
        }

        public void setOutputPrefix(String outputPrefix) {
            this.outputPrefix = outputPrefix;
        }

        public int getCounterLength() {
            return counterLength;
        }

        public void setCounterLength(int counterLength) {
            this.counterLength = counterLength;
        }

        public boolean isBothStrands() {
            return bothStrands;
        }

        public void setBothStrands(boolean bothStrands) {
            this.bothStrands = bothStrands;
        }

        public long getLowerCount() {
            return lowerCount;
        }

        public void setLowerCount(long lowerCount) {
            this.lowerCount = lowerCount;
        }

        public long getUpperCount() {
            return upperCount;
        }

        public void setUpperCount(long upperCount) {
            this.upperCount = upperCount;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.inputFile != null && !this.inputFile.isEmpty()) {
                pvp.put(params.getInputFile(), this.inputFile);
            }

            pvp.put(params.getMerLength(), Integer.toString(this.merLength));
            pvp.put(params.getHashSize(), Long.toString(this.hashSize));
            pvp.put(params.getThreads(), Integer.toString(this.threads));
            pvp.put(params.getMemoryMb(), Integer.toString(this.memoryMb));

            if (this.outputPrefix != null && !this.outputPrefix.isEmpty()) {
                pvp.put(params.getOutputPrefix(), this.outputPrefix);
            }

            pvp.put(params.getCounterLength(), Integer.toString(this.counterLength));
            pvp.put(params.getBothStrands(), Boolean.toString(this.bothStrands));
            pvp.put(params.getLowerCount(), Long.toString(this.lowerCount));

            if (this.upperCount != Long.MAX_VALUE) {
                pvp.put(params.getUpperCount(), Long.toString(this.upperCount));
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getMerLength())) {
                this.merLength = Integer.parseInt(value);
            } else if (param.equals(params.getHashSize())) {
                this.hashSize = Long.parseLong(value);
            } else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            } else if (param.equals(params.getMemoryMb())) {
                this.memoryMb = Integer.parseInt(value);
            } else if (param.equals(params.getOutputPrefix())) {
                this.outputPrefix = value;
            } else if (param.equals(params.getCounterLength())) {
                this.counterLength = Integer.parseInt(value);
            } else if (param.equals(params.getBothStrands())) {
                this.bothStrands = Boolean.parseBoolean(value);
            } else if (param.equals(params.getLowerCount())) {
                this.lowerCount = Long.parseLong(value);
            } else if (param.equals(params.getUpperCount())) {
                this.upperCount = Long.parseLong(value);
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getInputFile())) {
                this.inputFile = value;
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }


        /**
         * Returns the expected output file.  WARNING!  Jellyfish may create many output files if the hash size is not
         * large enough to handle all the output.  This function will only return the expected output file that ends in
         * "_0".
         * @return The expected output file.
         */
        public File getOutputFile() {
            return new File(this.getOutputPrefix() + "_0");
        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter inputFile;
        private ConanParameter merLength;
        private ConanParameter hashSize;
        private ConanParameter threads;
        private ConanParameter memoryMb;
        private ConanParameter outputPrefix;
        private ConanParameter counterLength;
        private ConanParameter bothStrands;
        private ConanParameter lowerCount;
        private ConanParameter upperCount;

        public Params() {

            this.inputFile = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("Input file (supports globbing)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.merLength = new NumericParameter(
                    "m",
                    "Length of mer",
                    false);

            this.hashSize = new ParameterBuilder()
                    .isOptional(false)
                    .shortName("s")
                    .description("Hash size")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.threads = new NumericParameter(
                    "t",
                    "Number of threads (1)",
                    true);

            this.memoryMb = new NumericParameter(
                    "memory",
                    "Amount of memory to request from the scheduler",
                    true);

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .longName("output")
                    .description("Output prefix (mer_counts)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.counterLength = new NumericParameter(
                    "c",
                    "Length of counting field (7)",
                    true);

            this.bothStrands = new FlagParameter(
                    "C",
                    "Count both strand, canonical representation (false)");

            this.lowerCount = new NumericParameter(
                    "L",
                    "Don't output k-mer with count < lower-count",
                    true);

            this.upperCount = new NumericParameter(
                    "U",
                    "Don't output k-mer with count > upper-count",
                    true);
        }

        public ConanParameter getInputFile() {
            return inputFile;
        }

        public ConanParameter getMerLength() {
            return merLength;
        }

        public ConanParameter getHashSize() {
            return hashSize;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMemoryMb() {
            return memoryMb;
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getCounterLength() {
            return counterLength;
        }

        public ConanParameter getBothStrands() {
            return bothStrands;
        }

        public ConanParameter getLowerCount() {
            return lowerCount;
        }

        public ConanParameter getUpperCount() {
            return upperCount;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.inputFile,
                    this.merLength,
                    this.hashSize,
                    this.threads,
                    this.memoryMb,
                    this.outputPrefix,
                    this.counterLength,
                    this.bothStrands,
                    this.lowerCount,
                    this.upperCount
            };
        }
    }

}
