package uk.ac.tgac.conan.process.rnaasm;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class FullLengtherNextV2013Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private String correctCommand;
    private String correctFullCommand;
    private String correctBlastDBCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "full_lengther_next --fasta " + pwd + "/transcripts.fasta --taxon_group plants --workers 32 --server 0.0.0";
        correctFullCommand = "cd " + pwd + "/fln" + "; " + correctCommand + " 2>&1; cd " + pwd;
        correctBlastDBCommand = "cd " + pwd + "/fln" + "; export BLASTDB=/path/tp/blastdb; " + correctCommand + " 2>&1; cd " + pwd;
    }

    @Test
    public void testTransdecoderCommand() throws ConanParameterException {

        FullLengtherNextV2013 trn = new FullLengtherNextV2013(null, createFlnArgs());

        String command = trn.getCommand();

        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testTransdecoderFullCommand() throws ConanParameterException {

        FullLengtherNextV2013 trn = new FullLengtherNextV2013(null, createFlnArgs());
        trn.setup();

        String command = trn.getFullCommand();

        assertTrue(command.equals(correctFullCommand));
    }

    @Test
    public void testTransdecoderBlastDb() throws ConanParameterException {

        FullLengtherNextV2013.Args args = createFlnArgs();
        args.setBlastDb(new File("/path/tp/blastdb"));

        FullLengtherNextV2013 trn = new FullLengtherNextV2013(null, args);
        trn.setup();

        String command = trn.getFullCommand();

        assertTrue(command.equals(correctBlastDBCommand));
    }


    protected FullLengtherNextV2013.Args createFlnArgs() {

        FullLengtherNextV2013.Args args = new FullLengtherNextV2013.Args();
        args.setOutputDir(new File("fln"));
        args.setFastaFile(new File("transcripts.fasta"));
        args.setTaxonGroup("plants");
        args.setWorkers(32);

        return args;
    }
}