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

import org.apache.commons.lang.StringUtils;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:30
 */
public class CegmaV2_4Args implements ProcessArgs {

    private CegmaV2_4Params params = new CegmaV2_4Params();

    private File genomeFile;
    private File modifiedGenomeFile;
    private File outputPrefix;
    private int threads;

    public CegmaV2_4Args() {
        this.genomeFile = null;
        this.modifiedGenomeFile = null;
        this.outputPrefix = null;
        this.threads = 1;
    }

    public File getGenomeFile() {
        return genomeFile;
    }

    public void setGenomeFile(File genomeFile) {
        this.genomeFile = genomeFile;
    }

    public File getModifiedGenomeFile() {
        return modifiedGenomeFile;
    }

    public void setModifiedGenomeFile(File modifiedGenomeFile) {
        this.modifiedGenomeFile = modifiedGenomeFile;
    }

    public File getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(File outputPrefix) {
        this.outputPrefix = outputPrefix;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }


    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<ConanParameter, String> getArgMap() {

        Map<ConanParameter, String> pvp = new LinkedHashMap<ConanParameter, String>();

        if (this.genomeFile != null) {
            pvp.put(params.getGenomeFile(), this.genomeFile.getAbsolutePath());
        }

        if (this.outputPrefix != null) {
            pvp.put(params.getOutputPrefix(), this.outputPrefix.getAbsolutePath());
        }

        pvp.put(params.getThreads(), Integer.toString(this.threads));

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

            if (param.equals(this.params.getGenomeFile().getName())) {
                this.genomeFile = new File(val);
            }
            else if (param.equals(this.params.getOutputPrefix().getName())) {
                this.outputPrefix = new File(val);
            }
            else if (param.equals(this.params.getThreads().getName())) {
                this.threads = Integer.parseInt(val);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

}
