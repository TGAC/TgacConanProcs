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
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 12/08/13
 * Time: 13:29
 */
public class QuastV2_4Report {

    private List<QuastV2_2AssemblyStats> statList;

    public QuastV2_4Report() {
        statList = new ArrayList<QuastV2_2AssemblyStats>();
    }

    public QuastV2_4Report(File reportFile) throws IOException {
        parse(reportFile);
    }

    private void parse(File reportFile) throws IOException {

        statList = new ArrayList<QuastV2_2AssemblyStats>();

        List<String> lines = FileUtils.readLines(reportFile);

        for(String line : lines) {
            String tLine = line.trim();

            if (tLine.startsWith("Assembly")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 1; i < parts.length; i++) {
                    statList.add(new QuastV2_2AssemblyStats());
                    statList.get(i-1).setName(parts[i]);
                }
            }
            else if (tLine.startsWith("# contigs (>= 0 bp)")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 5; i < parts.length; i++) {
                    statList.get(i-5).setNbContigsGt0(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("# contigs (>= 1000 bp)")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 5; i < parts.length; i++) {
                    statList.get(i-5).setNbContigsGt1k(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("Total length (>= 0 bp)")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 5; i < parts.length; i++) {
                    statList.get(i-5).setTotalLengthGt0(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("Total length (>= 1000 bp)")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 5; i < parts.length; i++) {
                    statList.get(i-5).setTotalLengthGt1k(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("# contigs")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 2; i < parts.length; i++) {
                    statList.get(i-2).setNbContigsGt500(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("Total length")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 2; i < parts.length; i++) {
                    statList.get(i-2).setTotalLengthGt500(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("Largest contig")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 2; i < parts.length; i++) {
                    statList.get(i-2).setLargestContig(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("GC (%)")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 2; i < parts.length; i++) {
                    statList.get(i-2).setGcPc(Double.parseDouble(parts[i]));
                }
            }
            else if (tLine.startsWith("N50")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 1; i < parts.length; i++) {
                    statList.get(i-1).setN50(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("N75")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 1; i < parts.length; i++) {
                    statList.get(i-1).setN75(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("L50")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 1; i < parts.length; i++) {
                    statList.get(i-1).setL50(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("L75")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 1; i < parts.length; i++) {
                    statList.get(i-1).setL75(Integer.parseInt(parts[i]));
                }
            }
            else if (tLine.startsWith("# N's per 100 kbp")) {

                String[] parts = tLine.split("\\s+");

                for(int i = 5; i < parts.length; i++) {
                    statList.get(i-5).setNsPer100k(Double.parseDouble(parts[i]));
                }
            }
        }

    }

    public List<QuastV2_2AssemblyStats> getStatList() {
        return statList;
    }

    public QuastV2_2AssemblyStats getAssemblyStats(int index) {
        return this.statList.get(index);
    }

    public QuastV2_2AssemblyStats getAssemblyStats(String name) {
        for(QuastV2_2AssemblyStats stats : this.statList) {
            if (stats.getName().equalsIgnoreCase(name)) {
                return stats;
            }
        }

        return null;
    }

    public class QuastV2_2AssemblyStats {

        private String name;
        private int nbContigsGt0;
        private int nbContigsGt1k;
        private int totalLengthGt0;
        private int totalLengthGt1k;
        private int nbContigsGt500;
        private int totalLengthGt500;
        private int largestContig;
        private double gcPc;
        private int n50;
        private int n75;
        private int l50;
        private int l75;
        private double nsPer100k;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNbContigsGt0() {
            return nbContigsGt0;
        }

        public void setNbContigsGt0(int nbContigsGt0) {
            this.nbContigsGt0 = nbContigsGt0;
        }

        public int getNbContigsGt1k() {
            return nbContigsGt1k;
        }

        public void setNbContigsGt1k(int nbContigsGt1k) {
            this.nbContigsGt1k = nbContigsGt1k;
        }

        public int getTotalLengthGt0() {
            return totalLengthGt0;
        }

        public void setTotalLengthGt0(int totalLengthGt0) {
            this.totalLengthGt0 = totalLengthGt0;
        }

        public int getTotalLengthGt1k() {
            return totalLengthGt1k;
        }

        public void setTotalLengthGt1k(int totalLengthGt1k) {
            this.totalLengthGt1k = totalLengthGt1k;
        }

        public int getNbContigsGt500() {
            return nbContigsGt500;
        }

        public void setNbContigsGt500(int nbContigsGt500) {
            this.nbContigsGt500 = nbContigsGt500;
        }

        public int getTotalLengthGt500() {
            return totalLengthGt500;
        }

        public void setTotalLengthGt500(int totalLengthGt500) {
            this.totalLengthGt500 = totalLengthGt500;
        }

        public int getLargestContig() {
            return largestContig;
        }

        public void setLargestContig(int largestContig) {
            this.largestContig = largestContig;
        }

        public double getGcPc() {
            return gcPc;
        }

        public void setGcPc(double gcPc) {
            this.gcPc = gcPc;
        }

        public int getN50() {
            return n50;
        }

        public void setN50(int n50) {
            this.n50 = n50;
        }

        public int getN75() {
            return n75;
        }

        public void setN75(int n75) {
            this.n75 = n75;
        }

        public int getL50() {
            return l50;
        }

        public void setL50(int l50) {
            this.l50 = l50;
        }

        public int getL75() {
            return l75;
        }

        public void setL75(int l75) {
            this.l75 = l75;
        }

        public double getNsPer100k() {
            return nsPer100k;
        }

        public void setNsPer100k(double nsPer100k) {
            this.nsPer100k = nsPer100k;
        }
    }
}
