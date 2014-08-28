package uk.ac.tgac.conan.process.rnaasm;

import org.apache.commons.cli.CommandLine;
import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.*;
import uk.ac.ebi.fgpt.conan.service.ConanExecutorService;

import java.io.File;

/**
 * Created by maplesod on 01/08/14.
 */
public class GTSV0 extends AbstractConanProcess {

    protected static final String NAME = "GTS_V0";
    protected static final String EXE = "gts";

    public GTSV0() {
        this(null);
    }

    public GTSV0(ConanExecutorService ces) {
        this(ces, new Args());
    }

    public GTSV0(ConanExecutorService ces, Args args) {
        super(EXE, args, new Params(), ces);
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static class Args extends AbstractProcessArgs {

        public static double DEFAULT_CDS_LEN_RATIO = 0.4;
        public static double DEFAULT_CDNA_LEN_RATIO = 0.5;
        public static int DEFAULT_WINDOW_SIZE = 1000;

        private File genomicGff;
        private File transcriptGff;
        private File gtf;
        private File flnDir;
        private File output;
        private boolean includePutative;
        private double cdsLenRatio;
        private double cdnaLenRatio;
        private int windowSize;
        private boolean all;

        public Args() {
            super(new Params());

            this.genomicGff = null;
            this.transcriptGff = null;
            this.gtf = null;
            this.flnDir = null;
            this.output = null;
            this.includePutative = false;
            this.cdsLenRatio = DEFAULT_CDS_LEN_RATIO;
            this.cdnaLenRatio = DEFAULT_CDNA_LEN_RATIO;
            this.windowSize = DEFAULT_WINDOW_SIZE;
            this.all = false;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public File getPassFile() {
            return new File(output.getAbsolutePath() + ".pass.gff3");
        }

        public File getFailFile() {
            return new File(output.getAbsolutePath() + ".fail.gff3");
        }

        public File getGenomicGff() {
            return genomicGff;
        }

        public void setGenomicGff(File genomicGff) {
            this.genomicGff = genomicGff;
        }

        public File getTranscriptGff() {
            return transcriptGff;
        }

        public void setTranscriptGff(File transcriptGff) {
            this.transcriptGff = transcriptGff;
        }

        public File getGtf() {
            return gtf;
        }

        public void setGtf(File gtf) {
            this.gtf = gtf;
        }

        public File getFlnDir() {
            return flnDir;
        }

        public void setFlnDir(File flnDir) {
            this.flnDir = flnDir;
        }

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        public boolean isIncludePutative() {
            return includePutative;
        }

        public void setIncludePutative(boolean includePutative) {
            this.includePutative = includePutative;
        }

        public double getCdsLenRatio() {
            return cdsLenRatio;
        }

        public void setCdsLenRatio(double cdsLenRatio) {
            this.cdsLenRatio = cdsLenRatio;
        }

        public double getCdnaLenRatio() {
            return cdnaLenRatio;
        }

        public void setCdnaLenRatio(double cdnaLenRatio) {
            this.cdnaLenRatio = cdnaLenRatio;
        }

        public int getWindowSize() {
            return windowSize;
        }

        public void setWindowSize(int windowSize) {
            this.windowSize = windowSize;
        }

        public boolean isAll() {
            return all;
        }

        public void setAll(boolean all) {
            this.all = all;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getGenomicGff())) {
                this.genomicGff = new File(value);
            }
            else if (param.equals(params.getTranscriptGff())) {
                this.transcriptGff = new File(value);
            }
            else if (param.equals(params.getGtf())) {
                this.gtf = new File(value);
            }
            else if (param.equals(params.getFlnDir())) {
                this.flnDir = new File(value);
            }
            else if (param.equals(params.getOutput())) {
                this.output = new File(value);
            }
            else if (param.equals(params.getIncludePutative())) {
                this.includePutative = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getCdsLenRatio())) {
                this.cdsLenRatio = Double.parseDouble(value);
            }
            else if (param.equals(params.getCdnaLenRatio())) {
                this.cdnaLenRatio = Double.parseDouble(value);
            }
            else if (param.equals(params.getWindowSize())) {
                this.windowSize = Integer.parseInt(value);
            }
            else if (param.equals(params.getAll())) {
                this.all = Boolean.parseBoolean(value);
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
            if (this.genomicGff != null) {
                pvp.put(params.getGenomicGff(), this.genomicGff.getAbsolutePath());
            }

            if (this.transcriptGff != null) {
                pvp.put(params.getTranscriptGff(), this.transcriptGff.getAbsolutePath());
            }

            if (this.gtf != null) {
                pvp.put(params.getGtf(), this.gtf.getAbsolutePath());
            }

            if (this.flnDir != null) {
                pvp.put(params.getFlnDir(), this.flnDir.getAbsolutePath());
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            if (this.includePutative) {
                pvp.put(params.getIncludePutative(), Boolean.toString(this.includePutative));
            }

            if (this.cdsLenRatio != DEFAULT_CDS_LEN_RATIO) {
                pvp.put(params.getCdsLenRatio(), Double.toString(this.cdsLenRatio));
            }

            if (this.cdnaLenRatio != DEFAULT_CDNA_LEN_RATIO) {
                pvp.put(params.getCdnaLenRatio(), Double.toString(this.cdnaLenRatio));
            }

            if (this.windowSize != DEFAULT_WINDOW_SIZE) {
                pvp.put(params.getWindowSize(), Integer.toString(this.windowSize));
            }

            if (this.all) {
                pvp.put(params.getAll(), Boolean.toString(this.all));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter genomicGff;
        private ConanParameter transcriptGff;
        private ConanParameter gtf;
        private ConanParameter flnDir;
        private ConanParameter output;
        private ConanParameter includePutative;
        private ConanParameter cdsLenRatio;
        private ConanParameter cdnaLenRatio;
        private ConanParameter windowSize;
        private ConanParameter all;

        public Params() {

            this.genomicGff = new ParameterBuilder()
                    .shortName("g")
                    .longName("genomic_gff")
                    .description("GFF3 file containing the genomic coordinates for the transcript features.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.transcriptGff = new ParameterBuilder()
                    .shortName("t")
                    .longName("transcript_gff")
                    .description("GFF3 file containing the transcript coordinates for the transcript features.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.gtf = new ParameterBuilder()
                    .longName("gtf")
                    .description("GTF file containing transcripts.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.flnDir = new ParameterBuilder()
                    .shortName("f")
                    .longName("fln_dir")
                    .description("Full lengther results directory, containing the \"dbannotated.txt\" and \"new_coding.txt\" files.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.output = new ParameterBuilder()
                    .shortName("o")
                    .longName("output")
                    .description("The output prefix for all output files generated.")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.includePutative = new ParameterBuilder()
                    .longName("include_putative")
                    .description("Include transcripts with no full lengther homology hit, providing it has a full lengther new_coding hit.")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.cdsLenRatio = new ParameterBuilder()
                    .longName("cds_ratio")
                    .description("Min percentage length of CDS content relative to full length transcripts for hits with homology.  0.0 -> 1.0.  Default: 0.4")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.cdnaLenRatio = new ParameterBuilder()
                    .longName("cdna_ratio")
                    .description("Min percentage length of cDNA (exon) content relative to full length transcripts for hits with homology.  0.0 -> 1.0.  Default: 0.5")
                    .argValidator(ArgValidator.FLOAT)
                    .create();

            this.windowSize = new ParameterBuilder()
                    .longName("window_size")
                    .description("The gap to enforce between genes.")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.all = new ParameterBuilder()
                    .shortName("a")
                    .longName("all")
                    .description("Whether or not to output GFF entries filtered at each stage.")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();
        }

        public ConanParameter getGenomicGff() {
            return genomicGff;
        }

        public ConanParameter getTranscriptGff() {
            return transcriptGff;
        }

        public ConanParameter getGtf() {
            return gtf;
        }

        public ConanParameter getFlnDir() {
            return flnDir;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getIncludePutative() {
            return includePutative;
        }

        public ConanParameter getCdsLenRatio() {
            return cdsLenRatio;
        }

        public ConanParameter getCdnaLenRatio() {
            return cdnaLenRatio;
        }

        public ConanParameter getWindowSize() {
            return windowSize;
        }

        public ConanParameter getAll() {
            return all;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.genomicGff,
                    this.transcriptGff,
                    this.gtf,
                    this.flnDir,
                    this.output,
                    this.includePutative,
                    this.cdsLenRatio,
                    this.cdnaLenRatio,
                    this.windowSize,
                    this.all
            };
        }
    }
}
