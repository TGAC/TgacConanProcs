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
package uk.ac.tgac.conan.process.asmIO.dedup.nizar;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOArgs;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 15/03/13
 * Time: 11:46
 */
@MetaInfServices(uk.ac.tgac.conan.process.asmIO.AssemblyIOArgsCreator.class)
public class NizarDedupArgs extends AbstractAssemblyIOArgs {

    public NizarDedupArgs() {
        super();
    }

    @Override
    public File getOutputFile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<ConanParameter, String> getArgMap() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setFromArgMap(Map<ConanParameter, String> pvp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AbstractAssemblyIOArgs create(File inputFile, File outputDir, String outputPrefix, List<Library> libs,
                                         int threads, int memory, String otherArgs) {

        NizarDedupArgs newArgs = new NizarDedupArgs();
        newArgs.setInputFile(inputFile);
        newArgs.setOutputDir(outputDir);
        newArgs.setOutputPrefix(outputPrefix);
        newArgs.setLibraries(libs);
        newArgs.setThreads(threads);
        newArgs.setMemory(memory);
        newArgs.parse(otherArgs);

        return newArgs;
    }

    @Override
    public String getName() {
        return NizarDedupProcess.NAME;
    }

    @Override
    public String getAssemblyIOProcessType() {
        return NizarDedupProcess.TYPE;
    }
}
