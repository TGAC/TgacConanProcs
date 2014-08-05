package uk.ac.tgac.conan.process.rnaasm;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.process.re.tools.MusketV10;

import java.io.File;

import static org.junit.Assert.*;

public class TransdecoderV2013Test {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String pwd;

    private String correctCommand;
    private String correctFullCommand;

    @Before
    public void setup() {

        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        correctCommand = "TransDecoder -t " + pwd + "/transdecoder/transcripts.fasta --CPU 32 -m 50";
        correctFullCommand = "cd " + pwd + "/transdecoder" + "; ln -s -f " + pwd + "/transcripts.fasta " +
                pwd + "/transdecoder/transcripts.fasta; " + correctCommand + " 2>&1; cd " + pwd;
    }

    @Test
    public void testTransdecoderCommand() throws ConanParameterException {

        TransdecoderV2013 trn = new TransdecoderV2013(null, createTransdecoderArgs());

        String command = trn.getCommand();

        assertTrue(command.equals(correctCommand));
    }

    @Test
    public void testTransdecoderFullCommand() throws ConanParameterException {

        TransdecoderV2013 trn = new TransdecoderV2013(null, createTransdecoderArgs());
        trn.setup();

        String command = trn.getFullCommand();

        assertTrue(command.equals(correctFullCommand));
    }

    protected TransdecoderV2013.Args createTransdecoderArgs() {

        TransdecoderV2013.Args args = new TransdecoderV2013.Args();
        args.setOutputDir(new File("transdecoder"));
        args.setThreads(32);
        args.setTranscripts(new File("transcripts.fasta"));
        args.setMinProteinLength(50);

        return args;
    }

}