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
package uk.ac.tgac.conan.process.asmIO.scaffold;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 28/02/13
 * Time: 11:36
 */
public class SSpaceBasicV2Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private File outputDir;
    private static String correctCommand;
    private static String correctFullCommand;

    @Before
    public void setup() throws IOException {

        outputDir = temp.newFolder("sspaceTest");

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "SSPACE_Basic_v2.0.pl -l " + pwd + "/testlib.lib -s " + pwd + "/contigs.fa -T 8 -b Output 2>&1";

        correctFullCommand = "cd " + outputDir.getAbsolutePath() + "; " + correctCommand + "; cd " + pwd;
    }

    private SSpaceBasicV2 createSspaceProc() {

        File libFile = new File("testlib.lib");
        File contigsFile = new File("contigs.fa");

        SSpaceBasicV2.Args args = new SSpaceBasicV2.Args();
        args.setLibraryConfigFile(libFile);
        args.setThreads(8);
        args.setInputAssembly(contigsFile);
        args.setOutputDir(outputDir);
        args.setBaseName("Output");

        return new SSpaceBasicV2(null, args);
    }

    @Test
    public void testSSpaceBasicV2Command() throws ConanParameterException {

        SSpaceBasicV2 task = createSspaceProc();

        String command = task.getCommand();

        assertTrue(command != null && !command.isEmpty());
        assertTrue(correctCommand != null && !correctCommand.isEmpty());
        assertTrue(command.length() == correctCommand.length());
        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testSSpaceBasicV2FullCommand() throws ConanParameterException {

        SSpaceBasicV2 task = createSspaceProc();
        task.setup();

        String command = task.getFullCommand();

        assertTrue(command != null && !command.isEmpty());
        assertTrue(correctFullCommand != null && !correctFullCommand.isEmpty());
        assertTrue(command.length() == correctFullCommand.length());
        assertTrue(command.equals(correctFullCommand));
    }

    @Test
    public void testParse() throws IOException {

        String argString = "-z 1000";

        SSpaceBasicV2.Args args = new SSpaceBasicV2.Args();
        args.parse(argString);

        assertTrue(args.getMinContigLength() == 1000);
    }
}
