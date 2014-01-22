package uk.ac.tgac.conan.process.rnaasm;

import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.AbstractProcessParams;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.tgac.conan.core.util.PathUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 16/01/14
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class TrinityV20130814 extends AbstractConanProcess {

    public static final String EXE = "Trinity.pl";

    public TrinityV20130814() {
        this(new Args());
    }

    public TrinityV20130814(Args args) {
        super(EXE, args, new Params());
    }

    public Args getArgs() {
        return (Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Trinity_V2013_08_14";
    }

    public static class Args extends AbstractProcessArgs {

        private static final int DEFAULT_MIN_CONTIG_LENGTH = 200;
        private static final int DEFAULT_CPUS = 2;
        private static final int DEFAULT_INCHWORM_MIN_KMER_CVG = 1;
        private static final int DEFAULT_CHYSALIS_MAX_READS_PER_GRAPH = 200000;
        private static final int DEFAULT_BUTTERFLY_GROUP_PAIRS_DISTANCE = 500;
        private static final int DEFAULT_BUTTERFLY_PATH_REINF_DISTANCE = -1;


        private Params params = new Params();

        public static enum SeqType {
            FA,
            FQ;

            public String toArgString() {
                return this.name().toLowerCase();
            }
        }

        public static enum SSLibType {
            RF,
            FR,
            F,
            R;
        }

        // General
        private SeqType seqType;
        private int jellyfishMemory;
        private File[] leftInput;
        private File[] rightInput;
        private File[] singleInput;
        private SSLibType ssLibType;
        private File output;
        private int cpu;
        private int minContigLength;
        private boolean genomeGuided;

        // Inchworm
        private int inchwormMinKmerCoverage;

        // Chysalis
        private int chysalisMaxReadsPerGraph;

        // Butterfly
        private int butterflyGroupPairsDistance;
        private int butterflyPathReinforcementDistance;

        public Args() {
            super(new Params());
            this.seqType = null;
            this.jellyfishMemory = 20;
            this.leftInput = null;
            this.rightInput = null;
            this.singleInput = null;
            this.ssLibType = null;
            this.output = null;
            this.cpu = DEFAULT_CPUS;
            this.minContigLength = DEFAULT_MIN_CONTIG_LENGTH;
            this.genomeGuided = false;
            this.inchwormMinKmerCoverage = DEFAULT_INCHWORM_MIN_KMER_CVG;
            this.chysalisMaxReadsPerGraph = DEFAULT_CHYSALIS_MAX_READS_PER_GRAPH;
            this.butterflyGroupPairsDistance = DEFAULT_BUTTERFLY_GROUP_PAIRS_DISTANCE;
            this.butterflyPathReinforcementDistance = DEFAULT_BUTTERFLY_PATH_REINF_DISTANCE;
        }

        public Params getParams() {
            return (Params)this.params;
        }

        public SeqType getSeqType() {
            return seqType;
        }

        public void setSeqType(SeqType seqType) {
            this.seqType = seqType;
        }

        public int getJellyfishMemory() {
            return jellyfishMemory;
        }

        public void setJellyfishMemory(int jellyfishMemory) {
            this.jellyfishMemory = jellyfishMemory;
        }

        public File[] getLeftInput() {
            return leftInput;
        }

        public void setLeftInput(File[] leftInput) {
            this.leftInput = leftInput;
        }

        public File[] getRightInput() {
            return rightInput;
        }

        public void setRightInput(File[] rightInput) {
            this.rightInput = rightInput;
        }

        public File[] getSingleInput() {
            return singleInput;
        }

        public void setSingleInput(File[] singleInput) {
            this.singleInput = singleInput;
        }

        public SSLibType getSsLibType() {
            return ssLibType;
        }

        public void setSsLibType(SSLibType ssLibType) {
            this.ssLibType = ssLibType;
        }

        public File getOutput() {
            return output;
        }

        public void setOutput(File output) {
            this.output = output;
        }

        public int getCpu() {
            return cpu;
        }

        public void setCpu(int cpu) {
            this.cpu = cpu;
        }

        public int getMinContigLength() {
            return minContigLength;
        }

        public void setMinContigLength(int minContigLength) {
            this.minContigLength = minContigLength;
        }

        public boolean isGenomeGuided() {
            return genomeGuided;
        }

        public void setGenomeGuided(boolean genomeGuided) {
            this.genomeGuided = genomeGuided;
        }

        public int getInchwormMinKmerCoverage() {
            return inchwormMinKmerCoverage;
        }

        public void setInchwormMinKmerCoverage(int inchwormMinKmerCoverage) {
            this.inchwormMinKmerCoverage = inchwormMinKmerCoverage;
        }

        public int getChysalisMaxReadsPerGraph() {
            return chysalisMaxReadsPerGraph;
        }

        public void setChysalisMaxReadsPerGraph(int chysalisMaxReadsPerGraph) {
            this.chysalisMaxReadsPerGraph = chysalisMaxReadsPerGraph;
        }

        public int getButterflyGroupPairsDistance() {
            return butterflyGroupPairsDistance;
        }

        public void setButterflyGroupPairsDistance(int butterflyGroupPairsDistance) {
            this.butterflyGroupPairsDistance = butterflyGroupPairsDistance;
        }

        public int getButterflyPathReinforcementDistance() {
            return butterflyPathReinforcementDistance;
        }

        public void setButterflyPathReinforcementDistance(int butterflyPathReinforcementDistance) {
            this.butterflyPathReinforcementDistance = butterflyPathReinforcementDistance;
        }

        @Override
        protected void setOptionFromMapEntry(ConanParameter param, String value) {

            Params params = this.getParams();

            if (param.equals(this.params.getSeqType())) {
                this.seqType = SeqType.valueOf(value.toUpperCase());
            }
            else if (param.equals(this.params.getJellyfishMemory())) {
                this.jellyfishMemory = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getLeftInput())) {
                this.leftInput = PathUtils.splitPaths(value, " ");
            }
            else if (param.equals(this.params.getRightInput())) {
                this.rightInput = PathUtils.splitPaths(value, " ");
            }
            else if (param.equals(this.params.getSingleInput())) {
                this.singleInput = PathUtils.splitPaths(value, " ");
            }
            else if (param.equals(this.params.getSsLibType())) {
                this.ssLibType = SSLibType.valueOf(value.toUpperCase());
            }
            else if (param.equals(this.params.getOutput())) {
                this.output = new File(value);
            }
            else if (param.equals(this.params.getCpu())) {
                this.cpu = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getMinContigLength())) {
                this.minContigLength = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getGenomeGuided())) {
                this.genomeGuided = Boolean.parseBoolean(value);
            }
            else if (param.equals(this.params.getInchwormMinKmerCoverage())) {
                this.inchwormMinKmerCoverage = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getChysalisMaxReadsPerGraph())) {
                this.chysalisMaxReadsPerGraph = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getButterflyGroupPairsDistance())) {
                this.butterflyGroupPairsDistance = Integer.parseInt(value);
            }
            else if (param.equals(this.params.getButterflyPathReinforcementDistance())) {
                this.butterflyPathReinforcementDistance = Integer.parseInt(value);
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }

        @Override
        protected void setArgFromMapEntry(ConanParameter param, String value) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void parse(String args) throws IOException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ParamMap getArgMap() {

            ParamMap pvp = new DefaultParamMap();

            if (this.seqType != null) {
                pvp.put(params.getSeqType(), this.seqType.toArgString());
            }

            pvp.put(params.getJellyfishMemory(), Integer.toString(this.jellyfishMemory));

            if (this.leftInput != null && this.leftInput.length > 0) {
                pvp.put(params.getLeftInput(), PathUtils.joinAbsolutePaths(this.leftInput, " "));
            }

            if (this.rightInput != null && this.rightInput.length > 0) {
                pvp.put(params.getRightInput(), PathUtils.joinAbsolutePaths(this.rightInput, " "));
            }

            if (this.singleInput != null && this.singleInput.length > 0) {
                pvp.put(params.getSingleInput(), PathUtils.joinAbsolutePaths(this.singleInput, " "));
            }

            if (this.ssLibType != null) {
                pvp.put(params.getSsLibType(), this.ssLibType.toString());
            }

            if (this.output != null) {
                pvp.put(params.getOutput(), this.output.getAbsolutePath());
            }

            if (this.cpu != DEFAULT_CPUS && this.cpu > 0) {
                pvp.put(params.getCpu(), Integer.toString(this.cpu));
            }

            if (this.minContigLength != DEFAULT_MIN_CONTIG_LENGTH && this.minContigLength > 0) {
                pvp.put(params.getMinContigLength(), Integer.toString(this.minContigLength));
            }

            if (this.genomeGuided) {
                pvp.put(params.getGenomeGuided(), Boolean.toString(this.genomeGuided));
            }

            if (this.inchwormMinKmerCoverage != DEFAULT_INCHWORM_MIN_KMER_CVG && this.inchwormMinKmerCoverage > 0) {
                pvp.put(params.getInchwormMinKmerCoverage(), Integer.toString(this.inchwormMinKmerCoverage));
            }

            if (this.butterflyGroupPairsDistance != DEFAULT_BUTTERFLY_GROUP_PAIRS_DISTANCE && this.butterflyGroupPairsDistance > 0) {
                pvp.put(params.getButterflyGroupPairsDistance(), Integer.toString(this.butterflyGroupPairsDistance));
            }

            if (this.butterflyPathReinforcementDistance != DEFAULT_BUTTERFLY_PATH_REINF_DISTANCE && this.butterflyPathReinforcementDistance > 0) {
                pvp.put(params.getButterflyPathReinforcementDistance(), Integer.toString(this.butterflyPathReinforcementDistance));
            }

            return pvp;
        }
    }

    public static class Params extends AbstractProcessParams {

        // General
        private ConanParameter seqType;
        private ConanParameter jellyfishMemory;
        private ConanParameter leftInput;
        private ConanParameter rightInput;
        private ConanParameter singleInput;
        private ConanParameter ssLibType;
        private ConanParameter output;
        private ConanParameter cpu;
        private ConanParameter minContigLength;
        private ConanParameter genomeGuided;

        // Inchworm
        private ConanParameter inchwormMinKmerCoverage;

        // Chysalis
        private ConanParameter chysalisMaxReadsPerGraph;

        // Butterfly
        private ConanParameter butterflyGroupPairsDistance;
        private ConanParameter butterflyPathReinforcementDistance;


        public Params() {
        }

        public ConanParameter getSeqType() {
            return seqType;
        }

        public ConanParameter getJellyfishMemory() {
            return jellyfishMemory;
        }

        public ConanParameter getLeftInput() {
            return leftInput;
        }

        public ConanParameter getRightInput() {
            return rightInput;
        }

        public ConanParameter getSingleInput() {
            return singleInput;
        }

        public ConanParameter getSsLibType() {
            return ssLibType;
        }

        public ConanParameter getOutput() {
            return output;
        }

        public ConanParameter getCpu() {
            return cpu;
        }

        public ConanParameter getMinContigLength() {
            return minContigLength;
        }

        public ConanParameter getGenomeGuided() {
            return genomeGuided;
        }

        public ConanParameter getInchwormMinKmerCoverage() {
            return inchwormMinKmerCoverage;
        }

        public ConanParameter getChysalisMaxReadsPerGraph() {
            return chysalisMaxReadsPerGraph;
        }

        public ConanParameter getButterflyGroupPairsDistance() {
            return butterflyGroupPairsDistance;
        }

        public ConanParameter getButterflyPathReinforcementDistance() {
            return butterflyPathReinforcementDistance;
        }

        @Override
        public ConanParameter[] getConanParametersAsArray() {
            return new ConanParameter[] {
                    this.seqType,
                    this.jellyfishMemory,
                    this.leftInput,
                    this.rightInput,
                    this.singleInput,
                    this.ssLibType,
                    this.output,
                    this.cpu,
                    this.minContigLength,
                    this.genomeGuided,
                    this.inchwormMinKmerCoverage,
                    this.chysalisMaxReadsPerGraph,
                    this.butterflyGroupPairsDistance,
                    this.butterflyPathReinforcementDistance
            };
        }
    }
}
