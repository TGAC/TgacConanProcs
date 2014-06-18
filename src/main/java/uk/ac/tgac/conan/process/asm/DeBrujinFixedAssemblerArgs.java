package uk.ac.tgac.conan.process.asm;

import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

/**
 * Created by maplesod on 18/06/14.
 */
public abstract class DeBrujinFixedAssemblerArgs extends AbstractAssemblerArgs {

    protected DeBrujinFixedAssemblerArgs(ProcessParams params, String name) {
        super(params, name);
    }

    @Override
    public AssemblerType getType() {
        return AssemblerType.DE_BRUIJN_FIXED;
    }
}
