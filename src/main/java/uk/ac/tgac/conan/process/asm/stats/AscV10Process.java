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

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;

import java.io.File;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:30
 */
public class AscV10Process extends AbstractConanProcess {


    private static File jarDir = new File(AscV10Process.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
    public static final String EXE = "java -jar " + jarDir.getAbsolutePath() + "/scripts/java/asc-1.0-SNAPSHOT.jar";

    public AscV10Process() {
        this(new AscV10Args());
    }

    public AscV10Process(AscV10Args args) {
        super(EXE, args, new AscV10Params());
    }

    @Override
    public String getCommand() {
        return this.getCommand(this.getProcessArgs(), true, "--", " ");
    }

    @Override
    public String getName() {
        return "ASC_V1.0";
    }
}
