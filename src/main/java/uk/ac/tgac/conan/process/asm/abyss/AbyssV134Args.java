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
package uk.ac.tgac.conan.process.asm.abyss;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.core.param.DefaultParamMap;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.Organism;
import uk.ac.tgac.conan.process.asm.AbstractAssemblerArgs;

import java.io.File;
import java.util.List;
import java.util.Map;

@MetaInfServices(uk.ac.tgac.conan.process.asm.AssemblerArgsCreator.class)
public class AbyssV134Args extends AbstractAssemblerArgs {

    private AbyssV134Params params = new AbyssV134Params();

    private int nbContigPairs;
    private String outputName;

    public AbyssV134Args() {
        super();

        this.nbContigPairs = 10;

        StringJoiner nameJoiner = new StringJoiner("-");
        nameJoiner.add("abyss_1.3.4");
        nameJoiner.add(this.getKmer() != 0 && this.getKmer() != DEFAULT_KMER, "", "k" + Integer.toString(this.getKmer()));
        nameJoiner.add(this.getCoverageCutoff() != 0, "", "cc" + Integer.toString(this.getCoverageCutoff()));

        this.outputName = nameJoiner.toString();
    }

    public int getNbContigPairs() {
        return nbContigPairs;
    }

    public void setNbContigPairs(int nbContigPairs) {
        this.nbContigPairs = nbContigPairs;
    }

    @Override
    public String getName() {
        return AbyssV134Process.NAME;
    }

    @Override
    public AbstractAssemblerArgs create(int k, List<Library> libs, File outputDir, int threads, int memory, int coverage, Organism organism) {

        AbyssV134Args args = new AbyssV134Args();
        args.setKmer(k);
        args.setOutputDir(outputDir);
        args.setLibraries(libs);
        args.setThreads(threads);
        args.setMemory(memory);
        args.setDesiredCoverage(coverage);
        args.setOrganism(organism);
        return args;
    }

    public String getOutputName() {
        return outputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    @Override
    public void parse(String args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ParamMap getArgMap() {

        ParamMap pvp = new DefaultParamMap();

        if (this.outputName != null) {
            pvp.put(params.getName(), this.outputName);
        }

        if (this.nbContigPairs != 10) {
            pvp.put(params.getNbContigPairs(), Integer.toString(this.nbContigPairs));
        }

        if (this.getKmer() > 0) {
            pvp.put(params.getKmer(), Integer.toString(this.getKmer()));
        }

        if (this.getCoverageCutoff() > 0) {
            pvp.put(params.getCoverageCutoff(), Integer.toString(this.getCoverageCutoff()));
        }

        if (this.getThreads() > 1) {
            pvp.put(params.getThreads(), Integer.toString(this.getThreads()));
        }

        if (this.getLibraries() != null && !this.getLibraries().isEmpty()) {
            pvp.put(params.getLibs(), new AbyssV134InputLibsArg(this.getLibraries()).toString());
        }

        return pvp;
    }

    @Override
    public void setFromArgMap(ParamMap pvp) {
        for (Map.Entry<ConanParameter, String> entry : pvp.entrySet()) {

            if (!entry.getKey().validateParameterValue(entry.getValue())) {
                throw new IllegalArgumentException("Parameter invalid: " + entry.getKey() + " : " + entry.getValue());
            }

            ConanParameter param = entry.getKey();
            String val = entry.getValue();

            if (param.equals(this.params.getName())) {
                this.outputName = val;
            }
            else if (param.equals(this.params.getKmer())) {
                this.setKmer(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getCoverageCutoff())) {
                this.setCoverageCutoff(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getNbContigPairs())) {
                this.nbContigPairs = Integer.parseInt(val);
            }
            else if (param.equals(this.params.getThreads())) {
                this.setThreads(Integer.parseInt(val));
            }
            else if (param.equals(this.params.getLibs())) {
                this.setLibraries(AbyssV134InputLibsArg.parse(val).getLibs());
            }
            else {
                throw new IllegalArgumentException("Unknown param found: " + param);
            }
        }
    }

    @Override
    public AbstractAssemblerArgs copy() {

        AbyssV134Args copy = new AbyssV134Args();
        copy.setOutputName(this.getOutputName());
        copy.setKmer(this.getKmer());
        copy.setThreads(this.getThreads());
        copy.setNbContigPairs(this.getNbContigPairs());
        copy.setLibraries(this.getLibraries());  // Not really copying this!!

        return copy;
    }

}
