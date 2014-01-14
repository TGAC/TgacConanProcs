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

import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.CommandLineFormat;
import uk.ac.ebi.fgpt.conan.service.exception.ConanParameterException;

import java.util.ArrayList;
import java.util.List;

/**
 * User: maplesod
 * Date: 22/04/13
 * Time: 18:30
 */
public class QuastV2_2Process extends AbstractConanProcess {

    public static final String EXE = "quast.py";

    public QuastV2_2Process() {
        this(new QuastV2_2Args());
    }

    public QuastV2_2Process(QuastV2_2Args args) {
        super(EXE, args, new QuastV2_2Params());
    }

    @Override
    public String getCommand() throws ConanParameterException {

        List<String> commands = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" ");
        sb.append(this.getProcessArgs().getArgMap().buildOptionString(CommandLineFormat.POSIX));

        String firstPart = sb.toString().trim();
        String files = ((QuastV2_2Args)this.getProcessArgs()).getInputFilesAsString();

        commands.add(firstPart + " " + files);

        String command = StringUtils.join(commands, "; ");

        return command;
    }

    @Override
    public String getName() {
        return "Quast_V2.2";
    }
}
