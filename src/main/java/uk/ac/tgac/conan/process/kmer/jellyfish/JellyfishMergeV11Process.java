package uk.ac.tgac.conan.process.kmer.jellyfish;

import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishMergeV11Process extends AbstractConanProcess {

    public static final String EXE = "jellyfish";
    public static final String MODE = "merge";

    public JellyfishMergeV11Process() {
        this(new JellyfishMergeV11Args());
    }

    public JellyfishMergeV11Process(JellyfishMergeV11Args args) {
        super(EXE, args, new JellyfishMergeV11Params());
        this.setMode(MODE);
    }

    public JellyfishMergeV11Args getArgs() {
        return (JellyfishMergeV11Args) this.getProcessArgs();
    }

    @Override
    public String getCommand() throws ConanParameterException {
        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" merge ");
        sb.append(this.getArgs().getArgMap().buildOptionString(CommandLineFormat.POSIX));

        JellyfishMergeV11Args args = (JellyfishMergeV11Args) this.getProcessArgs();

        List<String> paths = new ArrayList<>();
        for(File f : args.getInputFiles()) {
            paths.add(f.getAbsolutePath());
        }

        return sb.toString().trim() + " " + StringUtils.join(paths, " ");
    }

    @Override
    public String getName() {
        return "Jellyfish_Merge_V1.1";
    }
}
