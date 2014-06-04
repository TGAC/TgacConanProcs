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
package uk.ac.tgac.conan.process.asm.velvet;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.AbstractAssembler;
import uk.ac.tgac.conan.process.asm.AbstractAssemblerArgs;
import uk.ac.tgac.conan.process.asm.abyss.AbyssV134Args;
import uk.ac.tgac.conan.process.asm.abyss.AbyssV134Params;

import java.io.File;

/**
 * User: maplesod
 * Date: 13/03/13
 * Time: 15:49
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.AssemblerCreator.class)
public class VelvetV12Process extends AbstractAssembler {

    public static final String NAME = "Velvet_V1.2";

    public VelvetV12Process() {
        this(new VelvetV12Args());
    }
    public VelvetV12Process(AbstractAssemblerArgs args) {
        super("velveth-127", args, new VelvetV12Params());
    }

    protected VelvetV12Process(String executable, AbstractAssemblerArgs args, ProcessParams params) {
        super(executable, args, params);
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
    public AbstractAssembler create(AbstractAssemblerArgs args, ConanProcessService conanProcessService) {
        VelvetV12Process proc = new VelvetV12Process(args);
        proc.setConanProcessService(conanProcessService);

        return proc;
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

        VelvetV12Args args = (VelvetV12Args)this.getArgs();

        StringBuilder libString = new StringBuilder();

        for(int i = 0; i < args.getLibraries().size(); i++) {
            libString.append(this.createLibString(args.getLibraries().get(i), i == 0)).append(" ");
        }

        final String velvetHCmd = "velveth-127 " + args.getOutputDir().getAbsolutePath() + " " + args.getKmer() + " -create_binary " + libString.toString();

        StringBuilder insString = new StringBuilder();

        for(int i = 0; i < args.getLibraries().size(); i++) {
            insString.append("-ins_length" + (i != 0 ? i : "") + " " + args.getLibraries().get(i).getAverageInsertSize()).append(" ");
        }

        final String velvetGCmd = "velvetg-127 " + args.getOutputDir().getAbsolutePath() + " -cov_cutoff auto " + insString.toString() + " -exp_cov auto";

        return velvetHCmd + "; " + velvetGCmd;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
