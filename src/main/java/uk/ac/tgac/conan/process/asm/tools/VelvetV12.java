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

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.util.List;

/**
 * User: maplesod
 * Date: 13/03/13
 * Time: 15:49
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class VelvetV12 extends AbstractAssembler {

    public static final String NAME = "Velvet_V1.2";
    public static final String EXE = "velveth-127";

    public VelvetV12() {
        this(null);
    }
    public VelvetV12(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }
    public VelvetV12(ConanExecutorService conanExecutorService, AbstractProcessArgs args) {
        super(NAME, EXE, args, new Params(), conanExecutorService);
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
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN;
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

    @Override
    public String getCommand() {

        Args args = this.getArgs();

        StringBuilder libString = new StringBuilder();

        for(int i = 0; i < args.getLibs().size(); i++) {
            libString.append(this.createLibString(args.getLibs().get(i), i == 0)).append(" ");
        }

        final String velvetHCmd = "velveth-127 " + args.getOutputDir().getAbsolutePath() + " " + args.getK() + " -create_binary " + libString.toString();

        StringBuilder insString = new StringBuilder();

        for(int i = 0; i < args.getLibs().size(); i++) {
            insString.append("-ins_length" + (i != 0 ? i : "") + " " + args.getLibs().get(i).getAverageInsertSize()).append(" ");
        }

        final String velvetGCmd = "velvetg-127 " + args.getOutputDir().getAbsolutePath() + " -cov_cutoff auto " + insString.toString() + " -exp_cov auto";

        return velvetHCmd + "; " + velvetGCmd;
    }

    @MetaInfServices(AssemblerArgs.class)
    public static class Args extends AbstractProcessArgs implements DeBruijnArgs {

        public static final int DEFAULT_K = 61;
        public static final int DEFAULT_COVERAGE_CUTOFF = 0;
        public static final int DEFAULT_THREADS = 1;

        private File outputDir;
        private List<Library> libs;
        private int k;
        private int coverageCutoff;
        private int threads;

        public Args() {
            super(new Params());

            this.outputDir = new File("");
            this.libs = null;
            this.k = DEFAULT_K;
            this.coverageCutoff = DEFAULT_COVERAGE_CUTOFF;
            this.threads = DEFAULT_THREADS;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public List<Library> getLibs() {
            return libs;
        }

        public void setLibs(List<Library> libs) {
            this.libs = libs;
        }

        public int getK() {
            return k;
        }

        public void setK(int k) {
            this.k = k;
        }

        public int getCoverageCutoff() {
            return coverageCutoff;
        }

        public void setCoverageCutoff(int coverageCutoff) {
            this.coverageCutoff = coverageCutoff;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        @Override
        public void parse(String args) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            return null;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String val) {

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public GenericDeBruijnArgs getDeBruijnArgs() {

            GenericDeBruijnArgs args = new GenericDeBruijnArgs();

            args.setCoverageCutoff(this.coverageCutoff);
            args.setK(this.k);
            args.setThreads(this.threads);
            args.setLibraries(this.libs);
            args.setOutputDir(this.outputDir);

            return args;
        }

        @Override
        public void setDeBruijnArgs(GenericDeBruijnArgs args) {

            this.outputDir = args.getOutputDir();
            this.libs = args.getLibraries();
            this.k = args.getK();
            this.coverageCutoff = args.getCoverageCutoff();
            this.threads = args.getThreads();
        }

        @Override
        public String getProcessName() {
            return NAME;
        }

        @Override
        public AbstractProcessArgs toConanArgs() {
            return this;
        }
    }

    public static class Params extends AbstractProcessParams {

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[0];
        }
    }
}
