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
package uk.ac.tgac.conan.process.asmIO;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;
import java.io.IOException;

/**
 * User: maplesod
 * Date: 26/03/13
 * Time: 17:42
 */
public abstract class AbstractAssemblyEnhancer extends AbstractConanProcess implements AssemblyEnhancer {

    private String name;
    private AssemblyEnhancerType type;

    protected AbstractAssemblyEnhancer(String name, AssemblyEnhancerType type, String executable,
                                       AbstractAssemblyEnhancerArgs args, ProcessParams params, ConanExecutorService ces) {
        super(executable, args, params, ces);
        this.name = name;
        this.type = type;
    }

    public AbstractAssemblyEnhancerArgs getAssemblyEnhancerArgs() {
        return (AbstractAssemblyEnhancerArgs)this.getProcessArgs();
    }

    @Override
    public void initialise(AssemblyEnhancerArgs args, ConanExecutorService ces) throws IOException {
        this.setProcessArgs(args);
        this.conanExecutorService = ces;
    }



    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Gets the type of assembly enhancer process this is
     * @return
     */
    @Override
    public AssemblyEnhancerType getAssemblyEnhancerType() {
        return this.type;
    }

    @Override
    public File getOutputDir() {
        return this.getAssemblyEnhancerArgs().getOutputDir();
    }

    @Override
    public File getOutputFile() {
        return this.getAssemblyEnhancerArgs().getOutputFile();
    }

    @Override
    public void setup() {
    }

}
