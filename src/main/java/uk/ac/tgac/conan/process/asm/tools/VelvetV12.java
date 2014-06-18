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
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.Organism;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.IOException;
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
    public VelvetV12(ConanExecutorService conanExecutorService, AbstractAssemblerArgs args) {
        super(NAME, EXE, args, new Params(), conanExecutorService);
    }

    public Args getArgs() {
        return (Args)this.getAssemblerArgs();
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
    public AssemblerType getType() {
        return AssemblerType.DE_BRUIJN;
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

        for(int i = 0; i < args.getLibraries().size(); i++) {
            libString.append(this.createLibString(args.getLibraries().get(i), i == 0)).append(" ");
        }

        final String velvetHCmd = "velveth-127 " + args.getOutputDir().getAbsolutePath() + " " + args.getK() + " -create_binary " + libString.toString();

        StringBuilder insString = new StringBuilder();

        for(int i = 0; i < args.getLibraries().size(); i++) {
            insString.append("-ins_length" + (i != 0 ? i : "") + " " + args.getLibraries().get(i).getAverageInsertSize()).append(" ");
        }

        final String velvetGCmd = "velvetg-127 " + args.getOutputDir().getAbsolutePath() + " -cov_cutoff auto " + insString.toString() + " -exp_cov auto";

        return velvetHCmd + "; " + velvetGCmd;
    }

    @MetaInfServices(AssemblerArgs.class)
    public static class Args extends DeBruijnAssemblerArgs {

        public Args() {
            super(new Params(), NAME);
        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public void setFromArgMap(ParamMap pvp) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter libs;
        private ConanParameter kmer;
        private ConanParameter coverageCutoff;

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[0];
        }

        @Override
        public List<ConanParameter> getConanParameters() {
            return null;
        }
    }
}