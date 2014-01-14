package uk.ac.tgac.conan.process.kmer.kat;

import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ParamMap;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class KatCompV1Process extends AbstractConanProcess {

    public static final String EXE = "kat";
    public static final String MODE = "comp";

    public KatCompV1Process() {
        this(new KatCompV1Args());
    }

    public KatCompV1Process(KatCompV1Args args) {
        super(EXE, args, new KatCompV1Params());
        this.setMode(MODE);
    }

    public KatCompV1Args getArgs() {
        return (KatCompV1Args) this.getProcessArgs();
    }

    @Override
    public String getCommand() throws ConanParameterException {

        ParamMap map = this.getProcessArgs().getArgMap();

        // Ensure all parameters are valid before we try to make a command
        map.validate(this.getProcessParams());

        List<ConanParameter> exclusions = new ArrayList<>();
        exclusions.add(new KatCompV1Params().getMemoryMb());


        List<String> commands = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(EXE).append(" ").append(MODE);

        // Add the options
        String options = map.buildOptionString(CommandLineFormat.POSIX, exclusions).trim();
        if (!options.isEmpty()) {
            sb.append(" ").append(options);
        }

        // Add the arguments
        String args = map.buildArgString().trim();
        if (!args.isEmpty()) {
            sb.append(" ").append(args);
        }

        commands.add(sb.toString().trim());

        String command = StringUtils.join(commands, "; ");

        return command;
    }

    @Override
    public String getName() {
        return "KAT_Comp_V1.0";
    }
}
