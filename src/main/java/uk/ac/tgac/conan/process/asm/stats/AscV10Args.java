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
package uk.ac.tgac.conan.process.asm.stats;

import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:30
 */
public class AscV10Args implements ProcessArgs {

    private AscV10Params params = new AscV10Params();

    private File input;
    private File output;
    private String mode;

    public AscV10Args() {
        this.input = null;
        this.output = null;
        this.mode = "FULL";
    }

    public File getInput() {
        return input;
    }

    public void setInput(File input) {
        this.input = input;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<ConanParameter, String> getArgMap() {

        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();

        if (this.input != null) {
            pvp.put(params.getInputDir(), this.input.getAbsolutePath());
        }

        if (this.output != null) {
            pvp.put(params.getOutputDir(), this.output.getAbsolutePath());
        }

        if (this.mode != null) {
            pvp.put(params.getMode(), this.mode);
        }

        return pvp;
    }

    @Override
    public void setFromArgMap(Map<ConanParameter, String> pvp) {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();
            String val = entry.getValue();

            if (param.equals(this.params.getInputDir().getName())) {
                this.input = new File(val);
            }
            else if (param.equals(this.params.getOutputDir().getName())) {
                this.output = new File(val);
            }
            else if (param.equals(this.params.getMode().getName())) {
                this.mode = val;
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }
}
