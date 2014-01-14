package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 11/11/13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishStatsV11Process extends AbstractConanProcess {

    public static final String EXE = "jellyfish";
    public static final String MODE = "stats";


    public JellyfishStatsV11Process() {
        this(new JellyfishStatsV11Args());
    }

    public JellyfishStatsV11Process(JellyfishStatsV11Args args) {
        super(EXE, args, new JellyfishStatsV11Params());
        this.setMode(MODE);
    }

    public JellyfishStatsV11Args getArgs() {
        return (JellyfishStatsV11Args) this.getProcessArgs();
    }

    @Override
    public String getName() {
        return "Jellyfish_Stats_V1.1";
    }
}
