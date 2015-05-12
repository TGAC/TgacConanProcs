package uk.ac.tgac.conan.process.rnaasm;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class TransdecoderV2Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private String correctCommand;
    private String correctFullCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "TransDecoder.LongOrfs -t " + pwd + "/transdecoder/transcripts.fasta -m 50 2>&1";
        correctFullCommand =
                "cd " + pwd + "/transdecoder" + "; " +
                "ln -s -f " + pwd + "/transcripts.fasta " + pwd + "/transdecoder/transcripts.fasta; " +
                correctCommand + "; " +
                "TransDecoder.Predict -t " + pwd + "/transdecoder/transcripts.fasta; " +
                "cd " + pwd;
    }

    @Test
    public void testTransdecoderCommand() throws ConanParameterException {

        TransdecoderV2 trn = new TransdecoderV2(null, createTransdecoderArgs());

        String command = trn.getCommand();

        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testTransdecoderFullCommand() throws ConanParameterException {

        TransdecoderV2 trn = new TransdecoderV2(null, createTransdecoderArgs());
        trn.setup();

        String command = trn.getFullCommand();

        assertTrue(command.equals(correctFullCommand));
    }

    protected TransdecoderV2.Args createTransdecoderArgs() {

        TransdecoderV2.Args args = new TransdecoderV2.Args();
        args.setOutputDir(new File("transdecoder"));
        args.setTranscripts(new File("transcripts.fasta"));
        args.setMinProteinLength(50);

        return args;
    }

}