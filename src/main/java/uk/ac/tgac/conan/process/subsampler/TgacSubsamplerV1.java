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
package uk.ac.tgac.conan.process.subsampler;

import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.FlagParameter;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;

import java.io.File;

/**
 * User: maplesod
 * Date: 16/01/13
 * Time: 16:13
 */
public class TgacSubsamplerV1 extends AbstractConanProcess {

    public static final String EXE = "subsampler";

    public TgacSubsamplerV1() {
        this(new Args());
    }

    public TgacSubsamplerV1(Args args) {
        super(EXE, args, new Params());
    }

    @Override
    public String getName() {
        return "Subsampler_V1.0";
    }

    public static class Args extends AbstractProcessArgs {

        private boolean append;
        private File inputFile;
        private File outputFile;
        private File logFile;
        private int maxReadLength;
        private int maxNameLength;
        private double probability;
        private long seed;

        public Args() {
            super(new Params());

            this.append = false;
            this.inputFile = null;
            this.outputFile = null;
            this.logFile = null;
            this.maxReadLength = 2000;
            this.maxNameLength = 1000;
            this.probability = 0.0;
            this.seed = 0;
        }


        public boolean isAppend() {
            return append;
        }

        public void setAppend(boolean append) {
            this.append = append;
        }

        public File getInputFile() {
            return inputFile;
        }

        public void setInputFile(File inputFile) {
            this.inputFile = inputFile;
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

        public int getMaxReadLength() {
            return maxReadLength;
        }

        public void setMaxReadLength(int maxReadLength) {
            this.maxReadLength = maxReadLength;
        }

        public int getMaxNameLength() {
            return maxNameLength;
        }

        public void setMaxNameLength(int maxNameLength) {
            this.maxNameLength = maxNameLength;
        }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }

        public long getSeed() {
            return seed;
        }

        public void setSeed(long seed) {
            this.seed = seed;
        }

        @Override
        public void parse(String args) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            Params params = (Params)this.params;
            ParamMap pvp = new DefaultParamMap();

            if (this.append) {
                pvp.put(params.getAppend(), Boolean.toString(this.append));
            }

            if (this.inputFile != null) {
                pvp.put(params.getInputFile(), this.inputFile.getAbsolutePath());
            }

            if (this.outputFile != null) {
                pvp.put(params.getOutputFile(), this.outputFile.getAbsolutePath());
            }

            if (this.logFile != null) {
                pvp.put(params.getLogFile(), this.logFile.getAbsolutePath());
            }

            if (this.maxReadLength != 2000 && this.maxReadLength > 0) {
                pvp.put(params.getMaxReadLength(), Integer.toString(this.maxReadLength));
            }

            if (this.maxNameLength != 1000 && this.maxNameLength > 0) {
                pvp.put(params.getMaxNameLength(), Integer.toString(this.maxNameLength));
            }

            if (this.probability > 0.0 && this.probability < 1.0) {
                pvp.put(params.getProbability(), Double.toString(this.probability));
            }

            if (this.seed > 0) {
                pvp.put(params.getSeed(), Long.toString(this.seed));
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;

            if (param.equals(params.getAppend())) {
                this.append = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getInputFile())) {
                this.inputFile = new File(value);
            }
            else if (param.equals(params.getOutputFile())) {
                this.outputFile = new File(value);
            }
            else if (param.equals(params.getLogFile())) {
                this.logFile = new File(value);
            }
            else if (param.equals(params.getMaxReadLength())) {
                this.maxReadLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxNameLength())) {
                this.maxNameLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getProbability())) {
                this.probability = Double.parseDouble(value);
            }
            else if (param.equals(params.getSeed())) {
                this.seed = Long.parseLong(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter append;
        private ConanParameter inputFile;
        private ConanParameter outputFile;
        private ConanParameter logFile;
        private ConanParameter maxReadLength;
        private ConanParameter maxNameLength;
        private ConanParameter probability;
        private ConanParameter seed;


        public Params() {

            this.append = new FlagParameter(
                    "a",
                    "Append to an existing file");

            this.inputFile = new PathParameter(
                    "i",
                    "Input from a file. Default stdin.",
                    true);

            this.outputFile = new PathParameter(
                    "o",
                    "Output to a file. Default stdout.",
                    true);

            this.logFile = new PathParameter(
                    "L",
                    "Logfile. In addition to stderr, the log is printed to this file.",
                    true);

            this.maxReadLength = new NumericParameter(
                    "l",
                    "Maximum read length. Default 2000",
                    true);

            this.maxNameLength = new NumericParameter(
                    "n",
                    "Maximum name length. Default 1000",
                    true);

            this.probability = new NumericParameter(
                    "p",
                    "Probability to pick a read. Default 0.01 (1% of the reads will be picked)",
                    true);

            this.seed = new NumericParameter(
                    "s",
                    "Seed for the random number generator. The defualt is a timestamp. Useful if you want to get the same output on different runs.",
                    true);
        }

        public ConanParameter getAppend() {
            return append;
        }

        public ConanParameter getInputFile() {
            return inputFile;
        }

        public ConanParameter getOutputFile() {
            return outputFile;
        }

        public ConanParameter getLogFile() {
            return logFile;
        }

        public ConanParameter getMaxReadLength() {
            return maxReadLength;
        }

        public ConanParameter getMaxNameLength() {
            return maxNameLength;
        }

        public ConanParameter getProbability() {
            return probability;
        }

        public ConanParameter getSeed() {
            return seed;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.append,
                    this.inputFile,
                    this.outputFile,
                    this.logFile,
                    this.maxReadLength,
                    this.maxNameLength,
                    this.probability,
                    this.seed
            };
        }
    }
}
