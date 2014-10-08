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
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.re.AbstractReadEnhancer;
import uk.ac.tgac.conan.process.re.AbstractReadEnhancerArgs;
import uk.ac.tgac.conan.process.re.ReadEnhancerArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 14:36
 */
@MetaInfServices(uk.ac.tgac.conan.process.re.ReadEnhancer.class)
public class SickleV12 extends AbstractReadEnhancer {

    protected static final String NAME = "Sickle_V1.2";
    protected static final String EXE = "sickle";

    public SickleV12() {
        this(null);
    }

    public SickleV12(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public SickleV12(ConanExecutorService ces, Args args) {
        super(NAME, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getReadEnchancerArgs();
    }

    @Override
    public void setup() {
        Args args = this.getArgs();

        this.setMode(args.getInput().isPairedEnd() ? "pe" : "se");

        args.qualType = args.getInput().getPhred() == Library.Phred.PHRED_64 ?
                QualityTypeParameter.SickleQualityTypeOptions.ILLUMINA :
                QualityTypeParameter.SickleQualityTypeOptions.SANGER;

        if (args.getFastqOut1() == null) {
            this.setOutputFiles();
        }
    }

    protected void setOutputFiles() {

        Args args = this.getArgs();

        // Auto set the output variables if not already set.
        if (args.getInput() != null) {
            if (args.getInput().isPairedEnd()) {
                args.setFastqOut1(new File(args.getOutputDir(), args.getInput().getName() + "_1.sickle.fastq"));
                args.setFastqOut2(new File(args.getOutputDir(), args.getInput().getName() + "_2.sickle.fastq"));
                args.setFastqOutSe(new File(args.getOutputDir(), args.getInput().getName() + "_se.sickle.fastq"));
            } else {
                args.setFastqOut1(new File(args.getOutputDir(), args.getInput().getName() + ".sickle.fastq"));
            }
        }
    }

    @Override
    public List<File> getEnhancedFiles() {

        List<File> enhancedFiles = new ArrayList<>();

        Args args = this.getArgs();

        if (args.getFastqOut1() == null) {
            this.setOutputFiles();
        }

        if (args.getInput().isPairedEnd()) {

            enhancedFiles.add(args.getFastqOut1());
            enhancedFiles.add(args.getFastqOut2());
            enhancedFiles.add(args.getFastqOutSe());
        }
        else {
            enhancedFiles.add(args.getFastqOut1());
        }

        return enhancedFiles;
    }



    @MetaInfServices(ReadEnhancerArgs.class)
    public static class Args extends AbstractReadEnhancerArgs {

        public static final int DEFAULT_QUAL_THRESHOLD = 20;
        public static final int DEFAULT_LENGTH_THREHSOLD = 20;

        private File fastqOut1;
        private File fastqOut2;
        private File fastqOutSe;

        private int qualThreshold;
        private int lengthThreshold;
        private boolean noFivePrime;
        private boolean discardN;
        private QualityTypeParameter.SickleQualityTypeOptions qualType;


        public Args() {
            super(new Params(), NAME);

            this.fastqOut1 = null;
            this.fastqOut2 = null;
            this.fastqOutSe = null;
            this.qualThreshold = DEFAULT_QUAL_THRESHOLD;
            this.lengthThreshold = DEFAULT_LENGTH_THREHSOLD;
            this.noFivePrime = false;
            this.discardN = false;
            this.qualType = QualityTypeParameter.SickleQualityTypeOptions.SANGER;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public boolean isDiscardN() {
            return discardN;
        }

        public void setDiscardN(boolean discardN) {
            this.discardN = discardN;
        }

        public QualityTypeParameter.SickleQualityTypeOptions getQualType() {
            return this.qualType;
        }

        public void setQualType(QualityTypeParameter.SickleQualityTypeOptions qualType) {
            this.qualType = qualType;
        }

        public File getFastqOut1() {
            return fastqOut1;
        }

        public void setFastqOut1(File fastqOut1) {
            this.fastqOut1 = fastqOut1;
        }

        public File getFastqOut2() {
            return fastqOut2;
        }

        public void setFastqOut2(File fastqOut2) {
            this.fastqOut2 = fastqOut2;
        }

        public File getFastqOutSe() {
            return fastqOutSe;
        }

        public void setFastqOutSe(File fastqOutSe) {
            this.fastqOutSe = fastqOutSe;
        }

        public int getQualThreshold() {
            return qualThreshold;
        }

        public void setQualThreshold(int qualThreshold) {
            this.qualThreshold = qualThreshold;
        }

        public int getLengthThreshold() {
            return lengthThreshold;
        }

        public void setLengthThreshold(int lengthThreshold) {
            this.lengthThreshold = lengthThreshold;
        }

        public boolean isNoFivePrime() {
            return noFivePrime;
        }

        public void setNoFivePrime(boolean noFivePrime) {
            this.noFivePrime = noFivePrime;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

            this.qualThreshold = cmdLine.hasOption(params.getQualityThreshold().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getQualityThreshold().getShortName())) :
                    this.qualThreshold;

            this.lengthThreshold = cmdLine.hasOption(params.getLengthThreshold().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getLengthThreshold().getShortName())) :
                    this.lengthThreshold;

            this.noFivePrime = cmdLine.hasOption(params.getNoFivePrime().getShortName());
            this.discardN = cmdLine.hasOption(params.getDiscardN().getShortName());
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.qualThreshold != DEFAULT_QUAL_THRESHOLD) {
                pvp.put(params.getQualityThreshold(), Integer.toString(this.qualThreshold));
            }

            if (this.lengthThreshold != DEFAULT_LENGTH_THREHSOLD) {
                pvp.put(params.getLengthThreshold(), Integer.toString(this.lengthThreshold));
            }

            if (this.noFivePrime) {
                pvp.put(params.getNoFivePrime(), Boolean.toString(true));
            }

            if (this.discardN) {
                pvp.put(params.getDiscardN(), Boolean.toString(true));
            }

            if (this.getQualType() != null) {
                pvp.put(params.getQualityType(), this.getQualType().toString().toLowerCase());
            }

            if (this.input.isPairedEnd()) {
                pvp.put(params.getFastqIn1(), this.input.getFile1().getAbsolutePath());
                pvp.put(params.getFastqIn2(), this.input.getFile2().getAbsolutePath());
                pvp.put(params.getFastqOut1(), this.fastqOut1.getAbsolutePath());
                pvp.put(params.getFastqOut2(), this.fastqOut2.getAbsolutePath());
                pvp.put(params.getFastqOutSE(), this.fastqOutSe.getAbsolutePath());
            }
            else {
                pvp.put(params.getFastqIn1(), this.input.getFile1().getAbsolutePath());
                pvp.put(params.getFastqOut1(), this.fastqOut1.getAbsolutePath());
            }

            return pvp;
        }


        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getNoFivePrime())) {
                this.noFivePrime = Boolean.parseBoolean(value);
            } else if (param.equals(params.getDiscardN())) {
                this.discardN = Boolean.parseBoolean(value);
            } else if (param.equals(params.getLengthThreshold())) {
                this.lengthThreshold = Integer.parseInt(value);
            } else if (param.equals(params.getQualityThreshold())) {
                this.qualThreshold = Integer.parseInt(value);
            }

            //TODO Need to deal with library params somehow...
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter qualityType;
        private ConanParameter fastqIn1;
        private ConanParameter fastqIn2;
        private ConanParameter fastqOut1;
        private ConanParameter fastqOut2;
        private ConanParameter fastqOutSE;
        private ConanParameter qualityThreshold;
        private ConanParameter lengthThreshold;
        private ConanParameter discardN;
        private ConanParameter noFivePrime;

        public Params() {

            this.qualityType = new QualityTypeParameter();

            this.fastqIn1 = new ParameterBuilder()
                    .shortName("f")
                    .description("First fastq input file.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.fastqIn2 = new ParameterBuilder()
                    .shortName("r")
                    .description("Second fastq input file.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.fastqOut1 = new ParameterBuilder()
                    .shortName("o")
                    .description("First fastq output file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.fastqOut2 = new ParameterBuilder()
                    .shortName("p")
                    .description("Second fastq output file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.fastqOutSE = new ParameterBuilder()
                    .shortName("s")
                    .description("Output trimmed singles fastq file.  (Only for use in paired end mode)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.qualityThreshold = new ParameterBuilder()
                    .shortName("q")
                    .longName("qual-threshold")
                    .description("Threshold for trimming based on average quality in a window. Default 20.")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.lengthThreshold = new ParameterBuilder()
                    .shortName("l")
                    .longName("length-threshold")
                    .description("Threshold to keep a read based on length after trimming. Default 20.")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.discardN = new ParameterBuilder()
                    .shortName("n")
                    .longName("discard-n")
                    .description("Discard sequences with any Ns in them.")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();


            this.noFivePrime = new ParameterBuilder()
                    .shortName("x")
                    .longName("no-fiveprime")
                    .description("Don't do five prime trimming.")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.qualityType,
                    this.fastqIn1,
                    this.fastqIn2,
                    this.fastqOut1,
                    this.fastqOut2,
                    this.fastqOutSE,
                    this.qualityThreshold,
                    this.lengthThreshold,
                    this.discardN,
                    this.noFivePrime
            };
        }

        public ConanParameter getQualityThreshold() {
            return qualityThreshold;
        }

        public ConanParameter getLengthThreshold() {
            return lengthThreshold;
        }

        public ConanParameter getDiscardN() {
            return discardN;
        }

        public ConanParameter getQualityType() {
            return qualityType;
        }

        public ConanParameter getNoFivePrime() {
            return noFivePrime;
        }

        public ConanParameter getFastqIn1() {
            return fastqIn1;
        }

        public ConanParameter getFastqIn2() {
            return fastqIn2;
        }

        public ConanParameter getFastqOut1() {
            return fastqOut1;
        }

        public ConanParameter getFastqOut2() {
            return fastqOut2;
        }

        public ConanParameter getFastqOutSE() {
            return fastqOutSE;
        }
    }

    public static class QualityTypeParameter extends DefaultConanParameter {

        private static final long serialVersionUID = 3065149558945750682L;

        public static enum SickleQualityTypeOptions {

            ILLUMINA,
            PHRED,
            SANGER
        }

        public QualityTypeParameter() {
            super();
            this.name = "t";
            this.longName = "qual-type";
            this.description = "Type of quality values (illumina, phred, sanger) (required)";
            this.paramType = ParamType.OPTION;
            this.isOptional = false;
            this.isFlag = false;
            this.argValidator = ArgValidator.OFF;
        }

        @Override
        public boolean validateParameterValue(String value) {
            try {
                SickleQualityTypeOptions.valueOf(value.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }
}
