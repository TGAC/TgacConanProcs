package uk.ac.tgac.conan.process.kmer.jellyfish;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishMergeV11 extends AbstractConanProcess {

    public static final String EXE = "jellyfish";
    public static final String MODE = "merge";

    public JellyfishMergeV11() {
        this(null);
    }

    public JellyfishMergeV11(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public JellyfishMergeV11(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);
        this.setMode(MODE);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getCommand() throws ConanParameterException {
        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" ").append(this.getMode()).append(" ");
        sb.append(this.getArgs().getArgMap().buildOptionString(CommandLineFormat.POSIX));

        Args args = (Args) this.getProcessArgs();

        List<String> paths = new ArrayList<>();
        for(File f : args.getInputFiles()) {
            paths.add(f.getAbsolutePath());
        }

        return sb.toString().trim() + " " + StringUtils.join(paths, " ");
    }

    @Override
    public String getName() {
        return "Jellyfish_Merge_V1.1";
    }

    public static class Args extends AbstractProcessArgs {

        private List<File> inputFiles;
        private File outputFile;
        private long bufferSize;

        public Args() {

            super(new Params());
            this.inputFiles = new ArrayList<>();
            this.outputFile = null;
            this.bufferSize = 10000000;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public List<File> getInputFiles() {
            return inputFiles;
        }

        public void setInputFiles(List<File> inputFiles) {
            this.inputFiles = inputFiles;
        }

        public File getOutputFile() {
            return outputFile;
        }

        public void setOutputFile(File outputFile) {
            this.outputFile = outputFile;
        }

        public long getBufferSize() {
            return bufferSize;
        }

        public void setBufferSize(long bufferSize) {
            this.bufferSize = bufferSize;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

            Params params = this.getParams();
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();

            ParamMap pvp = new DefaultParamMap();

            if (this.inputFiles != null && !this.inputFiles.isEmpty()) {

                List<String> paths = new ArrayList<>();
                for(File f : this.inputFiles) {
                    paths.add(f.getAbsolutePath());
                }

                pvp.put(params.getInputFiles(), StringUtils.join(paths, " "));
            }

            pvp.put(params.getBufferSize(), Long.toString(this.bufferSize));

            if (this.outputFile != null) {
                pvp.put(params.getOutputFile(), this.outputFile.getAbsolutePath());
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter inputFiles;
        private ConanParameter outputFile;
        private ConanParameter bufferSize;

        public Params() {

            this.inputFiles = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("Input files (supports globbing)")
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.outputFile = new ParameterBuilder()
                    .shortName("o")
                    .longName("output")
                    .description("Output file (mer_counts_merged.jf)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.bufferSize = new NumericParameter(
                    "s",
                    "Length in bytes of input buffer (1000000)",
                    true);

        }

        public ConanParameter getInputFiles() {
            return inputFiles;
        }

        public ConanParameter getOutputFile() {
            return outputFile;
        }

        public ConanParameter getBufferSize() {
            return bufferSize;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.inputFiles,
                    this.outputFile,
                    this.bufferSize
            };
        }
    }

}
