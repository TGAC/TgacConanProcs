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
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.Organism;
import uk.ac.tgac.conan.process.asm.AbstractAssemblerArgs;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: maplesod
 * Date: 13/03/13
 * Time: 15:49
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.AssemblerArgsCreator.class)
public class VelvetV11Args extends AbstractAssemblerArgs {

    private VelvetV11Params params = new VelvetV11Params();


    @Override
    public AbstractAssemblerArgs copy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AbstractAssemblerArgs create(int k, List<Library> libs, File outputDir, int threads, int memory, int coverage, Organism organism) {
        VelvetV11Args args = new VelvetV11Args();
        args.setKmer(k);
        args.setOutputDir(outputDir);
        args.setLibraries(libs);
        args.setThreads(threads);
        args.setMemory(memory);
        args.setDesiredCoverage(coverage);
        args.setOrganism(organism);
        return args;
    }

    @Override
    public String getName() {
        return VelvetV11Process.NAME;  //To change body of implemented methods use File | Settings | File Templates.
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
    public void setFromArgMap(ParamMap pvp) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
