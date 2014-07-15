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
package uk.ac.tgac.conan.process.r;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * User: maplesod
 * Date: 16/01/13
 * Time: 16:13
 */
public class RV2122 extends AbstractConanProcess {

    public static final String EXE = "Rscript";

    public RV2122() {
        this(new Args());
    }

    public RV2122(Args args) {
        super(EXE, args, new Params());
    }

    @Override
    public String getName() {
        return "R_V2.12.2";
    }


    public static class Args extends AbstractProcessArgs {

        private List<String> args;
        private File script;
        private File output;

        public Args() {
            super(new Params());

            this.args = null;
            this.script = null;
            this.output = null;
        }

        public List<String> getArgs() {
            return args;
        }

        public void setArgs(List<String> args) {
            this.args = args;
        }

        public File getScript() {
            return script;
        }

        public void setScript(File script) {
            this.script = script;
        }

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        @Override
        public void parseCommandLine(CommandLine cmdLine) {

        }

        @Override
        public ParamMap getArgMap() {

            Params params = (Params)this.params;
            ParamMap pvp = new DefaultParamMap();

            if (this.script != null) {
                pvp.put(params.getScript(), this.script.getPath());
            }

            if (this.args != null) {
                pvp.put(params.getArgs(), StringUtils.join(this.args, " "));
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), "> " + this.output.getPath());
            }

            return pvp;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = (Params)this.params;

            if (param.equals(params.getArgs())) {
                this.args = Arrays.asList(value.split(" "));
            } else if (param.equals(params.getScript())) {
                this.script = new File(value);
            } else if (param.equals(params.getOutput())) {
                this.output = new File(value);
            } else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter args;
        private ConanParameter script;
        private ConanParameter output;

        public Params() {

            this.args = new ParameterBuilder()
                    .longName("args")
                    .description("Any arguments that should be provided to the script")
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(1)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.script = new ParameterBuilder()
                    .longName("script")
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .argValidator(ArgValidator.PATH)
                    .description("The R script to execute")
                    .create();

            this.output = new ParameterBuilder()
                    .longName("output")
                    .description("The location to store output from R")
                    .argIndex(2)
                    .argValidator(ArgValidator.PATH)
                    .isOption(false)
                    .isOptional(false)
                    .create();
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.args,
                    this.script,
                    this.output
            };
        }

        public ConanParameter getArgs() {
            return args;
        }

        public ConanParameter getScript() {
            return script;
        }

        public ConanParameter getOutput() {
            return output;
        }

    }
}
