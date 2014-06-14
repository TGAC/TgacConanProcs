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
package uk.ac.tgac.conan.process.asmIO.gapclose;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.process.asmIO.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by maplesod on 14/06/14.
 */
@MetaInfServices(AssemblyEnhancer.class)
public class PlatanusGapCloseV12 extends AbstractAssemblyEnhancer {

    public static final String NAME = "Platanus_Assemble_V1.2";
    public static final String EXE = "platanus";
    public static final String MODE = "scaffold";
    public static final AssemblyEnhancerType TYPE = AssemblyEnhancerType.SCAFFOLDER;

    public PlatanusGapCloseV12() {
        this(null);
    }

    public PlatanusGapCloseV12(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public PlatanusGapCloseV12(ConanExecutorService ces, Args args) {
        super(NAME, TYPE, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @MetaInfServices(AssemblyEnhancerArgs.class)
    public static class Args extends AbstractAssemblyEnhancerArgs {

        public Args() {

            super(new Params(), NAME, TYPE);

        }

        protected Params getParams() {
            return (Params) this.params;
        }

        @Override
        public File getOutputFile() {
            return new File(this.getOutputDir(), this.getOutputPrefix() + "_scaffold.fa");
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public void parse(String args) throws IOException {

        }

        @Override
        public ParamMap getArgMap() {
            return null;
        }
    }

    public static class Params extends AbstractProcessParams {

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[0];
        }
    }
}