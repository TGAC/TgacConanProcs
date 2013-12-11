package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.process.asmIO.scaffold.sspace.SSpaceBasicV2Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 11/11/13
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishStatsV11Process extends AbstractConanProcess {

    public static final String EXE = "jellyfish";

    public JellyfishStatsV11Process() {
        this(new JellyfishStatsV11Args());
    }

    public JellyfishStatsV11Process(JellyfishStatsV11Args args) {
        super(EXE, args, new JellyfishStatsV11Params());
    }

    public JellyfishStatsV11Args getArgs() {
        return (JellyfishStatsV11Args) this.getProcessArgs();
    }

    @Override
    public String getCommand() {
        List<String> commands = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" stats ");
        for (Map.Entry<ConanParameter, String> param : this.getProcessArgs().getArgMap().entrySet()) {
            if (!param.getKey().getName().equals("input")) {
                sb.append("-");
                sb.append(param.getKey());
                if (!param.getKey().isBoolean()) {
                    sb.append(" ");
                    sb.append(param.getValue());
                }
                sb.append(" ");
            }
        }

        JellyfishStatsV11Args args = (JellyfishStatsV11Args) this.getProcessArgs();

        return sb.toString().trim() + " " + args.getInput().getAbsolutePath();
    }

    @Override
    public String getName() {
        return "Jellyfish_Stats_V1.1";
    }
}
