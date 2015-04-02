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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * User: maplesod
 * Date: 07/01/13
 * Time: 12:12
 */
@MetaInfServices(Assembler.class)
public class DiscovarV51XXX extends AbstractAssembler {

    public static final String EXE = "DiscovarExp";
    public static final String NAME = "Discovar_V51XXX";

    public DiscovarV51XXX() {
        this(null);
    }

    public DiscovarV51XXX(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public DiscovarV51XXX(ConanExecutorService ces, AbstractProcessArgs args) {
        super(NAME, EXE, args, new Params(), ces);

        this.setFormat(CommandLineFormat.KEY_VALUE_PAIR);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public boolean makesUnitigs() {
        return true;
    }


    /**
     * Abyss only produces contigs if at least one paired end library is provided
     * @return
     */
    @Override
    public boolean makesContigs() {
        return false;
    }

    /**
     * Abyss only produces scaffolds if at least one paired end library is provided
     * @return
     */
    @Override
    public boolean makesScaffolds() {
        return true;
    }

    @Override
    public boolean makesBubbles() {
        return false;
    }


    @Override
    public File getUnitigsFile() {
        return new File(this.getArgs().getOutputDir(), "a.final/a.fasta");
    }

    @Override
    public File getContigsFile() {
        return null;
    }

    @Override
    public File getScaffoldsFile() {
        return new File(this.getArgs().getOutputDir(), "a.final/a.lines.fasta");
    }

    @Override
    public File getBubbleFile() {
        return null;
    }

    @Override
    public File getBestAssembly() {
        return this.getScaffoldsFile();
    }

    @Override
    public boolean usesOpenMpi() {
        return true;
    }

    @Override
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN_AUTO;
    }

    /**
     * Discovar can only process 250bp PCR free libraries.
     * @param libraries
     * @return
     */
    @Override
    public boolean acceptsLibraries(List<Library> libraries) {

        for(Library lib : libraries) {
            if (lib.getType() != Library.Type.PE || lib.getReadLength() != 250) {
                return false;
            }
        }

        return true;
    }



    @MetaInfServices(DeBruijnAutoArgs.class)
    public static class Args extends AbstractAssemblerArgs implements DeBruijnAutoArgs {

        private File[] reads;
        private File refHead;

        public Args() {
            super(new Params(), NAME);

            this.reads = null;
            this.refHead = null;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File[] getReads() {
            return reads;
        }

        public void setReads(File[] reads) {
            this.reads = reads;
        }

        public File getRefHead() {
            return refHead;
        }

        public void setRefHead(File refHead) {
            this.refHead = refHead;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();

            this.refHead = cmdLine.hasOption(params.getRefHead().getLongName()) ?
                    new File(cmdLine.getOptionValue(params.getRefHead().getLongName())) :
                    this.refHead;

        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            if (this.libs != null && !this.libs.isEmpty()) {

                StringJoiner sb = new StringJoiner(" :: ");
                for(Library l : this.libs) {
                    StringJoiner sb2 = new StringJoiner(",");

                    sb2.add(l.getFile1().getAbsolutePath());

                    if (l.getFile2() != null) {
                        sb2.add(l.getFile2());
                    }

                    sb.add(sb2.toString());
                }

                pvp.put(params.getReads(), sb.toString());
            }
            else if (this.reads != null) {

                StringJoiner sj = new StringJoiner(",");

                for(File f : this.reads) {
                    sj.add(f.getAbsolutePath());
                }

                pvp.put(params.getReads(), sj.toString());
            }

            if (this.threads > 0) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.refHead != null) {
                pvp.put(params.getRefHead(), this.refHead.getAbsolutePath());
            }

            if (this.maxMemUsageMB > 0) {
                pvp.put(params.getMaxMemGb(), Integer.toString(this.maxMemUsageMB / 1000));
            }

            if (this.outputDir != null) {
                pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String val) {

            Params params = this.getParams();

            if (param.equals(params.getRefHead())) {
                this.refHead = new File(val);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public GenericDeBruijnAutoArgs getDeBruijnAutoArgs() {
            GenericDeBruijnAutoArgs args = new GenericDeBruijnAutoArgs();

            args.setOutputDir(this.getOutputDir());
            args.setLibraries(this.getLibs());
            args.setThreads(this.getThreads());
            args.setMemory(this.getMaxMemUsageMB());
            args.setEstimatedWalltimeMins(this.getEstimatedWalltimeMins());
            //args.setOrganism(this.organism);

            return args;
        }

        @Override
        public void setDeBruijnAutoArgs(GenericDeBruijnAutoArgs args) {

            this.outputDir = args.getOutputDir();
            this.libs = args.getLibraries();
            this.threads = args.getThreads();
            this.maxMemUsageMB = args.getMemory();
            this.estimatedWalltimeMins = args.getEstimatedWalltimeMins();
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter reads;
        private ConanParameter outputDir;
        private ConanParameter threads;
        private ConanParameter refHead;
        private ConanParameter maxMemGb;

        public Params() {

            this.reads = new ParameterBuilder()
                    .longName("READS")
                    .description("Comma-separated list of input files, see manual for details")
                    .isOptional(false)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.outputDir = new ParameterBuilder()
                    .longName("OUT_DIR")
                    .description("name of output directory")
                    .argValidator(ArgValidator.PATH)
                    .isOptional(false)
                    .create();

            this.threads = new ParameterBuilder()
                    .longName("NUM_THREADS")
                    .description("Number of threads. By default, the number of processors online.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.refHead = new ParameterBuilder()
                    .longName("REFHEAD")
                    .description("use reference sequence REFHEAD.fasta to annotate assembly, and also REFHEAD.names if it exists")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.maxMemGb = new ParameterBuilder()
                    .longName("MAX_MEM_GB")
                    .description("if specified, maximum allowed RAM use in GB; in some cases may be exceeded by our code")
                    .argValidator(ArgValidator.DIGITS)
                    .create();


        }

        public ConanParameter getReads() {
            return reads;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getRefHead() {
            return refHead;
        }

        public ConanParameter getMaxMemGb() {
            return maxMemGb;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.reads,
                    this.outputDir,
                    this.threads,
                    this.refHead,
                    this.maxMemGb
            };
        }

    }

}
