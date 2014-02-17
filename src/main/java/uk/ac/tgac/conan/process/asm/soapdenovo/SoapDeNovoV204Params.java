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
package uk.ac.tgac.conan.process.asm.soapdenovo;

import uk.ac.ebi.fgpt.conan.core.param.FlagParameter;
import uk.ac.ebi.fgpt.conan.core.param.NumericParameter;
import uk.ac.ebi.fgpt.conan.core.param.PathParameter;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: maplesod
 * Date: 16/04/13
 * Time: 15:57
 */
public class SoapDeNovoV204Params implements ProcessParams {

    private ConanParameter configFile;
    private ConanParameter prefix;
    private ConanParameter kmer;
    private ConanParameter resolveRepeats;
    private ConanParameter kmerFreqCutoff;
    private ConanParameter threads;
    private ConanParameter memory;
    private ConanParameter fillGaps;


    public SoapDeNovoV204Params() {

        this.configFile = new PathParameter(
                "s",
                "configFile: the config file of solexa reads",
                false);

        this.prefix = new PathParameter(
                "o",
                "outputGraph: prefix of output graph file name",
                false);

        this.kmer = new NumericParameter(
                "K",
                "kmer(min 13, max 63/127): kmer size, [23]",
                false);

        this.threads = new NumericParameter(
                "p",
                "n_cpu: number of cpu for use, [8]",
                true);

        this.memory = new NumericParameter(
                "a",
                "initMemoryAssumption: memory assumption initialized to avoid further reallocation, unit G, [0]",
                true);

        this.kmerFreqCutoff = new NumericParameter(
                "d",
                "KmerFreqCutoff: kmers with frequency no larger than KmerFreqCutoff will be deleted, [0]",
                true);

        this.resolveRepeats = new FlagParameter(
                "R",
                "resolve repeats by reads, [NO]");

        this.fillGaps = new FlagParameter(
                "F",
                "fill gaps in scaffold, [NO]");
    }

    public ConanParameter getConfigFile() {
        return configFile;
    }

    public ConanParameter getKmer() {
        return kmer;
    }

    public ConanParameter getPrefix() {
        return prefix;
    }

    public ConanParameter getResolveRepeats() {
        return resolveRepeats;
    }

    public ConanParameter getKmerFreqCutoff() {
        return kmerFreqCutoff;
    }

    public ConanParameter getThreads() {
        return threads;
    }

    public ConanParameter getMemory() {
        return memory;
    }

    public ConanParameter getFillGaps() {
        return fillGaps;
    }

    @Override
    public List<ConanParameter> getConanParameters() {
        return new ArrayList<ConanParameter>(Arrays.asList(
                new ConanParameter[]{
                     this.configFile,
                     this.kmer,
                     this.prefix,
                     this.resolveRepeats,
                     this.kmerFreqCutoff,
                     this.threads,
                     this.memory,
                     this.fillGaps
                }
        ));
    }
}
