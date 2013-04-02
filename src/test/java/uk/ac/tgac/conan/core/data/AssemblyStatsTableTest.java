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
package uk.ac.tgac.conan.core.data;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 19/03/13
 * Time: 15:03
 */
public class AssemblyStatsTableTest {


    private File statsFile1 = FileUtils.toFile(this.getClass().getResource("/stats/stats1.txt"));
    private File statsFile2 = FileUtils.toFile(this.getClass().getResource("/stats/stats2.txt"));


    @Test
    public void testAssemblyStatsTable() throws IOException {

        List<File> statsFiles = new ArrayList<File>();
        statsFiles.add(statsFile1);
        statsFiles.add(statsFile2);


        AssemblyStatsTable table = new AssemblyStatsTable(statsFile1);

        AssemblyStatsMatrix matrix = table.generateStatsMatrix();

        AssemblyStats bestStats = table.getBest();

        assertTrue(bestStats.getScore() == 94.5);
    }
}
