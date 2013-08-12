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
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 12/08/13
 * Time: 13:47
 */
public class QuastV2_4ReportTest {

    private File quastReportFile = FileUtils.toFile(this.getClass().getResource("/stats/quast-report.txt"));

    @Test
    public void testQuastReport() throws IOException {

        QuastV2_4Report report = new QuastV2_4Report(quastReportFile);

        assertTrue(report.getAssemblyStats(0).getName().equalsIgnoreCase("rampart-5pc-k61-scaffolds"));
        assertTrue(report.getAssemblyStats(7).getName().equalsIgnoreCase("rampart-100pc-k71-scaffolds"));
    }


}
