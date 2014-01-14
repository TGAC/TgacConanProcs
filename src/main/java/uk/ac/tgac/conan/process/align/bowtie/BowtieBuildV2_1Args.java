package uk.ac.tgac.conan.process.align.bowtie;

import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 13/01/14
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class BowtieBuildV2_1Args extends AbstractProcessArgs {

    private BowtieBuildV2_1Params params = new BowtieBuildV2_1Params();

    private File[] referenceIn;
    private String baseName;
    private int threads;


    public BowtieBuildV2_1Args() {

        this.referenceIn = null;
        this.baseName = "";
        this.threads = 1;
    }

    public File[] getReferenceIn() {
        return referenceIn;
    }

    public String getReferenceAsString() {

        if (this.referenceIn == null)
            return null;

        if (this.referenceIn.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();

        sb.append(this.referenceIn[0].getAbsolutePath());

        for(int i = 1; i < this.referenceIn.length; i++) {
            sb.append(",").append(this.referenceIn[i].getAbsolutePath());
        }

        return sb.toString();
    }

    public void setReferenceIn(File[] referenceIn) {
        this.referenceIn = referenceIn;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    @Override
    public void parse(String args) throws IOException {


    }

    @Override
    public ParamMap getArgMap() {

        ParamMap pvp = new DefaultParamMap();

        if (this.referenceIn != null) {
            pvp.put(params.getReferenceIn(), this.getReferenceAsString());
        }

        if (this.baseName != null && !this.getBaseName().equals("")) {
            pvp.put(params.getBaseName(), this.baseName);
        }

        if (this.threads > 1) {
            pvp.put(params.getThreads(), Integer.toString(this.getThreads()));
        }

        return pvp;
    }

    @Override
    protected void setOptionFromMapEntry(ConanParameter param, String value) {
        if (param.equals(this.params.getThreads())) {
            this.threads = Integer.parseInt(value);
        }
        else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }

    @Override
    protected void setArgFromMapEntry(ConanParameter param, String value) {

        if (param.equals(this.params.getReferenceIn())) {

            String[] parts = value.split(",");

            File[] files = new File[parts.length];
            for(int i = 0; i < parts.length; i++) {
                files[i] = new File(parts[i]);
            }

            this.referenceIn = files;
        }
        else if (param.equals(this.params.getBaseName())) {
            this.baseName = value;
        }
        else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }

}
