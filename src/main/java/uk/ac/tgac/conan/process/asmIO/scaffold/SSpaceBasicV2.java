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
package uk.ac.tgac.conan.process.asmIO.scaffold;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asmIO.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 16:00
 */
@MetaInfServices(AssemblyEnhancer.class)
public class SSpaceBasicV2 extends AbstractAssemblyEnhancer {

    private static Logger log = LoggerFactory.getLogger(SSpaceBasicV2.class);

    public static final String EXE = "SSPACE_Basic_v2.0.pl";
    public static final String NAME = "SSPACE_Basic_v2.0";
    public static final AssemblyEnhancerType TYPE = AssemblyEnhancerType.SCAFFOLDER;

    public SSpaceBasicV2() {
        this(null);
    }

    public SSpaceBasicV2(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public SSpaceBasicV2(ConanExecutorService ces, Args args) {
        super(NAME, TYPE, EXE, args, new Params(), ces);
    }

    @Override
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + ((Args)this.getProcessArgs()).getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @Override
    public boolean execute(ExecutionContext executionContext) throws ProcessExecutionException, InterruptedException {

        Args args = (Args)this.getProcessArgs();

        // Create the SSPACE lib configuration file from the library list
        try {
            if (args.getLibraryConfigFile() == null) {

                args.setLibraryConfigFile(new File(args.getOutputDir(), "sspace.libs"));
            }

            args.createLibraryConfigFile(args.getLibraries(), args.getLibraryConfigFile());
        }
        catch(IOException ioe) {
            throw new ProcessExecutionException(-1, ioe);
        }

        return super.execute(executionContext);
    }


    @MetaInfServices(AssemblyEnhancerArgs.class)
    public static class Args extends AbstractAssemblyEnhancerArgs {

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


        public Args() {

            super(new Params(), NAME, TYPE);

            // **** Main args ****
            this.libraryConfigFile = null;
            this.setInputAssembly(null);
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

            log.debug("Writing SSPACE config file to: " + libraryConfigFile.getAbsolutePath());
            FileUtils.writeLines(libraryConfigFile, lines);
        }


        public Options createOptions() {

            Params params = (Params)this.params;

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

            Params params = (Params)this.params;
            String[] splitArgs = new String(SSpaceBasicV2.EXE + " " + args).split(" ");
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

            Params params = (Params)this.params;
            ParamMap pvp = new DefaultParamMap();

            // **** Main args ****

            if (this.libraryConfigFile != null)
                pvp.put(params.getLibraryFile(), this.libraryConfigFile.getAbsolutePath());

            if (this.getInputAssembly() != null)
                pvp.put(params.getContigsFile(), this.getInputAssembly().getAbsolutePath());

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
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;
            if (param.equals(params.getLibraryFile())) {
                this.libraryConfigFile = new File(value);
            }
            else if (param.equals(params.getContigsFile())) {
                this.setInputAssembly(new File(value));
            }
            else if (param.equals(params.getExtend())) {
                this.extend = Integer.parseInt(value);
            }

            // **** Extension args ****

            else if (param.equals(params.getMinOverlap())) {
                this.minOverlap = Integer.parseInt(value);
            }
            else if (param.equals(params.getNbReads())) {
                this.nbReads = Integer.parseInt(value);
            }
            else if (param.equals(params.getTrim())) {
                this.trim = Integer.parseInt(value);
            }


            // **** Scaffolding args ****

            else if (param.equals(params.getMinLinks())) {
                this.minLinks = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxLinks())) {
                this.maxLinks = Double.parseDouble(value);
            }
            else if (param.equals(params.getMinContigOverlap())) {
                this.minContigOverlap = Integer.parseInt(value);
            }
            else if (param.equals(params.getMinContigLength())) {
                this.minContigLength = Integer.parseInt(value);
            }


            // **** Bowtie args ****

            else if (param.equals(params.getBowtieMaxGaps())) {
                this.bowtieMaxGaps = Integer.parseInt(value);
            }
            else if (param.equals(params.getBowtieThreads())) {
                this.setThreads(Integer.parseInt(value));
            }


            // **** Additional args ****

            else if (param.equals(params.getPlot())) {
                this.plot = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getBaseName())) {
                this.setOutputPrefix(value);
            }
            else if (param.equals(params.getVerbose())) {
                this.verbose = Boolean.parseBoolean(value);
            }



            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

    }

    public static class Params extends AbstractProcessParams {

        // Main parameters
        private ConanParameter contigsFile;
        private ConanParameter libraryFile;
        private ConanParameter extend;

        // Extension parameters
        private ConanParameter minOverlap;
        private ConanParameter nbReads;
        private ConanParameter trim;
        //private ConanParameter minBaseRatio;

        // Scaffolding parameters
        private ConanParameter minLinks;
        private ConanParameter maxLinks;
        private ConanParameter minContigOverlap;
        private ConanParameter minContigLength;


        // Bowtie parameters
        private ConanParameter bowtieMaxGaps;
        private ConanParameter bowtieThreads;

        // Additional parameters
        private ConanParameter plot;
        private ConanParameter baseName;
        private ConanParameter verbose;


        public Params() {

            // **** Main parameters ****

            this.contigsFile = new PathParameter(
                    "s",
                    "The ‘–s’ contigs file should be in a .fasta format. The headers are used " +
                            "to trace back the original contigs on the final scaffold fasta file. " +
                            "Therefore, names of the headers should not be too complex. A naming " +
                            "of “>contig11” or “>11”, should be fine. Otherwise, headers of the final " +
                            "scaffold fasta file will be too large and hard to read. " +
                            "Contigs having a non-ACGT character like “.” or “N” are not discarded. " +
                            "They are used for extension, mapping and building scaffolds. However, " +
                            "contigs having such character at either end of the sequence, could fail " +
                            "for proper contig extension and read mapping.",
                    false);

            this.libraryFile = new PathParameter(
                    "l",
                    "Library file containing two mate pair files with insert size, error and either mate pair or paired end indication.",
                    false);

            this.extend = new NumericParameter(
                    "x",
                    "Indicate whether to extend the contigs of -s using paired reads in -l. (-x 1=extension, -x 0=no extension, default -x 0)",
                    true);



            // **** Extension parameters ****

            this.minOverlap = new NumericParameter(
                    "m",
                    "Minimum number of overlapping bases of the reads with the contig " +
                            "during overhang consensus build up. Higher ‘-m’ values lead to more " +
                            "accurate contigs at the cost of decreased contiguity. We suggest to take" +
                            "a value close to the largest read length. (default -m 32)",
                    true);

            this.nbReads = new NumericParameter(
                    "o",
                    "Minimum number of reads needed to call a base during an extension, " +
                            "also known as base coverage. The higher the ‘-o’, the more reads are " +
                            "considered for an extension, increasing the reliability of the extension. (default -o 20)",
                    true);

            this.trim = new NumericParameter(
                    "t",
                    "Trims up to ‘-t’ base(s) on the contig end when all possibilities have been " +
                            "exhausted for an extension. (default -t 0, optional)",
                    true
            );

            // **** Scaffolding parameters ****

            this.minLinks = new NumericParameter(
                    "k",
                    "The minimum number of links (read pairs) a valid contig pair must have to be considered. " +
                            "(default -k 5, optional)",
                    true
            );

            this.maxLinks = new NumericParameter(
                    "a",
                    "The maximum ratio between the best two contig pairs for a given contig being extended. " +
                            "*higher values lead to least accurate scaffolding* (default -a 0.7, optional)",
                    true
            );

            this.minContigOverlap = new NumericParameter(
                    "n",
                    "Minimum overlap required between contigs to merge adjacent contigs in " +
                            "a scaffold. Overlaps in the final output are shown in lower-case " +
                            "characters. (default -n 15, optional)",
                    true
            );

            this.minContigLength = new NumericParameter(
                    "z",
                    "Minimal contig size to use for scaffolding. Contigs below this value are " +
                            "not used for scaffolding and are filtered out. Larger contigs produce " +
                            "more reliable scaffolds and also the amount of scaffolds is vastly " +
                            "reduced. Smaller contigs (< 100bp) are likely to be repeated elements " +
                            "and can stop the extension of the scaffold due to exceeding the -a " +
                            "parameter. (default -z 0 (no filtering), optional)",
                    true
            );


            // **** Bowtie parameters ****

            this.bowtieMaxGaps = new NumericParameter(
                    "g",
                    "Maximum allowed gaps for Bowtie, this parameter is used both at " +
                            "mapping during extension and mapping during scaffolding. This option " +
                            "corresponds to the -v option in Bowtie. We strongly recommend using no " +
                            "gaps, since this will slow down the process and can decrease the " +
                            "reliability of the scaffolds. We only suggest to increase this parameter " +
                            "when large reads are used, e.g. Roche 454 data or Illumina 100bp. " +
                            "*higher number of allowed gaps can lead to least accurate scaffolding* " +
                            "(default -v 0, optional)",
                    true
            );

            this.bowtieThreads = new NumericParameter(
                    "T",
                    "Number of search threads for mapping reads to the contigs with Bowtie. (default -T 1, optional)",
                    true);


            // **** Additional parameters ****

            this.plot = new FlagParameter(
                    "p",
                    "Indicate whether to generate a .dot file for visualisation of the produced scaffolds. " +
                            "(-p 1=yes, -p 0=no, default -p 0, optional)"
            );

            this.baseName = new ParameterBuilder()
                    .shortName("b")
                    .description("Base name for your output files (optional)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.verbose = new FlagParameter(
                    "v",
                    "Indicate whether to run in verbose mode or not. If set, detailed " +
                            "information about the contig extension and contig pairing process is " +
                            "printed on the screen."
            );
        }

        public ConanParameter getContigsFile() {
            return contigsFile;
        }

        public ConanParameter getLibraryFile() {
            return libraryFile;
        }

        public ConanParameter getExtend() {
            return extend;
        }

        public ConanParameter getMinOverlap() {
            return minOverlap;
        }

        public ConanParameter getNbReads() {
            return nbReads;
        }

        public ConanParameter getTrim() {
            return trim;
        }

    /*public ConanParameter getMinBaseRatio() {
        return minBaseRatio;
    }*/

        public ConanParameter getMinLinks() {
            return minLinks;
        }

        public ConanParameter getMaxLinks() {
            return maxLinks;
        }

        public ConanParameter getMinContigOverlap() {
            return minContigOverlap;
        }

        public ConanParameter getMinContigLength() {
            return minContigLength;
        }

        public ConanParameter getBowtieMaxGaps() {
            return bowtieMaxGaps;
        }

        public ConanParameter getBowtieThreads() {
            return bowtieThreads;
        }

        public ConanParameter getPlot() {
            return plot;
        }

        public ConanParameter getBaseName() {
            return baseName;
        }

        public ConanParameter getVerbose() {
            return verbose;
        }


        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.contigsFile,
                    this.libraryFile,
                    this.extend,

                    this.minOverlap,
                    this.nbReads,
                    this.trim,
                    //this.minBaseRatio,

                    this.minLinks,
                    this.maxLinks,
                    this.minContigOverlap,
                    this.minContigLength,

                    this.bowtieMaxGaps,
                    this.bowtieThreads,

                    this.plot,
                    this.baseName,
                    this.verbose
            };
        }
    }

}
