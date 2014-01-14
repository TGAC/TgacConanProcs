package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.service.ConanProcessService;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 10/12/13
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public interface AssemblerCreator {

    /**
     * Creates an instance of an AbstractAssemblyIOProcess using Basic args
     * @return
     */
    AbstractAssembler create(AbstractAssemblerArgs args, ConanProcessService conanProcessService);

    /**
     * Gets the name of this process
     * @return
     */
    String getName();
}
