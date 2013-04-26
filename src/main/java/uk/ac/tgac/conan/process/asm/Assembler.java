package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.model.ConanProcess;

import java.io.File;
import java.io.IOException;

public interface Assembler extends ConanProcess {

    AssemblerArgs getArgs();

    boolean makesUnitigs();

    boolean makesContigs();

    boolean makesScaffolds();

    File getUnitigsFile();

    File getContigsFile();

    File getScaffoldsFile();

    boolean usesOpenMpi();

    void initialise() throws IOException;
}