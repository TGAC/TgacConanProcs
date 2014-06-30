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
package uk.ac.tgac.conan.process.asmIO.clip;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.tgac.conan.process.asmIO.*;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 07/01/13
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
@MetaInfServices(AssemblyEnhancer.class)
public class SimpleClipper extends AbstractAssemblyEnhancer {


    public static final String NAME = "simple-clip";
    public static final AssemblyEnhancerType TYPE = AssemblyEnhancerType.CLIPPER;

    public SimpleClipper() {
        this(new Args());
    }

    public SimpleClipper(Args args) {
        super(NAME, TYPE, "", args, new Params(), null);
    }


    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public boolean execute(ExecutionContext executionContext) throws ProcessExecutionException, InterruptedException {

        try {
            clip();
        }
        catch(IOException ioe) {
            throw new ProcessExecutionException(-1, ioe);
        }

        return true;
    }


    public void clip() throws IOException {

        Args args = (Args)this.getProcessArgs();

        if (args.getInputAssembly() == null || !args.getInputAssembly().exists()) {
            throw new IOException("Input file does not exist");
        }

        BufferedReader reader = new BufferedReader(new FileReader(args.getInputAssembly()));
        PrintWriter writer = new PrintWriter(new FileWriter(args.getOutputFile()));

        // Ignore everything but the sequences
        // While loop handles multi-line sequences
        boolean firstLine = true;
        int nbSeqBases = 0;
        StringBuilder lastSeq = new StringBuilder();
        String lastHeader = "";
        String line = null;

        while ((line = reader.readLine()) != null) {

            if (!line.isEmpty()) {

                char firstChar = line.charAt(0);

                // If we have found a header line then increment analyser for this seq (unless this is the first time here)
                if (firstChar == '>') {

                    if (firstLine) {

                        // Store header
                        lastHeader = line;

                        firstLine = false;
                    } else {

                        // Print out the last sequence if it was big enough
                        if (nbSeqBases > args.getMinLen()) {
                            writer.println(lastHeader);
                            writer.println(lastSeq.toString());
                        }

                        // Store header and clean seq buffer
                        lastHeader = line;
                        lastSeq = new StringBuilder();
                        nbSeqBases = 0;
                    }
                } else {
                    lastSeq.append(line);
                    nbSeqBases += line.length();
                }
            }
        }

        writer.close();
        reader.close();
    }

    @MetaInfServices(AssemblyEnhancerArgs.class)
    public static class Args extends AbstractAssemblyEnhancerArgs {

        private File outputFile;
        private int minLen;


        public Args() {
            super(new Params(), NAME, TYPE);
        }

        protected Params getParams() {
            return (Params)this.params;
        }

        @Override
        public File getOutputFile() {
            return outputFile;
        }

        public void setOutputFile(File outputFile) {
            this.outputFile = outputFile;
        }

        public int getMinLen() {
            return minLen;
        }

        public void setMinLen(int minLen) {
            this.minLen = minLen;
        }

        @Override
        public void parse(String args) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            if (this.getInputAssembly() != null)
                pvp.put(params.getInputFile(), this.getInputAssembly().getAbsolutePath());

            if (this.getOutputFile() != null)
                pvp.put(params.getOutputFile(), this.getOutputFile().getAbsolutePath());

            if (this.getMinLen() > 1)
                pvp.put(params.getMinLen(), String.valueOf(this.getMinLen()));

            return pvp;
        }


        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getInputFile())) {
                this.setInputAssembly(new File(value));
            } else if (param.equals(params.getOutputFile())) {
                this.setOutputFile(new File(value));
            } else if (param.equals(params.getMinLen())) {
                this.setMinLen(Integer.parseInt(value));
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter inputFile;
        private ConanParameter outputFile;
        private ConanParameter minLen;

        public Params() {

            this.inputFile = new PathParameter(
                    "in",
                    "input scaffold file name",
                    false);

            this.outputFile = new PathParameter(
                    "o",
                    "output scaffold file name",
                    false);

            this.minLen = new NumericParameter(
                    "minlen",
                    "minimum scaffold length to leave in the output file, default=1000",
                    true);
        }


        public ConanParameter getInputFile() {
            return inputFile;
        }

        public ConanParameter getOutputFile() {
            return outputFile;
        }

        public ConanParameter getMinLen() {
            return minLen;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[]{
                    this.inputFile,
                    this.outputFile,
                    this.minLen
            };
        }
    }


}
