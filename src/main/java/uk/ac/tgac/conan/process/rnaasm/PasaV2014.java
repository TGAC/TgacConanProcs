package uk.ac.tgac.conan.process.rnaasm;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maplesod on 06/02/15.
 */
public class PasaV2014 extends AbstractConanProcess {

    public static final String EXE = "Launch_PASA_pipeline.pl";

    public PasaV2014(ConanExecutorService conanExecutorService) {
        this(conanExecutorService, new Args());
    }

    public PasaV2014(ConanExecutorService conanExecutorService, Args args) {
        super(EXE, args, new Params(), conanExecutorService);
    }

    public Args getArgs() {
        return (Args) this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "PASA_V2014";
    }


    public static enum Aligner {
        BLAT,
        GMAP;

        public static Aligner[] parseArray(String aligners) {

            String[] parts = aligners.trim().toUpperCase().split(",");

            List<Aligner> alignerList = new ArrayList<>();

            for(String p : parts) {

                if (p != null && !p.isEmpty()) {
                    alignerList.add(Aligner.valueOf(p));
                }
            }

            return alignerList.toArray(new Aligner[alignerList.size()]);
        }

        public static String arrayToString(Aligner[] aligners) {

            StringJoiner sj = new StringJoiner(",");

            for(Aligner a : aligners) {
                sj.add(a.toString());
            }

            return sj.toString().toLowerCase();
        }
    }

    public static class Args extends AbstractProcessArgs {

        private static final int DEFAULT_CPUS = 2;
        private static final int DEFAULT_MAX_INTRON_LENGTH = 100000;

        private File assemblyConfig;
        private boolean createDatabase;
        private boolean dropDatabase;
        private boolean runPipeline;
        private Aligner[] aligners;
        private File transcriptsFile;
        private int cpus;
        private File tdn;
        private File cufflinksGtf;
        private File genome;
        private int maxIntronLength;
        private boolean transdecoder;
        private double stringentAlignmentOverlap;
        private boolean altSplicing;
        private boolean transcribedIsAlignedOrientated;

        public Args() {
            super(new Params());
        }

        public Params getParams() {
            return (Params)this.params;
        }


        public File getAssemblyConfig() {
            return assemblyConfig;
        }

        public void setAssemblyConfig(File assemblyConfig) {
            this.assemblyConfig = assemblyConfig;
        }

        public boolean isCreateDatabase() {
            return createDatabase;
        }

        public void setCreateDatabase(boolean createDatabase) {
            this.createDatabase = createDatabase;
        }

        public boolean isDropDatabase() {
            return dropDatabase;
        }

        public void setDropDatabase(boolean dropDatabase) {
            this.dropDatabase = dropDatabase;
        }

        public boolean isRunPipeline() {
            return runPipeline;
        }

        public void setRunPipeline(boolean runPipeline) {
            this.runPipeline = runPipeline;
        }

        public Aligner[] getAligners() {
            return aligners;
        }

        public void setAligners(Aligner[] aligners) {
            this.aligners = aligners;
        }

        public File getTranscriptsFile() {
            return transcriptsFile;
        }

        public void setTranscriptsFile(File transcriptsFile) {
            this.transcriptsFile = transcriptsFile;
        }

        public int getCpus() {
            return cpus;
        }

        public void setCpus(int cpus) {
            this.cpus = cpus;
        }

        public File getTdn() {
            return tdn;
        }

        public void setTdn(File tdn) {
            this.tdn = tdn;
        }

        public File getCufflinksGtf() {
            return cufflinksGtf;
        }

        public void setCufflinksGtf(File cufflinksGtf) {
            this.cufflinksGtf = cufflinksGtf;
        }

        public File getGenome() {
            return genome;
        }

        public void setGenome(File genome) {
            this.genome = genome;
        }

        public int getMaxIntronLength() {
            return maxIntronLength;
        }

        public void setMaxIntronLength(int maxIntronLength) {
            this.maxIntronLength = maxIntronLength;
        }

        public boolean isTransdecoder() {
            return transdecoder;
        }

        public void setTransdecoder(boolean transdecoder) {
            this.transdecoder = transdecoder;
        }

        public double getStringentAlignmentOverlap() {
            return stringentAlignmentOverlap;
        }

        public void setStringentAlignmentOverlap(double stringentAlignmentOverlap) {
            this.stringentAlignmentOverlap = stringentAlignmentOverlap;
        }

        public boolean isAltSplicing() {
            return altSplicing;
        }

        public void setAltSplicing(boolean altSplicing) {
            this.altSplicing = altSplicing;
        }

        public boolean isTranscribedIsAlignedOrientated() {
            return transcribedIsAlignedOrientated;
        }

        public void setTranscribedIsAlignedOrientated(boolean transcribedIsAlignedOrientated) {
            this.transcribedIsAlignedOrientated = transcribedIsAlignedOrientated;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getAssemblyConfig())) {
                this.assemblyConfig = new File(value);
            }
            else if (param.equals(params.getCreateDatabase())) {
                this.createDatabase = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getDropDatabase())) {
                this.dropDatabase = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getRunPipeline())) {
                this.runPipeline = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getAligners())) {
                this.aligners = Aligner.parseArray(value.toUpperCase());
            }
            else if (param.equals(params.getTranscriptsFile())) {
                this.transcriptsFile = new File(value);
            }
            else if (param.equals(params.getCpus())) {
                this.cpus = Integer.parseInt(value);
            }
            else if (param.equals(params.getTdn())) {
                this.tdn = new File(value);
            }
            else if (param.equals(params.getCufflinksGtf())) {
                this.cufflinksGtf = new File(value);
            }
            else if (param.equals(params.getGenome())) {
                this.genome = new File(value);
            }
            else if (param.equals(params.getMaxIntronLength())) {
                this.maxIntronLength = Integer.parseInt(value);
            }
            else if (param.equals(params.getTransdecoder())) {
                this.transdecoder = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getStringentAlignmentOverlap())) {
                this.stringentAlignmentOverlap = Double.parseDouble(value);
            }
            else if (param.equals(params.getAltSplicing())) {
                this.altSplicing = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getTranscribedIsAlignedOrientated())) {
                this.transcribedIsAlignedOrientated = Boolean.parseBoolean(value);
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

            if (this.getAssemblyConfig() != null) {
                pvp.put(params.getAssemblyConfig(), this.getAssemblyConfig().getAbsolutePath());
            }

            if (this.isCreateDatabase()) {
                pvp.put(params.getCreateDatabase(), Boolean.toString(this.isCreateDatabase()));
            }

            if (this.isDropDatabase()) {
                pvp.put(params.getDropDatabase(), Boolean.toString(this.isDropDatabase()));
            }

            if (this.isRunPipeline()) {
                pvp.put(params.getRunPipeline(), Boolean.toString(this.isRunPipeline()));
            }

            if (this.getAligners() != null) {
                pvp.put(params.getAligners(), Aligner.arrayToString(this.getAligners()));
            }

            if (this.getTranscriptsFile() != null) {
                pvp.put(params.getTranscriptsFile(), this.getTranscriptsFile().getAbsolutePath());
            }

            if (this.getCpus() > DEFAULT_CPUS) {
                pvp.put(params.getCpus(), Integer.toString(this.getCpus()));
            }

            if (this.getTdn() != null) {
                pvp.put(params.getTdn(), this.getTdn().getAbsolutePath());
            }

            if (this.getCufflinksGtf() != null) {
                pvp.put(params.getCufflinksGtf(), this.getCufflinksGtf().getAbsolutePath());
            }

            if (this.getGenome() != null) {
                pvp.put(params.getGenome(), this.getGenome().getAbsolutePath());
            }

            if (this.getMaxIntronLength() != DEFAULT_MAX_INTRON_LENGTH) {
                pvp.put(params.getMaxIntronLength(), Integer.toString(this.getMaxIntronLength()));
            }

            if (this.isTransdecoder()) {
                pvp.put(params.getTransdecoder(), Boolean.toString(this.isTransdecoder()));
            }
            
            if (this.getStringentAlignmentOverlap() > 1.0) {
                pvp.put(params.getStringentAlignmentOverlap(), Double.toString(this.getStringentAlignmentOverlap()));
            }
            
            if (this.isAltSplicing()) {
                pvp.put(params.getAltSplicing(), Boolean.toString(this.isAltSplicing()));
            }
            
            if (this.isTranscribedIsAlignedOrientated()) {
                pvp.put(params.getTranscribedIsAlignedOrientated(), Boolean.toString(this.isTranscribedIsAlignedOrientated()));
            }
            
            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter assemblyConfig;
        private ConanParameter createDatabase;
        private ConanParameter dropDatabase;
        private ConanParameter runPipeline;
        private ConanParameter aligners;
        private ConanParameter transcriptsFile;
        private ConanParameter cpus;
        private ConanParameter tdn;
        private ConanParameter cufflinksGtf;
        private ConanParameter genome;
        private ConanParameter maxIntronLength;
        private ConanParameter transdecoder;
        private ConanParameter stringentAlignmentOverlap;
        private ConanParameter altSplicing;
        private ConanParameter transcribedIsAlignedOrientated;

        public Params() {

            this.assemblyConfig = new ParameterBuilder()
                    .shortName("c")
                    .description("configuration file")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.createDatabase = new ParameterBuilder()
                    .shortName("C")
                    .description("flag, create MYSQL database")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

            this.dropDatabase = new ParameterBuilder()
                    .shortName("r")
                    .description("flag, drop MYSQL database if -C is also given. This will DELETE all your data and it is irreversible.")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

            this.runPipeline = new ParameterBuilder()
                    .shortName("R")
                    .description("flag, run alignment/assembly pipeline.")
                    .argValidator(ArgValidator.OFF)
                    .isFlag(true)
                    .create();

            this.aligners = new ParameterBuilder()
                    .longName("ALIGNERS")
                    .description("aligners (available options include: gmap, blat... can run both using 'gmap,blat')")
                    .create();

            this.transcriptsFile = new ParameterBuilder()
                    .shortName("t")
                    .description("transcript db")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.cpus = new ParameterBuilder()
                    .longName("CPU")
                    .description("multithreading (default: 2)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.tdn = new ParameterBuilder()
                    .longName("TDN")
                    .description("file containing a list of accessions corresponding to Trinity (full) de novo assemblies (not genome-guided)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.cufflinksGtf = new ParameterBuilder()
                    .longName("cufflinks_gtf")
                    .description("incorporate cufflinks-generated transcripts")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.genome = new ParameterBuilder()
                    .shortName("g")
                    .description("genome sequence FASTA file (should contain annot db asmbl_id as header accession.)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.maxIntronLength = new ParameterBuilder()
                    .shortName("I")
                    .longName("MAX_INTRON_LENGTH")
                    .description("max intron length parameter passed to GMAP or BLAT  (default: 100000)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.transdecoder = new ParameterBuilder()
                    .longName("TRANSDECODER")
                    .description("flag, run transdecoder to identify candidate full-length coding transcripts")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.stringentAlignmentOverlap = new ParameterBuilder()
                    .longName("stringent_alignment_overlap")
                    .description("(suggested: 30.0)  overlapping transcripts must have this min % overlap to be clustered.")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.altSplicing = new ParameterBuilder()
                    .longName("ALT_SPLICE")
                    .description("flag, run alternative splicing analysis")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.transcribedIsAlignedOrientated = new ParameterBuilder()
                    .longName("transcribed_is_aligned_orient")
                    .description("flag for strand-specific RNA-Seq assemblies, the aligned orientation should correspond to the transcribed orientation.")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        public ConanParameter getAssemblyConfig() {
            return assemblyConfig;
        }

        public ConanParameter getCreateDatabase() {
            return createDatabase;
        }

        public ConanParameter getDropDatabase() {
            return dropDatabase;
        }

        public ConanParameter getRunPipeline() {
            return runPipeline;
        }

        public ConanParameter getAligners() {
            return aligners;
        }

        public ConanParameter getTranscriptsFile() {
            return transcriptsFile;
        }

        public ConanParameter getCpus() {
            return cpus;
        }

        public ConanParameter getTdn() {
            return tdn;
        }

        public ConanParameter getCufflinksGtf() {
            return cufflinksGtf;
        }

        public ConanParameter getGenome() {
            return genome;
        }

        public ConanParameter getMaxIntronLength() {
            return maxIntronLength;
        }

        public ConanParameter getTransdecoder() {
            return transdecoder;
        }

        public ConanParameter getStringentAlignmentOverlap() {
            return stringentAlignmentOverlap;
        }

        public ConanParameter getAltSplicing() {
            return altSplicing;
        }

        public ConanParameter getTranscribedIsAlignedOrientated() {
            return transcribedIsAlignedOrientated;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                assemblyConfig,
                createDatabase,
                dropDatabase,
                runPipeline,
                aligners,
                transcriptsFile,
                cpus,
                tdn,
                cufflinksGtf,
                genome,
                maxIntronLength,
                transdecoder,
                stringentAlignmentOverlap,
                altSplicing,
                transcribedIsAlignedOrientated
            };
        }
    }
}
