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
package uk.ac.tgac.conan.process.asm.tools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 13/08/13
 * Time: 15:24
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class AllpathsLgV44837 extends AbstractAssembler implements Subsampler {

    public static final String EXE = "RunAllPathsLG";
    public static final String NAME = "AllpathsLg_V44837";

    @Override
    public void setDesiredCoverage(int desiredCoverage) {
        this.getArgs().setDesiredCoverage(desiredCoverage);
    }

    @Override
    public int getDesiredCoverage() {
        return this.getArgs().getDesiredCoverage();
    }

    private static class GroupInfo {
        private File inGroupsPhred33;
        private File inGroupsPhred64;
        private List<String> groupNames;

        private GroupInfo(File inGroupsPhred33, File inGroupsPhred64, List<String> groupNames) {
            this.inGroupsPhred33 = inGroupsPhred33;
            this.inGroupsPhred64 = inGroupsPhred64;
            this.groupNames = groupNames;
        }

        private File getInGroupsPhred33() {
            return inGroupsPhred33;
        }

        private File getInGroupsPhred64() {
            return inGroupsPhred64;
        }

        private List<String> getGroupNames() {
            return groupNames;
        }
    }


    private File refDir;
    private File cacheDir;
    private File dataDir;
    private File runDir;
    private File assembliesDir;

    private File inLibs;
    private GroupInfo groupInfo;

    public AllpathsLgV44837() {
        this(null);
    }

    public AllpathsLgV44837(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public AllpathsLgV44837(ConanExecutorService conanExecutorService, Args args) {
        super(NAME, EXE, args, new Params(), conanExecutorService);
    }

    public Args getArgs() {
        return (Args)this.getAssemblerArgs();
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
    public boolean doesSubsampling() {
        return true;
    }


    @Override
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN_AUTO;
    }


    /**
     * ALLPATHS requires at least a "fragment" library (i.e. an overlapping paired end library), and a "jumping" library
     * (i.e. a normal paired end or mate pair library)
     * @param libraries
     * @return
     */
    @Override
    public boolean acceptsLibraries(List<Library> libraries) {

        boolean foundFragmentLib = false;
        boolean foundJumpingLib = false;

        for(Library lib : libraries) {
            if (lib.getType() == Library.Type.OPE) {
                foundFragmentLib = true;
            }
            else if (lib.getType() == Library.Type.PE || lib.getType() == Library.Type.MP) {
                foundJumpingLib = true;
            }
        }

        return foundFragmentLib && foundJumpingLib;
    }

    @Override
    public void setup() throws IOException {

        // Quick handle to args
        Args args = this.getArgs();

        // Create lib input file
        this.inLibs = new File(args.getOutputDir(), "in_libs.csv");
        this.createLibFile(this.inLibs, args.getLibraries());

        // Create groups input files (sets inGroupsPhredXX)
        this.groupInfo = this.createGroupFiles(args.getLibraries());

        String fileSystemFriendlyName = args.getOrganism().getName().replaceAll(" ", "_");

        // Define ALLPATHS dirs
        this.refDir = new File(args.getOutputDir(), fileSystemFriendlyName);
        this.cacheDir = new File(refDir, "cache");
        this.dataDir = new File(refDir, "data");
        this.runDir = new File(dataDir, "rampart");
        this.assembliesDir = new File(runDir, "ASSEMBLIES/test");

        // Create required ALLPATHS dirs
        if (!this.cacheDir.exists()) {
            if (!this.cacheDir.mkdirs()) {
                throw new IOException("Could not create cache directory for ALLPATHS-LG: " + this.cacheDir.getAbsolutePath());
            }
        }


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

            if (lib.getType() == Library.Type.OPE ||
                    (lib.getType() == Library.Type.SE && lib.getReadLength() < 500)) {
                type = "fragment";

                if (lib.getType() == Library.Type.OPE) {
                    paired = "1";
                    fragSize = Integer.toString(lib.getAverageInsertSize());
                    fragDev = Integer.toString((int)(lib.getAverageInsertSize() * lib.getInsertErrorTolerance()));
                    readOrientation = lib.getSeqOrientation() == Library.SeqOrientation.FR ? "inward" : "outward";
                }
                else {
                    paired = "0";
                    fragSize = Integer.toString(lib.getReadLength());
                }
            }
            else if (lib.getType() == Library.Type.PE ||
                    (lib.getType() == Library.Type.MP && lib.getAverageInsertSize() < 20000)) {
                type = "jumping";
                paired = "1";
                insertSize = Integer.toString(lib.getAverageInsertSize());
                insertDev = Integer.toString((int)(lib.getAverageInsertSize() * lib.getInsertErrorTolerance()));
                readOrientation = lib.getSeqOrientation() == Library.SeqOrientation.FR ? "inward" : "outward";
            }
            else if (lib.getType() == Library.Type.MP && lib.getAverageInsertSize() >= 20000) {
                type = "long_jump";
                paired = "1";
                insertSize = Integer.toString(lib.getAverageInsertSize());
                insertDev = Integer.toString((int)(lib.getAverageInsertSize() * lib.getInsertErrorTolerance()));
                readOrientation = lib.getSeqOrientation() == Library.SeqOrientation.FR ? "inward" : "outward";
                break;
            }
            else if (lib.getType() == Library.Type.SE && lib.getReadLength() >= 500) {
               type = "long";
               paired = "0";
            }



            List<String> parts = new ArrayList<String>();
            parts.add(lib.getName());
            parts.add("rampart");
            parts.add(this.getArgs().getOrganism().getName().replaceAll(" ", "_"));
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

    private GroupInfo createGroupFiles(List<Library> libraries) throws IOException {

        int countOpe = 0;
        int countPe = 0;
        int countMp = 0;
        int countLong = 0;

        List<String> phred33Lines = new ArrayList<>();
        List<String> phred64Lines = new ArrayList<>();
        List<String> groupNames = new ArrayList<>();

        final String groupHeader = "group_name, library_name, file_name";

        phred33Lines.add(groupHeader);
        phred64Lines.add(groupHeader);

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

            groupNames.add(groupName);

            List<String> parts = new ArrayList<String>();
            parts.add(groupName);
            parts.add(lib.getName());

            if (lib.getType() == Library.Type.SE) {
                parts.add(lib.getFile1().getAbsolutePath());
            }
            else {
                parts.add(createGlobPattern(lib.getFile1(), lib.getFile2()));
            }

            String groupLine = StringUtils.join(parts, ", ");

            if (lib.getPhred() == null || lib.getPhred() == Library.Phred.PHRED_64) {
                phred64Lines.add(groupLine);
            }
            else {
                phred33Lines.add(groupLine);
            }


        }

        File p33File = null;
        File p64File = null;
        File outputDir = this.getArgs().getOutputDir();

        if (phred33Lines.size() > 1) {
            p33File = new File(outputDir, "in_groups_phred33.csv");
            FileUtils.writeLines(p33File, phred33Lines);
        }

        if (phred64Lines.size() > 1) {
            p64File = new File(outputDir, "in_groups_phred64.csv");
            FileUtils.writeLines(p64File, phred64Lines);
        }

        return new GroupInfo(p33File, p64File, groupNames);
    }

    public String createGlobPattern(File file1, File file2) throws IOException {

        String filePath1 = file1.getAbsolutePath();
        String filePath2 = file2.getAbsolutePath();

        if (filePath1.length() != filePath2.length()) {
            throw new IOException("Can't create GLOB pattern of these two file paths: " + filePath1 + ", " + filePath2 + "; " +
                    "paths must be the same length and differ in only one character (i.e. the file pair id)");
        }


        int diffCount = 0;
        int diffIndex = 0;
        for(int i = 0; i < filePath1.length(); i++) {

            if (filePath1.charAt(i) != filePath2.charAt(i)) {
                diffCount++;
                diffIndex = i;
            }
        }

        if (diffCount == 0) {
            throw new IOException("File paths are identical can't create glob pattern: " + filePath1);
        }
        else if (diffCount > 1) {
            throw new IOException("Found " + diffCount + " differences in file paths: " + filePath1 + ", " + filePath2 +
                    " Can only create glob pattern if there is a single character difference");
        }

        return filePath1.substring(0, diffIndex) + "?" + filePath1.substring(diffIndex+1);
    }


    @Override
    public String getCommand() {

        List<String> mainCommands = new ArrayList<String>();

        // Generate cache libraries command ****
        mainCommands.add(makeCacheLibsCommand());

        // **** Generate cache groups command(s) ****

        if (this.groupInfo.getInGroupsPhred33() != null) {
            mainCommands.add(makeCacheGroupsCommand(this.groupInfo.getInGroupsPhred33(), false));
        }

        if (this.groupInfo.getInGroupsPhred64() != null) {
            mainCommands.add(makeCacheGroupsCommand(this.groupInfo.getInGroupsPhred64(), true));
        }

        // **** Generated ALLPATHS-LG cache to input commands ****
        mainCommands.add(makeCacheToInputsCommand(this.groupInfo.getGroupNames()));

        // **** Generate run ALLPATHS-LG command ****
        mainCommands.add(makeRunCommand());

        // Join commands
        return StringUtils.join(mainCommands, "; ");
    }

    private String makeCacheLibsCommand() {

        return "CacheLibs.pl " +
                "CACHE_DIR=" + this.cacheDir.getAbsolutePath() + " " +
                "IN_LIBS_CSV=" + this.inLibs.getAbsolutePath() + " " +
                "ACTION=Add " +
                "OVERWRITE=True";
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


    protected String makeCacheToInputsCommand(List<String> groupNames) {

        Args args = this.getArgs();
        String groupsString   = makeGroupsString(groupNames);
        String coverageString = makeCoverageString(groupNames.size(), args.getDesiredCoverage());

        return  "CacheToAllPathsInputs.pl " +
                "CACHE_DIR=" + this.cacheDir.getAbsolutePath() + " " +
                "DATA_DIR=" + this.dataDir.getAbsolutePath() + " " +
                "GENOME_SIZE=" + args.getOrganism().getEstGenomeSize() + " " +
                "GROUPS=" + groupsString + " " +
                "COVERAGES=" + coverageString + " " +
                "PLOIDY=" + args.getOrganism().getPloidy();
    }

    private String makeGroupsString(List<String> groupNames) {

        return "'{" + StringUtils.join(groupNames, ",") + "}'";
    }

    private String makeCoverageString(int size, int desiredCoverage) {

        if (desiredCoverage == -1)
            return "";

        List<String> coverages = new ArrayList<String>();

        for(int i = 0; i < size; i++) {
            coverages.add(Integer.toString(desiredCoverage));
        }

        return "'{" + StringUtils.join(coverages, ",") + "}'";
    }


    protected String makeRunCommand() {

        Args args = this.getArgs();

        return "RunAllPathsLG " +
                "PRE=" + args.getOutputDir().getAbsolutePath() + " " +
                "REFERENCE_NAME=" + args.getOrganism().getName().replaceAll(" ", "_") + " " +
                "DATA_SUBDIR=data " +
                "RUN=rampart " +
                "THREADS=" + args.getThreads() + " " +
                "OVERWRITE=True " +
                "TARGETS=full_eval";
    }


    @MetaInfServices(AssemblerArgs.class)
    public static class Args extends DeBruijnAutoAssemblerArgs {

        private int desiredCoverage;

        public Args() {
            super(new Params(), NAME);
            desiredCoverage = -1;
        }


        @Override
        public void parse(String args) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        public void setFromArgMap(ParamMap pvp) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setDesiredCoverage(int desiredCoverage) {
            this.desiredCoverage = desiredCoverage;
        }

        public int getDesiredCoverage() {
            return desiredCoverage;
        }
    }

    public static class Params extends AbstractProcessParams {


        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[0];
        }
    }

}
