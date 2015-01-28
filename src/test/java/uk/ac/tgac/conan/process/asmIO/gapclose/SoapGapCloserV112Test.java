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

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 28/02/13
 * Time: 16:40
 */
public class SoapGapCloserV112Test {

    private String pwd;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 1);
    }

    @Test
    public void testGapCloserV112() throws Exception {

        SoapGapCloserV112.Args args = new SoapGapCloserV112.Args();
        args.setOutputFile(new File("output.fa"));
        args.setInputAssembly(new File("input.fa"));
        args.setThreads(8);
        args.setOverlap(29);
        args.setLibraryFile(new File("libFile.cfg"));

        SoapGapCloserV112 gc = new SoapGapCloserV112(null, args);

        String command = gc.getCommand();
        String correct = "GapCloser " +
                "-a " + pwd + "input.fa " +
                "-b " + pwd + "libFile.cfg " +
                "-o " + pwd + "output.fa " +
                "-p 29 -t 8 2>&1";

        assertTrue(command != null && !command.isEmpty());
        assertTrue(correct != null && !correct.isEmpty());
        assertTrue(command.length() == correct.length());
        assertTrue(command.equals(correct));
    }
}
