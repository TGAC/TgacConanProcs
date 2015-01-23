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

import org.apache.commons.io.FileUtils;
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
 * Date: 06/08/13
 * Time: 14:15
 */
public class CegmaV24Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private static String testDir;

    private static String correctCommand;
    private static String correctFullCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.testDir = temp.getRoot().getAbsolutePath();

        correctCommand = "cegma --genome " + pwd + "/assembly.fa --output " + testDir + "/cegma-output --threads 16 2>&1";
        correctFullCommand = "cd " + testDir + "; CEGMATMP=" + testDir + "/temp; " +
                "sed 's/>/>seq_/g' " + pwd + "/assembly.fa > " + testDir + "/assembly.fa.mod.fa; " +
                "cegma --genome " + testDir + "/assembly.fa.mod.fa --output " + testDir + "/cegma-output --threads 16 2>&1" +
                "; cd " + pwd;
    }

    private CegmaV24 createProcess() {

        CegmaV24.Args args = new CegmaV24.Args();
        args.setGenomeFile(new File("assembly.fa"));
        args.setOutputPrefix(new File(testDir, "cegma-output"));
        args.setThreads(16);

        return new CegmaV24(null, args);
    }

    @Test
    public void testCommand() {

        CegmaV24 process = createProcess();

        String command = process.getCommand();

        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testFullCommand() throws IOException, ConanParameterException {

        CegmaV24 process = createProcess();

        process.initialise();

        String fullCommand = process.getFullCommand();

        assertTrue(fullCommand.equals(correctFullCommand));
    }

    private File cegmaReportFile = FileUtils.toFile(this.getClass().getResource("/stats/cegma-report.txt"));

    @Test
    public void testQuastReport() throws IOException {

        CegmaV24.Report report = new CegmaV24.Report(cegmaReportFile);

        assertTrue(report.getPcComplete() == 93.55);
        assertTrue(report.getPcPartial() == 93.95);
    }
}
