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
package uk.ac.tgac.conan.process.ec.musket;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.tgac.conan.process.ec.ErrorCorrector;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorArgs;
import uk.ac.tgac.conan.process.ec.quake.QuakeV034Args;
import uk.ac.tgac.conan.process.ec.quake.QuakeV034Params;

import java.io.File;

/**
 * User: maplesod
 * Date: 02/05/13
 * Time: 15:31
 */
public class MusketV106Process extends AbstractConanProcess implements ErrorCorrector {

    public MusketV106Process() {
        this(new MusketV106Args());
    }

    public MusketV106Process(ErrorCorrectorArgs args) {
        super("musket", args, new MusketV106Params());
    }

    @Override
    public ErrorCorrectorArgs getArgs() {
        return (ErrorCorrectorArgs) this.getProcessArgs();
    }

    @Override
    public void configure(ConanProcessService conanProcessService) {
        this.conanProcessService = conanProcessService;
    }

    @Override
    public void initialise() {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @Override
    public String getCommand() {

        String command = this.getCommand(this.getProcessArgs(), true, "-", " ");

        // Musket doesn't expect a param name for the input files, so remove that if present.
        String modCommand = command.replaceAll("-input", "");

        return modCommand;
    }

    @Override
    public String getName() {
        return "Musket_V1.0.6";
    }
}
