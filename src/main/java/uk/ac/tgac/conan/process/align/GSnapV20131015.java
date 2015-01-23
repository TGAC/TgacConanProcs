package uk.ac.tgac.conan.process.align;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 15/01/14
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public class GSnapV20131015 extends AbstractConanProcess {

    public static final String EXE = "gsnap";

    public GSnapV20131015(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public GSnapV20131015(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
    }

    @Override
    public String getName() {
        return "GSnap_V20131015";
    }

    public static class Args extends AbstractProcessArgs {

        private static final int DEFAULT_LOCAL_SPLICED_DISTANCE = 200000;
        private static final int DEFAULT_NB_PATHS = 100;

        private File genomeDir;
        private String genomeDB;
        private File read1;
        private File read2;
        private boolean novelSplicing;
        private int localSplicedDist;
        private int threads;
        private String outputFormat;
        private int nbPaths;
        private File outputFile;
        private File logFile;

        public Args() {

            super(new Params());

            this.genomeDir = null;
            this.genomeDB = "";
            this.read1 = null;
            this.read2 = null;
            this.novelSplicing = false;
            this.localSplicedDist = DEFAULT_LOCAL_SPLICED_DISTANCE;
            this.threads = 1;
            this.outputFormat = "SAM";
            this.nbPaths = DEFAULT_NB_PATHS;
            this.outputFile = null;
            this.logFile = null;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getGenomeDir() {
            return genomeDir;
        }

        public void setGenomeDir(File genomeDir) {
            this.genomeDir = genomeDir;
        }

        public String getGenomeDB() {
            return genomeDB;
        }

        public void setGenomeDB(String genomeDB) {
            this.genomeDB = genomeDB;
        }

        public File getRead1() {
            return read1;
        }

        public void setRead1(File read1) {
            this.read1 = read1;
        }

        public File getRead2() {
            return read2;
        }

        public void setRead2(File read2) {
            this.read2 = read2;
        }

        public boolean isNovelSplicing() {
            return novelSplicing;
        }

        public void setNovelSplicing(boolean novelSplicing) {
            this.novelSplicing = novelSplicing;
        }

        public int getLocalSplicedDist() {
            return localSplicedDist;
        }

        public void setLocalSplicedDist(int localSplicedDist) {
            this.localSplicedDist = localSplicedDist;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        public String getOutputFormat() {
            return outputFormat;
        }

        public void setOutputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
        }

        public int getNbPaths() {
            return nbPaths;
        }

        public void setNbPaths(int nbPaths) {
            this.nbPaths = nbPaths;
        }

        public File getOutputFile() {
            return outputFile;
        }

        public void setOutputFile(File outputFile) {
            this.outputFile = outputFile;
        }

        public File getLogFile() {
            return logFile;
        }

        public void setLogFile(File logFile) {
            this.logFile = logFile;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getGenomeDir())) {
                this.genomeDir = new File(value);
            }
            else if (param.equals(params.getGenomeDB())) {
                this.genomeDB = value;
            }
            else if (param.equals(params.getNovelSplicing())) {
                this.novelSplicing = Integer.parseInt(value) > 0;
            }
            else if (param.equals(params.getLocalSplicedDist())) {
                this.localSplicedDist = Integer.parseInt(value);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
            }
            else if (param.equals(params.getOutputFormat())) {
                this.outputFormat = value;
            }
            else if (param.equals(params.getNbPaths())) {
                this.nbPaths = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getRead1())) {
                this.read1 = new File(value);
            }
            else if (param.equals(params.getRead2())) {
                this.read2 = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setStdOutRedirectFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutputFile())) {
                this.outputFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setStdErrRedirectFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getLogFile())) {
                this.logFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.genomeDir != null) {
                pvp.put(params.getGenomeDir(), this.genomeDir.getAbsolutePath());
            }

            if (this.genomeDB != null && !this.genomeDB.isEmpty()) {
                pvp.put(params.getGenomeDB(), this.genomeDB);
            }

            if (this.read1 != null) {
                pvp.put(params.getRead1(), this.read1.getAbsolutePath());
            }

            if (this.read2 != null) {
                pvp.put(params.getRead2(), this.read2.getAbsolutePath());
            }

            if (this.outputFile != null) {
                pvp.put(params.getOutputFile(), this.outputFile.getAbsolutePath());
            }

            if (this.logFile != null) {
                pvp.put(params.getLogFile(), this.logFile.getAbsolutePath());
            }

            if (this.novelSplicing) {
                pvp.put(params.getNovelSplicing(), "1");
            }

            if (this.localSplicedDist != DEFAULT_LOCAL_SPLICED_DISTANCE) {
                pvp.put(params.getLocalSplicedDist(), Integer.toString(this.localSplicedDist));
            }

            if (this.threads > 1) {
                pvp.put(params.getThreads(), Integer.toString(this.threads));
            }

            if (this.outputFormat != null && !this.outputFormat.isEmpty()) {
                pvp.put(params.getOutputFormat(), this.outputFormat);
            }

            if (this.nbPaths != DEFAULT_NB_PATHS) {
                pvp.put(params.getNbPaths(), Integer.toString(this.nbPaths));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter genomeDir;
        private ConanParameter genomeDB;
        private ConanParameter read1;
        private ConanParameter read2;
        private ConanParameter novelSplicing;
        private ConanParameter localSplicedDist;
        private ConanParameter threads;
        private ConanParameter outputFormat;
        private ConanParameter nbPaths;
        private ConanParameter outputFile;
        private ConanParameter logFile;


        public Params() {

            this.genomeDir = new ParameterBuilder()
                    .shortName("D")
                    .longName("dir")
                    .isOptional(false)
                    .description("Genome directory")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.genomeDB = new ParameterBuilder()
                    .shortName("d")
                    .longName("db")
                    .isOptional(false)
                    .description("Genome database")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.read1 = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("Query file containing reads to align")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.read2 = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(true)
                    .argIndex(1)
                    .description("Second query file containing reads to align")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.novelSplicing = new ParameterBuilder()
                    .shortName("N")
                    .longName("novelsplicing")
                    .description("Look for novel splicing (0=no (default), 1=yes)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.localSplicedDist = new ParameterBuilder()
                    .shortName("w")
                    .longName("localsplicedist")
                    .description("Definition of local novel splicing event (default 200000)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("t")
                    .longName("nthreads")
                    .description("Number of worker threads")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.outputFormat = new ParameterBuilder()
                    .shortName("A")
                    .longName("format")
                    .description("Another format type, other than default.\n" +
                            "                                   Currently implemented: sam, m8 (BLAST tabular format)\n" +
                            "                                   Also allowed, but not installed at compile-time: goby\n" +
                            "                                   (To install, need to re-compile with appropriate options)\n")
                    .create();

            this.nbPaths = new ParameterBuilder()
                    .shortName("n")
                    .longName("npaths")
                    .description("Maximum number of paths to print (default 100).")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.outputFile = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .type(DefaultConanParameter.ParamType.STDOUT_REDIRECTION)
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.logFile = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .type(DefaultConanParameter.ParamType.STDERR_REDIRECTION)
                    .argValidator(ArgValidator.PATH)
                    .create();

        }

        public ConanParameter getGenomeDir() {
            return genomeDir;
        }

        public ConanParameter getGenomeDB() {
            return genomeDB;
        }

        public ConanParameter getRead1() {
            return read1;
        }

        public ConanParameter getRead2() {
            return read2;
        }

        public ConanParameter getNovelSplicing() {
            return novelSplicing;
        }

        public ConanParameter getLocalSplicedDist() {
            return localSplicedDist;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        public ConanParameter getOutputFormat() {
            return outputFormat;
        }

        public ConanParameter getNbPaths() {
            return nbPaths;
        }

        public ConanParameter getOutputFile() {
            return outputFile;
        }

        public ConanParameter getLogFile() {
            return logFile;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.genomeDir,
                    this.genomeDB,
                    this.read1,
                    this.read2,
                    this.novelSplicing,
                    this.localSplicedDist,
                    this.threads,
                    this.outputFormat,
                    this.nbPaths,
                    this.outputFile,
                    this.logFile
            };
        }
    }
}
