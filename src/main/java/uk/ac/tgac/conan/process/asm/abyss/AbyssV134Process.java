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
package uk.ac.tgac.conan.process.asm.abyss;

import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asm.AbstractAssembler;
import uk.ac.tgac.conan.process.asm.AbstractAssemblerArgs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * User: maplesod
 * Date: 07/01/13
 * Time: 12:12
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.AssemblerCreator.class)
public class AbyssV134Process extends AbstractAssembler {

    private static Logger log = LoggerFactory.getLogger(AbyssV134Process.class);

    public static final String EXE = "abyss-pe";
    public static final String NAME = "Abyss_V1.3.4";

    public AbyssV134Process() {
        this(new AbyssV134Args());
    }

    public AbyssV134Process(AbstractAssemblerArgs args) {
        super(EXE, args, new AbyssV134Params());
    }

    @Override
    public AbstractAssemblerArgs getArgs() {
        return (AbstractAssemblerArgs) this.getProcessArgs();
    }


    /**
     * Abyss only produces contigs if at least one paired end library is provided
     * @return
     */
    @Override
    public boolean makesContigs() {
        return containsPairedEndLib();
    }

    /**
     * Abyss only produces scaffolds if at least one paired end library is provided
     * @return
     */
    @Override
    public boolean makesScaffolds() {
        return containsPairedEndLib();
    }

    protected boolean containsPairedEndLib() {
        for(Library lib : this.getArgs().getLibraries()) {
            if (lib.isPairedEnd()) {
                return true;
            }
        }

        return false;
    }

    public File findUnitigsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-unitigs.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    public File findContigsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-contigs.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    public File findScaffoldsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-scaffolds.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    @Override
    public File getUnitigsFile() {

        AbyssV134Args abyssV134Args = (AbyssV134Args)this.getArgs();
        File unitigsFile = new File(abyssV134Args.getOutputDir(), abyssV134Args.getOutputName() + "-unitigs.fa");
        return unitigsFile;
    }

    @Override
    public File getContigsFile() {

        AbyssV134Args abyssV134Args = (AbyssV134Args)this.getArgs();
        File unitigsFile = new File(abyssV134Args.getOutputDir(), abyssV134Args.getOutputName() + "-contigs.fa");
        return unitigsFile;
    }

    @Override
    public File getScaffoldsFile() {

        AbyssV134Args abyssV134Args = (AbyssV134Args)this.getArgs();
        File unitigsFile = new File(abyssV134Args.getOutputDir(), abyssV134Args.getOutputName() + "-scaffolds.fa");
        return unitigsFile;
    }

    @Override
    public boolean usesOpenMpi() {
        return true;
    }

    @Override
    public void initialise() throws IOException {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @Override
    public String getCommand() throws ConanParameterException {
        return super.getCommand(CommandLineFormat.KEY_VALUE_PAIR);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AbstractAssembler create(AbstractAssemblerArgs args, ConanProcessService conanProcessService) {

        AbyssV134Process proc = new AbyssV134Process(args);
        proc.setConanProcessService(conanProcessService);

        return proc;
    }
}
