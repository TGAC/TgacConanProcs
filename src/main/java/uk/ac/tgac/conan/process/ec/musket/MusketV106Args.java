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
package uk.ac.tgac.conan.process.ec.musket;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorArgs;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorPairedEndArgs;

import java.io.File;
import java.util.List;

/**
 * User: maplesod
 * Date: 02/05/13
 * Time: 15:31
 */
@MetaInfServices(uk.ac.tgac.conan.process.ec.ErrorCorrectorArgsCreator.class)
public class MusketV106Args extends AbstractErrorCorrectorPairedEndArgs {

    public static final int DEFAULT_MAX_ERR = 4;
    public static final int DEFAULT_MAX_ITER = 2;


    private String outputPrefix;
    private long totalKmers;
    private int maxTrim;
    private boolean inOrder;
    private boolean multiK;
    private int maxErr;
    private int maxIter;

    private int readLength;


    public MusketV106Args() {
        super(new MusketV106Params());
        this.outputPrefix = "output";
        this.totalKmers = 0;
        this.maxTrim = 0;
        this.inOrder = true;
        this.multiK = false;
        this.maxErr = 4;
        this.maxIter = 2;

        this.readLength = 0;
    }

    public MusketV106Params getParams() {
        return (MusketV106Params)this.params;
    }


    public String getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(String outputPrefix) {
        this.outputPrefix = outputPrefix;
    }

    public long getTotalKmers() {

        // This is a bit of a fudge.  Dividing by 20 is just a arbitrary number used to move the theoretical number of
        // kmers closer to something that might be seen in reality.  This number isn't critical however, but if it is
        // wildly wrong it might effect Musket's memory usage and runtime performance.
        return this.totalKmers == 0 ?
                (long)Math.pow(4, this.getKmer()) / 20 :
                totalKmers;
    }

    public void setTotalKmers(long totalKmers) {
        this.totalKmers = totalKmers;
    }

    public int getMaxTrim() {
        return maxTrim;
    }

    public void setMaxTrim(int maxTrim) {

        if (this.readLength != 0) {
            super.setMinLength(this.readLength - maxTrim);
        }
        this.maxTrim = maxTrim;
    }

    public boolean isInOrder() {
        return inOrder;
    }

    public void setInOrder(boolean inOrder) {
        this.inOrder = inOrder;
    }

    public boolean isMultiK() {
        return multiK;
    }

    public void setMultiK(boolean multiK) {
        this.multiK = multiK;
    }

    public int getMaxErr() {
        return maxErr;
    }

    public void setMaxErr(int maxErr) {
        this.maxErr = maxErr;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(int maxIter) {
        this.maxIter = maxIter;
    }

    /**
     * Must have a minimum of 2 threads for musket.
     * @param threads
     */
    @Override
    public void setThreads(int threads) {
        super.setThreads(threads < 2 ? 2 : threads);
    }

    @Override
    public void setMinLength(int minLength) {

        if (this.readLength != 0) {
            this.maxTrim = this.readLength - minLength;
        }

        super.setMinLength(minLength);
    }


    @Override
    public FilePair getPairedEndCorrectedFiles() {

        File out1 = new File(this.getOutputDir(), this.outputPrefix + ".0");
        File out2 = new File(this.getOutputDir(), this.outputPrefix + ".1");

        return new FilePair(out1, out2);
    }

    @Override
    public List<File> getSingleEndCorrectedFiles() {
        return null;
    }

    @Override
    public FilePair getPairedEndErrorFiles() {
        return null;
    }

    @Override
    public List<File> getSingleEndErrorFiles() {
        return null;
    }

    @Override
    public AbstractErrorCorrectorArgs copy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<File> getCorrectedFiles() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setFromLibrary(Library lib, List<File> files) {

        this.setPairedEndInputFiles(new FilePair(
                files.get(0), files.get(1)));

        this.readLength = lib.getReadLength();

        // If we have read length set, then try to get the maxtrim and min length vars set correctly too.
        if (this.readLength != 0) {
            if (this.maxTrim != 0) {
                this.setMaxTrim(this.maxTrim);
            }
            else if (this.getMinLength() != 0) {
                this.setMinLength(this.getMinLength());
            }
        }


    }

    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ParamMap getArgMap() {

        MusketV106Params params = this.getParams();

        ParamMap pvp = new DefaultParamMap();

        pvp.put(params.getKmer(), String.valueOf(this.getKmer()) + " " + String.valueOf(this.getTotalKmers()));
        pvp.put(params.getOutputPrefix(), this.getOutputPrefix());
        pvp.put(params.getThreads(), String.valueOf(this.getThreads()));

        if (this.maxTrim != 0) {
            pvp.put(params.getMaxTrim(), String.valueOf(this.maxTrim));
        }

        if (this.inOrder) {
            pvp.put(params.getInOrder(), String.valueOf(this.inOrder));
        }

        if (this.multiK) {
            pvp.put(params.getMultiK(), String.valueOf(this.multiK));
        }

        if (this.maxErr != DEFAULT_MAX_ERR) {
            pvp.put(params.getMaxErr(), String.valueOf(this.maxErr));
        }

        if (this.maxIter != DEFAULT_MAX_ITER) {
            pvp.put(params.getMaxIter(), String.valueOf(this.maxIter));
        }

        if (this.getPairedEndInputFiles() != null) {
            pvp.put(params.getReadFiles(), this.getPairedEndInputFiles().getFile1().getAbsolutePath() + " " + this.getPairedEndInputFiles().getFile2().getAbsolutePath());
        }

        return pvp;
    }


    @Override
    protected void setOptionFromMapEntry(ConanParameter param, String value) {

        MusketV106Params params = this.getParams();

        if (param.equals(params.getKmer())) {

            String[] parts = value.split(" ");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Parameter/Arg map does not contain a valid kmer value pair for musket.  Musket requires two value, the first being the kmer value, the second being the estimate number of distinct kmers (4^k).");
            }

            this.setKmer(Integer.parseInt(parts[0]));
            this.setTotalKmers(Long.parseLong(parts[1]));
        }
        else if (param.equals(params.getMaxTrim())) {
            this.setMaxTrim(Integer.parseInt(value));
        }
        else if (param.equals(params.getInOrder())) {

            this.setInOrder(value == null || value.isEmpty() || Boolean.parseBoolean(value));
        }
        else if (param.equals(params.getMultiK())) {
            this.setMultiK(value == null || value.isEmpty() || Boolean.parseBoolean(value));
        }
        else if (param.equals(params.getThreads())) {
            this.setThreads(Integer.parseInt(value));
        }
        else if (param.equals(params.getMaxErr())) {
            this.setMaxErr(Integer.parseInt(value));
        }
        else if (param.equals(params.getMaxIter())) {
            this.setMaxIter(Integer.parseInt(value));
        }
        else if (param.equals(params.getOutputPrefix())) {
            this.setOutputPrefix(value);
        }
        else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }

    @Override
    protected void setArgFromMapEntry(ConanParameter param, String value) {

        MusketV106Params params = this.getParams();

        if (param.equals(params.getReadFiles())) {

            // Assumes there are no space in the path!!!
            String[] parts = value.split(" ");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Parameter/Arg map does not contain two file valid paths.  Note that each path must contain no spaces!");
            }

            this.setPairedEndInputFiles(new FilePair(new File(parts[0]), new File(parts[1])));
        } else {
            throw new IllegalArgumentException("Unknown param found: " + param);
        }
    }


    @Override
    public AbstractErrorCorrectorArgs create(File outputDir, Library lib, int threads,
                                             int memory, int kmer,
                                             int minLength,
                                             int minQual) {

        MusketV106Args margs = new MusketV106Args();

        margs.setOutputDir(outputDir);
        margs.setOutputPrefix("output");
        margs.setFromLibrary(lib);
        margs.setThreads(threads);
        margs.setMemoryGb(memory);
        margs.setKmer(kmer);
        margs.setMinLength(minLength);
        margs.setQualityThreshold(minQual);

        return margs;
    }

    @Override
    public String getName() {
        return MusketV106Process.NAME;
    }
}
