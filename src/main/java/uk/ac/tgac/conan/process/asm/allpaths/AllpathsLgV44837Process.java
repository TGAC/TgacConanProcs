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
package uk.ac.tgac.conan.process.asm.allpaths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asm.Assembler;
import uk.ac.tgac.conan.process.asm.AssemblerArgs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 13/08/13
 * Time: 15:24
 */
public class AllpathsLgV44837Process extends AbstractConanProcess implements Assembler {

    private File refDir;
    private File cacheDir;
    private File dataDir;
    private File runDir;
    private File assembliesDir;

    private File inLibs;
    private File inGroupsPhred33;
    private File inGroupsPhred64;

    private AllpathsLgV44837Args allpathsArgs;


    public AllpathsLgV44837Process() {
        this(new AllpathsLgV44837Args());
    }

    public AllpathsLgV44837Process(AssemblerArgs args) {
        super("", args, new AllpathsLgV44837Params());
    }

    @Override
    public AssemblerArgs getArgs() {
        return (AssemblerArgs) this.getProcessArgs();
    }

    @Override
    public boolean makesUnitigs() {
        return false;
    }

    @Override
    public boolean makesContigs() {
        return true;
    }

    @Override
    public boolean makesScaffolds() {
        return true;
    }

    @Override
    public File getUnitigsFile() {
        return null;
    }

    @Override
    public File getContigsFile() {
        return new File(assembliesDir, "final.contigs.fasta");
    }

    @Override
    public File getScaffoldsFile() {
        return new File(assembliesDir, "final.assembly.fasta");
    }

    @Override
    public boolean usesOpenMpi() {
        return false;
    }

    /**
     * Actually ALLPATHS does have a K param but apparently we shouldn't mess about with it according to the ALLPATHs
     * documentation
     * @return
     */
    @Override
    public boolean hasKParam() {

        return false;
    }

    @Override
    public void initialise() throws IOException {

        // Quick handle to args
        this.allpathsArgs = (AllpathsLgV44837Args)this.getArgs();

        // Create lib input file
        this.inLibs = new File(allpathsArgs.getOutputDir(), "in_libs.csv");
        this.createLibFile(this.inLibs, allpathsArgs.getLibraries());

        // Create groups input files (sets inGroupsPhredXX)
        this.createGroupFiles(allpathsArgs.getLibraries());

        // Setup ALLPATHS dirs
        this.refDir = new File(allpathsArgs.getOutputDir(), allpathsArgs.getOrganism().getName());
        this.cacheDir = new File(refDir, "cache");
        this.dataDir = new File(refDir, "data");
        this.runDir = new File(dataDir, "rampart");
        this.assembliesDir = new File(runDir, "ASSEMBLIES/test");
    }

    protected void createLibFile(File libFile, List<Library> libraries) throws IOException {

        List<String> lines = new ArrayList<String>();

        lines.add("library_name, project_name, organism_name, type, paired, frag_size, frag_stddev, insert_size, insert_stddev, read_orientation, genomic_start, genomic_end");

        for(Library lib : libraries) {

            String type = "";
            String paired = "";
            String fragSize = "";
            String fragDev = "";
            String insertSize = "";
            String insertDev = "";
            String readOrientation = "";

            switch(lib.getType()) {
                case OPE:
                    type = "fragment";
                    paired = "1";
                    fragSize = Integer.toString(lib.getAverageInsertSize());
                    fragDev = Integer.toString((int)(lib.getAverageInsertSize() * lib.getInsertErrorTolerance()));
                    readOrientation = lib.getSeqOrientation() == Library.SeqOrientation.RF ? "inward" : "outward";
                    break;
                case PE:
                case MP:
                    type = "jumping";
                    paired = "1";
                    insertSize = Integer.toString(lib.getAverageInsertSize());
                    insertDev = Integer.toString((int)(lib.getAverageInsertSize() * lib.getInsertErrorTolerance()));
                    readOrientation = lib.getSeqOrientation() == Library.SeqOrientation.RF ? "inward" : "outward";
                    break;
                case SE:
                    type = "long";
                    paired = "0";
                    break;
                default:
            }



            List<String> parts = new ArrayList<String>();
            parts.add(lib.getName());
            parts.add("rampart");
            parts.add(allpathsArgs.getOrganism().getName());
            parts.add(type);
            parts.add(paired);
            parts.add(fragSize);
            parts.add(fragDev);
            parts.add(insertSize);
            parts.add(insertDev);
            parts.add(readOrientation);
            parts.add("");
            parts.add("");

            lines.add(StringUtils.join(parts, ", "));
        }

        FileUtils.writeLines(libFile, lines);
    }

    private void createGroupFiles(List<Library> libraries) throws IOException {

        int countOpe = 0;
        int countPe = 0;
        int countMp = 0;
        int countLong = 0;

        List<String> phred33Lines = new ArrayList<String>();
        List<String> phred64Lines = new ArrayList<String>();

        for(Library lib : libraries) {

            String groupName = lib.getType().toString();

            switch(lib.getType()) {
                case OPE:
                    countOpe++;
                    groupName += countOpe;
                    break;
                case PE:
                    countPe++;
                    groupName += countPe;
                    break;
                case MP:
                    countMp++;
                    groupName += countMp;
                    break;
                case SE:
                    countLong++;
                    groupName += countLong;
                    break;
                default:
            }

            List<String> parts = new ArrayList<String>();
            parts.add(groupName);
            parts.add(lib.getName());

            if (lib.getType() == Library.Type.SE) {
                parts.add(lib.getFile1().getAbsolutePath());
            }
            else {
                parts.add("\"{" + lib.getFile1().getAbsolutePath() + "," + lib.getFile2().getAbsolutePath() + "}\"");
            }

            String groupLine = StringUtils.join(parts, ", ");

            if (lib.getPhred() == null || lib.getPhred() == Library.Phred.PHRED_64) {
                phred64Lines.add(groupLine);
            }
            else {
                phred33Lines.add(groupLine);
            }
        }

        if (phred33Lines.size() > 0) {
            this.inGroupsPhred33 = new File(allpathsArgs.getOutputDir(), "in_groups_phred33.csv");
            FileUtils.writeLines(this.inGroupsPhred33, phred33Lines);
        }

        if (phred64Lines.size() > 0) {
            this.inGroupsPhred64 = new File(allpathsArgs.getOutputDir(), "in_groups_phred64.csv");
            FileUtils.writeLines(this.inGroupsPhred64, phred64Lines);
        }
    }

    private void createGroupFile(File groupFile, List<Library> libs) throws IOException {

        List<String> lines = new ArrayList<String>();

        lines.add("group_name, library_name, file_name");

        for(Library lib : libs) {

        }

        FileUtils.writeLines(groupFile, lines);
    }

    @Override
    public String getCommand() {

        List<String> mainCommands = new ArrayList<String>();


        // **** Generate cache library groups command(s) ****

        if (this.inGroupsPhred33 != null) {
            mainCommands.add(makeCacheGroupsCommand(this.inGroupsPhred33, false));
        }

        if (this.inGroupsPhred64 != null) {
            mainCommands.add(makeCacheGroupsCommand(this.inGroupsPhred64, true));
        }

        // **** Generated ALLPATHS-LG cache to input commands ****
        mainCommands.add(makeCacheToInputsCommand());

        // **** Generate run ALLPATHS-LG command ****
        mainCommands.add(makeRunCommand());

        // Join commands
        return StringUtils.join(mainCommands, "; ");
    }

    protected String makeCacheGroupsCommand(File inputCsv, boolean phred64) {

        String phredString = phred64 ? "True" : "False";

        return  "CacheGroups.pl " +
                "CACHE_DIR=" + this.cacheDir.getAbsolutePath() + " " +
                "IN_GROUPS_CSV=" + inputCsv.getAbsolutePath() + " " +
                "TMP_DIR=" + new File(this.cacheDir, "tmp").getAbsolutePath() + " " +
                "ACTION=Add " +
                "PHRED_64=" + phredString + " " +
                "OVERWRITE=True";
    }


    protected String makeCacheToInputsCommand() {

        String groupsString   = makeGroupsString(allpathsArgs.getLibraries());
        String coverageString = makeCoverageString(allpathsArgs.getLibraries().size(), allpathsArgs.getDesiredCoverage());

        return  "CacheToAllPathsInputs.pl " +
                "CACHE_DIR=" + this.cacheDir.getAbsolutePath() + " " +
                "DATA_DIR=" + this.dataDir.getAbsolutePath() + " " +
                "GENOME_SIZE=" + allpathsArgs.getOrganism().getEstGenomeSize() + " " +
                "GROUPS=" + groupsString + " " +
                "COVERAGES=" + coverageString + " " +
                "PLOIDY=" + allpathsArgs.getOrganism().getPloidy();
    }

    private String makeGroupsString(List<Library> libraries) {

        List<String> groups = new ArrayList<String>();

        for(Library lib : libraries) {
            groups.add(lib.getName());
        }

        return "\"{" + StringUtils.join(groups, ",") + "}\"";
    }

    private String makeCoverageString(int size, int desiredCoverage) {

        List<String> coverages = new ArrayList<String>();

        for(int i = 0; i < size; i++) {
            coverages.add(Integer.toString(desiredCoverage));
        }

        return "\"{" + StringUtils.join(coverages, ",") + "}\"";
    }


    protected String makeRunCommand() {

        return "RunAllPathsLG " +
                "PRE=" + allpathsArgs.getOutputDir().getAbsolutePath() + " " +
                "REFERENCE_NAME=rampart " +
                "DATA_SUBDIR=data " +
                "RUN=rampart " +
                "THREADS=" + allpathsArgs.getThreads() + " " +
                "OVERWRITE=True " +
                "TARGETS=full_eval";
    }

    @Override
    public String getName() {
        return "AllpathsLg_V44837";
    }
}
