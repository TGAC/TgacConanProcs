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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:30
 */
public class QuastV23 extends AbstractConanProcess {

    public static final String EXE = "quast.py";
    public static final String NAME = "Quast_V2.3";

    public QuastV23() {
        this(null);
    }

    public QuastV23(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public QuastV23(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getCommand() throws ConanParameterException {

        List<String> commands = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" ");
        sb.append(this.getProcessArgs().getArgMap().buildOptionString(CommandLineFormat.POSIX));

        String firstPart = sb.toString().trim();
        String files = this.getArgs().getInputFilesAsString();

        commands.add(firstPart + " " + files);

        String command = StringUtils.join(commands, "; ");

        return command;
    }

    @Override
    public String getName() {
        return NAME;
    }


    public static class Args extends AbstractProcessArgs {

        private Params params = new Params();

        private List<File> inputFiles;
        private List<String> labels;
        private File outputDir;
        private int threads;
        private long estimatedGenomeSize;
        private boolean scaffolds;
        private boolean findGenes;
        private boolean eukaryote;

        public Args() {

            super(new Params());

            this.inputFiles = new ArrayList<File>();
            this.labels = new ArrayList<String>();
            this.outputDir = null;
            this.threads = 1;
            this.estimatedGenomeSize = -1;
            this.scaffolds = false;
            this.findGenes = false;
            this.eukaryote = false;
        }

        public List<File> getInputFiles() {
            return inputFiles;
        }

        public void setInputFiles(List<File> inputFiles) {
            this.inputFiles = inputFiles;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public long getEstimatedGenomeSize() {
            return estimatedGenomeSize;
        }

        public void setEstimatedGenomeSize(long estimatedGenomeSize) {
            this.estimatedGenomeSize = estimatedGenomeSize;
        }

        public boolean isScaffolds() {
            return scaffolds;
        }

        public void setScaffolds(boolean scaffolds) {
            this.scaffolds = scaffolds;
        }

        public boolean isFindGenes() {
            return findGenes;
        }

        public void setFindGenes(boolean findGenes) {
            this.findGenes = findGenes;
        }

        public boolean isEukaryote() {
            return eukaryote;
        }

        public void setEukaryote(boolean eukaryote) {
            this.eukaryote = eukaryote;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

        }

        @Override
        public ParamMap getArgMap() {

            ParamMap pvp = new DefaultParamMap();

            if (this.inputFiles != null && !this.inputFiles.isEmpty()) {
                pvp.put(params.getInputFiles(), this.getInputFilesAsString());
            }

            if (this.labels != null && !this.labels.isEmpty()) {
                pvp.put(params.getLabels(), org.apache.commons.lang.StringUtils.join(this.labels, ", "));
            }

            if (this.outputDir != null) {
                pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
            }

            pvp.put(params.getThreads(), Integer.toString(this.threads));

            if (this.estimatedGenomeSize > -1) {
                pvp.put(params.getEstimatedGenomeSize(), Long.toString(this.estimatedGenomeSize));
            }

            if (this.scaffolds) {
                pvp.put(params.getScaffolds(), Boolean.toString(this.scaffolds));
            }

            if (this.findGenes) {
                pvp.put(params.getFindGenes(), Boolean.toString(this.findGenes));
            }

            if (this.eukaryote) {
                pvp.put(params.getEukaryote(), Boolean.toString(this.eukaryote));
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            if (param.equals(this.params.getInputFiles())) {
                String[] paths = value.split(" ");
                for(String path : paths) {
                    this.inputFiles.add(new File(path.trim()));
                }
            }
            else if (param.equals(this.params.getLabels())) {
                String[] labelArray = value.split(",");
                for(String label : labelArray) {
                    this.labels.add(label.trim());
                }
            }
            else if (param.equals(this.params.getOutputDir())) {
                this.outputDir = new File(value);
            }
            else if (param.equals(this.params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getEstimatedGenomeSize())) {
                this.estimatedGenomeSize = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getScaffolds())) {
                this.scaffolds = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getFindGenes())) {
                this.findGenes = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getEukaryote())) {
                this.eukaryote = Boolean.parseBoolean(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }


        public String getInputFilesAsString() {
            List<String> paths = new ArrayList<String>();
            for(File file : this.inputFiles) {
                paths.add(file.getAbsolutePath());
            }

            return org.apache.commons.lang.StringUtils.join(paths, " ");
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter inputFiles;
        private ConanParameter labels;
        private ConanParameter outputDir;
        private ConanParameter threads;
        private ConanParameter estimatedGenomeSize;
        private ConanParameter scaffolds;
        private ConanParameter findGenes;
        private ConanParameter eukaryote;

        public Params() {

            this.inputFiles = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .longName("input")
                    .description("A list of space separated input files to compare for contiguity")
                    .argIndex(0)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.labels = new ParameterBuilder()
                    .shortName("l")
                    .longName("labels")
                    .description("Names of assemblies to use in reports, comma-separated. If contain spaces, use quotes")
                    .create();

            this.outputDir = new ParameterBuilder()
                    .longName("output")
                    .description("The output directory")
                    .isOptional(true)
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.threads = new ParameterBuilder()
                    .longName("threads")
                    .description("Maximum number of threads [default: number of CPUs]")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.estimatedGenomeSize = new ParameterBuilder()
                    .longName("est-ref-size")
                    .description("Estimated reference size (for computing NGx metrics without a reference)")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.scaffolds = new ParameterBuilder()
                    .longName("scaffolds")
                    .description("Assemblies are scaffolds, split them and add contigs to the comparison")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.findGenes = new ParameterBuilder()
                    .shortName("f")
                    .longName("gene-finding")
                    .description("Predict genes (with GeneMark.hmm for prokaryotes (default), GlimmerHMM for eukaryotes " +
                            "(--eukaryote), or MetaGeneMark for metagenomes (--meta)")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.eukaryote = new ParameterBuilder()
                    .shortName("e")
                    .longName("eukaryote")
                    .description("Genome is eukaryotic")
                    .isFlag(true)
                    .isOptional(true)
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        public ConanParameter getInputFiles() {
            return inputFiles;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getLabels() {
            return labels;
        }

        public ConanParameter getEstimatedGenomeSize() {
            return estimatedGenomeSize;
        }

        public ConanParameter getScaffolds() {
            return scaffolds;
        }

        public ConanParameter getFindGenes() {
            return findGenes;
        }

        public ConanParameter getEukaryote() {
            return eukaryote;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.inputFiles,
                    this.labels,
                    this.outputDir,
                    this.threads,
                    this.estimatedGenomeSize,
                    this.scaffolds,
                    this.findGenes,
                    this.eukaryote
            };
        }
    }

    public static class Report {

        private List<AssemblyStats> statList;

        public Report() {
            statList = new ArrayList<>();
        }

        public Report(File reportFile) throws IOException {
            parse(reportFile);
        }

        private void parse(File reportFile) throws IOException {

            statList = new ArrayList<>();

            List<String> lines = FileUtils.readLines(reportFile);

            for (String line : lines) {
                String tLine = line.trim();

                if (tLine.startsWith("Assembly")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 1; i < parts.length; i++) {

                        String name = parts[i];

                        boolean skip = false;
                        if (i + 1 < parts.length && parts[i + 1].equalsIgnoreCase("broken")) {
                            name += "_broken";
                            skip = true;
                        }
                        AssemblyStats stats = new AssemblyStats();
                        stats.setName(name);
                        statList.add(stats);

                        if (skip) i++;
                    }
                } else if (tLine.startsWith("# contigs (>= 0 bp)")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 5; i < parts.length; i++) {
                        statList.get(i - 5).setNbContigsGt0(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("# contigs (>= 1000 bp)")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 5; i < parts.length; i++) {
                        statList.get(i - 5).setNbContigsGt1k(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("Total length (>= 0 bp)")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 5; i < parts.length; i++) {
                        statList.get(i - 5).setTotalLengthGt0(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("Total length (>= 1000 bp)")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 5; i < parts.length; i++) {
                        statList.get(i - 5).setTotalLengthGt1k(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("# contigs")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 2; i < parts.length; i++) {
                        statList.get(i - 2).setNbContigsGt500(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("Total length")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 2; i < parts.length; i++) {
                        statList.get(i - 2).setTotalLengthGt500(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("Largest contig")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 2; i < parts.length; i++) {
                        statList.get(i - 2).setLargestContig(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("GC (%)")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 2; i < parts.length; i++) {
                        statList.get(i - 2).setGcPc(Double.parseDouble(parts[i]));
                    }
                } else if (tLine.startsWith("N50")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 1; i < parts.length; i++) {
                        statList.get(i - 1).setN50(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("N75")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 1; i < parts.length; i++) {
                        statList.get(i - 1).setN75(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("L50")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 1; i < parts.length; i++) {
                        statList.get(i - 1).setL50(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("L75")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 1; i < parts.length; i++) {
                        statList.get(i - 1).setL75(Integer.parseInt(parts[i]));
                    }
                } else if (tLine.startsWith("# N's per 100 kbp")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 5; i < parts.length; i++) {
                        statList.get(i - 5).setNsPer100k(Double.parseDouble(parts[i]));
                    }
                } else if (tLine.startsWith("# predicted genes (unique)")) {

                    String[] parts = tLine.split("\\s+");

                    for (int i = 4; i < parts.length; i++) {
                        statList.get(i - 4).setNbGenes(Integer.parseInt(parts[i]));
                    }
                }
            }

        }

        public List<AssemblyStats> getStatList() {
            return statList;
        }

        public AssemblyStats getAssemblyStats(int index) {
            return this.statList.get(index);
        }

        public AssemblyStats getAssemblyStats(String name) {
            for (AssemblyStats stats : this.statList) {
                if (stats.getName().equalsIgnoreCase(name)) {
                    return stats;
                }
            }

            return null;
        }
    }

    public static class AssemblyStats {

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
        private int nbGenes;

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

        public int getNbGenes() {
            return nbGenes;
        }

        public void setNbGenes(int nbGenes) {
            this.nbGenes = nbGenes;
        }
    }
}
