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
package uk.ac.tgac.conan.process.asm.tools;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.ebi.fgpt.conan.utils.CommandExecutionException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 16/04/13
 * Time: 16:54
 */
@RunWith(MockitoJUnitRunner.class)
public class SoapAssemblerV24Test {


    @Rule
    public TemporaryFolder temp = new TemporaryFolder();


    private String pwd;

    @Mock
    private ExecutionContext ec;

    @Mock
    private ConanProcessService conanProcessService;

    private static String correctCommand;
    private static String correctFullCommand;

    @Before
    public void setup() {

        String testDir = temp.getRoot().getAbsolutePath();
        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);
        correctCommand = "SOAPdenovo-127mer pregraph -s testlib.libs -o graph -K 63 -p 32 -a 0 -R -d 0; " +
                "SOAPdenovo-127mer contig -g graph -R";

        correctFullCommand = "cd " + testDir + "; " + correctCommand + " 2>&1; cd " + pwd;
    }

    private SoapAssemblerArgsV24 createProcess() {

        File libFile = new File("testlib.libs");

        SoapAssemblerArgsV24.Args args = new SoapAssemblerArgsV24.Args();
        args.setConfigFile(libFile);
        args.setK(63);
        args.setOutputPrefix("OUTPUT_FILE");
        args.setThreads(32);
        args.setOutputDir(temp.getRoot());

        return new SoapAssemblerArgsV24(null, args);
    }

    @Test
    public void testSoapCommand() throws Exception {

        SoapAssemblerArgsV24 soap = createProcess();

        String command = soap.getCommand();

        assertTrue(command != null && !command.isEmpty());
        assertTrue(correctCommand != null && !correctCommand.isEmpty());
        assertTrue(command.length() == correctCommand.length());
        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testSoapFullCommand() throws InterruptedException, ProcessExecutionException, IOException, CommandExecutionException, ConanParameterException {

        SoapAssemblerArgsV24 soap = createProcess();
        soap.setup();

        String fullCommand = soap.getFullCommand();

        assertTrue(fullCommand != null && !fullCommand.isEmpty());
        assertTrue(correctFullCommand != null && !correctFullCommand.isEmpty());
        assertTrue(fullCommand.length() == correctFullCommand.length());
        assertTrue(fullCommand.equals(correctFullCommand));
    }
}
