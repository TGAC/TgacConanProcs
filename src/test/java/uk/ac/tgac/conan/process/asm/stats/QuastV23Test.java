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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 06/08/13
 * Time: 14:15
 */
public class QuastV23Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private static String testDir;

    private static String correctCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.testDir = temp.getRoot().getAbsolutePath();

        correctCommand = "quast.py --output=" + testDir + " --threads=16 --est-ref-size=50000000 --scaffolds " +
                pwd + "/file1.fa " + pwd + "/file2.fa";
    }

    private QuastV23 createProcess() {

        List<File> inputFiles = new ArrayList<>();

        inputFiles.add(new File("file1.fa"));
        inputFiles.add(new File("file2.fa"));

        QuastV23.Args args = new QuastV23.Args();
        args.setInputFiles(inputFiles);
        args.setScaffolds(true);
        args.setEstimatedGenomeSize(50000000);
        args.setThreads(16);
        args.setOutputDir(new File(testDir));

        return new QuastV23(null, args);
    }

    @Test
    public void testCommand() throws ConanParameterException {

        QuastV23 process = createProcess();

        String command = process.getCommand();

        assertTrue(command.equals(correctCommand));
    }

    private File quastReportFile = FileUtils.toFile(this.getClass().getResource("/stats/quast-report.txt"));
    private File quastReportScaffoldsFile = FileUtils.toFile(this.getClass().getResource("/stats/quast-report-scaffolds.txt"));

    @Test
    public void testQuastReport() throws IOException {

        QuastV23.Report report = new QuastV23.Report(quastReportFile);

        assertTrue(report.getAssemblyStats(0).getName().equalsIgnoreCase("rampart-5pc-k61-scaffolds"));
        assertTrue(report.getAssemblyStats(7).getName().equalsIgnoreCase("rampart-100pc-k71-scaffolds"));
    }

    @Test
    public void testQuastScaffoldsReport() throws IOException {

        QuastV23.Report report = new QuastV23.Report(quastReportScaffoldsFile);

        assertTrue(report.getAssemblyStats(0).getName().equalsIgnoreCase("abyss-raw-kmer-cvg-all_k-61-scaffolds_broken"));
        assertTrue(report.getAssemblyStats(7).getName().equalsIgnoreCase("abyss-raw-kmer-cvg-all_k-75-scaffolds"));
    }
}
