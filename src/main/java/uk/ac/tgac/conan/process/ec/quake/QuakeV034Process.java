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
package uk.ac.tgac.conan.process.ec.quake;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.tgac.conan.process.ec.ErrorCorrector;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorArgs;

import java.io.File;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 09:49
 */
public class QuakeV034Process extends AbstractConanProcess implements ErrorCorrector {

    public QuakeV034Process() {
        this(new QuakeV034Args());
    }

    public QuakeV034Process(ErrorCorrectorArgs args) {
        super("quake.py", args, new QuakeV034Params());
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
        String pwd = pwdFull.substring(0, pwdFull.length() - 1);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @Override
    public String getCommand() {
        return this.getCommand(this.getProcessArgs(), true, "-", " ");
    }

    @Override
    public String getName() {
        return "Quake_V0.3.4";
    }
}
