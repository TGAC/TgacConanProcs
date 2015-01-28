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
package uk.ac.tgac.conan.process.re.tools;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.fgpt.conan.service.DefaultExecutorService;
import uk.ac.ebi.fgpt.conan.service.DefaultProcessService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 10:56
 */
public class QuakeV03Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private String correctCommand;
    private String correctFullCommand;

    private File readsListFile = FileUtils.toFile(this.getClass().getResource("/ec/quake/readListFile.lst"));

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "quake.py -f " + readsListFile.getAbsolutePath() + " -k 18 -p 32 -l 50 2>&1";

        correctFullCommand = "cd " + pwd + "/quake" + "; " + correctCommand + "; cd " + pwd;
    }

    @Test
    public void testQuakeCommand() throws ConanParameterException, IOException {

        QuakeV03 quake = new QuakeV03(null, createQuakeArgs());

        String command = quake.getCommand();

        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testQuakeFullCommand() throws ConanParameterException, IOException {

        QuakeV03 quake = new QuakeV03(null, createQuakeArgs());
        quake.setup();

        String command = quake.getFullCommand();

        assertTrue(command.equals(correctFullCommand));
    }


    @Test
    public void testReadsFileCreation() throws IOException {

        File outputDir = temp.newFolder("quake");

        Library lib = new Library();
        lib.setFiles(new File(pwd, "file1.fastq"), new File(pwd, "file2.fastq"));

        QuakeV03.Args args = createQuakeArgs();
        args.setInput(lib);
        args.setReadsListFile(null);
        args.setOutputDir(outputDir);

        QuakeV03 quake = new QuakeV03(new DefaultExecutorService(new DefaultProcessService(), null), args);
        quake.setup();

        List<String> lines = FileUtils.readLines(args.getReadsListFile());
        String line = lines.get(0);
        String odp = outputDir.getAbsolutePath();

        assertTrue(line.equals(odp + "/file1.fastq " + odp + "/file2.fastq"));
    }



    protected QuakeV03.Args createQuakeArgs() throws IOException {

        QuakeV03.Args args = new QuakeV03.Args();
        args.setReadsListFile(readsListFile);
        args.setOutputDir(new File(pwd + "/quake"));
        args.setMinLength(50);
        args.setKmer(18);
        args.setThreads(32);

        return args;
    }
}
