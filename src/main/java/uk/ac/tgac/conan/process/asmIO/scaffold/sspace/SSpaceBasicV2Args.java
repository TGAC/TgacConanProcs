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
package uk.ac.tgac.conan.process.asmIO.scaffold.sspace;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asmIO.AbstractAssemblyIOArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MetaInfServices(uk.ac.tgac.conan.process.asmIO.AssemblyIOArgsCreator.class)
public class SSpaceBasicV2Args extends AbstractAssemblyIOArgs {

    private SSpaceBasicV2Params params = new SSpaceBasicV2Params();

    public static final int DEFAULT_EXTEND = 0;

    public static final int DEFAULT_MIN_OVERLAP = 32;
    public static final int DEFAULT_NB_READS = 20;
    public static final int DEFAULT_TRIM = 0;

    public static final int DEFAULT_MIN_LINKS = 5;
    public static final double DEFAULT_MAX_LINKS = 0.7;
    public static final int DEFAULT_MIN_CONTIG_OVERLAP = 15;
    public static final int DEFAULT_MIN_CONTIG_LENGTH = 0;

    public static final int DEFAULT_BOWTIE_MAX_GAPS = 0;
    public static final int DEFAULT_THREADS = 1;



    // **** Main args ****
    private File libraryConfigFile;
    private int extend;

    // **** Extension args ****
    private int minOverlap;
    private int nbReads;
    private int trim;

    // **** Scaffolding args ****
    private int minLinks;
    private double maxLinks;
    private int minContigOverlap;
    private int minContigLength;

    // **** Bowtie args ****
    private int bowtieMaxGaps;

    // **** Additional args ****
    private boolean plot;
    private boolean verbose;


    public SSpaceBasicV2Args() {

        super();

        // **** Main args ****
        this.libraryConfigFile = null;
        this.setInputFile(null);
        this.extend = DEFAULT_EXTEND;

        // **** Extension args ****
        this.minOverlap = DEFAULT_MIN_OVERLAP;
        this.nbReads = DEFAULT_NB_READS;
        this.trim = DEFAULT_TRIM;

        // **** Scaffolding args ****
        this.minLinks = DEFAULT_MIN_LINKS;
        this.maxLinks = DEFAULT_MAX_LINKS;
        this.minContigOverlap = DEFAULT_MIN_CONTIG_OVERLAP;
        this.minContigLength = DEFAULT_MIN_CONTIG_LENGTH;

        // **** Bowtie args ****
        this.bowtieMaxGaps = DEFAULT_BOWTIE_MAX_GAPS;
        this.setThreads(DEFAULT_THREADS);

        // **** Additional args ****
        this.plot = false;
        this.verbose = false;
    }

    @Override
    public File getOutputFile() {
        return new File(this.getOutputDir(), this.getBaseName() + ".final.scaffolds.fasta");
    }

    public File getLibraryConfigFile() {
        return libraryConfigFile;
    }

    public void setLibraryConfigFile(File libraryConfigFile) {
        this.libraryConfigFile = libraryConfigFile;
    }

    public int getExtend() {
        return extend;
    }

    public void setExtend(int extend) {
        this.extend = extend;
    }

    public String getBaseName() {
        return this.getOutputPrefix();
    }

    public void setBaseName(String baseName) {
        this.setOutputPrefix(baseName);
    }

    public int getMinOverlap() {
        return minOverlap;
    }

    public void setMinOverlap(int minOverlap) {
        this.minOverlap = minOverlap;
    }

    public int getNbReads() {
        return nbReads;
    }

    public void setNbReads(int nbReads) {
        this.nbReads = nbReads;
    }

    public int getTrim() {
        return trim;
    }

    public void setTrim(int trim) {
        this.trim = trim;
    }

    public int getMinLinks() {
        return minLinks;
    }

    public void setMinLinks(int minLinks) {
        this.minLinks = minLinks;
    }

    public double getMaxLinks() {
        return maxLinks;
    }

    public void setMaxLinks(double maxLinks) {
        this.maxLinks = maxLinks;
    }

    public int getMinContigOverlap() {
        return minContigOverlap;
    }

    public void setMinContigOverlap(int minContigOverlap) {
        this.minContigOverlap = minContigOverlap;
    }

    public int getMinContigLength() {
        return minContigLength;
    }

    public void setMinContigLength(int minContigLength) {
        this.minContigLength = minContigLength;
    }

    public int getBowtieMaxGaps() {
        return bowtieMaxGaps;
    }

    public void setBowtieMaxGaps(int bowtieMaxGaps) {
        this.bowtieMaxGaps = bowtieMaxGaps;
    }

    public boolean isPlot() {
        return plot;
    }

    public void setPlot(boolean plot) {
        this.plot = plot;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void createLibraryConfigFile(List<Library> libs, File libraryConfigFile) throws IOException {

        List<String> lines = new ArrayList<String>();

        for (Library lib : libs) {

            String[] parts = new String[]{
                    lib.getName(),
                    lib.getFile1().getAbsolutePath(),
                    lib.getFile2().getAbsolutePath(),
                    Integer.toString(lib.getAverageInsertSize()),
                    Double.toString(lib.getInsertErrorTolerance()),
                    lib.getSeqOrientation().toString()
            };

            lines.add(StringUtils.join(parts, " "));

        }

        FileUtils.writeLines(libraryConfigFile, lines);
    }


    public Options createOptions() {

        // create Options object
        Options options = new Options();

        options.addOption(new Option(params.getExtend().getShortName(), true, params.getExtend().getDescription()));
        options.addOption(new Option(params.getMinOverlap().getShortName(), true, params.getMinOverlap().getDescription()));
        options.addOption(new Option(params.getNbReads().getShortName(), true, params.getNbReads().getDescription()));
        options.addOption(new Option(params.getTrim().getShortName(), true, params.getTrim().getDescription()));
        options.addOption(new Option(params.getMinLinks().getShortName(), true, params.getMinLinks().getDescription()));
        options.addOption(new Option(params.getMaxLinks().getShortName(), true, params.getMaxLinks().getDescription()));
        options.addOption(new Option(params.getMinContigOverlap().getShortName(), true, params.getMinContigOverlap().getDescription()));
        options.addOption(new Option(params.getMinContigLength().getShortName(), true, params.getMinContigLength().getDescription()));
        options.addOption(new Option(params.getBowtieMaxGaps().getShortName(), true, params.getBowtieMaxGaps().getDescription()));
        options.addOption(new Option(params.getBowtieThreads().getShortName(), true, params.getBowtieThreads().getDescription()));
        options.addOption(new Option(params.getPlot().getShortName(), true, params.getPlot().getDescription()));
        options.addOption(new Option(params.getVerbose().getShortName(), false, params.getVerbose().getDescription()));

        return options;
    }

    @Override
    public void parse(String args) throws IOException {
        String[] splitArgs = new String(SSpaceBasicV2Process.EXE + " " + args).split(" ");
        CommandLine cmdLine = null;
        try {
            cmdLine = new PosixParser().parse(createOptions(), splitArgs);
        } catch (ParseException e) {
            throw new IOException(e);
        }

        if (cmdLine == null)
            return;

        this.extend = cmdLine.hasOption(params.getExtend().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getExtend().getShortName())) :
                this.extend;

        this.minOverlap = cmdLine.hasOption(params.getMinOverlap().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getMinOverlap().getShortName())) :
                this.minOverlap;

        this.nbReads = cmdLine.hasOption(params.getNbReads().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getNbReads().getShortName())) :
                this.nbReads;

        this.trim = cmdLine.hasOption(params.getTrim().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getTrim().getShortName())) :
                this.trim;

        this.minLinks = cmdLine.hasOption(params.getMinLinks().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getMinLinks().getShortName())) :
                this.minLinks;

        this.maxLinks = cmdLine.hasOption(params.getMaxLinks().getShortName()) ?
                Double.parseDouble(cmdLine.getOptionValue(params.getMaxLinks().getShortName())) :
                this.maxLinks;

        this.minContigOverlap = cmdLine.hasOption(params.getMinContigOverlap().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getMinContigOverlap().getShortName())) :
                this.minContigOverlap;

        this.minContigLength = cmdLine.hasOption(params.getMinContigLength().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getMinContigLength().getShortName())) :
                this.minContigLength;

        this.bowtieMaxGaps = cmdLine.hasOption(params.getBowtieMaxGaps().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getBowtieMaxGaps().getShortName())) :
                this.bowtieMaxGaps;

        this.setThreads(cmdLine.hasOption(params.getBowtieThreads().getShortName()) ?
                Integer.parseInt(cmdLine.getOptionValue(params.getBowtieThreads().getShortName())) :
                this.getThreads());

        this.plot = cmdLine.hasOption(params.getPlot().getShortName());

        this.verbose = cmdLine.hasOption(params.getVerbose().getShortName());
    }

    @Override
    public ParamMap getArgMap() {

        ParamMap pvp = new DefaultParamMap();

        // **** Main args ****

        if (this.libraryConfigFile != null)
            pvp.put(params.getLibraryFile(), this.libraryConfigFile.getAbsolutePath());

        if (this.getInputFile() != null)
            pvp.put(params.getContigsFile(), this.getInputFile().getAbsolutePath());

        if (this.extend != DEFAULT_EXTEND)
            pvp.put(params.getExtend(), Integer.toString(this.extend));


        // **** Extension args ****

        if (this.minOverlap != DEFAULT_MIN_OVERLAP)
            pvp.put(params.getMinOverlap(), Integer.toString(this.minOverlap));

        if (this.nbReads != DEFAULT_NB_READS)
            pvp.put(params.getNbReads(), Integer.toString(this.nbReads));

        if (this.trim != DEFAULT_TRIM)
            pvp.put(params.getTrim(), Integer.toString(this.trim));



        // **** Scaffolding args ****

        if (this.minLinks != DEFAULT_MIN_LINKS)
            pvp.put(params.getMinLinks(), Integer.toString(this.minLinks));

        if (this.maxLinks != DEFAULT_MAX_LINKS)
            pvp.put(params.getMaxLinks(), Double.toString(this.maxLinks));

        if (this.minContigOverlap != DEFAULT_MIN_CONTIG_OVERLAP)
            pvp.put(params.getMinContigOverlap(), Integer.toString(this.minContigOverlap));

        if (this.getMinContigLength() != DEFAULT_MIN_CONTIG_LENGTH)
            pvp.put(params.getMinContigLength(), Integer.toString(this.minContigLength));


        // **** Bowtie args ****

        if (this.bowtieMaxGaps != DEFAULT_BOWTIE_MAX_GAPS)
            pvp.put(params.getBowtieMaxGaps(), Integer.toString(this.bowtieMaxGaps));

        if (this.getThreads() != DEFAULT_THREADS)
            pvp.put(params.getBowtieThreads(), Integer.toString(this.getThreads()));


        // **** Additional args ****

        if (this.plot)
            pvp.put(params.getPlot(), Boolean.toString(this.plot));

        if (this.getOutputPrefix() != null)
            pvp.put(params.getBaseName(), this.getOutputPrefix());

        if (this.verbose)
            pvp.put(params.getVerbose(), Boolean.toString(this.verbose));

        return pvp;
    }

    @Override
    public void setFromArgMap(ParamMap pvp) {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            ConanParameter param = entry.getKey();

            // **** Main args ****

            if (param.equals(this.params.getLibraryFile())) {
                this.libraryConfigFile = new File(entry.getValue());
            }
            else if (param.equals(this.params.getContigsFile())) {
                this.setInputFile(new File(entry.getValue()));
            }
            else if (param.equals(this.params.getExtend())) {
                this.extend = Integer.parseInt(entry.getValue());
            }

            // **** Extension args ****

            else if (param.equals(this.params.getMinOverlap())) {
                this.minOverlap = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getNbReads())) {
                this.nbReads = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getTrim())) {
                this.trim = Integer.parseInt(entry.getValue());
            }


            // **** Scaffolding args ****

            else if (param.equals(this.params.getMinLinks())) {
                this.minLinks = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getMaxLinks())) {
                this.maxLinks = Double.parseDouble(entry.getValue());
            }
            else if (param.equals(this.params.getMinContigOverlap())) {
                this.minContigOverlap = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getMinContigLength())) {
                this.minContigLength = Integer.parseInt(entry.getValue());
            }


            // **** Bowtie args ****

            else if (param.equals(this.params.getBowtieMaxGaps())) {
                this.bowtieMaxGaps = Integer.parseInt(entry.getValue());
            }
            else if (param.equals(this.params.getBowtieThreads())) {
                this.setThreads(Integer.parseInt(entry.getValue()));
            }


            // **** Additional args ****

            else if (param.equals(this.params.getPlot())) {
                this.plot = Boolean.parseBoolean(entry.getValue());
            }
            else if (param.equals(this.params.getBaseName())) {
                this.setOutputPrefix(entry.getValue());
            }
            else if (param.equals(this.params.getVerbose())) {
                this.verbose = Boolean.parseBoolean(entry.getValue());
            }



            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }


    @Override
    public AbstractAssemblyIOArgs create(File inputFile, File outputDir, String outputPrefix, List<Library> libs,
                                         int threads, int memory, String otherArgs) throws IOException {

        SSpaceBasicV2Args newArgs = new SSpaceBasicV2Args();
        newArgs.setInputFile(inputFile);
        newArgs.setOutputDir(outputDir);
        newArgs.setOutputPrefix(outputPrefix);
        newArgs.setLibraries(libs);
        newArgs.setThreads(threads);
        newArgs.setMemory(memory);
        newArgs.parse(otherArgs);

        return newArgs;
    }

    @Override
    public String getName() {
        return SSpaceBasicV2Process.NAME;
    }

    @Override
    public String getAssemblyIOProcessType() {
        return SSpaceBasicV2Process.TYPE;
    }
}
