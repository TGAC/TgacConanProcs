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
package uk.ac.tgac.conan.process.asm.soapdenovo;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.process.asm.Assembler;
import uk.ac.tgac.conan.process.asm.AssemblerArgs;

import java.io.File;
import java.io.IOException;

/**
 * User: maplesod
 * Date: 16/04/13
 * Time: 15:57
 */
public class SoapDeNovoV204Process extends AbstractConanProcess implements Assembler {

    public static final String EXE = "soap all";

    public SoapDeNovoV204Process() {
        this(new SoapDeNovoV204Args());
    }

    public SoapDeNovoV204Process(AssemblerArgs args) {
        super(EXE, args, new SoapDeNovoV204Params());

        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 1);

        this.addPreCommand("cd " + ((SoapDeNovoV204Args)this.getProcessArgs()).getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    protected SoapDeNovoV204Args getSoapDeNovoArgs() {
        return (SoapDeNovoV204Args)this.getArgs();
    }


    @Override
    public AssemblerArgs getArgs() {
        return null;
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
        return true;
    }

    @Override
    public File getUnitigsFile() {
        return null;
    }

    @Override
    public File getContigsFile() {
        return new File(this.getArgs().getOutputDir(), this.getSoapDeNovoArgs().getOutputPrefix() + ".contig");
    }

    @Override
    public File getScaffoldsFile() {
        return new File(this.getArgs().getOutputDir(), this.getSoapDeNovoArgs().getOutputPrefix() + ".scafSeq");
    }

    @Override
    public boolean usesOpenMpi() {
        return false;
    }

    @Override
    public String getCommand() {
        return this.getCommand(this.getProcessArgs(), true, "-", " ");
    }

    @Override
    public String getName() {
        return "SoapDeNovo_V2.04";
    }

    @Override
    public boolean execute(ExecutionContext executionContext) throws ProcessExecutionException, InterruptedException {

        SoapDeNovoV204Args args = this.getSoapDeNovoArgs();

        // Create the SOAP lib configuration file from the library list
        try {
            if (args.getConfigFile() == null) {

                args.setConfigFile(new File(args.getOutputDir(), "soap.libs"));
            }

            args.createLibraryConfigFile(args.getLibraries(), args.getConfigFile());
        }
        catch(IOException ioe) {
            throw new ProcessExecutionException(-1, ioe);
        }

        ExecutionContext executionContextCopy = executionContext.copy();

        if (executionContextCopy.usingScheduler()) {
            executionContextCopy.getScheduler().getArgs().setMonitorFile(new File(args.getOutputDir(), args.getOutputPrefix() + ".scheduler.log"));
        }

        return super.execute(executionContextCopy);
    }
}
