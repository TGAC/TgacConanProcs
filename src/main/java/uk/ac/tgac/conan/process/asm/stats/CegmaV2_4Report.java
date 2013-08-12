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

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: maplesod
 * Date: 12/08/13
 * Time: 13:29
 */
public class CegmaV2_4Report {

    private double pcComplete;
    private double pcPartial;

    public CegmaV2_4Report() {
        pcComplete = 0.0;
        pcPartial = 0.0;
    }

    public CegmaV2_4Report(File reportFile) throws IOException {
        parse(reportFile);
    }

    private void parse(File reportFile) throws IOException {
        List<String> lines = FileUtils.readLines(reportFile);

        for(String line : lines) {
            String tLine = line.trim();

            if (tLine.startsWith("Complete")) {

                String[] parts = tLine.split("\\s+");

                this.pcComplete = Double.parseDouble(parts[2]);
            }
            else if (tLine.startsWith("Partial")) {

                String[] parts = tLine.split("\\s+");

                this.pcPartial = Double.parseDouble(parts[2]);
            }
        }

    }

    public double getPcComplete() {
        return pcComplete;
    }

    public void setPcComplete(double pcComplete) {
        this.pcComplete = pcComplete;
    }

    public double getPcPartial() {
        return pcPartial;
    }

    public void setPcPartial(double pcPartial) {
        this.pcPartial = pcPartial;
    }

}
