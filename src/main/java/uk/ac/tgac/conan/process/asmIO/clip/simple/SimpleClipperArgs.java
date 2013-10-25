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
package uk.ac.tgac.conan.process.asmIO.clip.simple;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOArgs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 01/02/13
 * Time: 09:58
 */
@MetaInfServices(uk.ac.tgac.conan.process.asmIO.AssemblyIOArgsCreator.class)
public class SimpleClipperArgs extends AbstractAssemblyIOArgs {

    private SimpleClipperParams params = new SimpleClipperParams();

    private File outputFile;
    private int minLen;


    public SimpleClipperArgs() {
        super();
    }

    @Override
    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public int getMinLen() {
        return minLen;
    }

    public void setMinLen(int minLen) {
        this.minLen = minLen;
    }

    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<ConanParameter, String> getArgMap() {
        Map<ConanParameter, String> pvp = new HashMap<ConanParameter, String>();

        if (this.getInputFile() != null)
            pvp.put(params.getInputFile(), this.getInputFile().getAbsolutePath());

        if (this.getOutputFile() != null)
            pvp.put(params.getOutputFile(), this.getOutputFile().getAbsolutePath());

        if (this.getMinLen() > 1)
            pvp.put(params.getMinLen(), String.valueOf(this.getMinLen()));

        return pvp;
    }

    @Override
    public void setFromArgMap(Map<ConanParameter, String> pvp) {

        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            String param = entry.getKey().getName();

            if (param.equals(this.params.getInputFile().getName())) {
                this.setInputFile(new File(entry.getValue()));
            } else if (param.equals(this.params.getOutputFile().getName())) {
                this.setOutputFile(new File(entry.getValue()));
            } else if (param.equals(this.params.getMinLen().getName())) {
                this.setMinLen(Integer.parseInt(entry.getValue()));
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    @Override
    public AbstractAssemblyIOArgs create(File inputFile, File outputDir, String outputPrefix, List<Library> libs,
                                         int threads, int memory, String otherArgs) {

        SimpleClipperArgs newArgs = new SimpleClipperArgs();
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
        return SimpleClipperProcess.NAME;
    }

    @Override
    public String getAssemblyIOProcessType() {
        return SimpleClipperProcess.TYPE;
    }
}
