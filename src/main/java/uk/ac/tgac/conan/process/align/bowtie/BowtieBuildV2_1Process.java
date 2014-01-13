package uk.ac.tgac.conan.process.align.bowtie;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.tgac.conan.process.subsampler.SubsamplerV1_0Args;

import java.util.Map;

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
    public String getCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" ");
        for (Map.Entry<ConanParameter, String> param : this.getProcessArgs().getArgMap().entrySet()) {

            if (!param.getKey().getName().equals("reference") && !param.getKey().getName().equals("baseName")) {
                sb.append("--");
                sb.append(param.getKey());
                if (!param.getKey().isBoolean()) {
                    sb.append(" ");
                    sb.append(param.getValue());
                }
                sb.append(" ");
            }
        }

        sb.append(this.getArgs().getReferenceAsString());
        sb.append(" ");
        sb.append(this.getArgs().getBaseName());

        return sb.toString().trim();
    }

    @Override
    public String getName() {
        return "Bowtie2-Build_V2.1.X";
    }
}
