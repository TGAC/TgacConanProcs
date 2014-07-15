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

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asmIO.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maplesod on 14/06/14.
 */
@MetaInfServices(AssemblyEnhancer.class)
public class PlatanusScaffoldV12 extends AbstractAssemblyEnhancer {

    private static Logger log = LoggerFactory.getLogger(PlatanusScaffoldV12.class);

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

    @Override
    public void setup() throws IOException {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);

        Args args = this.getArgs();

        BufferedReader reader = new BufferedReader(new FileReader(args.getInputAssembly()));
        String line = null;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith(">")) {
                if (line.contains("_cov")) {
                    this.addPreCommand("ln -s -f " + args.getInputAssembly() + " " + args.getConvertedInputFile());
                }
                else if (line.split(" ").length == 3) {
                    // We are probably working with an abyss contig file... so convert to something suitable
                    this.convertAbyssContigs(args.getInputAssembly(), args.getConvertedInputFile());
                    log.info("Converted abyss assembly to platanus format.  New assembly file at: " + args.getConvertedInputFile().getAbsolutePath());
                }
                else {
                    throw new IOException("Unknown input file type.  Fasta header is unrecognised.  Currently we only support platanus or abyss style contig files for input.");
                }
                break;  // We are probably working with a platanus contig file.
            }
        }

    }

    protected void convertAbyssHeader(PrintWriter writer, String header, String seq) {

        String[] parts = header.split(" ");

        int index = Integer.parseInt(parts[0]) + 1;
        //int length = Integer.parseInt(parts[1]);
        int kcov = Integer.parseInt(parts[2]);

        writer.println(">scaffold" + index + "_cov" + kcov);
        writer.println(seq);
    }

    protected void convertAbyssContigs(File inputAssembly, File convertedInputFile) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(inputAssembly));
        PrintWriter contigWriter = new PrintWriter(new BufferedWriter(new FileWriter(convertedInputFile)));

        String currentId = "";
        StringBuilder currentContig = new StringBuilder();


        String line = null;
        while((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.startsWith(">")) {
                if (currentContig.length() > 0) {
                    this.convertAbyssHeader(contigWriter, currentId, currentContig.toString());
                }
                currentContig = new StringBuilder();
                currentId = line.substring(1);
            }
            else {
                currentContig.append(line);
            }
        }

        if (currentContig.length() > 0) {
            this.convertAbyssHeader(contigWriter, currentId, currentContig.toString());
        }

        if (reader != null) reader.close();
        if (contigWriter != null) contigWriter.close();
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

        private int mappingSeedLength;
        private int minOverlapLength;
        private int minLinks;
        private double maxDiffBubbleCrush;

        public Args() {

            super(new Params(), NAME, TYPE);

            this.mappingSeedLength = DEFAULT_MAPPING_SEED_LENGTH;
            this.minOverlapLength = DEFAULT_MIN_OVERLAP_LENGTH;
            this.minLinks = DEFAULT_MIN_LINKS;
            this.maxDiffBubbleCrush = DEFAULT_MAX_DIFF_BUBBLE_CRUSH;
        }

        protected Params getParams() {
            return (Params) this.params;
        }

        public File getConvertedInputFile() {
            return new File(this.getOutputDir(), "input.fa");
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
                this.setInputAssembly(new File(value));
            }
            else if (param.equals(params.getBubbleFile())) {
                this.setBubbleFile(new File(value));
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
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

            this.mappingSeedLength = cmdLine.hasOption(params.getMappingSeedLength().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMappingSeedLength().getShortName())) :
                    this.mappingSeedLength;

            this.minOverlapLength = cmdLine.hasOption(params.getMinOverlapLength().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinOverlapLength().getShortName())) :
                    this.minOverlapLength;

            this.minLinks = cmdLine.hasOption(params.getMinLinks().getShortName()) ?
                    Integer.parseInt(cmdLine.getOptionValue(params.getMinLinks().getShortName())) :
                    this.minLinks;

            this.maxDiffBubbleCrush = cmdLine.hasOption(params.getMaxDiffBubbleCrush().getShortName()) ?
                    Double.parseDouble(cmdLine.getOptionValue(params.getMaxDiffBubbleCrush().getShortName())) :
                    this.maxDiffBubbleCrush;
        }

        @Override
        public ParamMap getArgMap() {

            Params params = (Params)this.params;
            ParamMap pvp = new DefaultParamMap();

            if (this.getOutputPrefix() != null) {
                pvp.put(params.getOutputPrefix(), this.getOutputPrefix());
            }

            if (this.getInputAssembly() != null) {
                pvp.put(params.getContigFile(), this.getConvertedInputFile().getAbsolutePath());
            }

            if (this.getBubbleFile() != null) {
                pvp.put(params.getBubbleFile(), this.getBubbleFile().getAbsolutePath());
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

            if (this.getLibraries() != null && !this.getLibraries().isEmpty()) {
                pvp.put(params.getReads(), new InputLibsArg(this.getLibraries()).toString());
            }


            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter outputPrefix;
        private ConanParameter reads;
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
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.reads = new ParameterBuilder()
                    .isOption(false)
                    .description("Input reads for platanus scaffold")
                    .argValidator(ArgValidator.OFF)
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

        public ConanParameter getReads() {
            return reads;
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
                    this.reads,
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

                libStrings.add("-a" + index + " " + lib.getAverageInsertSize());
                libStrings.add("-d" + index + " " + lib.getInsertErrorTolerance());

                index++;
            }

            return StringUtils.join(libStrings, " ").trim();
        }

    }
}