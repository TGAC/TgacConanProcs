package uk.ac.tgac.conan.process.re;

import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;

/**
 * Created by maplesod on 10/07/14.
 */
public interface ReadEnhancerArgs {

    /**
     * Gets the name of this process
     * @return
     */
    String getProcessName();

    AbstractProcessArgs toConanArgs();

    GenericReadEnhancerArgs getReadEnhancerArgs();

    void setReadEnhancerArgs(GenericReadEnhancerArgs args);
}
