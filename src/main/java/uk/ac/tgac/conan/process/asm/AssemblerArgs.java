package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.core.process.AbstractProcessArgs;
import uk.ac.ebi.fgpt.conan.model.param.ProcessArgs;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.Organism;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 10/12/13
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */
public interface AssemblerArgs extends ProcessArgs {

    /**
     * Gets the name of this process
     * @return
     */
    String getProcessName();

    AbstractProcessArgs toConanArgs();
}
