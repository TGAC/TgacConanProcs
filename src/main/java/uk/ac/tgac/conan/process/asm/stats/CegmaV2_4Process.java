/**
 * RAMPART - Robust Automatic MultiPle AssembleR Toolkit
 * Copyright (C) 2013  Daniel Mapleson - TGAC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package uk.ac.tgac.conan.process.asm.stats;

import org.apache.commons.io.FileUtils;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 12/08/13
 * Time: 09:54
 */
public class CegmaV2_4Process extends AbstractConanProcess {

    public static final String EXE = "cegma";

    private File modifiedGenomeFile;

    public CegmaV2_4Process() {
        this(new CegmaV2_4Args());
    }

    public CegmaV2_4Process(CegmaV2_4Args args) {
        super(EXE, args, new CegmaV2_4Params());

        this.modifiedGenomeFile = null;
    }

    /**
     * Make sure you run this before executing the CEGMA process
     * @throws IOException
     */
    public void initialise() throws IOException {
        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        CegmaV2_4Args cArgs = (CegmaV2_4Args)this.getProcessArgs();

        File targetDir = cArgs.getOutputPrefix().getParentFile();

        // Create directories
        if (targetDir.exists()) {
            FileUtils.deleteDirectory(targetDir);
        }
        targetDir.mkdir();

        File tmpDir = new File(targetDir, "temp");
        if (tmpDir.exists()) {
            FileUtils.deleteDirectory(tmpDir);
        }
        tmpDir.mkdir();

        // Move into the output dir
        this.addPreCommand("cd " + targetDir.getAbsolutePath());

        // Add run specific cegmatmp env var
        this.addPreCommand("CEGMATMP=" + tmpDir.getAbsolutePath());

        // Create modified genome, that has BLAST friendly headers (this to avoid incompatibilities between Abyss and Cegma)
        // Only do this if required
        File modifiedFile = new File(targetDir, cArgs.getGenomeFile().getName() + ".mod.fa");
        this.modifiedGenomeFile = modifiedFile;
        this.addPreCommand("sed 's/>/>seq_/g' " + cArgs.getGenomeFile().getAbsolutePath() + " > " + modifiedFile.getAbsolutePath());

        // revert to existing dir
        this.addPostCommand("cd " + pwd);

        // Note, may need to add command to clean the input assembly here... can't have spaces or pipes (I think)

    }

    @Override
    public String getCommand() {

        List<String> commands = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" ");
        for (Map.Entry<ConanParameter, String> param : this.getProcessArgs().getArgMap().entrySet()) {

            if (param.getKey().getName().equals("genome") && this.modifiedGenomeFile != null) {
                sb.append("--");
                sb.append(param.getKey());
                sb.append(" ");
                sb.append(this.modifiedGenomeFile.getAbsolutePath());
                sb.append(" ");
            }
            else {
                sb.append("--");
                sb.append(param.getKey());
                if (!param.getKey().isBoolean()) {
                    sb.append(" ");
                    sb.append(param.getValue());
                }
                sb.append(" ");
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String getName() {
        return "Cegma_V2.4";
    }


}
