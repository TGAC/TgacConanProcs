package uk.ac.tgac.conan.process.repeat;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 28/01/14
 * Time: 18:44
 * To change this template use File | Settings | File Templates.
 */
public class RepeatModelerV1_0 extends AbstractConanProcess {

    public static final String EXE = "RepeatModeler";

    public RepeatModelerV1_0(ConanProcessService conanProcessService) {
        this(conanProcessService, new Args());
    }

    public RepeatModelerV1_0(ConanProcessService conanProcessService, Args args) {
        super(EXE, args, new Params());
        this.setConanProcessService(conanProcessService);
    }

    @Override
    public String getName() {
        return "Repeat_Modeler_V1.0";
    }

    public Args getArgs() {
        return (RepeatModelerV1_0.Args)this.getProcessArgs();
    }

    @Override
    public String getCommand() throws ConanParameterException {
        // Ensure all parameters are valid before we try to make a command
        this.getProcessArgs().getArgMap().validate(this.getProcessParams());

        List<String> commands = new ArrayList<>();

        Args args = this.getArgs();

        // Make sure the db directory exists
        if (!args.getDbDir().exists()) {
            args.getDbDir().mkdirs();
        }

        StringBuilder sb = new StringBuilder();

        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        // Link assembly
        sb.append("ln -s -f " + args.getInput().getAbsolutePath() + " " + new File(args.getOutputDir(), "genome.fa").getAbsolutePath()).append("; ");

        // Change dir to where we need to be
        sb.append("cd " + args.getDbDir().getAbsolutePath()).append("; ");

        // Build Database
        sb.append("BuildDatabase -dir " + args.getOutputDir().getAbsolutePath() + " -name " + args.getDbName() +
                " -engine " + args.getEngine().toArgString()).append("; ");

        // Change dir to where we need to be
        sb.append("cd " + args.getOutputDir().getAbsolutePath()).append("; ");

        // Run Repeat Modeller
        sb.append(EXE + " -database " + args.getDbDir().getAbsolutePath() + "/" + args.getDbName() + " -engine " + args.getEngine().toArgString()).append("; ");

        // Change dir back
        sb.append("cd " + pwd);

        return sb.toString();
    }


    public static class Args extends AbstractProcessArgs {

        public static enum Engine {

            ABBLAST,
            WUBLAST,
            NCBI;

            public String toArgString() {
                return this.name().toLowerCase();
            }
        }


        private File input;
        private File outputDir;
        private String dbName;
        private Engine engine;

        public Args() {
            super(new Params());

            this.input = null;
            this.outputDir = null;
            this.dbName = "genome";
            this.engine = Engine.NCBI;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getInput() {
            return input;
        }

        public void setInput(File input) {
            this.input = input;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public File getDbDir() {
            return new File(this.outputDir, "db");
        }

        public Engine getEngine() {
            return engine;
        }

        public void setEngine(Engine engine) {
            this.engine = engine;
        }

        public File getLatestRepeatModelerDir() {

            return this.getLatestRepeatModelerDir(this.outputDir);
        }

        public File getLatestRepeatModelerDir(File outputDir) {

            File[] dirs = outputDir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isDirectory() && file.getName().startsWith("RM_");
                }
            });

            if (dirs == null || dirs.length == 0)
                return null;

            long lastMod = Long.MIN_VALUE;
            File choice = null;

            for (File file : dirs) {
                if (file.lastModified() > lastMod) {
                    choice = file;
                    lastMod = file.lastModified();
                }
            }
            return choice;
        }

        public File getFastaOutputFile() {

            File rmDir = this.getLatestRepeatModelerDir();

            return rmDir == null ? null : new File(rmDir, "consensi.fa");
        }

        public File getMaskedOutputFile() {

            return this.getMaskedOutputFile(this.outputDir);
        }

        public File getMaskedOutputFile(File outputDir) {

            File rmDir = this.getLatestRepeatModelerDir(outputDir);

            return rmDir == null ? null : new File(rmDir, "consensi.fa.masked");
        }

        public File getClassifiedOutputFile() {

            File rmDir = this.getLatestRepeatModelerDir();

            return rmDir == null ? null : new File(rmDir, "consensi.fa.classified");
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getOutputDir())) {
                this.outputDir = new File(value);
            }
            else if (param.equals(params.getDbName())) {
                this.dbName = value;
            }
            else if (param.equals(params.getEngine())) {
                this.engine = Engine.valueOf(value.toUpperCase());
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            Params params = this.getParams();

            if (param.equals(params.getInputAssembly())) {
                this.input = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {
            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.input != null) {
                pvp.put(params.getInputAssembly(), this.input.getAbsolutePath());
            }

            if (this.outputDir != null) {
                pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
            }

            if (this.dbName != null && !this.dbName.isEmpty()) {
                pvp.put(params.getDbName(), this.dbName);
            }

            if (this.engine != null) {
                pvp.put(params.getEngine(), this.engine.toArgString());
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter inputAssembly;
        private ConanParameter outputDir;
        private ConanParameter dbName;
        private ConanParameter engine;

        public Params() {

            this.inputAssembly = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("The input assembly to analyse")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.outputDir = new ParameterBuilder()
                    .shortName("o")
                    .longName("output_dir")
                    .isOptional(false)
                    .description("The directory in which to create the output")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.dbName = new ParameterBuilder()
                    .shortName("n")
                    .longName("name")
                    .isOptional(false)
                    .description("The name of the database to create")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.engine = new ParameterBuilder()
                    .shortName("e")
                    .longName("engine")
                    .description("The name of the search engine we are using. I.e abblast/wublast or ncbi (rmblast version)")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();
        }

        public ConanParameter getInputAssembly() {
            return inputAssembly;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }

        public ConanParameter getDbName() {
            return dbName;
        }

        public ConanParameter getEngine() {
            return engine;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.inputAssembly,
                    this.outputDir,
                    this.dbName,
                    this.engine
            };
        }
    }
}
