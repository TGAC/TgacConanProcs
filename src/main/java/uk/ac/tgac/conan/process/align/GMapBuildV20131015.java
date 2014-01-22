package uk.ac.tgac.conan.process.align;

import uk.ac.ebi.fgpt.conan.core.param.ArgValidator;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.param.ParameterBuilder;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 15/01/14
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
public class GMapBuildV20131015 extends AbstractConanProcess {

    public static final String EXE = "gmap_build";

    public GMapBuildV20131015() {
        this(new Args());
    }

    public GMapBuildV20131015(Args args) {
        super(EXE, args, new Params());
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "GMap_Build_V20131015";
    }


    public static class Args extends AbstractProcessArgs {

        public static final int DEFAULT_KMER = 15;
        public static final int DEFAULT_BASE_SIZE = 12;

        public static enum Sort {
            NONE,
            ALPHA,
            NUMERIC_ALPHA,
            CHROM;

            public String toArgString() {
                return this.name().replace('_','-').toLowerCase();
            }

            public static Sort fromArgString(String value) {
                return Sort.valueOf(value.replace('-','_').toUpperCase());
            }
        }

        private File genomeDir;
        private String genomeDB;
        private File genomeFile;
        private int kmer;
        private int baseSize;
        private Sort sort;
        private boolean gunzip;
        private String circular;
        private File mdFlag;
        private String compression;

        public Args() {

            super(new Params());

            this.genomeDir = null;
            this.genomeDB = "";
            this.genomeFile = null;
            this.kmer = DEFAULT_KMER;
            this.baseSize = DEFAULT_BASE_SIZE;
            this.sort = null;
            this.gunzip = false;
            this.circular = "";
            this.mdFlag = null;
            this.compression = "";
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

        public File getGenomeFile() {
            return genomeFile;
        }

        public void setGenomeFile(File genomeFile) {
            this.genomeFile = genomeFile;
        }

        public int getKmer() {
            return kmer;
        }

        public void setKmer(int kmer) {
            this.kmer = kmer;
        }

        public int getBaseSize() {
            return baseSize;
        }

        public void setBaseSize(int baseSize) {
            this.baseSize = baseSize;
        }

        public Sort getSort() {
            return sort;
        }

        public void setSort(Sort sort) {
            this.sort = sort;
        }

        public boolean isGunzip() {
            return gunzip;
        }

        public void setGunzip(boolean gunzip) {
            this.gunzip = gunzip;
        }

        public String getCircular() {
            return circular;
        }

        public void setCircular(String circular) {
            this.circular = circular;
        }

        public File getMdFlag() {
            return mdFlag;
        }

        public void setMdFlag(File mdFlag) {
            this.mdFlag = mdFlag;
        }

        public String getCompression() {
            return compression;
        }

        public void setCompression(String compression) {
            this.compression = compression;
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
            else if (param.equals(params.getKmer())) {
                this.kmer = Integer.parseInt(value);
            }
            else if (param.equals(params.getBaseSize())) {
                this.baseSize = Integer.parseInt(value);
            }
            else if (param.equals(params.getSort())) {
                this.sort = Sort.fromArgString(value);
            }
            else if (param.equals(params.getGunzip())) {
                this.gunzip = Boolean.parseBoolean(value);
            }
            else if (param.equals(params.getCircular())) {
                this.circular = value;
            }
            else if (param.equals(params.getMdFlag())) {
                this.mdFlag = new File(value);
            }
            else if (param.equals(params.getCompression())) {
                this.compression = value;
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(params.getGenomeFile())) {
                this.genomeFile = new File(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
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

            if (this.genomeFile != null) {
                pvp.put(params.getGenomeFile(), this.genomeFile.getAbsolutePath());
            }

            if (this.kmer != DEFAULT_KMER) {
                pvp.put(params.getKmer(), Integer.toString(this.kmer));
            }

            if (this.baseSize != DEFAULT_BASE_SIZE) {
                pvp.put(params.getBaseSize(), Integer.toString(this.baseSize));
            }

            if (this.sort != null) {
                pvp.put(params.getSort(), this.sort.toArgString());
            }

            if (this.gunzip) {
                pvp.put(params.getGunzip(), Boolean.toString(this.gunzip));
            }

            if (this.circular != null && !this.circular.isEmpty()) {
                pvp.put(params.getCircular(), this.circular);
            }

            if (this.mdFlag != null) {
                pvp.put(params.getMdFlag(), this.mdFlag.getAbsolutePath());
            }

            if (this.compression != null) {
                pvp.put(params.getCompression(), this.compression);
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        private ConanParameter genomeDir;
        private ConanParameter genomeDB;
        private ConanParameter genomeFile;
        private ConanParameter kmer;
        private ConanParameter baseSize;
        private ConanParameter sort;
        private ConanParameter gunzip;
        private ConanParameter circular;
        private ConanParameter mdFlag;
        private ConanParameter compression;

        public Params() {

            this.genomeDir = new ParameterBuilder()
                    .shortName("D")
                    .longName("dir")
                    .isOptional(false)
                    .description("Destination directory for installation (defaults to gmapdb directory specified at configure time)")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.genomeDB = new ParameterBuilder()
                    .shortName("d")
                    .longName("db")
                    .isOptional(false)
                    .description("Genome name")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.genomeFile = new ParameterBuilder()
                    .argIndex(0)
                    .isOption(false)
                    .isOptional(false)
                    .description("The genome file in fasta format")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.kmer = new ParameterBuilder()
                    .shortName("k")
                    .longName("kmer")
                    .description("k-mer value for genomic index (allowed: 16 or less, default is 15)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.baseSize = new ParameterBuilder()
                    .shortName("b")
                    .longName("basesize")
                    .description("Basesize for offsetscomp (if kmer chosen and not 15, default is kmer; else default is 12)")
                    .argValidator(ArgValidator.DIGITS)
                    .create();

            this.sort = new ParameterBuilder()
                    .shortName("s")
                    .longName("sort")
                    .description("Sort chromosomes using given method:\n" +
                            "\t\t\t        none - use chromosomes as found in FASTA file(s)\n" +
                            "\t\t\t        alpha - sort chromosomes alphabetically (chr10 before chr 1)\n" +
                            "\t\t\t        numeric-alpha - chr1, chr1U, chr2, chrM, chrU, chrX, chrY\n" +
                            "\t\t\t        chrom - chr1, chr2, chrM, chrX, chrY, chr1U, chrU\n")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.gunzip = new ParameterBuilder()
                    .shortName("g")
                    .longName("gunzip")
                    .description("Files are gzipped, so need to gunzip each file first")
                    .isFlag(true)
                    .argValidator(ArgValidator.OFF)
                    .create();

            this.circular = new ParameterBuilder()
                    .shortName("c")
                    .longName("circular")
                    .description("Circular chromosomes (either a list of chromosomes separated by a comma, or a filename " +
                            "containing circular chromosomes, one per line)")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();

            this.mdFlag = new ParameterBuilder()
                    .shortName("M")
                    .longName("mdflag")
                    .description("Use MD file from NCBI for mapping contigs to chromosomal coordinates")
                    .argValidator(ArgValidator.PATH)
                    .create();

            this.compression = new ParameterBuilder()
                    .shortName("z")
                    .longName("compression")
                    .description("Use given compression types (separated by commas; default is bitpack,gamma). \n" +
                            "                                bitpack - optimized for modern computers with SIMD instructions (recommended)\n" +
                            "                                gamma - old implementation.  Needed only for backward compatibility with old versions\n" +
                            "                                all - create all available compression types, currently bitpack and gamma\n" +
                            "                                none - do not compress offset files\n")
                    .argValidator(ArgValidator.DEFAULT)
                    .create();
        }

        public ConanParameter getGenomeDir() {
            return genomeDir;
        }

        public ConanParameter getGenomeDB() {
            return genomeDB;
        }

        public ConanParameter getGenomeFile() {
            return genomeFile;
        }

        public ConanParameter getKmer() {
            return kmer;
        }

        public ConanParameter getBaseSize() {
            return baseSize;
        }

        public ConanParameter getSort() {
            return sort;
        }

        public ConanParameter getGunzip() {
            return gunzip;
        }

        public ConanParameter getCircular() {
            return circular;
        }

        public ConanParameter getMdFlag() {
            return mdFlag;
        }

        public ConanParameter getCompression() {
            return compression;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.genomeDir,
                    this.genomeDB,
                    this.genomeFile,
                    this.kmer,
                    this.baseSize,
                    this.sort,
                    this.gunzip,
                    this.circular,
                    this.mdFlag,
                    this.compression
            };
        }
    }
}
