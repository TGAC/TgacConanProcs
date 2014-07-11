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
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 12/08/13
 * Time: 09:54
 */
public class CegmaV24 extends AbstractConanProcess {

    public static final String EXE = "cegma";

    private File modifiedGenomeFile;

    public CegmaV24() {
        this(null);
    }

    public CegmaV24(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public CegmaV24(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);

        this.modifiedGenomeFile = null;
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    /**
     * Make sure you run this before executing the CEGMA process
     * @throws IOException
     */
    public void initialise() throws IOException {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        Args cArgs = this.getArgs();

        File targetDir = cArgs.getOutputPrefix().getParentFile();

        // Create directories
        if (targetDir.exists()) {
            FileUtils.deleteDirectory(targetDir);
        }
        targetDir.mkdir();

        File tmpDir = new File(targetDir, "temp");
        if (tmpDir.exists()) {
            FileUtils.deleteDirectory(tmpDir);
        }
        tmpDir.mkdir();

        // Move into the output dir
        this.addPreCommand("cd " + targetDir.getAbsolutePath());

        // Add run specific cegmatmp env var
        this.addPreCommand("CEGMATMP=" + tmpDir.getAbsolutePath());

        // Create modified genome, that has BLAST friendly headers (this to avoid incompatibilities between Abyss and Cegma)
        // Only do this if required
        File modifiedFile = new File(targetDir, cArgs.getGenomeFile().getName() + ".mod.fa");
        this.modifiedGenomeFile = modifiedFile;
        this.addPreCommand("sed 's/>/>seq_/g' " + cArgs.getGenomeFile().getAbsolutePath() + " > " + modifiedFile.getAbsolutePath());

        // revert to existing dir
        this.addPostCommand("cd " + pwd);

        // Note, may need to add command to clean the input assembly here... can't have spaces or pipes (I think)

    }

    @Override
    public String getCommand() {

        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" ");
        for (Map.Entry<ConanParameter, String> param : this.getProcessArgs().getArgMap().entrySet()) {

            if (param.getKey().getLongName().equals("genome") && this.modifiedGenomeFile != null) {
                sb.append("--");
                sb.append(param.getKey().getLongName());
                sb.append(" ");
                sb.append(this.modifiedGenomeFile.getAbsolutePath());
                sb.append(" ");
            }
            else {
                sb.append("--");
                sb.append(param.getKey().getLongName());
                if (!param.getKey().isBoolean()) {
                    sb.append(" ");
                    sb.append(param.getValue());
                }
                sb.append(" ");
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String getName() {
        return "Cegma_V2.4";
    }

    public static class Args extends AbstractProcessArgs {

        private File genomeFile;
        private File modifiedGenomeFile;
        private File outputPrefix;
        private int threads;

        public Args() {

            super(new Params());

            this.genomeFile = null;
            this.modifiedGenomeFile = null;
            this.outputPrefix = null;
            this.threads = 1;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getGenomeFile() {
            return genomeFile;
        }

        public void setGenomeFile(File genomeFile) {
            this.genomeFile = genomeFile;
        }

        public File getModifiedGenomeFile() {
            return modifiedGenomeFile;
        }

        public void setModifiedGenomeFile(File modifiedGenomeFile) {
            this.modifiedGenomeFile = modifiedGenomeFile;
        }

        public File getOutputPrefix() {
            return outputPrefix;
        }

        public void setOutputPrefix(File outputPrefix) {
            this.outputPrefix = outputPrefix;
        }

        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }


        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            if (this.genomeFile != null) {
                pvp.put(params.getGenomeFile(), this.genomeFile.getAbsolutePath());
            }

            if (this.outputPrefix != null) {
                pvp.put(params.getOutputPrefix(), this.outputPrefix.getAbsolutePath());
            }

            pvp.put(params.getThreads(), Integer.toString(this.threads));

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getGenomeFile())) {
                this.genomeFile = new File(value);
            }
            else if (param.equals(params.getOutputPrefix())) {
                this.outputPrefix = new File(value);
            }
            else if (param.equals(params.getThreads())) {
                this.threads = Integer.parseInt(value);
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

        private ConanParameter genomeFile;
        private ConanParameter outputPrefix;
        private ConanParameter threads;

        public Params() {

            this.genomeFile = new ParameterBuilder()
                    .shortName("g")
                    .longName("genome")
                    .description("fasta file of the query sequence")
                    .isOptional(false)
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.outputPrefix = new ParameterBuilder()
                    .shortName("o")
                    .longName("output")
                    .description("ouput file prefix")
                    .isOptional(false)
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.threads = new ParameterBuilder()
                    .shortName("T")
                    .longName("threads")
                    .description("Specify number of processor threads to use")
                    .isOptional(true)
                    .argValidator(ArgValidator.DIGITS)
                    .create();

        }

        public ConanParameter getGenomeFile() {
            return genomeFile;
        }

        public ConanParameter getOutputPrefix() {
            return outputPrefix;
        }

        public ConanParameter getThreads() {
            return threads;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.genomeFile,
                    this.outputPrefix,
                    this.threads
            };
        }
    }


    public static class Report {

        private double pcComplete;
        private double pcPartial;

        public Report() {
            pcComplete = 0.0;
            pcPartial = 0.0;
        }

        public Report(File reportFile) throws IOException {
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


}
