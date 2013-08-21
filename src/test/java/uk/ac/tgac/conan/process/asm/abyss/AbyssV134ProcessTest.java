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
package uk.ac.tgac.conan.process.asm.abyss;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.ebi.fgpt.conan.utils.CommandExecutionException;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 28/02/13
 * Time: 11:17
 */
@RunWith(MockitoJUnitRunner.class)
public class AbyssV134ProcessTest {

    private String pwd;

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Mock
    private ExecutionContext ec;

    @Mock
    private ConanProcessService conanProcessService;

    private static String correctCommand;
    private static String correctFullCommand;
    private static String correctLsfScheduledCommand;

    @Before
    public void setup() {

        String testDir = temp.getRoot().getAbsolutePath();
        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "abyss-pe  name=OUTPUT_FILE  k=61  np=16  lib='peLib1' " +
                "peLib1='" + pwd + "/tools/mass/LIB1896_R1.r95.fastq " + pwd + "/tools/mass/LIB1896_R2.r95.fastq'";

        correctFullCommand = "cd " + testDir + "; " + correctCommand + " 2>&1; cd " + pwd;

        correctLsfScheduledCommand = "bsub -aopenmpi \"" + correctFullCommand + "\"";
    }



    private List<Library> createLocalPETestLibrary() {

        Library peLib = new Library();
        peLib.setName("peLib1");
        peLib.setFiles(pwd + "/tools/mass/LIB1896_R1.r95.fastq", pwd + "/tools/mass/LIB1896_R2.r95.fastq");

        List<Library> libs = new ArrayList<Library>();
        libs.add(peLib);

        return libs;
    }

    private AbyssV134Process createProcess() {
        List<Library> libs = this.createLocalPETestLibrary();

        AbyssV134Args args = new AbyssV134Args();
        args.setLibraries(libs);
        args.setKmer(61);
        args.setName("OUTPUT_FILE");
        args.setThreads(16);
        args.setOutputDir(temp.getRoot());

        return new AbyssV134Process(args);
    }

    @Test
    public void testAbyssV134Command() throws InterruptedException, ProcessExecutionException, IOException, CommandExecutionException {

        AbyssV134Process abyss = createProcess();

        String command = abyss.getCommand();

        assertTrue(command != null && !command.isEmpty());
        assertTrue(correctCommand != null && !correctCommand.isEmpty());
        assertTrue(command.length() == correctCommand.length());
        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testAbyssV134FullCommand() throws InterruptedException, ProcessExecutionException, IOException, CommandExecutionException {

        AbyssV134Process abyss = createProcess();
        abyss.initialise();

        String fullCommand = abyss.getFullCommand();

        assertTrue(fullCommand != null && !fullCommand.isEmpty());
        assertTrue(correctFullCommand != null && !correctFullCommand.isEmpty());
        assertTrue(fullCommand.length() == correctFullCommand.length());
        assertTrue(fullCommand.equals(correctFullCommand));
    }
}
