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
package uk.ac.tgac.conan.process.asm.tools;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: maplesod
 * Date: 13/03/13
 * Time: 15:49
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class SpadesV31 extends AbstractAssembler {

    public static final String NAME = "Spades_V3.1";
    public static final String EXE = "spades.py";

    public SpadesV31() {
        this(null);
    }
    public SpadesV31(ConanExecutorService ces) {
        this(ces, new Args());
    }
    public SpadesV31(ConanExecutorService ces, AbstractProcessArgs args) {
        super(NAME, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public boolean makesUnitigs() {
        return false;
    }

    @Override
    public boolean makesContigs() {
        return true;
    }

    @Override
    public boolean makesScaffolds() {
        return false;
    }

    @Override
    public boolean makesBubbles() {
        return false;
    }

    @Override
    public File getUnitigsFile() {
        return null;
    }

    @Override
    public File getContigsFile() {
        return new File(this.getArgs().getOutputDir(), "contigs.fa");
    }

    @Override
    public File getScaffoldsFile() {
        return null;
    }

    @Override
    public File getBubbleFile() {
        return null;
    }

    @Override
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN_OPTIMISER;
    }


    protected String createLibString(Library lib, boolean additionalLib) {

        StringBuilder sb = new StringBuilder();

        if (lib.getFile1Type() == SeqFile.FileType.FASTQ) {
            sb.append("-fastq");
        }
        else if (lib.getFile1Type() == SeqFile.FileType.FASTA) {
            sb.append("-fasta");
        }
        else {
            throw new IllegalArgumentException("Unknown library file type: " + lib.getFile1Type());
        }

        sb.append(" ");

        if (lib.getReadLength() < 1000) {
            if (additionalLib) {
                sb.append(lib.isPairedEnd() ? "-shortPaired" : "-short");
            }
            else {
                sb.append(lib.isPairedEnd() ? "-shortPaired2" : "-short2");
            }
        }
        else {
            sb.append(lib.isPairedEnd() ? "-longPaired" : "-long");
        }

        sb.append(" ");

        if (lib.isPairedEnd()) {
            if (lib.getFiles().size() == 1) {
                sb.append("-interleaved");
            } else if (lib.getFiles().size() == 2) {
                sb.append("-separate");
            }
            else {
                throw new IllegalArgumentException("Incorrectly constructed library");
            }
        }

        sb.append(" ");

        for(File f : lib.getFiles()) {
            sb.append(f.getAbsolutePath()).append(" ");
        }

        return sb.toString();
    }


    @MetaInfServices(DeBruijnOptimiserArgs.class)
    public static class Args extends AbstractAssemblerArgs implements DeBruijnOptimiserArgs {

        private boolean careful;
        private KmerRange kmerRange;

        public Args() {
            super(new Params(), NAME);

            this.careful = true;
            this.kmerRange = new KmerRange();
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public boolean isCareful() {
            return careful;
        }

        public void setCareful(boolean careful) {
            this.careful = careful;
        }

        public KmerRange getKmerRange() {
            return kmerRange;
        }

        public void setKmerRange(KmerRange kmerRange) {
            this.kmerRange = kmerRange;
        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            if (this.outputDir != null) {
                pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
            }

            if (this.careful) {
                pvp.put(params.getCareful(), Boolean.toString(this.careful));
            }

            pvp.put(params.getThreads(), Integer.toString(this.getThreads()));

            if (this.maxMemUsageMB > 0) {
                pvp.put(params.getMemoryGB(), Integer.toString(this.getMaxMemUsageMB() / 1000));
            }

            if (this.kmerRange != null && !this.kmerRange.isEmpty()) {
                pvp.put(params.getkList(), StringUtils.join(this.kmerRange, ","));
            }

            if (this.getLibs() != null && !this.getLibs().isEmpty()) {
                pvp.put(params.getInput(), new InputLibsArg(this.getLibs()).toString());
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String val) {

            Params params = this.getParams();

            if (param.equals(params.getOutputDir())) {
                this.outputDir = new File(val);
            }
            else if (param.equals(params.getCareful())) {
                this.careful = Boolean.parseBoolean(val);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(val);
            }
            else if (param.equals(params.getMemoryGB())) {
                this.maxMemUsageMB = Integer.parseInt(val) * 1000;
            }
            else if (param.equals(params.getkList())) {
                this.kmerRange = new KmerRange(val);
            }
            /*else if (param.equals(params.getLibs())) {
                this.setLibs(InputLibsArg.parse(val).getLibs());
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }*/
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public void setFromArgMap(ParamMap pvp) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public GenericDeBruijnOptimiserArgs getDeBruijnOptimiserArgs() {

            GenericDeBruijnOptimiserArgs args = new GenericDeBruijnOptimiserArgs();

            args.setOutputDir(this.outputDir);
            args.setLibraries(this.libs);
            args.setThreads(this.threads);
            args.setMemory(this.maxMemUsageMB);
            args.setKmerRange(this.kmerRange);

            return args;
        }

        @Override
        public void setDeBruijnOptimiserArgs(GenericDeBruijnOptimiserArgs args) {

            this.outputDir = args.getOutputDir();
            this.libs = args.getLibraries();
            this.threads = args.getThreads();
            this.maxMemUsageMB = args.getMemory();
            this.kmerRange = args.getKmerRange();
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter outputDir;
        private ConanParameter careful;
        private ConanParameter input;
        private ConanParameter threads;
        private ConanParameter memoryGB;
        private ConanParameter kList;

        public Params() {

            this.outputDir = new ParameterBuilder()
                    .shortName("o")
                    .description("Specify the output directory. Required option.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.careful = new ParameterBuilder()
                    .longName("careful")
                    .description("Tries to reduce the number of mismatches and short indels. Also runs MismatchCorrector – a post processing tool, which uses BWA tool (comes with SPAdes). This option is recommended.")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.input = new ParameterBuilder()
                    .description("Input for SPADES.  See documentation for more details.")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .longName("threads")
                    .description("Number of threads. The default value is 16.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.memoryGB = new ParameterBuilder()
                    .shortName("m")
                    .longName("memory")
                    .description("Set memory limit in Gb. SPAdes terminates if it reaches this limit. The default value is 250 Gb. Actual amount of consumed RAM will be below this limit. Make sure this value is correct for the given machine. SPAdes uses the limit value to automatically determine the sizes of various buffers, etc.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.kList = new ParameterBuilder()
                    .shortName("k")
                    .description("Comma-separated list of k-mer sizes to be used (all values must be odd, less than 128 and listed in ascending order).")
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getCareful() {
            return careful;
        }

        public ConanParameter getInput() {
            return input;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getMemoryGB() {
            return memoryGB;
        }

        public ConanParameter getkList() {
            return kList;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.outputDir,
                    this.careful,
                    this.input,
                    this.threads,
                    this.memoryGB,
                    this.kList
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


        public String getOrienatation(Library lib) {
            if (lib.getSeqOrientation() == Library.SeqOrientation.FORWARD_FORWARD) {
                return "ff";
            }
            else if (lib.getSeqOrientation() == Library.SeqOrientation.FORWARD_FORWARD) {
                return "fr";
            }
            else if (lib.getSeqOrientation() == Library.SeqOrientation.REVERSE_FORWARD) {
                return "rf";
            }
            else {
                throw new UnsupportedOperationException("SPADES does not supported sequence orientation: " + lib.getSeqOrientation());
            }
        }


        @Override
        public String toString() {

            List<String> libStrings = new ArrayList<>();

            int index = 1;
            int nbSingleEndLibs = 0;
            for (Library lib : this.libs) {
                if (lib.getType() == Library.Type.PAIRED_END || lib.getType() == Library.Type.OVERLAPPING_PAIRED_END) {
                    final String peStart = "--pe" + index;
                    libStrings.add(
                            peStart + "-1 " + lib.getFile1().getAbsolutePath() + " " +
                                    peStart + "-2 " + lib.getFile2().getAbsolutePath() + " " +
                                    peStart + this.getOrienatation(lib));
                }
                else if (lib.getType() == Library.Type.MATE_PAIR) {
                    final String mpStart = "--mp" + index;
                    libStrings.add(
                            mpStart + "-1 " + lib.getFile1().getAbsolutePath() + " " +
                                    mpStart + "-2 " + lib.getFile2().getAbsolutePath() + " " +
                                    mpStart + this.getOrienatation(lib));
                }
                else if (lib.getType() == Library.Type.SINGLE_END) {

                    if (nbSingleEndLibs >= 1) {
                        throw new UnsupportedOperationException("Cannot use more than one single end library for SPADES: " + lib.getName());
                    }

                    libStrings.add("-s " + lib.getFile1().getAbsolutePath());
                    nbSingleEndLibs++;
                }
                else {
                    throw new IllegalArgumentException("Unknown library type detected \"" + lib.getType() + "\" for: " + lib.getName());
                }
                index++;
            }

            return StringUtils.join(libStrings, " ").trim();
        }

    }

}
