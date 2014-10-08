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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.re.AbstractReadEnhancer;
import uk.ac.tgac.conan.process.re.AbstractReadEnhancerArgs;
import uk.ac.tgac.conan.process.re.ReadEnhancer;
import uk.ac.tgac.conan.process.re.ReadEnhancerArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 09:49
 */
@MetaInfServices(ReadEnhancer.class)
public class QuakeV03 extends AbstractReadEnhancer {

    public static final String EXE = "quake.py";
    protected static final String NAME = "Quake_V0.3";

    public QuakeV03() {
        this(null);
    }

    public QuakeV03(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public QuakeV03(ConanExecutorService ces, Args args) {
        super(NAME, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getReadEnchancerArgs();
    }

    @Override
    public void setup() {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);

        Args args = this.getArgs();

        if (args.getReadsListFile() == null || !args.getReadsListFile().exists()) {

            if (args.getInput() == null) {
                throw new IllegalArgumentException("Input reads file does not exist and no library object provided.  Cannot continue.");
            }

            final ConanProcessService cps = this.conanExecutorService.getConanProcessService();

            boolean pairedEnd = args.getInput().isPairedEnd();

            File in1 = new File(args.getOutputDir(), args.getInput().getFile1().getName());
            File in2 = pairedEnd ? new File(args.getOutputDir(), args.getInput().getFile2().getName()) : null;

            try {
                cps.createLocalSymbolicLink(args.getInput().getFile1(), in1);
                if (pairedEnd) {
                    cps.createLocalSymbolicLink(args.getInput().getFile2(), in2);
                }
            } catch (ProcessExecutionException | InterruptedException e) {
                throw new IllegalArgumentException("Could not create symbolic links for input files.", e);
            }

            String fp1 = in1.getAbsolutePath();
            String fp2 = pairedEnd ? " " + in2.getAbsolutePath() : "";

            String outLine = fp1 + fp2;

            List<String> outLines = new ArrayList<>();
            outLines.add(outLine);

            File inputFileList = new File(args.getOutputDir(), "readsListFile.lst");
            args.setReadsListFile(inputFileList);
            args.setQualityScale(args.getInput().getPhred() == null ? 0 : args.getInput().getPhred() == Library.Phred.PHRED_33 ? 33 : 64);

            try {
                FileUtils.writeLines(inputFileList, outLines);
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not output Quake reads configuration file: " + inputFileList, e);
            }
        }
    }

    @Override
    public List<File> getEnhancedFiles() {

        List<File> correctedFiles = new ArrayList<>();

        FilePair fp = this.getArgs().getPairedEndCorrectedFiles();

        correctedFiles.add(fp.getFile1());
        correctedFiles.add(fp.getFile2());
        correctedFiles.addAll(this.getArgs().getSingleEndCorrectedFiles());

        return correctedFiles;
    }

    @MetaInfServices(ReadEnhancerArgs.class)
    public static class Args extends AbstractReadEnhancerArgs {

        public static final int DEFAULT_KMER = 17;
        public static final int DEFAULT_RATIO = 200;
        public static final int DEFAULT_THREADS = 4;

        private File readsListFile;
        private int kmer;
        private int qualityScale;
        private boolean noJelly;
        private boolean noCount;
        private boolean kmersAsInts;
        private int hashSize;
        private boolean noCut;
        private int ratio;
        private int minLength;
        private boolean outputErrors;
        private int bwaTrim;
        private boolean headersOnly;
        private boolean log;

        public Args() {

            super(new Params(), NAME);
            this.readsListFile = null;
            this.kmer = DEFAULT_KMER;
            this.qualityScale = 0;
            this.noJelly = false;
            this.noCount = false;
            this.kmersAsInts = false;
            this.hashSize = 0;
            this.noCut = false;
            this.ratio = DEFAULT_RATIO;
            this.minLength = 0;
            this.outputErrors = false;
            this.bwaTrim = 0;
            this.headersOnly = false;
            this.log = false;

            // Override
            this.threads = DEFAULT_THREADS;
        }

        public Params getParams() {
            return (Params)this.params;
        }


        public File getReadsListFile() {
            return readsListFile;
        }

        public void setReadsListFile(File readsListFile) {
            this.readsListFile = readsListFile;
        }

        public int getKmer() {
            return kmer;
        }

        public void setKmer(int kmer) {
            this.kmer = kmer;
        }

        public int getQualityScale() {
            return qualityScale;
        }

        public void setQualityScale(int qualityScale) {
            this.qualityScale = qualityScale;
        }

        public int getMinLength() {
            return minLength;
        }

        public void setMinLength(int minLength) {
            this.minLength = minLength;
        }

        public boolean isNoJelly() {
            return noJelly;
        }

        public void setNoJelly(boolean noJelly) {
            this.noJelly = noJelly;
        }

        public boolean isNoCount() {
            return noCount;
        }

        public void setNoCount(boolean noCount) {
            this.noCount = noCount;
        }

        public boolean isKmersAsInts() {
            return kmersAsInts;
        }

        public void setKmersAsInts(boolean kmersAsInts) {
            this.kmersAsInts = kmersAsInts;
        }

        public int getHashSize() {
            return hashSize;
        }

        public void setHashSize(int hashSize) {
            this.hashSize = hashSize;
        }

        public boolean isNoCut() {
            return noCut;
        }

        public void setNoCut(boolean noCut) {
            this.noCut = noCut;
        }

        public int getRatio() {
            return ratio;
        }

        public void setRatio(int ratio) {
            this.ratio = ratio;
        }

        public boolean isOutputErrors() {
            return outputErrors;
        }

        public void setOutputErrors(boolean outputErrors) {
            this.outputErrors = outputErrors;
        }

        public int getBwaTrim() {
            return bwaTrim;
        }

        public void setBwaTrim(int bwaTrim) {
            this.bwaTrim = bwaTrim;
        }

        public boolean isHeadersOnly() {
            return headersOnly;
        }

        public void setHeadersOnly(boolean headersOnly) {
            this.headersOnly = headersOnly;
        }

        public boolean isLog() {
            return log;
        }

        public void setLog(boolean log) {
            this.log = log;
        }

        protected FilePair loadInputFilePaths() throws IOException {
            if (this.readsListFile == null || !this.readsListFile.exists())
                return null;

            List<String> fileLines = FileUtils.readLines(this.readsListFile);

            // This wrapper only processes a single library, so ignore all but the first line.
            if (fileLines.size() == 0)
                return null;

            String[] pairedEndFilePaths = fileLines.get(0).split(" ");

            if (pairedEndFilePaths.length != 2)
                return null;

            FilePair inputFiles = new FilePair(new File(pairedEndFilePaths[0].trim()), new File(pairedEndFilePaths[1].trim()));

            return inputFiles;
        }

        public FilePair getPairedEndCorrectedFiles() {

            String f1n = this.input.getFile1().getName();
            String f2n = this.input.isPairedEnd() ? this.input.getFile2().getName() : null;

            String corOut1FileName = FilenameUtils.getBaseName(f1n) + ".cor." + FilenameUtils.getExtension(f1n);
            String corOut2FileName = this.input.isPairedEnd() ? FilenameUtils.getBaseName(f2n) + ".cor." + FilenameUtils.getExtension(f2n) : null;

            File corOut1 = new File(this.getOutputDir(), corOut1FileName);
            File corOut2 = this.input.isPairedEnd() ? new File(this.getOutputDir(), corOut2FileName) : null;

            return new FilePair(corOut1, corOut2);
        }

        public List<File> getSingleEndCorrectedFiles() {

            String f1n = this.input.getFile1().getName();
            String f2n = this.input.isPairedEnd() ? this.input.getFile2().getName() : null;

            String seCorOut1FileName = FilenameUtils.getBaseName(f1n) + ".cor_single." + FilenameUtils.getExtension(f1n);
            String seCorOut2FileName = this.input.isPairedEnd() ? FilenameUtils.getBaseName(f2n) + ".cor_single." + FilenameUtils.getExtension(f2n) : null;

            File seCorOut1 = new File(this.getOutputDir(), seCorOut1FileName);
            File seCorOut2 = this.input.isPairedEnd() ? new File(this.getOutputDir(), seCorOut2FileName) : null;

            List<File> seCorfiles = new ArrayList<>();
            seCorfiles.add(seCorOut1);

            if (this.input.isPairedEnd()) {
                seCorfiles.add(seCorOut2);
            }

            return seCorfiles;
        }

        public FilePair getPairedEndErrorFiles() {
            return null;
        }

        public List<File> getSingleEndErrorFiles() {
            return null;
        }


        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

            this.kmer = cmdLine.hasOption(params.getKmer().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getKmer().getShortName())) :
                    this.kmer;

            this.threads = cmdLine.hasOption(params.getProcesses().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getProcesses().getShortName())) :
                    this.threads;

            this.qualityScale = cmdLine.hasOption(params.getQualityScale().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getQualityScale().getShortName())) :
                    this.qualityScale;

            this.noJelly = cmdLine.hasOption(params.getNoJelly().getLongName());
            this.noCount = cmdLine.hasOption(params.getNoCount().getLongName());
            this.kmersAsInts = cmdLine.hasOption(params.getKmersAsInts().getLongName());

            this.hashSize = cmdLine.hasOption(params.getHashSize().getLongName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getQualityScale().getLongName())) :
                    this.hashSize;

            this.noCut = cmdLine.hasOption(params.getNoCut().getLongName());

            this.ratio = cmdLine.hasOption(params.getRatio().getLongName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getRatio().getLongName())) :
                    this.ratio;

            this.minLength = cmdLine.hasOption(params.getMinLength().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinLength().getShortName())) :
                    this.minLength;

            this.outputErrors = cmdLine.hasOption(params.getOutputErrors().getShortName());

            this.bwaTrim = cmdLine.hasOption(params.getBwaTrim().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getBwaTrim().getShortName())) :
                    this.bwaTrim;

            this.headersOnly = cmdLine.hasOption(params.getHeadersOnly().getLongName());
            this.log = cmdLine.hasOption(params.getLog().getLongName());
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.readsListFile != null) {
                pvp.put(params.getReadsListFile(), this.getReadsListFile().getAbsolutePath());
            }

            pvp.put(params.getKmer(), String.valueOf(this.kmer));

            if (this.threads != DEFAULT_THREADS && this.threads > 0) {
                pvp.put(params.getProcesses(), Integer.toString(this.threads));
            }

            if (this.qualityScale != 0) {
                pvp.put(params.getQualityScale(), Integer.toString(this.qualityScale));
            }

            if (this.noJelly) {
                pvp.put(params.getNoJelly(), Boolean.toString(this.noJelly));
            }

            if (this.noCount) {
                pvp.put(params.getNoCount(), Boolean.toString(this.noCount));
            }

            if (this.kmersAsInts) {
                pvp.put(params.getKmersAsInts(), Boolean.toString(this.kmersAsInts));
            }

            if (this.hashSize > 0) {
                pvp.put(params.getHashSize(), Integer.toString(this.hashSize));
            }

            if (this.noCut) {
                pvp.put(params.getNoCut(), Boolean.toString(this.noCut));
            }

            if (this.ratio != DEFAULT_RATIO) {
                pvp.put(params.getRatio(), Integer.toString(this.ratio));
            }

            if (this.getMinLength() > 0) {
                pvp.put(params.getMinLength(), Integer.toString(this.minLength));
            }

            if (this.outputErrors) {
                pvp.put(params.getOutputErrors(), Boolean.toString(this.outputErrors));
            }

            if (this.bwaTrim != 0) {
                pvp.put(params.getBwaTrim(), Integer.toString(this.bwaTrim));
            }

            if (this.headersOnly) {
                pvp.put(params.getHeadersOnly(), Boolean.toString(this.headersOnly));
            }

            if (this.log) {
                pvp.put(params.getLog(), Boolean.toString(this.log));
            }

            return pvp;
        }


        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getReadsListFile())) {
                this.readsListFile = new File(value);
            }
            else if (param.equals(params.getKmer())) {
                this.kmer = Integer.parseInt(value);
            }
            else if (param.equals(params.getQualityScale())) {
                this.qualityScale = Integer.parseInt(value);
            }
            else if (param.equals(params.getProcesses())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getNoJelly())) {
                this.noJelly = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getNoCount())) {
                this.noCount = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getKmersAsInts())) {
                this.kmersAsInts = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getHashSize())) {
                this.hashSize = Integer.parseInt(value);
            }
            else if (param.equals(params.getNoCut())) {
                this.noCut = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getRatio())) {
                this.ratio = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinLength())) {
                this.minLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getBwaTrim())) {
                this.bwaTrim = Integer.parseInt(value);
            }
            else if (param.equals(params.getOutputErrors())) {
                this.outputErrors = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getHeadersOnly())) {
                this.headersOnly = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getLog())) {
                this.log = Boolean.parseBoolean(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter readsListFile;
        private ConanParameter kmer;
        private ConanParameter processes;
        private ConanParameter qualityScale;
        private ConanParameter noJelly;
        private ConanParameter noCount;
        private ConanParameter kmersAsInts;
        private ConanParameter hashSize;
        private ConanParameter noCut;
        private ConanParameter ratio;
        private ConanParameter minLength;
        private ConanParameter outputErrors;
        private ConanParameter bwaTrim;
        private ConanParameter headersOnly;
        private ConanParameter log;

        public Params() {

            this.readsListFile = new PathParameter(
                    "f",
                    "File containing fastq file names, one per line or two per line for paired end reads.",
                    false);

            this.kmer = new NumericParameter(
                    "k",
                    "Size of k-mers to correct",
                    true);

            this.processes = new NumericParameter(
                    "p",
                    "Number of processes [default: 4]",
                    true);

            this.qualityScale = new NumericParameter(
                    "q",
                    "Quality value ascii scale, generally 64 or 33. If not specified, it will guess.",
                    true);

            this.noJelly = new ParameterBuilder()
                    .longName("no_jelly")
                    .description("Count k-mers using a simpler program than Jellyfish")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.noCount = new ParameterBuilder()
                    .longName("no_count")
                    .description("Kmers are already counted and in expected file [reads file].qcts or [reads file].cts [default: False]")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.kmersAsInts = new ParameterBuilder()
                    .longName("int")
                    .description("Count kmers as integers w/o the use of quality values [default: False]")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.hashSize = new ParameterBuilder()
                    .longName("hash_size")
                    .description("Jellyfish hash-size parameter. Quake will estimate using k if not given")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.noCut = new ParameterBuilder()
                    .longName("no_cut")
                    .description("Coverage model is optimized and cutoff was printed to expected file cutoff.txt [default: False]")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.ratio = new ParameterBuilder()
                    .longName("ratio")
                    .description(" Likelihood ratio to set trusted/untrusted cutoff. Generally set between 10-1000 with lower numbers suggesting a lower threshold. [default: 200]")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.minLength = new ParameterBuilder()
                    .shortName("l")
                    .description("Return only reads corrected and/or trimmed to <min_read> bp")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.outputErrors = new ParameterBuilder()
                    .shortName("l")
                    .description("Output error reads even if they can't be corrected, maintaing paired end reads")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.bwaTrim = new ParameterBuilder()
                    .shortName("t")
                    .description("Use BWA-like trim parameter <trim_par>")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.headersOnly = new ParameterBuilder()
                    .longName("headers")
                    .description("Output only the original read headers without correction messages")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.log = new ParameterBuilder()
                    .longName("log")
                    .description("Output a log of all corrections into *.log as \"quality position new_nt old_nt\"")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.readsListFile,
                    this.kmer,
                    this.processes,
                    this.qualityScale,
                    this.noJelly,
                    this.noCount,
                    this.kmersAsInts,
                    this.hashSize,
                    this.noCut,
                    this.ratio,
                    this.minLength,
                    this.outputErrors,
                    this.bwaTrim,
                    this.headersOnly,
                    this.log
            };
        }

        public ConanParameter getReadsListFile() {
            return readsListFile;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        public ConanParameter getProcesses() {
            return processes;
        }

        public ConanParameter getQualityScale() {
            return qualityScale;
        }

        public ConanParameter getMinLength() {
            return minLength;
        }

        public ConanParameter getNoJelly() {
            return noJelly;
        }

        public ConanParameter getNoCount() {
            return noCount;
        }

        public ConanParameter getKmersAsInts() {
            return kmersAsInts;
        }

        public ConanParameter getHashSize() {
            return hashSize;
        }

        public ConanParameter getNoCut() {
            return noCut;
        }

        public ConanParameter getRatio() {
            return ratio;
        }

        public ConanParameter getOutputErrors() {
            return outputErrors;
        }

        public ConanParameter getBwaTrim() {
            return bwaTrim;
        }

        public ConanParameter getHeadersOnly() {
            return headersOnly;
        }

        public ConanParameter getLog() {
            return log;
        }
    }

}
