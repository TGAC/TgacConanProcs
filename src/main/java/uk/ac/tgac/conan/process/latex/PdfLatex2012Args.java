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
package uk.ac.tgac.conan.process.latex;

import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;
import java.util.Map;

/**
 * User: maplesod
 * Date: 08/03/13
 * Time: 10:25
 */
public class PdfLatex2012Args implements ProcessArgs {

    private PdfLatex2012Params params = new PdfLatex2012Params();

    private File texFile;
    private File outputDir;

    public PdfLatex2012Args() {
        this.texFile = null;
        this.outputDir = null;
    }

    public File getTexFile() {
        return texFile;
    }

    public void setTexFile(File texFile) {
        this.texFile = texFile;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ParamMap getArgMap() {
        ParamMap pvp = new DefaultParamMap();

        if (this.texFile != null) {
            pvp.put(params.getTexFile(), this.texFile.getAbsolutePath());
        }

        if (this.outputDir != null) {
            pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
        }

        return pvp;
    }

    @Override
    public void setFromArgMap(ParamMap pvp) {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            ConanParameter param = entry.getKey();

            if (param.equals(this.params.getTexFile())) {
                this.texFile = new File(entry.getValue());
            } else if (param.equals(this.params.getOutputDir())) {
                this.outputDir = new File(entry.getValue());
            }else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }
}
