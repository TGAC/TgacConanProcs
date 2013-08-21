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
package uk.ac.tgac.conan.process.asm.allpaths;

import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.process.asm.AssemblerArgs;

import java.util.Map;

/**
 * User: maplesod
 * Date: 13/08/13
 * Time: 15:24
 */
public class AllpathsLgV44837Args extends AssemblerArgs {

    private AllpathsLgV44837Params params = new AllpathsLgV44837Params();



    @Override
    public AssemblerArgs copy() {
        AllpathsLgV44837Args copy = new AllpathsLgV44837Args();
        copy.setThreads(this.getThreads());
        copy.setDesiredCoverage(this.getDesiredCoverage());
        copy.setOrganism(this.getOrganism());
        copy.setLibraries(this.getLibraries());  // Not really copying this!!

        return copy;
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
}
