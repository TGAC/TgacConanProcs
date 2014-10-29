package uk.ac.tgac.conan.process.rnaasm;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;

/**
 * Created by maplesod on 25/07/14.
 */
public class FullLengtherNextV2013 extends AbstractConanProcess {

    protected static final String NAME = "FLN_V2013";
    protected static final String EXE = "full_lengther_next";

    public FullLengtherNextV2013() {
        this(null);
    }

    public FullLengtherNextV2013(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public FullLengtherNextV2013(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);

        this.setFormat(CommandLineFormat.POSIX_SPACE);
    }

    public void setup() {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.addPreCommand("cd " + this.getArgs().getOutputDir().getAbsolutePath());

        if (this.getArgs().blastDb != null) {
            this.addPreCommand("export BLASTDB=" + this.getArgs().blastDb.getAbsolutePath());
        }

        this.addPostCommand("cd " + pwd);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static class Args extends AbstractProcessArgs {

        public static final double DEFAULT_EVALUE = 1.0e-25;
        public static final double DEFAULT_IDENTITY_PERCENT = 45.0;
        public static final int DEFAULT_MAX_DISTANCE = 15;
        public static final int DEFAULT_CHUNK_SIZE = 200;

        private File outputDir;
        private File fastaFile;
        private String taxonGroup;
        private File blastDb;
        private File userDb;
        private double eValue;
        private double identityPercent;
        private int maxDistance;
        private boolean chimeraDetection;
        private int workers;
        private int chunkSize;
        private String serverIp;
        private int port;

        public Args() {
            super(new Params());

            this.outputDir = null;
            this.fastaFile = null;
            this.taxonGroup = "";
            this.blastDb = null;
            this.userDb = null;
            this.eValue = DEFAULT_EVALUE;
            this.identityPercent = DEFAULT_IDENTITY_PERCENT;
            this.maxDistance = DEFAULT_MAX_DISTANCE;
            this.chimeraDetection = false;
            this.workers = 1;
            this.chunkSize = DEFAULT_CHUNK_SIZE;
            this.serverIp = "0.0.0";
            this.port = 0;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(File outputDir) {
            this.outputDir = outputDir;
        }

        public File getFastaFile() {
            return fastaFile;
        }

        public void setFastaFile(File fastaFile) {
            this.fastaFile = fastaFile;
        }

        public String getTaxonGroup() {
            return taxonGroup;
        }

        public void setTaxonGroup(String taxonGroup) {
            this.taxonGroup = taxonGroup;
        }

        public File getBlastDb() {
            return blastDb;
        }

        public void setBlastDb(File blastDb) {
            this.blastDb = blastDb;
        }

        public File getUserDb() {
            return userDb;
        }

        public void setUserDb(File userDb) {
            this.userDb = userDb;
        }

        public double geteValue() {
            return eValue;
        }

        public void seteValue(double eValue) {
            this.eValue = eValue;
        }

        public double getIdentityPercent() {
            return identityPercent;
        }

        public void setIdentityPercent(double identityPercent) {
            this.identityPercent = identityPercent;
        }

        public int getMaxDistance() {
            return maxDistance;
        }

        public void setMaxDistance(int maxDistance) {
            this.maxDistance = maxDistance;
        }

        public boolean isChimeraDetection() {
            return chimeraDetection;
        }

        public void setChimeraDetection(boolean chimeraDetection) {
            this.chimeraDetection = chimeraDetection;
        }

        public int getWorkers() {
            return workers;
        }

        public void setWorkers(int workers) {
            this.workers = workers;
        }

        public int getChunkSize() {
            return chunkSize;
        }

        public void setChunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
        }

        public String getServerIp() {
            return serverIp;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getFastaFile())) {
                this.fastaFile = new File(value);
            }
            else if (param.equals(params.getTaxonGroup())) {
                this.taxonGroup = value;
            }
            else if (param.equals(params.getUserDb())) {
                this.userDb = new File(value);
            }
            else if (param.equals(params.geteValue())) {
                this.eValue = Double.parseDouble(value);
            }
            else if (param.equals(params.getIdentityPercent())) {
                this.identityPercent = Double.parseDouble(value);
            }
            else if (param.equals(params.getMaxDistance())) {
                this.maxDistance = Integer.parseInt(value);
            }
            else if (param.equals(params.getChimeraDetection())) {
                this.chimeraDetection = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getWorkers())) {
                this.workers = Integer.parseInt(value);
            }
            else if (param.equals(params.getChunkSize())) {
                this.chunkSize = Integer.parseInt(value);
            }
            else if (param.equals(params.getServerIp())) {
                this.serverIp = value;
            }
            else if (param.equals(params.getPort())) {
                this.port = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

        }

        @Override
        protected void parseCommandLine(CommandLine commandLine) {

        }

        @Override
        public ParamMap getArgMap() {

            Params params = this.getParams();
            ParamMap pvp = new DefaultParamMap();

            // Forcing use of the symbolic link for input
            if (this.fastaFile != null) {
                pvp.put(params.getFastaFile(), this.fastaFile.getAbsolutePath());
            }

            if (this.taxonGroup != null && !this.taxonGroup.isEmpty()) {
                pvp.put(params.getTaxonGroup(), this.taxonGroup);
            }

            if (this.userDb != null) {
                pvp.put(params.getUserDb(), this.userDb.getAbsolutePath());
            }

            if (this.eValue != DEFAULT_EVALUE) {
                pvp.put(params.geteValue(), Double.toString(this.eValue));
            }

            if (this.identityPercent != DEFAULT_IDENTITY_PERCENT) {
                pvp.put(params.getIdentityPercent(), Double.toString(this.identityPercent));
            }

            if (this.maxDistance != DEFAULT_MAX_DISTANCE) {
                pvp.put(params.getMaxDistance(), Integer.toString(this.maxDistance));
            }

            if (this.chimeraDetection) {
                pvp.put(params.getChimeraDetection(), Boolean.toString(this.chimeraDetection));
            }

            if (this.workers > 1) {
                pvp.put(params.getWorkers(), Integer.toString(this.workers));
            }

            if (this.chunkSize > 0 && this.chunkSize != DEFAULT_CHUNK_SIZE) {
                pvp.put(params.getChunkSize(), Integer.toString(this.chunkSize));
            }

            if (this.serverIp != null && !this.serverIp.isEmpty()) {
                pvp.put(params.getServerIp(), this.serverIp);
            }

            if (this.port > 0) {
                pvp.put(params.getPort(), Integer.toString(this.port));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter fastaFile;
        private ConanParameter taxonGroup;
        private ConanParameter userDb;
        private ConanParameter eValue;
        private ConanParameter identityPercent;
        private ConanParameter maxDistance;
        private ConanParameter chimeraDetection;
        private ConanParameter workers;
        private ConanParameter chunkSize;
        private ConanParameter serverIp;
        private ConanParameter port;

        public Params() {

            this.fastaFile = new ParameterBuilder()
                    .shortName("f")
                    .longName("fasta")
                    .description("Fasta input file (transcripts)")
                    .argValidator(ArgValidator.PATH)
                    .isOptional(false)
                    .create();

            this.taxonGroup = new ParameterBuilder()
                    .shortName("g")
                    .longName("taxon_group")
                    .description("Taxon group, required to use the best databases:\n" +
                            "\t\t\t\t\tfungi\n" +
                            "\t\t\t\t\thuman\n" +
                            "\t\t\t\t\tinvertebrates\n" +
                            "\t\t\t\t\tmammals\n" +
                            "\t\t\t\t\tplants\n" +
                            "\t\t\t\t\trodents\n" +
                            "\t\t\t\t\tvertebrates")
                    .isOptional(false)
                    .create();

            this.userDb = new ParameterBuilder()
                    .shortName("u")
                    .longName("user_db")
                    .description("User blast+ database")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.eValue = new ParameterBuilder()
                    .shortName("e")
                    .longName("evalue")
                    .description("e value threshold to consider as reliable the orthologue sequence. Default=1.0e-25")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.identityPercent = new ParameterBuilder()
                    .shortName("i")
                    .longName("identity_percent")
                    .description("identity percent threshold to consider as reliable the sequence similarity. Default=45.00")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.maxDistance = new ParameterBuilder()
                    .shortName("m")
                    .longName("max_distance")
                    .description("maximal distance between query and subject gene boundaries to be qualified as putative, the less distance the more strict. Default=15")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.chimeraDetection = new ParameterBuilder()
                    .shortName("q")
                    .longName("chimera_detection")
                    .description("apply chimera detection mode")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.workers = new ParameterBuilder()
                    .shortName("w")
                    .longName("workers")
                    .description("Number of CPUs, or a file containing machine names to launch workers with ssh")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.chunkSize = new ParameterBuilder()
                    .shortName("c")
                    .longName("chunk_size")
                    .description("Number of sequences processed in each block when parallelization is used. Default=200")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.serverIp = new ParameterBuilder()
                    .shortName("s")
                    .longName("server")
                    .description("Server ip. Can use a partial ip to select the apropriate interface")
                    .create();

            this.port = new ParameterBuilder()
                    .shortName("p")
                    .longName("port")
                    .description("Server port")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

        }

        public ConanParameter getFastaFile() {
            return fastaFile;
        }

        public ConanParameter getTaxonGroup() {
            return taxonGroup;
        }

        public ConanParameter getUserDb() {
            return userDb;
        }

        public ConanParameter geteValue() {
            return eValue;
        }

        public ConanParameter getIdentityPercent() {
            return identityPercent;
        }

        public ConanParameter getMaxDistance() {
            return maxDistance;
        }

        public ConanParameter getChimeraDetection() {
            return chimeraDetection;
        }

        public ConanParameter getWorkers() {
            return workers;
        }

        public ConanParameter getChunkSize() {
            return chunkSize;
        }

        public ConanParameter getServerIp() {
            return serverIp;
        }

        public ConanParameter getPort() {
            return port;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    fastaFile,
                    taxonGroup,
                    userDb,
                    eValue,
                    identityPercent,
                    maxDistance,
                    chimeraDetection,
                    workers,
                    chunkSize,
                    serverIp,
                    port
            };
        }
    }
}

