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

import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.*;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asm.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * User: maplesod
 * Date: 07/01/13
 * Time: 12:12
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.Assembler.class)
public class AbyssV13 extends AbstractAssembler {

    public static final String EXE = "abyss-pe";
    public static final String NAME = "Abyss_V1.3";

    public AbyssV13() {
        this(null);
    }

    public AbyssV13(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public AbyssV13(ConanExecutorService ces, AbstractAssemblerArgs args) {
        super(NAME, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args) this.getAssemblerArgs();
    }

    @Override
    public boolean makesUnitigs() {
        return true;
    }


    /**
     * Abyss only produces contigs if at least one paired end library is provided
     * @return
     */
    @Override
    public boolean makesContigs() {
        return containsPairedEndLib();
    }

    /**
     * Abyss only produces scaffolds if at least one paired end library is provided
     * @return
     */
    @Override
    public boolean makesScaffolds() {
        return containsPairedEndLib();
    }

    protected boolean containsPairedEndLib() {
        for(Library lib : this.getArgs().getLibraries()) {
            if (lib.isPairedEnd()) {
                return true;
            }
        }

        return false;
    }

    public File findUnitigsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-unitigs.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    public File findContigsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-contigs.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    public File findScaffoldsFile() {
        File[] files = this.getArgs().getOutputDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("-scaffolds.fa");
            }
        });

        return (files != null && files.length == 1) ? files[0] : null;
    }

    @Override
    public File getUnitigsFile() {

        Args abyssV134Args = (Args)this.getArgs();
        File unitigsFile = new File(abyssV134Args.getOutputDir(), abyssV134Args.getOutputName() + "-unitigs.fa");
        return unitigsFile;
    }

    @Override
    public File getContigsFile() {

        Args abyssV134Args = (Args)this.getArgs();
        File unitigsFile = new File(abyssV134Args.getOutputDir(), abyssV134Args.getOutputName() + "-contigs.fa");
        return unitigsFile;
    }

    @Override
    public File getScaffoldsFile() {

        Args abyssV134Args = (Args)this.getArgs();
        File unitigsFile = new File(abyssV134Args.getOutputDir(), abyssV134Args.getOutputName() + "-scaffolds.fa");
        return unitigsFile;
    }

    @Override
    public boolean usesOpenMpi() {
        return true;
    }

    @Override
    public Assembler.Type getType() {
        return Assembler.Type.DE_BRUIJN;
    }

    @Override
    public void setup() throws IOException {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);
    }

    @Override
    public String getCommand() throws ConanParameterException {
        return super.getCommand(CommandLineFormat.KEY_VALUE_PAIR);
    }

    @MetaInfServices(AssemblerArgs.class)
    public static class Args extends DeBruijnAssemblerArgs {

        private int nbContigPairs;
        private String outputName;

        public Args() {
            super(new Params(), NAME);

            this.nbContigPairs = 10;
            this.outputName = "abyss";
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public int getNbContigPairs() {
            return nbContigPairs;
        }

        public void setNbContigPairs(int nbContigPairs) {
            this.nbContigPairs = nbContigPairs;
        }

        public String getOutputName() {
            return outputName;
        }

        public void setOutputName(String outputName) {
            this.outputName = outputName;
        }

        @Override
        public void parse(String args) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            /*if (this.getOutputDir() != null) {
                pvp.put(params.getOutputDir(), this.getOutputDir().getAbsolutePath());
            } */

            if (this.outputName != null) {
                pvp.put(params.getName(), this.outputName);
            }

            if (this.nbContigPairs != 10) {
                pvp.put(params.getNbContigPairs(), Integer.toString(this.nbContigPairs));
            }

            if (this.getK() > 11) {
                pvp.put(params.getKmer(), Integer.toString(this.getK()));
            }

            if (this.getCoverageCutoff() > 0) {
                pvp.put(params.getCoverageCutoff(), Integer.toString(this.getCoverageCutoff()));
            }

            if (this.getThreads() > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.getThreads()));
            }

            if (this.getLibraries() != null && !this.getLibraries().isEmpty()) {
                pvp.put(params.getLibs(), new InputLibsArg(this.getLibraries()).toString());
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String val) {

            Params params = this.getParams();

            /*if (param.equals(params.getOutputDir())) {
                this.setOutputDir(new File(val));
            }
            else */if (param.equals(params.getName())) {
                this.outputName = val;
            }
            else if (param.equals(params.getKmer())) {
                this.setK(Integer.parseInt(val));
            }
            else if (param.equals(params.getCoverageCutoff())) {
                this.setCoverageCutoff(Integer.parseInt(val));
            }
            else if (param.equals(params.getNbContigPairs())) {
                this.nbContigPairs = Integer.parseInt(val);
            }
            else if (param.equals(params.getThreads())) {
                this.setThreads(Integer.parseInt(val));
            }
            else if (param.equals(params.getLibs())) {
                this.setLibraries(InputLibsArg.parse(val).getLibs());
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

        private ConanParameter outputDir;
        private ConanParameter libs;
        private ConanParameter nbContigPairs;
        private ConanParameter kmer;
        private ConanParameter coverageCutoff;
        private ConanParameter threads;
        private ConanParameter name;

        public Params() {

            this.outputDir = new ParameterBuilder()
                    .longName("directory")
                    .shortName("C")
                    .description("Change to the directory dir and store the results there.")
                    .argValidator(ArgValidator.PATH)
                    //.isOptional(false)
                    .create();

            this.libs = new InputLibsParameter();

            this.nbContigPairs = new NumericParameter(
                    "n",
                    "minimum  number  of  pairs  (default: 10). The optimal value for this param depends on  coverage, but 10 is a reasonable default.",
                    true);

            this.kmer = new NumericParameter(
                    "k",
                    "k-mer size",
                    false);

            this.coverageCutoff = new NumericParameter(
                    "c",
                    "remove contigs with mean k-mer coverage less than this threshold",
                    true);

            this.threads = new NumericParameter(
                    "np",
                    "the number of processes of an MPI assembly",
                    true);

            this.name = new ParameterBuilder()
                    .longName("name")
                    .description("The name of this assembly. The resulting contigs will be stored in ${name}-contigs.fa")
                    .isOptional(false)
                    .create();
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getLibs() {
            return libs;
        }

        public ConanParameter getNbContigPairs() {
            return nbContigPairs;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        public ConanParameter getCoverageCutoff() {
            return coverageCutoff;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getName() {
            return name;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.outputDir,
                    this.libs,
                    this.nbContigPairs,
                    this.kmer,
                    this.coverageCutoff,
                    this.threads,
                    this.name
            };
        }

    }

    public static class InputLibsArg {

        private List<Library> libs;

        public InputLibsArg() {
            this(new ArrayList<Library>());
        }

        public InputLibsArg(List<Library> libs) {
            this.libs = libs;
        }


        public List<Library> getLibs() {
            return libs;
        }

        public void setLibs(List<Library> libs) {
            this.libs = libs;
        }

        protected String joinPairedLibs(Map<String, FilePair> libs) {
            List<String> list = new ArrayList<String>();

            for (Map.Entry<String, FilePair> pp : libs.entrySet()) {
                list.add(pp.getKey() + "='" + pp.getValue().toString() + "'");
            }

            return StringUtils.join(list, " ");
        }

        public Map<String, FilePair> getPairedLibs(Library.Type type) {

            Map<String, FilePair> peLibs = new HashMap<String, FilePair>();

            for (Library lib : this.libs) {
                if (lib.getType() == type) {
                    peLibs.put(lib.getName(), new FilePair(lib.getFile1(), lib.getFile2()));
                }
            }

            return peLibs;
        }

        public Set<File> getSingleEndLibs() {
            Set<File> seLibs = new HashSet<File>();

            for (Library lib : this.libs) {
                if (lib.getType() == Library.Type.SE) {
                    seLibs.add(lib.getFile1());
                }
            }

            return seLibs;
        }


        @Override
        public String toString() {

            Map<String, FilePair> peLibs = getPairedLibs(Library.Type.PAIRED_END);
            Map<String, FilePair> opeLibs = getPairedLibs(Library.Type.OVERLAPPING_PAIRED_END);
            Map<String, FilePair> mpLibs = getPairedLibs(Library.Type.MATE_PAIR);
            Set<File> seLibs = getSingleEndLibs();

            StringBuilder sb = new StringBuilder();

            if (peLibs.size() > 0 || opeLibs.size() > 0) {

                sb.append("'");

                String peKeys = StringUtils.join(peLibs.keySet(), " ");
                String opeKeys = StringUtils.join(opeLibs.keySet(), " ");
                String keys = peKeys + " " + opeKeys;
                sb.append(keys.trim());

                sb.append("'");

                sb.append(" ");

                String peVals = joinPairedLibs(peLibs);
                String opeVals = joinPairedLibs(opeLibs);
                String vals = peVals + " " + opeVals;

                sb.append(vals.trim());

                sb.append(" ");
            }

            if (mpLibs != null && mpLibs.size() > 0) {

                sb.append("mp='");
                sb.append(StringUtils.join(mpLibs.keySet(), " "));
                sb.append("'");

                sb.append(" ");

                sb.append(joinPairedLibs(mpLibs));

                sb.append(" ");
            }

            if (seLibs != null && seLibs.size() > 0) {

                sb.append("se='");
                sb.append(StringUtils.join(seLibs, " "));
                sb.append("'");
            }

            return sb.toString().trim();
        }

        public static InputLibsArg parse(String libs) {

            InputLibsArg libsArg = new InputLibsArg();

            // Get PE libs
            List<String> peLibs = getAbyssArgs(libs, "lib");

            // Get MP libs
            List<String> mpLibs = getAbyssArgs(libs, "mp");

            // Get SE libs
            List<String> seLibs = getAbyssArgs(libs, "se");

            // Convert all string to actual library objects and add to libsArg.
            List<Library> allLibs = new ArrayList<Library>();

            for(String peLib : peLibs) {
                List<String> peLibPaths = getAbyssArgs(libs, peLib);

                if (peLibPaths.size() == 2) {
                    allLibs.add(createNewPELibrary(peLibPaths.get(0), peLibPaths.get(1), Library.Type.PE));
                }
                else {
                    throw new IllegalArgumentException("Paired end library does not contain two file paths");
                }
            }

            for(String mpLib : mpLibs) {
                List<String> mpLibPaths = getAbyssArgs(libs, mpLib);

                if (mpLibPaths.size() == 2) {
                    allLibs.add(createNewPELibrary(mpLibPaths.get(0), mpLibPaths.get(1), Library.Type.MP));
                }
                else {
                    throw new IllegalArgumentException("Paired end library does not contain two file paths");
                }
            }

            for(String seLib : seLibs) {

                allLibs.add(createNewSELibrary(seLib));
            }

            libsArg.setLibs(allLibs);

            return libsArg;
        }

        protected static Library createNewPELibrary(String libPath1, String libPath2, Library.Type type) {

            List<SeqFile> files = new ArrayList<SeqFile>();
            files.add(new SeqFile(libPath1));
            files.add(new SeqFile(libPath2));

            Library lib = new Library();

            lib.setType(type);
            lib.setFiles(files);
            lib.setSeqOrientation(type == Library.Type.PE ? Library.SeqOrientation.FR : Library.SeqOrientation.RF);

            return lib;
        }

        protected static Library createNewSELibrary(String libPath) {

            List<SeqFile> files = new ArrayList<SeqFile>();
            files.add(new SeqFile(libPath));

            Library lib = new Library();

            lib.setType(Library.Type.SE);
            lib.setFiles(files);

            return lib;
        }

        protected static List<String> getAbyssArgs(String string, String header) {

            int indexStart = string.indexOf("header");

            if (indexStart < 0) {
                return null;
            }

            String subStr = string.substring(indexStart);

            int indexStartArgs = subStr.indexOf("\"");

            if (indexStartArgs < 0) {
                return null;
            }

            String subStrArgs = string.substring(indexStartArgs);

            int indexEndArgs = subStrArgs.indexOf("\"");

            if (indexEndArgs < 0) {
                return null;
            }

            String args = subStrArgs.substring(0, indexEndArgs).trim();

            String[] argArray = args.split(" ");

            List<String> argList = new ArrayList<String>();

            for(String arg : argArray) {

                String argTrimmed = arg.trim();

                if (!arg.isEmpty()) {
                    argList.add(argTrimmed);
                }
            }

            return argList;
        }

    }


    public static class InputLibsParameter extends DefaultConanParameter {

        private static final long serialVersionUID = 4497529578973609010L;

        public InputLibsParameter() {
            super();

            this.name = "lib";
            this.description = "Required.  The input libraries to assemble with abyss.  Can include paired end and single end.  Will run paired end assemblies in parallel.";
            this.paramType = ParamType.OPTION;
            this.isOptional = false;
            this.isFlag = false;
            this.argValidator = ArgValidator.OFF;
        }
    }

}
