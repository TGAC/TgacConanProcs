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
package uk.ac.tgac.conan.process.asm.soapdenovo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
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
public class SoapDeNovoV204ProcessTest {


    private String pwd;

    @Mock
    private ExecutionContext ec;

    @Mock
    private ConanProcessService conanProcessService;

    private static String correctCommand;
    private static String correctFullCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 1);

        correctCommand = "soap all -o OUTPUT_FILE -K 63 -p 32 -s " + pwd + "testlib.libs";

        correctFullCommand = "cd " + pwd + ".; " + correctCommand + " 2>&1; cd " + pwd;
    }

    private SoapDeNovoV204Process createProcess() {

        File libFile = new File("testlib.libs");

        SoapDeNovoV204Args args = new SoapDeNovoV204Args();
        args.setConfigFile(libFile);
        args.setKmer(63);
        args.setOutputPrefix("OUTPUT_FILE");
        args.setThreads(32);

        return new SoapDeNovoV204Process(args);
    }

    @Test
    public void testSoapCommand() throws InterruptedException, ProcessExecutionException, IOException, CommandExecutionException {

        SoapDeNovoV204Process soap = createProcess();

        String command = soap.getCommand();

        assertTrue(command != null && !command.isEmpty());
        assertTrue(correctCommand != null && !correctCommand.isEmpty());
        assertTrue(command.length() == correctCommand.length());
        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testSoapFullCommand() throws InterruptedException, ProcessExecutionException, IOException, CommandExecutionException {

        SoapDeNovoV204Process soap = createProcess();

        String fullCommand = soap.getFullCommand();

        assertTrue(fullCommand != null && !fullCommand.isEmpty());
        assertTrue(correctFullCommand != null && !correctFullCommand.isEmpty());
        assertTrue(fullCommand.length() == correctFullCommand.length());
        assertTrue(fullCommand.equals(correctFullCommand));
    }
}
