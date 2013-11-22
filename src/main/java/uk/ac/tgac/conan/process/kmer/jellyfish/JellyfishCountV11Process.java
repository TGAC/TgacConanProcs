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
package uk.ac.tgac.conan.process.kmer.jellyfish;

import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: maplesod
 * Date: 26/03/13
 * Time: 17:58
 */
public class JellyfishCountV11Process extends AbstractConanProcess {

    public static final String EXE = "jellyfish count";

    public JellyfishCountV11Process() {
        this(new JellyfishCountV11Args());
    }

    public JellyfishCountV11Process(JellyfishCountV11Args args) {
        super(EXE, args, new JellyfishCountV11Params());
    }

    public JellyfishCountV11Args getArgs() {
        return (JellyfishCountV11Args) this.getProcessArgs();
    }

    @Override
    public String getCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append(EXE);
        sb.append(" ");
        for (Map.Entry<ConanParameter, String> param : this.getProcessArgs().getArgMap().entrySet()) {

            String name = param.getKey().getName();
            if (!name.equals("input") && !name.equals("memory")) {
                sb.append("-");
                sb.append(param.getKey());
                if (!param.getKey().isBoolean()) {
                    sb.append(" ");
                    sb.append(param.getValue());
                }
                sb.append(" ");
            }
        }

        JellyfishCountV11Args args = (JellyfishCountV11Args) this.getProcessArgs();

        return sb.toString().trim() + " " + args.getInputFile();
    }

    @Override
    public String getName() {
        return "Jellyfish_Count_V1.1";
    }
}
