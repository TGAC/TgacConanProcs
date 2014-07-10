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
package uk.ac.tgac.conan.process.re.tools;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.tgac.conan.core.data.FilePair;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.re.tools.SickleV12;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 28/02/13
 * Time: 14:05
 */
public class SickleV12Test {

    private String pwd;

    private String correctPeCommand;
    private String correctSeCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 1);

        correctPeCommand = "sickle pe --qual-threshold=50 --length-threshold=50 --discard-n --qual-type=illumina -f " + pwd + "1.fq -r " + pwd + "2.fq " +
                "-o " + pwd + "1.out.fq -p " + pwd + "2.out.fq -s " + pwd + "se.out.fq";

        correctSeCommand = "sickle se --qual-threshold=50 --length-threshold=50 --discard-n --qual-type=sanger -f " + pwd + "file.fastq -o " + pwd + "se.out.fq";
    }

    @Test
    public void testSickleV11Pe() throws ConanParameterException {

        Library lib = new Library();
        lib.setFiles(pwd + "/1.fq", pwd + "/2.fq");
        lib.setPhred(Library.Phred.PHRED_64);
        lib.setType(Library.Type.PAIRED_END);

        SickleV12.Args args = new SickleV12.Args();
        //args.setPairedEndInputFiles(new FilePair(new File("1.fq"), new File("2.fq")));
        args.setFastqOut1(new File("1.out.fq"));
        args.setFastqOut2(new File("2.out.fq"));
        args.setFastqOutSe(new File("se.out.fq"));
        args.setDiscardN(true);
        args.setLengthThreshold(50);
        args.setQualThreshold(50);
        args.setInput(lib);
        args.setOutputDir(new File(""));

        SickleV12 sickleV12 = new SickleV12(null, args);
        sickleV12.setup();

        String command = sickleV12.getCommand();

        assertTrue(command.equals(correctPeCommand));
    }

    @Test
    public void testSickleV11Se() throws ConanParameterException {

        Library lib = new Library();
        lib.setFiles(pwd + "/file.fastq", null);
        lib.setPhred(Library.Phred.PHRED_33);
        lib.setType(Library.Type.SINGLE_END);

        SickleV12.Args args = new SickleV12.Args();
        args.setFastqOut1(new File("se.out.fq"));
        args.setDiscardN(true);
        args.setLengthThreshold(50);
        args.setQualThreshold(50);
        args.setInput(lib);
        args.setOutputDir(new File(""));

        SickleV12 sickleV12 = new SickleV12(null, args);
        sickleV12.setup();

        String command = sickleV12.getCommand();

        assertTrue(command.equals(correctSeCommand));
    }
}
