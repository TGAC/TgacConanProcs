package uk.ac.tgac.conan.process.repeat;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by maplesod on 03/02/14.
 */
public class ReportTest {

    @Test
    public void testReportParse() throws IOException {
        File reportFile = FileUtils.toFile(this.getClass().getResource("/stats/repeat_masker_report.tbl"));

        RepeatMaskerV4_0.Report rp = new RepeatMaskerV4_0.Report(reportFile, "test1");

        String report = rp.toString();

        assertNotNull(rp);
        assertTrue(rp.getLabel().equals("test1"));
        assertTrue(rp.getPercLowComp() == 0.5);
        assertTrue(rp.getLenSimple() == 10406);

    }

    @Test
    public void testReport2Parse() throws IOException {
        File reportFile = FileUtils.toFile(this.getClass().getResource("/stats/repeat_masker_report_2.tbl"));

        RepeatMaskerV4_0.Report rp = new RepeatMaskerV4_0.Report(reportFile, "test2");

        String report = rp.toString();

        assertNotNull(rp);
        assertTrue(rp.getLabel().equals("test2"));
        assertTrue(rp.getLenSrna() == 1505);
        assertTrue(rp.getBasesMasked() == 16057);
    }

    @Test
    public void testReportCombiner() throws IOException {
        File reportFile1 = FileUtils.toFile(this.getClass().getResource("/stats/repeat_masker_report.tbl"));
        File reportFile2 = FileUtils.toFile(this.getClass().getResource("/stats/repeat_masker_report_2.tbl"));

        RepeatMaskerV4_0.CombinedReport report = new RepeatMaskerV4_0.CombinedReport();

        report.add(new RepeatMaskerV4_0.Report(reportFile1, "test1"));
        report.add(new RepeatMaskerV4_0.Report(reportFile2, "test2"));

        String reportPipeSep = report.toString();

        assertNotNull(report);

    }
}
