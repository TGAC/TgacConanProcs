package uk.ac.tgac.conan.process.asm.tools;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.ac.ebi.fgpt.conan.model.context.ExecutionContext;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.ebi.fgpt.conan.service.exception.ProcessExecutionException;
import uk.ac.ebi.fgpt.conan.utils.CommandExecutionException;
import uk.ac.tgac.conan.core.data.Library;
import uk.ac.tgac.conan.core.data.Organism;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 30/08/13
 * Time: 09:25
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class AllpathsLgV50Test {

    private String pwd;

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Mock
    private ExecutionContext ec;

    @Mock
    private ConanProcessService conanProcessService;

    private static String correctCommand;
    private static String correctFullCommand;
    private static String correctLsfScheduledCommand;

    @Before
    public void setup() {

        String testDir = temp.getRoot().getAbsolutePath();
        String pwdFull = new File(".").getAbsolutePath();
        this.pwd = pwdFull.substring(0, pwdFull.length() - 2);

        String correctCacheDirCommand = "CACHE_DIR=" + testDir + "/Unknown/cache";
        String correctTempDirCommand = "TMP_DIR=" + testDir + "/Unknown/cache/tmp";

        correctCommand =
                "CacheLibs.pl " + correctCacheDirCommand + " IN_LIBS_CSV=" + testDir + "/in_libs.csv " +
                                    "ACTION=Add OVERWRITE=True; " +
                "CacheGroups.pl " + correctCacheDirCommand + " IN_GROUPS_CSV=" + testDir + "/in_groups_phred33.csv " +
                                    correctTempDirCommand + " ACTION=Add PHRED_64=False OVERWRITE=True; " +
                "CacheGroups.pl " + correctCacheDirCommand + " IN_GROUPS_CSV=" + testDir + "/in_groups_phred64.csv " +
                                    correctTempDirCommand + " ACTION=Add PHRED_64=True OVERWRITE=True; " +
                "CacheToAllPathsInputs.pl " + correctCacheDirCommand + " DATA_DIR=" + testDir + "/Unknown/data GENOME_SIZE=500000 " +
                                    "GROUPS='{PE1,OPE1}' COVERAGES='{75,75}' PLOIDY=2; " +
                "RunAllPathsLG PRE=" + testDir + " REFERENCE_NAME=Unknown DATA_SUBDIR=data RUN=rampart THREADS=16 " +
                                    "OVERWRITE=True TARGETS=full_eval";
    }

    private List<Library> createLocalPETestLibrary() {

        Library peLib = new Library();
        peLib.setName("peLib1");
        peLib.setUniform(true);
        peLib.setPhred(Library.Phred.PHRED_33);
        peLib.setReadLength(101);
        peLib.setSeqOrientation(Library.SeqOrientation.FR);
        peLib.setAverageInsertSize(500);
        peLib.setType(Library.Type.PE);
        peLib.setFiles(pwd + "/tools/mass/Test2_PE_R1.r95.fastq", pwd + "/tools/mass/Test2_PE_R2.r95.fastq");

        Library opeLib = new Library();
        opeLib.setName("opeLib1");
        opeLib.setUniform(true);
        opeLib.setPhred(Library.Phred.PHRED_64);
        opeLib.setReadLength(101);
        opeLib.setSeqOrientation(Library.SeqOrientation.FR);
        opeLib.setAverageInsertSize(180);
        opeLib.setType(Library.Type.OPE);
        opeLib.setFiles(pwd + "/tools/mass/Test2_OPE_R1.r95.fastq", pwd + "/tools/mass/Test2_OPE_R2.r95.fastq");

        List<Library> libs = new ArrayList<Library>();
        libs.add(peLib);
        libs.add(opeLib);

        return libs;
    }

    private AllpathsLgV50 createProcess() {
        List<Library> libs = this.createLocalPETestLibrary();

        AllpathsLgV50.Args args = new AllpathsLgV50.Args();
        args.setLibs(libs);
        args.setDesiredCoverage(75);
        args.setThreads(16);
        args.setOutputDir(temp.getRoot());
        args.setOrganism(new Organism("Unknown", 2, 500000, 43.0, 20000, null));

        return new AllpathsLgV50(null, args);
    }

    @Test
    public void testAllpathsCommand() throws InterruptedException, ProcessExecutionException, IOException, CommandExecutionException {

        AllpathsLgV50 allpaths = createProcess();
        allpaths.setup();

        String command = allpaths.getCommand();

        assertTrue(command != null && !command.isEmpty());
        assertTrue(correctCommand != null && !correctCommand.isEmpty());
        assertTrue(command.length() == correctCommand.length());
        assertTrue(command.equals(correctCommand));
    }


    @Test
    public void testGlobPattern() throws IOException {

        String globPattern = new AllpathsLgV50().createGlobPattern(new File("/test/test1.fa"), new File("/test/test2.fa"));

        assertTrue(globPattern.equals("/test/test?.fa"));
    }

}
