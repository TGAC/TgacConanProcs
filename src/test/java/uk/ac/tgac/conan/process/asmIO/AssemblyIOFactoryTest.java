package uk.ac.tgac.conan.process.asmIO;

import org.junit.Test;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class AssemblyIOFactoryTest {

    @Test
    public void testCreate() throws IOException {

        AbstractAssemblyIOProcess proc = AssemblyIOFactory.create("SSPACE_Basic_v2.0", new File("input.fa"), new File(""), "test", new ArrayList<Library>(), 1, 1000, "");

        assertTrue(proc != null);
    }
}
