package uk.ac.tgac.conan.process.asm;

/**
 * Created by dan on 29/06/14.
 */
public interface DeBruijnAutoArgs extends AssemblerArgs {

    GenericDeBruijnAutoArgs getDeBruijnAutoArgs();

    void setDeBruijnAutoArgs(GenericDeBruijnAutoArgs args);
}
