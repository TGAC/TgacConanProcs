package uk.ac.tgac.conan.process.align;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 15/01/14
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public class GSnapV20131015 extends AbstractConanProcess {
    @Override
    public String getName() {
        return "GSnap_V20131015";
    }

    public static class Args extends AbstractProcessArgs {

        private File genomeDir;
        private String genomeDB;
        private File query;

        public Args() {

            super(new Params());

            this.genomeDir = null;
            this.genomeDB = "";
            this.query = null;
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

        public File getQuery() {
            return query;
        }

        public void setQuery(File query) {
            this.query = query;
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
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getQuery())) {
                this.query = new File(value);
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

            if (this.query != null) {
                pvp.put(params.getQuery(), this.query.getAbsolutePath());
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter genomeDir;
        private ConanParameter genomeDB;
        private ConanParameter query;

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

            this.query = new ParameterBuilder()
                    .isOption(false)
                    .isOptional(false)
                    .argIndex(0)
                    .description("Query file containing reads to align")
                    .argValidator(ArgValidator.PATH)
                    .create();
        }

        public ConanParameter getGenomeDir() {
            return genomeDir;
        }

        public ConanParameter getGenomeDB() {
            return genomeDB;
        }

        public ConanParameter getQuery() {
            return query;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.genomeDir,
                    this.genomeDB,
                    this.query
            };
        }
    }
}
