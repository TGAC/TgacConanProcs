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
package uk.ac.tgac.conan.process.asmIO.scaffold.sspace;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOArgs;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOProcess;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 16:00
 */
@MetaInfServices(uk.ac.tgac.conan.process.asmIO.AssemblyIOCreator.class)
public class SSpaceBasicV2Process extends AbstractAssemblyIOProcess {

    public static final String EXE = "SSPACE_Basic_v2.0.pl";
    public static final String NAME = "SSPACE_Basic_v2.0";
    public static final String TYPE = "scaffolder";

    public SSpaceBasicV2Process() {
        this(new SSpaceBasicV2Args());
    }

    public SSpaceBasicV2Process(SSpaceBasicV2Args args) {
        super(EXE, args, new SSpaceBasicV2Params());
    }

    @Override
    public void initialise() {

        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + ((SSpaceBasicV2Args)this.getProcessArgs()).getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @Override
    public String getCommand() {
        return this.getCommand(this.getProcessArgs(), true, "-", " ");
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getAssemblyIOProcessType() {
        return TYPE;
    }

    @Override
    public Collection<ConanParameter> getParameters() {
        return new SSpaceBasicV2Params().getConanParameters();
    }

    @Override
    public boolean execute(ExecutionContext executionContext) throws ProcessExecutionException, InterruptedException {

        SSpaceBasicV2Args args = (SSpaceBasicV2Args)this.getProcessArgs();

        // Create the SSPACE lib configuration file from the library list
        try {
            if (args.getLibraryConfigFile() == null) {

                args.setLibraryConfigFile(new File(args.getOutputDir(), "sspace.libs"));
            }

            args.createLibraryConfigFile(args.getLibraries(), args.getLibraryConfigFile());
        }
        catch(IOException ioe) {
            throw new ProcessExecutionException(-1, ioe);
        }

        ExecutionContext executionContextCopy = executionContext.copy();

        if (executionContextCopy.usingScheduler()) {
            executionContextCopy.getScheduler().getArgs().setMonitorFile(new File(args.getOutputDir(), args.getOutputFile().getName() + ".scheduler.log"));
        }

        return super.execute(executionContextCopy);
    }

    @Override
    public AbstractAssemblyIOProcess create(AbstractAssemblyIOArgs args) {
        return new SSpaceBasicV2Process((SSpaceBasicV2Args)args);
    }
}
