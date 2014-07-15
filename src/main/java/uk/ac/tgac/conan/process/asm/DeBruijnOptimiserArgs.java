package uk.ac.tgac.conan.process.asm;

/**
 * Created by maplesod on 27/06/14.
 */
public interface DeBruijnOptimiserArgs extends AssemblerArgs {

    GenericDeBruijnOptimiserArgs getDeBruijnOptimiserArgs();

    void setDeBruijnOptimiserArgs(GenericDeBruijnOptimiserArgs args);
}
