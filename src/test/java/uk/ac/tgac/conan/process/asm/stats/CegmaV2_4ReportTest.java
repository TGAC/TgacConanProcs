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
 * Time: 14:02
 */
public class CegmaV2_4ReportTest {

    private File cegmaReportFile = FileUtils.toFile(this.getClass().getResource("/stats/cegma-report.txt"));

    @Test
    public void testQuastReport() throws IOException {

        CegmaV2_4Report report = new CegmaV2_4Report(cegmaReportFile);

        assertTrue(report.getPcComplete() == 93.55);
        assertTrue(report.getPcPartial() == 93.95);
    }
}
