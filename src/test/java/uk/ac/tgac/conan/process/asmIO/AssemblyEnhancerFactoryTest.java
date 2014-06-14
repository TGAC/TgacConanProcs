package uk.ac.tgac.conan.process.asmIO;

import org.junit.Test;
import uk.ac.ebi.fgpt.conan.service.DefaultExecutorService;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class AssemblyEnhancerFactoryTest {

    @Test
    public void testCreate() throws IOException {

        AssemblyEnhancer proc = AssemblyEnhancerFactory.create(
                "SSPACE_Basic_v2.0",
                new File("input.fa"),
                new File(""),
                "test",
                new ArrayList<Library>(),
                1,
                1000,
                "",
                new DefaultExecutorService());

        assertTrue(proc != null);
    }
}
