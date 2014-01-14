package uk.ac.tgac.conan.process.align.bowtie;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 13/01/14
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public class BowtieBuildV2_1Process extends AbstractConanProcess {

    public static final String EXE = "bowtie2-build";

    public BowtieBuildV2_1Process() {
        this(new BowtieBuildV2_1Args());
    }

    public BowtieBuildV2_1Process(BowtieBuildV2_1Args args) {
        super(EXE, args, new BowtieBuildV2_1Params());
    }

    public BowtieBuildV2_1Args getArgs() {
        return (BowtieBuildV2_1Args)this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Bowtie2-Build_V2.1.X";
    }
}
