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

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.fgpt.conan.core.param.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.ec.sickle.SicklePeV11Args;
import uk.ac.tgac.conan.process.ec.sickle.SickleV11Process;
import uk.ac.tgac.conan.process.ec.sickle.SickleV11QualityTypeParameter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 10:56
 */
public class QuakeV034ProcessTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private String correctCommand;
    private String correctFullCommand;

    private File readsListFile = FileUtils.toFile(this.getClass().getResource("/ec/quake/readListFile.lst"));

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 1);

        correctCommand = "quake.py -q 50 -l 50 -k 18 -p 32 -f " + readsListFile.getAbsolutePath();

        correctFullCommand = "cd " + pwd + "quake" + "; " + correctCommand + " 2>&1; cd " + pwd;
    }

    @Test
    public void testQuakeCommand() {

        QuakeV034Process quake = new QuakeV034Process(createQuakeArgs());

        String command = quake.getCommand();

        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testQuakeFullCommand() {

        QuakeV034Process quake = new QuakeV034Process(createQuakeArgs());

        String command = quake.getFullCommand();

        assertTrue(command.equals(correctFullCommand));
    }


    @Test
    public void testReadsFileCreation() throws IOException {

        File outputDir = temp.newFolder("quake");

        QuakeV034Args args = createQuakeArgs();
        args.setOutputDir(outputDir);

        Library lib = new Library();
        lib.setFilePaired1(new SeqFile("file1.fastq"));
        lib.setFilePaired2(new SeqFile("file2.fastq"));

        args.setFromLibrary(lib);

        File readsFile = args.getReadsListFile();

        List<String> lines = FileUtils.readLines(readsFile);
        String line = lines.get(0);

        assertTrue(line.equals("file1.fastq file2.fastq"));
    }



    protected QuakeV034Args createQuakeArgs() {

        QuakeV034Args args = new QuakeV034Args();
        args.setReadsListFile(readsListFile);
        args.setOutputDir(new File("quake"));
        args.setMinLength(50);
        args.setQualityThreshold(50);
        args.setKmer(18);
        args.setThreads(32);

        return args;
    }
}
