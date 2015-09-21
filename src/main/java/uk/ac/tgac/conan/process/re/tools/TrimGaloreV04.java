package uk.ac.tgac.conan.process.re.tools;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FilenameUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.*;
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
 * Created by maplesod on 19/08/15.
 */
@MetaInfServices(uk.ac.tgac.conan.process.re.ReadEnhancer.class)
public class TrimGaloreV04 extends AbstractReadEnhancer {

    protected static final String NAME = "TrimGalore_V0.4";
    protected static final String EXE = "trim_galore";

    public TrimGaloreV04() {
        this(null);
    }

    public TrimGaloreV04(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public TrimGaloreV04(ConanExecutorService ces, Args args) {
        super(NAME, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getReadEnchancerArgs();
    }


    @Override
    public List<File> getEnhancedFiles() {

        List<File> enhancedFiles = new ArrayList<>();

        Args args = this.getArgs();

        if (args.getInput().isPairedEnd()) {

            String f1Base = FilenameUtils.getBaseName(args.getInput().getFile1().getName());
            String f2Base = FilenameUtils.getBaseName(args.getInput().getFile2().getName());

            enhancedFiles.add(new File(args.getOutputDir(), f1Base + "_val_1.fq"));
            enhancedFiles.add(new File(args.getOutputDir(), f2Base + "_val_2.fq"));

            if (args.retainUnpaired) {
                enhancedFiles.add(new File(args.getOutputDir(), f1Base + "_unpaired_1.fq"));
                enhancedFiles.add(new File(args.getOutputDir(), f1Base + "_unpaired_2.fq"));
            }
        }
        else {
            String f1Base = FilenameUtils.getBaseName(args.getInput().getFile1().getName());
            enhancedFiles.add(new File(args.getOutputDir(), f1Base + "_trimmed.fq"));
        }

        return enhancedFiles;
    }



    @MetaInfServices(ReadEnhancerArgs.class)
    public static class Args extends AbstractReadEnhancerArgs {

        public static final int DEFAULT_QUAL_THRESHOLD = 20;
        public static final int DEFAULT_STRINGENCY = 1;
        public static final double DEFAULT_ERROR_RATE = 0.1;
        public static final int DEFAULT_LENGTH_THREHSOLD = 20;
        public static final int DEFAULT_UNPAIRED_LENGTH_CUTOFF = 35;

        private int qualityThreshold;
        private boolean phred33;
        private boolean phred64;
        private boolean fastqc;
        private String fastqcArgs;
        private String adapter;
        private String adapter2;
        private boolean illumina;
        private boolean nextera;
        private boolean small_rna;
        private int stringency;
        private double errorRate;
        private boolean gzip;
        private boolean dontGzip;
        private int lengthThreshold;
        private int clipR1;
        private int clipR2;
        private int threePrimeClipR1;
        private int threePrimeClipR2;
        private boolean paired;
        private boolean retainUnpaired;
        private int length1;
        private int length2;
        private File fastqIn1;
        private File fastqIn2;


        public Args() {
            super(new Params(), NAME);

            this.qualityThreshold = DEFAULT_QUAL_THRESHOLD;
            this.phred33 = false;
            this.phred64 = false;
            this.fastqc = false;
            this.fastqcArgs = "";
            this.adapter = "";
            this.adapter2 = "";
            this.illumina = false;
            this.nextera = false;
            this.small_rna = false;
            this.stringency = DEFAULT_STRINGENCY;
            this.errorRate = DEFAULT_ERROR_RATE;
            this.gzip = false;
            this.dontGzip = false;
            this.lengthThreshold = DEFAULT_LENGTH_THREHSOLD;
            this.outputDir = null;
            this.clipR1 = 0;
            this.clipR2 = 0;
            this.threePrimeClipR1 = 0;
            this.threePrimeClipR2 = 0;
            this.paired = false;
            this.retainUnpaired = false;
            this.length1 = DEFAULT_UNPAIRED_LENGTH_CUTOFF;
            this.length2 = DEFAULT_UNPAIRED_LENGTH_CUTOFF;
            this.fastqIn1 = null;
            this.fastqIn2 = null;
        }

        public Params getParams() {
            return (Params)this.params;
        }


        public int getQualityThreshold() {
            return qualityThreshold;
        }

        public void setQualityThreshold(int qualityThreshold) {
            this.qualityThreshold = qualityThreshold;
        }

        public boolean isPhred33() {
            return phred33;
        }

        public void setPhred33(boolean phred33) {
            this.phred33 = phred33;
        }

        public boolean isPhred64() {
            return phred64;
        }

        public void setPhred64(boolean phred64) {
            this.phred64 = phred64;
        }

        public boolean isFastqc() {
            return fastqc;
        }

        public void setFastqc(boolean fastqc) {
            this.fastqc = fastqc;
        }

        public String getFastqcArgs() {
            return fastqcArgs;
        }

        public void setFastqcArgs(String fastqcArgs) {
            this.fastqcArgs = fastqcArgs;
        }

        public String getAdapter() {
            return adapter;
        }

        public void setAdapter(String adapter) {
            this.adapter = adapter;
        }

        public String getAdapter2() {
            return adapter2;
        }

        public void setAdapter2(String adapter2) {
            this.adapter2 = adapter2;
        }

        public boolean isIllumina() {
            return illumina;
        }

        public void setIllumina(boolean illumina) {
            this.illumina = illumina;
        }

        public boolean isNextera() {
            return nextera;
        }

        public void setNextera(boolean nextera) {
            this.nextera = nextera;
        }

        public boolean isSmall_rna() {
            return small_rna;
        }

        public void setSmall_rna(boolean small_rna) {
            this.small_rna = small_rna;
        }

        public int getStringency() {
            return stringency;
        }

        public void setStringency(int stringency) {
            this.stringency = stringency;
        }

        public double getErrorRate() {
            return errorRate;
        }

        public void setErrorRate(double errorRate) {
            this.errorRate = errorRate;
        }

        public boolean isGzip() {
            return gzip;
        }

        public void setGzip(boolean gzip) {
            this.gzip = gzip;
        }

        public boolean isDontGzip() {
            return dontGzip;
        }

        public void setDontGzip(boolean dontGzip) {
            this.dontGzip = dontGzip;
        }

        public int getLengthThreshold() {
            return lengthThreshold;
        }

        public void setLengthThreshold(int lengthThreshold) {
            this.lengthThreshold = lengthThreshold;
        }

        public int getClipR1() {
            return clipR1;
        }

        public void setClipR1(int clipR1) {
            this.clipR1 = clipR1;
        }

        public int getClipR2() {
            return clipR2;
        }

        public void setClipR2(int clipR2) {
            this.clipR2 = clipR2;
        }

        public int getThreePrimeClipR1() {
            return threePrimeClipR1;
        }

        public void setThreePrimeClipR1(int threePrimeClipR1) {
            this.threePrimeClipR1 = threePrimeClipR1;
        }

        public int getThreePrimeClipR2() {
            return threePrimeClipR2;
        }

        public void setThreePrimeClipR2(int threePrimeClipR2) {
            this.threePrimeClipR2 = threePrimeClipR2;
        }

        public boolean isPaired() {
            return paired;
        }

        public void setPaired(boolean paired) {
            this.paired = paired;
        }

        public boolean isRetainUnpaired() {
            return retainUnpaired;
        }

        public void setRetainUnpaired(boolean retainUnpaired) {
            this.retainUnpaired = retainUnpaired;
        }

        public int getLength1() {
            return length1;
        }

        public void setLength1(int length1) {
            this.length1 = length1;
        }

        public int getLength2() {
            return length2;
        }

        public void setLength2(int length2) {
            this.length2 = length2;
        }

        public File getFastqIn1() {
            return fastqIn1;
        }

        public void setFastqIn1(File fastqIn1) {
            this.fastqIn1 = fastqIn1;
        }

        public File getFastqIn2() {
            return fastqIn2;
        }

        public void setFastqIn2(File fastqIn2) {
            this.fastqIn2 = fastqIn2;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

            if (cmdLine.hasOption(params.getQualityThreshold().getShortName())) {
                this.qualityThreshold = Integer.parseInt(cmdLine.getOptionValue(params.getQualityThreshold().getShortName()));
            }
            else if (cmdLine.hasOption(params.getQualityThreshold().getLongName())) {
                this.qualityThreshold = Integer.parseInt(cmdLine.getOptionValue(params.getQualityThreshold().getLongName()));;
            }
            else {
                this.qualityThreshold = DEFAULT_QUAL_THRESHOLD;
            }

            this.fastqc = cmdLine.hasOption(params.getFastqc().getLongName());

            this.fastqcArgs = cmdLine.hasOption(params.getFastqcArgs().getLongName()) ?
                    cmdLine.getOptionValue(params.getFastqcArgs().getLongName()) :
                    "";

            if (cmdLine.hasOption(params.getAdapter().getShortName())) {
                this.adapter = cmdLine.getOptionValue(params.getAdapter().getShortName());
            }
            else if (cmdLine.hasOption(params.getAdapter().getLongName())) {
                this.adapter = cmdLine.getOptionValue(params.getAdapter().getLongName());
            }
            else {
                this.adapter = "";
            }

            if (cmdLine.hasOption(params.getAdapter2().getShortName())) {
                this.adapter2 = cmdLine.getOptionValue(params.getAdapter2().getShortName());
            }
            else if (cmdLine.hasOption(params.getAdapter2().getLongName())) {
                this.adapter2 = cmdLine.getOptionValue(params.getAdapter2().getLongName());
            }
            else {
                this.adapter2 = "";
            }

            this.illumina = cmdLine.hasOption(params.getIllumina().getLongName());
            this.nextera = cmdLine.hasOption(params.getNextera().getLongName());
            this.small_rna = cmdLine.hasOption(params.getSmall_rna().getLongName());

            this.stringency = cmdLine.hasOption(params.getStringency().getLongName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getStringency().getLongName())) :
                    DEFAULT_STRINGENCY;

            this.errorRate = cmdLine.hasOption(params.getErrorRate().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getErrorRate().getShortName())) :
                    DEFAULT_ERROR_RATE;

            this.gzip = cmdLine.hasOption(params.getGzip().getLongName());
            this.dontGzip = cmdLine.hasOption(params.getDontGzip().getLongName());

            this.lengthThreshold = cmdLine.hasOption(params.getLengthThreshold().getLongName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getLengthThreshold().getLongName())) :
                    this.lengthThreshold;

            this.clipR1 = cmdLine.hasOption(params.getClipR1().getLongName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getClipR1().getLongName())) :
                    this.clipR1;

            this.clipR2 = cmdLine.hasOption(params.getClipR2().getLongName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getClipR2().getLongName())) :
                    this.clipR2;

            this.threePrimeClipR1 = cmdLine.hasOption(params.getThreePrimeClipR1().getLongName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getThreePrimeClipR1().getLongName())) :
                    this.threePrimeClipR1;

            this.threePrimeClipR2 = cmdLine.hasOption(params.getThreePrimeClipR2().getLongName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getThreePrimeClipR2().getLongName())) :
                    this.threePrimeClipR2;

            this.paired = cmdLine.hasOption(params.getPaired().getLongName());
            this.retainUnpaired = cmdLine.hasOption(params.getRetainUnpaired().getLongName());

            if (cmdLine.hasOption(params.getLength1().getShortName())) {
                this.length1 = Integer.parseInt(cmdLine.getOptionValue(params.getLength1().getShortName()));
            }
            else if (cmdLine.hasOption(params.getLength1().getLongName())) {
                this.length1 = Integer.parseInt(cmdLine.getOptionValue(params.getLength1().getLongName()));
            }
            else {
                length1 = DEFAULT_UNPAIRED_LENGTH_CUTOFF;
            }

            if (cmdLine.hasOption(params.getLength2().getShortName())) {
                this.length2 = Integer.parseInt(cmdLine.getOptionValue(params.getLength2().getShortName()));
            }
            else if (cmdLine.hasOption(params.getLength2().getLongName())) {
                this.length2 = Integer.parseInt(cmdLine.getOptionValue(params.getLength2().getLongName()));
            }
            else {
                length2 = DEFAULT_UNPAIRED_LENGTH_CUTOFF;
            }
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.outputDir != null) {
                pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
            }

            if (this.qualityThreshold != DEFAULT_QUAL_THRESHOLD) {
                pvp.put(params.getQualityThreshold(), Integer.toString(this.qualityThreshold));
            }

            if (this.fastqc) {
                pvp.put(params.getFastqc(), Boolean.toString(true));
            }

            if (this.fastqcArgs != null && !this.fastqcArgs.isEmpty()) {
                pvp.put(params.getFastqcArgs(), this.fastqcArgs);
            }

            if (this.adapter != null && !this.adapter.isEmpty()) {
                pvp.put(params.getAdapter(), this.adapter);
            }

            if (this.adapter2 != null && !this.adapter2.isEmpty()) {
                pvp.put(params.getAdapter2(), this.adapter2);
            }

            if (this.illumina) {
                pvp.put(params.getIllumina(), Boolean.toString(true));
            }

            if (this.nextera) {
                pvp.put(params.getNextera(), Boolean.toString(true));
            }

            if (this.small_rna) {
                pvp.put(params.getSmall_rna(), Boolean.toString(true));
            }

            if (this.stringency != DEFAULT_STRINGENCY) {
                pvp.put(params.getStringency(), Integer.toString(this.stringency));
            }

            if (this.errorRate != DEFAULT_ERROR_RATE) {
                pvp.put(params.getErrorRate(), Double.toString(this.errorRate));
            }

            if (this.gzip) {
                pvp.put(params.getGzip(), Boolean.toString(true));
            }

            if (this.dontGzip) {
                pvp.put(params.getGzip(), Boolean.toString(true));
            }

            if (this.lengthThreshold != DEFAULT_LENGTH_THREHSOLD) {
                pvp.put(params.getLengthThreshold(), Integer.toString(this.lengthThreshold));
            }

            if (this.clipR1 > 0) {
                pvp.put(params.getClipR1(), Integer.toString(this.clipR1));
            }

            if (this.clipR2 > 0) {
                pvp.put(params.getClipR2(), Integer.toString(this.clipR2));
            }

            if (this.threePrimeClipR1 > 0) {
                pvp.put(params.getThreePrimeClipR1(), Integer.toString(this.threePrimeClipR1));
            }

            if (this.threePrimeClipR2 > 0) {
                pvp.put(params.getThreePrimeClipR2(), Integer.toString(this.threePrimeClipR2));
            }

            if (this.retainUnpaired) {
                pvp.put(params.getRetainUnpaired(), Boolean.toString(true));
            }

            if (this.length1 != DEFAULT_UNPAIRED_LENGTH_CUTOFF) {
                pvp.put(params.getLength1(), Integer.toString(this.length1));
            }

            if (this.length2 != DEFAULT_UNPAIRED_LENGTH_CUTOFF) {
                pvp.put(params.getLength2(), Integer.toString(this.length2));
            }

            if (this.input.getPhred() == Library.Phred.PHRED_33) {
                pvp.put(params.getPhred33(), Boolean.toString(true));
            }
            else if (this.input.getPhred() == Library.Phred.PHRED_64) {
                pvp.put(params.getPhred64(), Boolean.toString(true));
            }

            if (this.input.isPairedEnd()) {
                pvp.put(params.getPaired(), Boolean.toString(true));
                pvp.put(params.getFastqIn1(), this.input.getFile1().getAbsolutePath());
                pvp.put(params.getFastqIn2(), this.input.getFile2().getAbsolutePath());
            }
            else {
                pvp.put(params.getFastqIn1(), this.input.getFile1().getAbsolutePath());
            }

            return pvp;
        }


        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutputDir())) {
                this.outputDir = new File(value);
            } else if (param.equals(params.getQualityThreshold())) {
                this.qualityThreshold = Integer.parseInt(value);
            } else if (param.equals(params.getPhred33())) {
                this.phred33 = Boolean.parseBoolean(value);
            } else if (param.equals(params.getPhred64())) {
                this.phred64 = Boolean.parseBoolean(value);
            } else if (param.equals(params.getFastqc())) {
                this.fastqc = Boolean.parseBoolean(value);
            } else if (param.equals(params.getFastqcArgs())) {
                this.fastqcArgs = value;
            } else if (param.equals(params.getAdapter())) {
                this.adapter = value;
            } else if (param.equals(params.getAdapter2())) {
                this.adapter2 = value;
            } else if (param.equals(params.getIllumina())) {
                this.illumina = Boolean.parseBoolean(value);
            } else if (param.equals(params.getNextera())) {
                this.nextera = Boolean.parseBoolean(value);
            } else if (param.equals(params.getSmall_rna())) {
                this.small_rna = Boolean.parseBoolean(value);
            } else if (param.equals(params.getStringency())) {
                this.stringency = Integer.parseInt(value);
            } else if (param.equals(params.getErrorRate())) {
                this.errorRate = Double.parseDouble(value);
            } else if (param.equals(params.getGzip())) {
                this.gzip = Boolean.parseBoolean(value);
            } else if (param.equals(params.getDontGzip())) {
                this.dontGzip = Boolean.parseBoolean(value);
            } else if (param.equals(params.getLengthThreshold())) {
                this.lengthThreshold = Integer.parseInt(value);
            } else if (param.equals(params.getClipR1())) {
                this.clipR1 = Integer.parseInt(value);
            } else if (param.equals(params.getClipR2())) {
                this.clipR2 = Integer.parseInt(value);
            } else if (param.equals(params.getThreePrimeClipR1())) {
                this.threePrimeClipR1 = Integer.parseInt(value);
            } else if (param.equals(params.getThreePrimeClipR2())) {
                this.threePrimeClipR2 = Integer.parseInt(value);
            } else if (param.equals(params.getPaired())) {
                this.paired = Boolean.parseBoolean(value);
            } else if (param.equals(params.getRetainUnpaired())) {
                this.retainUnpaired = Boolean.parseBoolean(value);
            } else if (param.equals(params.getLength1())) {
                this.length1 = Integer.parseInt(value);
            } else if (param.equals(params.getLength2())) {
                this.length2 = Integer.parseInt(value);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getFastqIn1())) {
                this.fastqIn1 = new File(value);
            }
            else if (param.equals(params.getFastqIn2())) {
                this.fastqIn2 = new File(value);
            }
        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter qualityThreshold;
        private ConanParameter phred33;
        private ConanParameter phred64;
        private ConanParameter fastqc;
        private ConanParameter fastqcArgs;
        private ConanParameter adapter;
        private ConanParameter adapter2;
        private ConanParameter illumina;
        private ConanParameter nextera;
        private ConanParameter small_rna;
        private ConanParameter stringency;
        private ConanParameter errorRate;
        private ConanParameter gzip;
        private ConanParameter dontGzip;
        private ConanParameter lengthThreshold;
        private ConanParameter outputDir;
        private ConanParameter clipR1;
        private ConanParameter clipR2;
        private ConanParameter threePrimeClipR1;
        private ConanParameter threePrimeClipR2;
        private ConanParameter paired;
        private ConanParameter retainUnpaired;
        private ConanParameter length1;
        private ConanParameter length2;
        private ConanParameter fastqIn1;
        private ConanParameter fastqIn2;

        public Params() {

            this.fastqIn1 = new ParameterBuilder()
                    .argIndex(0)
                    .isOption(false)
                    .isOptional(false)
                    .description("First fastq input file.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.fastqIn2 = new ParameterBuilder()
                    .argIndex(1)
                    .isOption(false)
                    .isOptional(true)
                    .description("Second fastq input file.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.qualityThreshold = new ParameterBuilder()
                    .shortName("q")
                    .longName("quality")
                    .description("Trim low-quality ends from reads in addition to adapter removal. Default 20.")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.phred33 = new ParameterBuilder()
                    .longName("phred33")
                    .description("Instructs Cutadapt to use ASCII+33 quality scores as Phred scores (Sanger/Illumina 1.9+ encoding) for quality trimming. Default: ON.")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.phred64 = new ParameterBuilder()
                    .longName("phred64")
                    .description("Instructs Cutadapt to use ASCII+64 quality scores as Phred scores (Illumina 1.5 encoding) for quality trimming.")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.fastqc = new ParameterBuilder()
                    .longName("fastqc")
                    .description("Run FastQC in the default mode on the FastQ file once trimming is complete.")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.fastqcArgs = new ParameterBuilder()
                    .longName("fastqc_args")
                    .description("Passes extra arguments to FastQC. If more than one argument is to be passed to FastQC " +
                            "they must be in the form \"arg1 arg2 etc.\". An example would be: " +
                            "--fastqc_args \"--nogroup --outdir /home/\". Passing extra arguments will automatically invoke " +
                            "FastQC, so --fastqc does not have to be specified separately.")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.adapter = new ParameterBuilder()
                    .shortName("a")
                    .longName("adapter")
                    .description("Adapter sequence to be trimmed. If not specified explicitly, Trim Galore will\n" +
                            " try to auto-detect whether the Illumina universal, Nextera transposase or Illumina\n" +
                            " small RNA adapter sequence was used. Also see '--illumina', '--nextera' and\n" +
                            " '--small_rna'. If no adapter can be detected within the first 1 million sequences\n" +
                            " of the first file specified Trim Galore defaults to '--illumina'.")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.adapter2 = new ParameterBuilder()
                    .shortName("a2")
                    .longName("adapter2")
                    .description("Optional adapter sequence to be trimmed off read 2 of paired-end files. This\n" +
                            " option requires '--paired' to be specified as well.")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.lengthThreshold = new ParameterBuilder()
                    .shortName("l")
                    .longName("length-threshold")
                    .description("Threshold to keep a read based on length after trimming. Default 20.")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.illumina = new ParameterBuilder()
                    .longName("illumina")
                    .description("Adapter sequence to be trimmed is the first 13bp of the Illumina universal adapter\n" +
                            " 'AGATCGGAAGAGC' instead of the default auto-detection of adapter sequence.\n")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.nextera = new ParameterBuilder()
                    .longName("nextera")
                    .description("Adapter sequence to be trimmed is the first 12bp of the Nextera adapter\n" +
                            " 'CTGTCTCTTATA' instead of the default auto-detection of adapter sequence.")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.small_rna = new ParameterBuilder()
                    .longName("nextera")
                    .description("Adapter sequence to be trimmed is the first 12bp of the Illumina Small RNA Adapter\n" +
                            " 'ATGGAATTCTCG' instead of the default auto-detection of adapter sequence.")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.stringency = new ParameterBuilder()
                    .longName("stringency")
                    .description("Overlap with adapter sequence required to trim a sequence. Defaults to a\n" +
                            " very stringent setting of 1, i.e. even a single bp of overlapping sequence\n" +
                            " will be trimmed off from the 3' end of any read.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.errorRate = new ParameterBuilder()
                    .shortName("e")
                    .description("Maximum allowed error rate (no. of errors divided by the length of the matching\n" +
                            " region) (default: 0.1)")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.gzip = new ParameterBuilder()
                    .longName("gzip")
                    .description("Compress the output file with GZIP. If the input files are GZIP-compressed\n" +
                            " the output files will automatically be GZIP compressed as well. As of v0.2.8 the\n" +
                            " compression will take place on the fly.")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.dontGzip = new ParameterBuilder()
                    .longName("dont_gzip")
                    .description("Output files won't be compressed with GZIP. This option overrides --gzip.")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.lengthThreshold = new ParameterBuilder()
                    .longName("length")
                    .description("Discard reads that became shorter than length INT because of either quality or adapter " +
                            "trimming. A value of '0' effectively disables this behaviour. Default: 20 bp.\n" +
                            "For paired-end files, both reads of a read-pair need to be longer than <INT> bp to be printed " +
                            "out to validated paired-end files (see option --paired). If only one read became too short there " +
                            "is the possibility of keeping such unpaired single-end reads (see --retain_unpaired). Default " +
                            "pair-cutoff: 20 bp.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.outputDir = new ParameterBuilder()
                    .shortName("o")
                    .longName("output_dir")
                    .description("If specified all output will be written to this directory instead of the current directory.")
                    .argValidator(ArgValidator.PATH)
                    .isOptional(false)
                    .create();

            this.clipR1 = new ParameterBuilder()
                    .longName("clip_R1")
                    .description("Instructs Trim Galore to remove <int> bp from the 5' end of read 1 (or single-end\n" +
                            " reads). This may be useful if the qualities were very poor, or if there is some\n" +
                            " sort of unwanted bias at the 5' end. Default: OFF.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.clipR2 = new ParameterBuilder()
                    .longName("clip_R2")
                    .description("Instructs Trim Galore to remove <int> bp from the 5' end of read 2 (paired-end reads\n" +
                            "                        only). This may be useful if the qualities were very poor, or if there is some sort\n" +
                            "                        of unwanted bias at the 5' end. For paired-end BS-Seq, it is recommended to remove\n" +
                            "                        the first few bp because the end-repair reaction may introduce a bias towards low\n" +
                            "                        methylation. Please refer to the M-bias plot section in the Bismark User Guide for\n" +
                            "                        some examples. Default: OFF.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.threePrimeClipR1 = new ParameterBuilder()
                    .longName("three_prime_clip_R1")
                    .description("Instructs Trim Galore to remove <int> bp from the 3' end of read 1 (or single-end\n" +
                            "                        reads) AFTER adapter/quality trimming has been performed. This may remove some unwanted\n" +
                            "                        bias from the 3' end that is not directly related to adapter sequence or basecall quality.\n" +
                            "                        Default: OFF.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.threePrimeClipR2 = new ParameterBuilder()
                    .longName("three_prime_clip_R2")
                    .description("Instructs Trim Galore to remove <int> bp from the 3' end of read 2 AFTER\n" +
                            "                        adapter/quality trimming has been performed. This may remove some unwanted bias from\n" +
                            "                        the 3' end that is not directly related to adapter sequence or basecall quality.\n" +
                            "                        Default: OFF.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.paired = new ParameterBuilder()
                    .longName("paired")
                    .description("This option performs length trimming of quality/adapter/RRBS trimmed reads for\n" +
                            "                        paired-end files. To pass the validation test, both sequences of a sequence pair\n" +
                            "                        are required to have a certain minimum length which is governed by the option\n" +
                            "                        --length (see above). If only one read passes this length threshold the\n" +
                            "                        other read can be rescued (see option --retain_unpaired). Using this option lets\n" +
                            "                        you discard too short read pairs without disturbing the sequence-by-sequence order\n" +
                            "                        of FastQ files which is required by many aligners.\n" +
                            "\n" +
                            "                        Trim Galore! expects paired-end files to be supplied in a pairwise fashion, e.g.\n" +
                            "                        file1_1.fq file1_2.fq SRR2_1.fq.gz SRR2_2.fq.gz ... .")
                    .argValidator(ArgValidator.OFF)
                    .isOptional(false)
                    .isOption(true)
                    .isFlag(true)
                    .create();

            this.retainUnpaired = new ParameterBuilder()
                    .longName("retain_unpaired")
                    .description("If only one of the two paired-end reads became too short, the longer\n" +
                            "                        read will be written to either '.unpaired_1.fq' or '.unpaired_2.fq'\n" +
                            "                        output files. The length cutoff for unpaired single-end reads is\n" +
                            "                        governed by the parameters -r1/--length_1 and -r2/--length_2. Default: OFF")
                    .isOption(false)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.length1 = new ParameterBuilder()
                    .shortName("r1")
                    .longName("length_1")
                    .description("Unpaired single-end read length cutoff needed for read 1 to be written to\n" +
                            "                        '.unpaired_1.fq' output file. These reads may be mapped in single-end mode.\n" +
                            "                        Default: 35 bp.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.length2 = new ParameterBuilder()
                    .shortName("r2")
                    .longName("length_2")
                    .description("Unpaired single-end read length cutoff needed for read 2 to be written to\n" +
                            "                        '.unpaired_2.fq' output file. These reads may be mapped in single-end mode.\n" +
                            "                        Default: 35 bp.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    qualityThreshold,
                    phred33,
                    phred64,
                    fastqc,
                    fastqcArgs,
                    adapter,
                    adapter2,
                    illumina,
                    nextera,
                    small_rna,
                    stringency,
                    errorRate,
                    gzip,
                    dontGzip,
                    lengthThreshold,
                    outputDir,
                    clipR1,
                    clipR2,
                    threePrimeClipR1,
                    threePrimeClipR2,
                    paired,
                    retainUnpaired,
                    length1,
                    length2,
                    fastqIn1,
                    fastqIn2
            };
        }

        public ConanParameter getQualityThreshold() {
            return qualityThreshold;
        }

        public ConanParameter getPhred33() {
            return phred33;
        }

        public ConanParameter getPhred64() {
            return phred64;
        }

        public ConanParameter getFastqc() {
            return fastqc;
        }

        public ConanParameter getFastqcArgs() {
            return fastqcArgs;
        }

        public ConanParameter getAdapter() {
            return adapter;
        }

        public ConanParameter getAdapter2() {
            return adapter2;
        }

        public ConanParameter getIllumina() {
            return illumina;
        }

        public ConanParameter getNextera() {
            return nextera;
        }

        public ConanParameter getSmall_rna() {
            return small_rna;
        }

        public ConanParameter getStringency() {
            return stringency;
        }

        public ConanParameter getErrorRate() {
            return errorRate;
        }

        public ConanParameter getGzip() {
            return gzip;
        }

        public ConanParameter getDontGzip() {
            return dontGzip;
        }

        public ConanParameter getLengthThreshold() {
            return lengthThreshold;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getClipR1() {
            return clipR1;
        }

        public ConanParameter getClipR2() {
            return clipR2;
        }

        public ConanParameter getThreePrimeClipR1() {
            return threePrimeClipR1;
        }

        public ConanParameter getThreePrimeClipR2() {
            return threePrimeClipR2;
        }

        public ConanParameter getPaired() {
            return paired;
        }

        public ConanParameter getRetainUnpaired() {
            return retainUnpaired;
        }

        public ConanParameter getLength1() {
            return length1;
        }

        public ConanParameter getLength2() {
            return length2;
        }

        public ConanParameter getFastqIn1() {
            return fastqIn1;
        }

        public ConanParameter getFastqIn2() {
            return fastqIn2;
        }
    }

}

