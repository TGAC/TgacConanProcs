package uk.ac.tgac.conan.process.kmer.jellyfish;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 11/11/13
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class JellyfishCountV11ProcessTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private String correctCommand;


    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "jellyfish count -m 31 -s 400000000 -t 32 --output=jellyfish_counts.jf31 -c 16 -C -L 2 input.fastq";
    }

    @Test
    public void testJellyfishCommand() throws ConanParameterException {

        JellyfishCountV11Process jf = new JellyfishCountV11Process(createJellyfishArgs());

        String command = jf.getCommand();

        assertTrue(command.equals(correctCommand));
    }


    protected JellyfishCountV11Args createJellyfishArgs() {

        JellyfishCountV11Args args = new JellyfishCountV11Args();
        args.setInputFile("input.fastq");
        args.setOutputPrefix("jellyfish_counts.jf31");
        args.setMerLength(31);
        args.setHashSize(400000000);
        args.setBothStrands(true);
        args.setCounterLength(16);
        args.setThreads(32);
        args.setLowerCount(2);

        return args;
    }

}
