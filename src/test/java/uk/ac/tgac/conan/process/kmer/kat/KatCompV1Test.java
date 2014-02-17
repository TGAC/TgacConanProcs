package uk.ac.tgac.conan.process.kmer.kat;

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
 * Date: 20/11/13
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class KatCompV1Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private String correctCommand;


    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "kat comp --output=kat_comp.out --threads=32 " + pwd + "/reads.jf31_0 " + pwd + "/asm.jf31_0";
    }

    @Test
    public void testKatCompCommand() throws ConanParameterException {

        KatCompV1 jf = new KatCompV1(createKatCompArgs());

        String command = jf.getCommand();

        assertTrue(command.equals(correctCommand));
    }


    protected KatCompV1.Args createKatCompArgs() {

        KatCompV1.Args args = new KatCompV1.Args();
        args.setJellyfishHash1(new File("reads.jf31_0"));
        args.setJellyfishHash2(new File("asm.jf31_0"));
        args.setOutputPrefix("kat_comp.out");
        args.setThreads(32);
        args.setMemoryMb(4000);

        return args;
    }
}
