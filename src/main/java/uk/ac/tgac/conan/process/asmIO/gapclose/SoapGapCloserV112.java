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
package uk.ac.tgac.conan.process.asmIO.gapclose;

import org.apache.commons.io.FileUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.SeqFile;
import uk.ac.tgac.conan.process.asmIO.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 13:44
 */
@MetaInfServices(AssemblyEnhancer.class)
public class SoapGapCloserV112 extends AbstractAssemblyEnhancer {

    private static Logger log = LoggerFactory.getLogger(SoapGapCloserV112.class);


    public static final String EXE = "GapCloser";
    public static final String NAME = "SOAP_GapCloser_V1.12";
    public static final AssemblyEnhancerType TYPE = AssemblyEnhancerType.GAP_CLOSER;

    public SoapGapCloserV112() {
        this(null);
    }

    public SoapGapCloserV112(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public SoapGapCloserV112(ConanExecutorService ces, Args args) {
        super(NAME, TYPE, EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public boolean execute(ExecutionContext executionContext) throws ProcessExecutionException, InterruptedException {

        Args args = this.getArgs();

        // Create the SSPACE lib configuration file from the library list
        try {
            if (args.getLibraryFile() == null) {

                args.setLibraryFile(new File(args.getOutputDir(), "soap_gc.libs"));
            }

            args.createLibraryFile(args.getLibraries(), args.getLibraryFile());
        }
        catch(IOException ioe) {
            throw new ProcessExecutionException(-1, ioe);
        }

        if (args.getOutputFile() == null) {
            args.setOutputFile(new File(args.getOutputDir(), "gc.fa"));
        }

        try {
            return super.execute(executionContext);
        }
        catch(ProcessExecutionException pee) {
            log.warn("Gap Closer threw an error.  This is probably not a problem, gap closer always finishes with an error condition but please check output.");
        }

        return true;
    }

    @MetaInfServices(AssemblyEnhancerArgs.class)
    public static class Args extends AbstractAssemblyEnhancerArgs {

        public static int DEFAULT_OVERLAP = 25;
        public static int DEFAULT_READ_LENGTH = 100;

        // GapCloser vars
        private File libraryFile;
        private File outputFile;
        private int overlap = DEFAULT_OVERLAP;
        private int maxReadLength = DEFAULT_READ_LENGTH;

        public Args() {
            super(new Params(), NAME, TYPE);
            this.libraryFile = null;
            this.outputFile = null;
            this.overlap = DEFAULT_OVERLAP;
            this.maxReadLength = DEFAULT_READ_LENGTH;
        }

        protected Params getParams() {
            return (Params)this.params;
        }
        @Override
        public File getOutputFile() {
            return this.outputFile;
        }

        @Override
        public void setOutputPrefix(String outputPrefix) {

            if (this.getOutputDir() != null) {
                this.setOutputFile(new File(this.getOutputDir(), outputPrefix + ".fa"));
            }
            else {
                this.setOutputFile(new File(outputPrefix + ".fa"));
            }

            super.setOutputPrefix(outputPrefix);
        }

        public void setOutputFile(File outputFile) {
            this.outputFile = outputFile;
        }

        public File getLibraryFile() {
            return libraryFile;
        }

        public void setLibraryFile(File libraryFile) {
            this.libraryFile = libraryFile;
        }

        public Integer getOverlap() {
            return overlap;
        }

        public void setOverlap(Integer overlap) {
            this.overlap = overlap;
        }

        public int getMaxReadLength() {
            return maxReadLength;
        }

        public void setMaxReadLength(int maxReadLength) {
            this.maxReadLength = maxReadLength;
        }

        public static void createLibraryFile(List<Library> libs, File outputLibFile) throws IOException {

            List<String> lines = new ArrayList<String>();

            int rank = 1;
            for (Library lib : libs) {

                StringJoiner sj = new StringJoiner("\n");

                sj.add("[LIB]");
                sj.add("max_rd_len=" + lib.getReadLength());
                sj.add("avg_ins=" + lib.getAverageInsertSize());
                sj.add(lib.getSeqOrientation() != null, "reverse_seq=", lib.getSeqOrientation() == Library.SeqOrientation.FORWARD_REVERSE ? "0" : "1");
                sj.add("asm_flags=3");
                sj.add("rank=" + rank);
                sj.add(lib.getFile1Type() == SeqFile.FileType.FASTQ, "q1=", lib.getFile1().getAbsolutePath());
                sj.add(lib.getFile2Type() == SeqFile.FileType.FASTQ, "q2=", lib.getFile2().getAbsolutePath());
                //sj.add(lib.getSeFile() != null && lib.getSeFile().getFileType() == SeqFile.FileType.FASTQ, "q=", lib.getSeFile().getFilePath());
                sj.add(lib.getFile1Type() == SeqFile.FileType.FASTA, "f1=", lib.getFile1().getAbsolutePath());
                sj.add(lib.getFile2Type() == SeqFile.FileType.FASTA, "f2=", lib.getFile2().getAbsolutePath());
                //sj.add(lib.getSeFile() != null && lib.getSeFile().getFileType() == SeqFile.FileType.FASTA, "f=", lib.getSeFile().getFilePath());

                lines.add(sj.toString() + "\n");

            }

            FileUtils.writeLines(outputLibFile, lines);
        }


        @Override
        public void parse(String args) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            if (this.getInputFile() != null)
                pvp.put(params.getInputScaffoldFile(), this.getInputFile().getAbsolutePath());

            if (this.libraryFile != null)
                pvp.put(params.getLibraryFile(), this.libraryFile.getAbsolutePath());

            if (this.getOutputFile() != null)
                pvp.put(params.getOutputFile(), this.getOutputFile().getAbsolutePath());

            if (this.overlap != DEFAULT_OVERLAP)
                pvp.put(params.getOverlap(), Integer.toString(this.overlap));

            if (this.maxReadLength != DEFAULT_READ_LENGTH)
                pvp.put(params.getMaxReadLength(), Integer.toString(this.maxReadLength));

            if (this.getThreads() > 1)
                pvp.put(params.getThreads(), Integer.toString(this.getThreads()));

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getLibraryFile())) {
                this.libraryFile = new File(value);
            }
            else if (param.equals(params.getOverlap())) {
                this.overlap = Integer.parseInt(value);
            }
            else if (param.equals(params.getMaxReadLength())) {
                this.maxReadLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getThreads())) {
                this.setThreads(Integer.parseInt(value));
            }
            else if (param.equals(params.getInputScaffoldFile())) {
                this.setInputFile(new File(value));
            }
            else if (param.equals(params.getOutputFile())) {
                this.setOutputFile(new File(value));
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

        private ConanParameter inputScaffoldFile;
        private ConanParameter libraryFile;
        private ConanParameter outputFile;
        private ConanParameter maxReadLength;
        private ConanParameter overlap;
        private ConanParameter threads;

        public Params() {

            this.inputScaffoldFile = new PathParameter(
                    "a",
                    "input scaffold file name",
                    false);

            this.libraryFile = new PathParameter(
                    "b",
                    "input library info file name",
                    false);

            this.outputFile = new PathParameter(
                    "o",
                    "output file name",
                    false);

            this.maxReadLength = new NumericParameter(
                    "l",
                    "maximal read length (<=155), default=100",
                    true);

            this.overlap = new NumericParameter(
                    "p",
                    "overlap param - kmer (<=31), default=25",
                    true);

            this.threads = new NumericParameter(
                    "t",
                    "thread number, default=1",
                    true);
        }

        public ConanParameter getInputScaffoldFile() {
            return inputScaffoldFile;
        }

        public ConanParameter getLibraryFile() {
            return libraryFile;
        }

        public ConanParameter getOutputFile() {
            return outputFile;
        }

        public ConanParameter getMaxReadLength() {
            return maxReadLength;
        }

        public ConanParameter getOverlap() {
            return overlap;
        }

        public ConanParameter getThreads() {
            return threads;
        }


        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.inputScaffoldFile,
                    this.libraryFile,
                    this.outputFile,
                    this.maxReadLength,
                    this.overlap,
                    this.threads
            };
        }

    }


}
