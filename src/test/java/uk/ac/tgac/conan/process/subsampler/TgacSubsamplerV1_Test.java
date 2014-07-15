package uk.ac.tgac.conan.process.subsampler;

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
 * Date: 29/08/13
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public class TgacSubsamplerV1_Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private static String testDir;

    private static String correctCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        this.testDir = temp.getRoot().getAbsolutePath();

        correctCommand = "subsampler -i " + pwd + "/file1.fq" + " -o " + testDir + "/file1.sub.fq -L " + testDir +
                "/file1.sub.log -p 0.01 -s 12345678";
    }

    private TgacSubsamplerV1 createProcess() {

        TgacSubsamplerV1.Args args = new TgacSubsamplerV1.Args();
        args.setInputFile(new File("file1.fq"));
        args.setOutputFile(new File(testDir, "file1.sub.fq"));
        args.setLogFile(new File(testDir, "file1.sub.log"));
        args.setProbability(0.01);
        args.setSeed(12345678);

        return new TgacSubsamplerV1(args);
    }

    @Test
    public void testCommand() throws ConanParameterException {

        TgacSubsamplerV1 process = createProcess();

        String command = process.getCommand();

        assertTrue(command.equals(correctCommand));
    }

}
