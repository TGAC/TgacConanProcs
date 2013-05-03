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

import uk.ac.ebi.fgpt.conan.core.param.DefaultConanParameter;
import uk.ac.ebi.fgpt.conan.core.param.FlagParameter;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: maplesod
 * Date: 02/05/13
 * Time: 15:31
 */
public class MusketV106Params implements ProcessParams {

    private ConanParameter kmer;
    private ConanParameter outputPrefix;
    private ConanParameter threads;
    private ConanParameter maxTrim;
    private ConanParameter inOrder;
    private ConanParameter multiK;
    private ConanParameter maxErr;
    private ConanParameter maxIter;
    private ConanParameter readFiles;

    public MusketV106Params() {

        this.kmer = new DefaultConanParameter(
                "k",
                "(specify two paramters: k-mer size and estimated total number of k-mers for this k-mer size) The " +
                        "second values  specifies the (possible) number of " +
                        "distinct k-mers in the input datasets (e.g. estimated number of k-mers: 67108864, 134217728, " +
                        "268435456 or 536870912). This second value does not affect the correctness of the program, " +
                        "but balances the memory consumption between Bloom filters and hash tables, thus affecting the " +
                        "overall memory footprint of the program. Please feel free to specify this value.",
                false,
                false
        );

        this.outputPrefix = new DefaultConanParameter(
                "omulti",
                "When this option is used, each input file will have its own output file. For each input file, all " +
                        "corrected reads in the file will be ouput to a file with prefix #str (the value of this " +
                        "option). The first input file has an output file name #str.0, the second #str.1, and so on. " +
                        "For example, the command \"musket -omutli myout infile_1.fa infile_2 fa infile2_1.fa " +
                        "infile2_2.fa\" will create 4 output files with names \"myout.0\", \"myout.1\", \"myout.2\" " +
                        "and \"myout.3\". File \"myout.0\" corresponds to file \"infile_1.fa\" and \"myout.1\" " +
                        "corresponds to \"infile_2.fa\", and so on",
                false,
                true
        );

        this.threads = new NumericParameter(
                "p",
                "number of threads [>=2]",
                true
        );

        this.maxTrim = new NumericParameter(
                "maxtrim",
                "maximal number of bases that can be trimmed, default 0",
                true
        );

        this.inOrder = new FlagParameter(
                "inorder",
                "keep sequences outputed in the same order with the input"
        );

        this.multiK = new FlagParameter(
                "multik",
                "enable the use of multiple k-mer sizes"
        );

        this.maxErr = new NumericParameter(
                "maxerr",
                "maximal number of mutations in any region of length #k, default 4",
                true
        );

        this.maxIter = new NumericParameter(
                "maxiter",
                "maximal number of correcting iterations per k-mer size, default 2",
                true
        );

        this.readFiles = new DefaultConanParameter(
                "input",
                "The input files to error correct separated by spaces",
                false,
                false
        );
    }

    public ConanParameter getKmer() {
        return kmer;
    }

    public ConanParameter getOutputPrefix() {
        return outputPrefix;
    }

    public ConanParameter getThreads() {
        return threads;
    }

    public ConanParameter getMaxTrim() {
        return maxTrim;
    }

    public ConanParameter getInOrder() {
        return inOrder;
    }

    public ConanParameter getMultiK() {
        return multiK;
    }

    public ConanParameter getMaxErr() {
        return maxErr;
    }

    public ConanParameter getMaxIter() {
        return maxIter;
    }

    public ConanParameter getReadFiles() {
        return readFiles;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<ConanParameter>(Arrays.asList(
                new ConanParameter[]{
                        this.kmer,
                        this.outputPrefix,
                        this.threads,
                        this.maxTrim,
                        this.inOrder,
                        this.multiK,
                        this.maxErr,
                        this.maxIter,
                        this.readFiles
                }
        ));
    }
}
