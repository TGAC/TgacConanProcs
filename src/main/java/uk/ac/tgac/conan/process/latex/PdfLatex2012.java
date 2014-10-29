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
package uk.ac.tgac.conan.process.latex;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.context.DefaultExecutionResult;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionResult;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;

import java.io.File;

/**
 * User: maplesod
 * Date: 08/03/13
 * Time: 10:21
 */
public class PdfLatex2012 extends AbstractConanProcess {

    public static final String EXE = "pdflatex";

    public PdfLatex2012() {
        this(new Args());
    }

    public PdfLatex2012(Args args) {
        super(EXE, args, new Params());

        if (args.getOutputDir() != null) {
            String pwdFull = new File(".").getAbsolutePath();
            String pwd = pwdFull.substring(0, pwdFull.length() - 1);

            this.addPreCommand("mkdir -p " + args.getOutputDir().getAbsolutePath());
            this.addPreCommand("cd " + args.getOutputDir().getAbsolutePath());
            this.addPostCommand("cd " + pwd);
        }
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getCommand() {

        return EXE + " -interaction=nonstopmode " + this.getArgs().getTexFile();
    }

    @Override
    public String getName() {
        return "pdflatex-2012";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ExecutionResult execute(ExecutionContext executionContext) throws InterruptedException, ProcessExecutionException {

       // We have to run PDF Latex 3 times to ensure the document is fully compiled.
        for(int i = 1; i <= 3; i++) {

            super.execute(executionContext);
        }

        return new DefaultExecutionResult(this.getName(), 0);
    }

    public static class Args extends AbstractProcessArgs {

        private File texFile;
        private File outputDir;

        public Args() {

            super(new Params());

            this.texFile = null;
            this.outputDir = null;
        }

        public File getTexFile() {
            return texFile;
        }

        public void setTexFile(File texFile) {
            this.texFile = texFile;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

        }

        @Override
        public ParamMap getArgMap() {

            Params params = (Params)this.params;
            ParamMap pvp = new DefaultParamMap();

            if (this.texFile != null) {
                pvp.put(params.getTexFile(), this.texFile.getAbsolutePath());
            }

            if (this.outputDir != null) {
                pvp.put(params.getOutputDir(), this.outputDir.getAbsolutePath());
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;

            if (param.equals(params.getTexFile())) {
                this.texFile = new File(value);
            } else if (param.equals(params.getOutputDir())) {
                this.outputDir = new File(value);
            }else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter texFile;
        private ConanParameter outputDir;

        public Params() {
            this.texFile = new PathParameter(
                    "tex",
                    "The tex file to compile",
                    false);

            this.outputDir = new PathParameter(
                    "outputDir",
                    "The output directory in which the output from PDF latex should be stored",
                    false);
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.texFile,
                    this.outputDir
            };
        }

        public ConanParameter getTexFile() {
            return texFile;
        }

        public ConanParameter getOutputDir() {
            return outputDir;
        }
    }
}
