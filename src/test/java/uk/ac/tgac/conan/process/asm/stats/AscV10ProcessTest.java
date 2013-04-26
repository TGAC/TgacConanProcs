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

import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:43
 */
public class AscV10ProcessTest {

    private String pwd;
    private String jarPath;

    @Before
    public void setup() {
        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 1);

        this.jarPath = new File(AscV10ProcessTest.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getAbsolutePath();
    }

    @Test
    public void testCommand() {

        AscV10Args args = new AscV10Args();
        args.setInputDir(new File("input"));
        args.setOutputDir(new File("output"));
        args.setPlot(true);

        AscV10Process asc = new AscV10Process(args);

        String command = asc.getCommand();

        /*String correctCommand = "java -jar " + jarPath + "scripts/java/asc-1.0-SNAPSHOT.jar --input " + pwd + "input --output " + pwd + "output --plot";

        assertTrue(command != null && !command.isEmpty());
        assertTrue(correctCommand != null && !correctCommand.isEmpty());
        assertTrue(command.length() == correctCommand.length());
        assertTrue(command.equals(correctCommand));  */
    }
}
