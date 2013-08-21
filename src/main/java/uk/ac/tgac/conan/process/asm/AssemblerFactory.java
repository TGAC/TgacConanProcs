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
package uk.ac.tgac.conan.process.asm;

import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.asm.abyss.AbyssV134Args;
import uk.ac.tgac.conan.process.asm.abyss.AbyssV134Process;
import uk.ac.tgac.conan.process.asm.allpaths.AllpathsLgV44837Args;
import uk.ac.tgac.conan.process.asm.allpaths.AllpathsLgV44837Process;
import uk.ac.tgac.conan.process.asm.soapdenovo.SoapDeNovoV204Args;
import uk.ac.tgac.conan.process.asm.soapdenovo.SoapDeNovoV204Process;

import java.io.File;
import java.util.List;

/**
 * User: maplesod
 * Date: 30/01/13
 * Time: 18:49
 */
public enum AssemblerFactory {

    ABYSS_V1_3_4 {
        @Override
        public String getToolName() {
            return "ABYSS";
        }

        @Override
        public Assembler create() {
            return new AbyssV134Process();
        }

        @Override
        public Assembler create(AssemblerArgs args) {
            return new AbyssV134Process(args);
        }

        @Override
        public AssemblerArgs createArgs(int kmer, List<Library> libs, File outputDir) {
            AbyssV134Args args = new AbyssV134Args();
            args.setKmer(kmer);
            args.setOutputDir(outputDir);
            args.setLibraries(libs);

            return args;
        }
    },
    SOAPDENOVO_V2_04 {
        @Override
        public String getToolName() {
            return "SOAPdenovo";
        }

        @Override
        public Assembler create() {
            return new SoapDeNovoV204Process();
        }

        @Override
        public Assembler create(AssemblerArgs args) {
            return new SoapDeNovoV204Process(args);
        }

        @Override
        public AssemblerArgs createArgs(int kmer, List<Library> libs, File outputDir) {

            SoapDeNovoV204Args args = new SoapDeNovoV204Args();
            args.setKmer(kmer);
            args.setOutputDir(outputDir);
            args.setLibraries(libs);

            return args;
        }
    },
    ALLPATHSLG_V44837 {
        @Override
        public String getToolName() {
            return "ALLPATHS-LG";
        }

        @Override
        public Assembler create() {
            return new AllpathsLgV44837Process();
        }

        @Override
        public Assembler create(AssemblerArgs args) {
            return new AllpathsLgV44837Process(args);
        }

        @Override
        public AssemblerArgs createArgs(int kmer, List<Library> libs, File outputDir) {

            AllpathsLgV44837Args args = new AllpathsLgV44837Args();
            args.setOutputDir(outputDir);
            args.setLibraries(libs);

            return args;
        }
    };

    public abstract String getToolName();
    public abstract Assembler create();
    public abstract Assembler create(AssemblerArgs args);
    public abstract AssemblerArgs createArgs(int kmer, List<Library> libs, File outputDir);

    public static Assembler createAssembler() {
        return ABYSS_V1_3_4.create();
    }

    public static Assembler createAssembler(String assembler) {
        return AssemblerFactory.valueOf(assembler).create();
    }

    public static Assembler createAssembler(String assembler, AssemblerArgs args) {
        return AssemblerFactory.valueOf(assembler).create(args);
    }

    public static Assembler createAssembler(String assembler, int k, List<Library> libs, File outputDir) {

        AssemblerArgs args = AssemblerFactory.valueOf(assembler).createArgs(k, libs, outputDir);

        return AssemblerFactory.createAssembler(assembler, args);
    }

}
