package uk.ac.tgac.conan.process.kmer.jellyfish;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 20/11/13
 * Time: 16:12
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishMergeV11Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private String correctCommand;


    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "jellyfish merge -s 20000000 --output=" + pwd + "/output " + pwd + "/input1 " + pwd + "/input2 " + pwd + "/input3 " + pwd + "/input4";
    }

    @Test
    public void testJellyfishCommand() throws ConanParameterException {

        JellyfishMergeV11 jf = new JellyfishMergeV11(null, createJellyfishArgs());

        String command = jf.getCommand();

        assertTrue(command.equals(correctCommand));
    }


    protected JellyfishMergeV11.Args createJellyfishArgs() {

        List<File> inputs = new ArrayList<>();
        inputs.add(new File("input1"));
        inputs.add(new File("input2"));
        inputs.add(new File("input3"));
        inputs.add(new File("input4"));


        JellyfishMergeV11.Args args = new JellyfishMergeV11.Args();
        args.setInputFiles(inputs);
        args.setOutputFile(new File("output"));
        args.setBufferSize(20000000);

        return args;
    }
}
