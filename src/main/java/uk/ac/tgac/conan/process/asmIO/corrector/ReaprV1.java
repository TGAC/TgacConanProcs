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
package uk.ac.tgac.conan.process.asmIO.corrector;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionResult;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asmIO.*;
import uk.ac.tgac.conan.process.misc.FastXRC_V0013;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:30
 */
@MetaInfServices(AssemblyEnhancer.class)
public class ReaprV1 extends AbstractAssemblyEnhancer {

    public static final String EXE = "reapr";
    public static final String NAME = "Reapr_V1";
    public static final AssemblyEnhancerType TYPE = AssemblyEnhancerType.CORRECTOR;


    public ReaprV1() {
        this(null);
    }

    public ReaprV1(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public ReaprV1(ConanExecutorService ces, Args args) {
        super(NAME, TYPE, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public ExecutionResult execute(ExecutionContext executionContext)
            throws ProcessExecutionException, InterruptedException {

        Args args = this.getArgs();

        List<String> commands = new ArrayList<>();

        if (args.getLibraries() == null || args.getLibraries().isEmpty()) {
            throw new IllegalArgumentException("Can't execute Reapr because not input read files have been provided");
        }
        else if (args.getLibraries().size() > 1) {
            throw new IllegalArgumentException("Can't execute Reapr on more than one library at a time");
        }

        Library inputLibrary = args.getLibraries().get(0);


        // Check for "outie" libraries, if found reverse complement it
        if (inputLibrary.getSeqOrientation() == Library.SeqOrientation.RF) {

            FastXRC_V0013.Args fxrc1Args = new FastXRC_V0013.Args();
            fxrc1Args.setIn(inputLibrary.getFile1());
            fxrc1Args.setOut(args.getInputReadsFile1());
            fxrc1Args.setQualityOffset(inputLibrary.getPhred() == Library.Phred.PHRED_33 ? 33 : 64);

            FastXRC_V0013.Args fxrc2Args = new FastXRC_V0013.Args();
            fxrc2Args.setIn(inputLibrary.getFile2());
            fxrc2Args.setOut(args.getInputReadsFile2());
            fxrc2Args.setQualityOffset(inputLibrary.getPhred() == Library.Phred.PHRED_33 ? 33 : 64);

            FastXRC_V0013 fxrc1 = new FastXRC_V0013(this.conanExecutorService, fxrc1Args);
            FastXRC_V0013 fxrc2 = new FastXRC_V0013(this.conanExecutorService, fxrc2Args);

            this.conanExecutorService.executeProcess(fxrc1, args.getOutputDir(), executionContext.getJobName() + "-rc1", 1, 0, false);
            this.conanExecutorService.executeProcess(fxrc2, args.getOutputDir(), executionContext.getJobName() + "-rc2", 1, 0, false);
        }
        else {
            // Make links
            this.conanExecutorService.getConanProcessService().createLocalSymbolicLink(inputLibrary.getFile1(), args.getInputReadsFile1());
            this.conanExecutorService.getConanProcessService().createLocalSymbolicLink(inputLibrary.getFile2(), args.getInputReadsFile2());

        }

        return super.execute(executionContext);
    }

    @Override
    public String getCommand() throws ConanParameterException {

        Args args = this.getArgs();

        List<String> commands = new ArrayList<>();

        File convertedAssemblyFileForFaCheck = new File(args.getOutputDir(), "input_assembly");
        File convertedAssemblyFile = new File(args.getOutputDir(), "input_assembly.fa");
        File bamFile = new File(args.getOutputDir(), "mapped_reads.bam");

        // Assembly conversion
        commands.add(EXE + " facheck "
                + args.getInputAssembly().getAbsolutePath() + " "
                + convertedAssemblyFileForFaCheck.getAbsolutePath());

        // Mapping command
        commands.add(EXE + " smaltmap -n " + args.getThreads() + " "
                + convertedAssemblyFile.getAbsolutePath() + " "
                + args.getInputReadsFile1().getAbsolutePath() + " "
                + args.getInputReadsFile2().getAbsolutePath() + " "
                + bamFile.getAbsolutePath());

        // Reapr pipeline command
        commands.add(EXE + " pipeline "
                + convertedAssemblyFile.getAbsolutePath() + " "
                + bamFile.getAbsolutePath() + " "
                + args.getPipelineOutDir().getAbsolutePath());

        String command = StringUtils.join(commands, "; ");

        return command;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @MetaInfServices(AssemblyEnhancerArgs.class)
    public static class Args extends AbstractAssemblyEnhancerArgs {

        private Params params = new Params();

        private boolean outies;

        public Args() {

            super(new Params(), NAME, TYPE);

            this.outies = false;
        }

        protected Params getParams() {
            return (Params)this.params;
        }

        public File getPipelineOutDir() {
            return new File(this.getOutputDir(), "pipeline_out");
        }

        public File getBrokenAssemblyFile() {
            return new File(this.getPipelineOutDir(), "04.break.broken_assembly.fa");
        }

        public File getSummaryReportFile() {
            return new File(this.getPipelineOutDir(), "05.summary.report.tsv");
        }

        public File getSummaryStatsFile() {
            return new File(this.getPipelineOutDir(), "05.summary.stats.tsv");
        }

        public File getInputReadsFile1() {
            return new File(this.getOutputDir(), "input_reads_1.fastq");
        }

        public File getInputReadsFile2() {
            return new File(this.getOutputDir(), "input_reads_2.fastq");
        }

        public boolean isOuties() {
            return outies;
        }

        public void setOuties(boolean outies) {
            this.outies = outies;
        }

        @Override
        public File getOutputFile() {
            return this.getBrokenAssemblyFile();
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            ParamMap pvp = new DefaultParamMap();

            if (this.getInputAssembly() != null) {
                pvp.put(params.getAssemblyFile(), this.getInputAssembly().getAbsolutePath());
            }

            if (this.getLibraries() != null && !this.getLibraries().isEmpty()) {
                pvp.put(params.getReadFile1(), this.getLibraries().get(0).getFile1().getAbsolutePath());
                pvp.put(params.getReadFile2(), this.getLibraries().get(0).getFile2().getAbsolutePath());
            }

            if (this.getThreads() > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.getThreads()));
            }

            if (this.outies) {
                pvp.put(params.getOuties(), Boolean.toString(this.outies));
            }

            if (this.getOutputDir() != null) {
                pvp.put(params.getOutputDir(), this.getOutputDir().getAbsolutePath());
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            if (param.equals(this.params.getAssemblyFile())) {
                this.setInputAssembly(new File(value));
            }
            else if (param.equals(this.params.getReadFile1())) {
                if (this.getLibraries().isEmpty()) {
                    Library lib = new Library();
                    lib.setFiles(new File(value), null);
                }
                else {
                    File file2 = this.getLibraries().get(0).getFile2();
                    this.getLibraries().get(0).setFiles(new File(value), file2);
                }
            }
            else if (param.equals(this.params.getReadFile2())) {
                if (this.getLibraries().isEmpty()) {
                    Library lib = new Library();
                    lib.setFiles(null, new File(value));
                }
                else {
                    File file1 = this.getLibraries().get(0).getFile1();
                    this.getLibraries().get(0).setFiles(file1, new File(value));
                }
            }
            else if (param.equals(this.params.getThreads())) {
                this.setThreads(Integer.parseInt(value));
            }
            else if (param.equals(this.params.getOutputDir())) {
                this.setOutputDir(new File(value));
            }
            else if (param.equals(this.params.getOuties())) {
                this.outies = Boolean.parseBoolean(value);
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

        private ConanParameter assemblyFile;
        private ConanParameter readFile1;
        private ConanParameter readFile2;
        private ConanParameter outies;
        private ConanParameter threads;
        private ConanParameter outputDir;

        public Params() {

            this.assemblyFile = new ParameterBuilder()
                    .isOptional(false)
                    .shortName("asm")
                    .description("The assembly file to analyse")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.readFile1 = new ParameterBuilder()
                    .isOptional(false)
                    .shortName("r1")
                    .description("The first read file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.readFile2 = new ParameterBuilder()
                    .isOptional(false)
                    .shortName("r2")
                    .description("The second read file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.outies = new ParameterBuilder()
                    .description("Whether or not the reads are outies (i.e. RF orientation)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.threads = new ParameterBuilder()
                    .longName("threads")
                    .description("Maximum number of threads [default: number of CPUs]")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.outputDir = new ParameterBuilder()
                    .longName("outputdir")
                    .description("The directory in which to place all output")
                    .argValidator(ArgValidator.PATH)
                    .create();
        }

        public ConanParameter getAssemblyFile() {
            return assemblyFile;
        }

        public ConanParameter getReadFile1() {
            return readFile1;
        }

        public ConanParameter getReadFile2() {
            return readFile2;
        }

        public ConanParameter getOuties() {
            return outies;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.assemblyFile,
                    this.readFile1,
                    this.readFile2,
                    this.threads,
                    this.outies,
                    this.outputDir
            };
        }
    }

}
