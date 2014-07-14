package uk.ac.tgac.conan.process.asmIO.scaffold;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class SoapScaffolderV24Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void testRemoveGaps() throws Exception {

        File asmWithGaps = FileUtils.toFile(this.getClass().getResource("/asm_wgaps.fa"));
        File output = temp.newFile("asm_wgaps.fa");

        SoapScaffolderV24 soap = new SoapScaffolderV24();

        soap.removeGaps(asmWithGaps, output);

        List<String> lines = FileUtils.readLines(output);

        assertTrue(lines.size() == 6);
        assertTrue(lines.get(1).length() == 58);
    }
}