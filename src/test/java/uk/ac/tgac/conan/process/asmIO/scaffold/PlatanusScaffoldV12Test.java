package uk.ac.tgac.conan.process.asmIO.scaffold;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class PlatanusScaffoldV12Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void testRemoveGaps() throws Exception {

        File abyssContigs = FileUtils.toFile(this.getClass().getResource("/abyss_contigs.fa"));
        File output = temp.newFile("platanus_contigs.fa");

        PlatanusScaffoldV12 plat = new PlatanusScaffoldV12();

        plat.convertAbyssContigs(abyssContigs, output);

        List<String> lines = FileUtils.readLines(output);

        assertTrue(lines.size() == 4);
        assertTrue(lines.get(0).equalsIgnoreCase(">scaffold1_cov113"));
    }
}