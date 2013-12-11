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
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.tgac.conan.process.asm.AbstractAssembler;
import uk.ac.tgac.conan.process.asm.AbstractAssemblerArgs;

import java.io.File;

/**
 * User: maplesod
 * Date: 13/03/13
 * Time: 15:49
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.AssemblerCreator.class)
public class VelvetV11Process extends AbstractAssembler {

    public static final String NAME = "Velvet_V1.1";

    protected VelvetV11Process(String executable, AbstractAssemblerArgs args, ProcessParams params) {
        super(executable, args, params);
    }

    @Override
    public File getUnitigsFile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File getContigsFile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File getScaffoldsFile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AbstractAssembler create(AbstractAssemblerArgs args, ConanProcessService conanProcessService) {
        throw new UnsupportedOperationException("Velvet wrapper is not implemented yet");
    }

    @Override
    public String getCommand() {
        throw new UnsupportedOperationException("Velvet wrapper is not implemented yet");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
