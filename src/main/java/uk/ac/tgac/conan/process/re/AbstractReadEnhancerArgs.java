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
package uk.ac.tgac.conan.process.re;

import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;

public abstract class AbstractReadEnhancerArgs extends AbstractProcessArgs implements ReadEnhancerArgs {

    private String processName;
    protected int threads;
    protected int memoryGb;
    protected Library input;
    protected File outputDir;

    public AbstractReadEnhancerArgs(ProcessParams params, String processName) {

        super(params);

        this.processName = processName;
        this.threads = 8;
        this.memoryGb = 20;
        this.outputDir = new File("");
        this.input = null;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getMemoryGb() {
        return memoryGb;
    }

    public void setMemoryGb(int memoryGb) {
        this.memoryGb = memoryGb;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public Library getInput() {
        return input;
    }

    public void setInput(Library input) {
        this.input = input;
    }

    @Override
    public String getProcessName() {
        return processName;
    }

    @Override
    public AbstractProcessArgs toConanArgs() {
        return this;
    }

    @Override
    public GenericReadEnhancerArgs getReadEnhancerArgs() {

        GenericReadEnhancerArgs gArgs = new GenericReadEnhancerArgs();

        gArgs.setInput(this.input);
        gArgs.setMemoryGb(this.memoryGb);
        gArgs.setThreads(this.threads);
        gArgs.setOutputDir(this.outputDir);

        return gArgs;
    }

    @Override
    public void setReadEnhancerArgs(GenericReadEnhancerArgs args) {

        this.input = args.getInput();
        this.memoryGb = args.getMemoryGb();
        this.threads = args.getThreads();
        this.outputDir = args.getOutputDir();
    }

}
