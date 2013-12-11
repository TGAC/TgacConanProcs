package uk.ac.tgac.conan.process.kmer.kat;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.process.kmer.jellyfish.JellyfishCountV11Args;
import uk.ac.tgac.conan.process.kmer.jellyfish.JellyfishCountV11Params;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatCompV1Process extends AbstractConanProcess {

    public static final String EXE = "kat";

    public KatCompV1Process() {
        this(new KatCompV1Args());
    }

    public KatCompV1Process(KatCompV1Args args) {
        super(EXE, args, new KatCompV1Params());
    }

    public KatCompV1Args getArgs() {
        return (KatCompV1Args) this.getProcessArgs();
    }

    @Override
    public String getCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" comp ");
        for (Map.Entry<ConanParameter, String> param : this.getProcessArgs().getArgMap().entrySet()) {

            String name = param.getKey().getName();
            if (!name.equals("input1") && !name.equals("input2") && !name.equals("memory")) {
                sb.append("-");
                sb.append(param.getKey());
                if (!param.getKey().isBoolean()) {
                    sb.append(" ");
                    sb.append(param.getValue());
                }
                sb.append(" ");
            }
        }

        KatCompV1Args args = (KatCompV1Args) this.getProcessArgs();

        return sb.toString().trim() + " " + args.getJellyfishHash1().getAbsolutePath() + " " + args.getJellyfishHash2().getAbsolutePath();
    }

    @Override
    public String getName() {
        return "KAT_Comp_V1.0";
    }
}
