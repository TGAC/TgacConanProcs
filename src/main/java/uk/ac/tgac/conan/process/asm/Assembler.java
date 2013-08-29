package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.model.ConanProcess;
import uk.ac.tgac.conan.core.data.Library;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Assembler extends ConanProcess {

    AssemblerArgs getArgs();

    boolean makesUnitigs();

    boolean makesContigs();

    boolean makesScaffolds();

    File getUnitigsFile();

    File getContigsFile();

    File getScaffoldsFile();

    boolean usesOpenMpi();

    boolean doesSubsampling();

    boolean hasKParam();

    boolean acceptsLibraries(List<Library> libraries);

    void initialise() throws IOException;
}