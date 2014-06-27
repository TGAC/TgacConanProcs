package uk.ac.tgac.conan.process.asm;

/**
 * Created by maplesod on 27/06/14.
 */
public interface DeBruijnArgs extends AssemblerArgs {

    GenericDeBruijnArgs getDeBruijnArgs();

    void setDeBruijnArgs(GenericDeBruijnArgs args);
}
