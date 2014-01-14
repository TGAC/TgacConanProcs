package uk.ac.tgac.conan.process.ec;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 09/12/13
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractErrorCorrector extends AbstractConanProcess implements ErrorCorrectorCreator {

    protected AbstractErrorCorrector(String executable, AbstractErrorCorrectorArgs args, ProcessParams params) {
        super(executable, args, params);
    }

    public AbstractErrorCorrectorArgs getECArgs() {
        return (AbstractErrorCorrectorArgs)this.getProcessArgs();
    }

    public void initialise() {
    }
}
