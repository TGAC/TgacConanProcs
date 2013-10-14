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
package uk.ac.tgac.conan.process.ec.sickle;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.fgpt.conan.core.param.FilePair;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * User: maplesod
 * Date: 28/02/13
 * Time: 14:05
 */
public class SickleV11ProcessTest {

    private String pwd;

    private String correctPeCommand;
    private String correctSeCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 1);

        correctPeCommand = "sickle pe  --qual-threshold=50  --length-threshold=50   --qual-type=sanger  --pe-file1=" + pwd + "1.fq  --pe-file2=" + pwd + "2.fq  " +
                "--output-pe1=" + pwd + "1.out.fq  --output-pe2=" + pwd + "2.out.fq  --output-single=" + pwd + "se.out.fq";

        correctSeCommand = "sickle se  --qual-threshold=50  --length-threshold=50   --qual-type=sanger  --fastq-file=" + pwd + "se.fq  --output-file=" + "se.out.fq";
    }

    @Test
    public void testSickleV11Pe() {

        SicklePeV11Args args = new SicklePeV11Args();
        args.setPairedEndInputFiles(new FilePair(new File("1.fq"), new File("2.fq")));
        args.setOutputFilePair(new FilePair(new File("1.out.fq"), new File("2.out.fq")));
        args.setSeOutFile(new File("se.out.fq"));
        args.setDiscardN(true);
        args.setMinLength(50);
        args.setQualityThreshold(50);
        args.setQualType(SickleV11QualityTypeParameter.SickleQualityTypeOptions.SANGER);

        SickleV11Process sickleV11Process = new SickleV11Process(args);

        String command = sickleV11Process.getCommand();

        assertTrue(command.equals(correctPeCommand));
    }

    @Test
    public void testSickleV11Se() {

        SickleSeV11Args args = new SickleSeV11Args();
        args.setSingleEndInputFile(new File("se.fq"));
        args.setOutputFile(new File("se.out.fq"));
        args.setDiscardN(true);
        args.setMinLength(50);
        args.setQualType(SickleV11QualityTypeParameter.SickleQualityTypeOptions.SANGER);
        args.setQualityThreshold(50);

        SickleV11Process sickleV11Process = new SickleV11Process(args);

        String command = sickleV11Process.getCommand();

        assertTrue(command.equals(correctSeCommand));
    }
}
