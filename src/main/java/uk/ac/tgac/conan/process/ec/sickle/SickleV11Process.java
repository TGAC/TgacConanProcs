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
package uk.ac.tgac.conan.process.ec.sickle;

import org.kohsuke.MetaInfServices;
import uk.ac.ebi.fgpt.conan.model.param.ConanParameter;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrector;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorCreator;
import uk.ac.tgac.conan.process.ec.AbstractErrorCorrectorArgs;

import java.util.Map;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 14:36
 */
@MetaInfServices(uk.ac.tgac.conan.process.ec.ErrorCorrectorCreator.class)
public class SickleV11Process extends AbstractErrorCorrector {

    protected static final String NAME = "Sickle_V1.1";
    protected static final String EXE = "sickle";

    public enum JobType {

        SINGLE_END {
            @Override
            public ProcessParams getParameters() {
                return new SickleSeV11Params();
            }

            @Override
            public AbstractErrorCorrectorArgs createArgs() {
                return new SickleSeV11Args();
            }

            @Override
            public String getMode() {
                return "se";
            }
        },
        PAIRED_END {
            @Override
            public ProcessParams getParameters() {
                return new SicklePeV11Params();
            }

            @Override
            public AbstractErrorCorrectorArgs createArgs() {
                return new SicklePeV11Args();
            }

            @Override
            public String getMode() {
                return "pe";
            }
        };

        public abstract String getMode();

        public abstract ProcessParams getParameters();
        public abstract AbstractErrorCorrectorArgs createArgs();
    }

    public SickleV11Process() {
        this(new SicklePeV11Args());
    }

    public SickleV11Process(AbstractErrorCorrectorArgs args) {
        super(EXE,
                args,
                args.isPairedEnd() ? JobType.PAIRED_END.getParameters() : JobType.SINGLE_END.getParameters());
    }

    @Override
    public AbstractErrorCorrectorArgs getArgs() {
        return (AbstractErrorCorrectorArgs) this.getProcessArgs();
    }

    @Override
    public String getCommand() {

        String mode = this.getArgs().isPairedEnd() ? JobType.PAIRED_END.getMode() : JobType.SINGLE_END.getMode();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ConanParameter, String> param : this.getArgs().getArgMap().entrySet()) {

            if (!param.getKey().isBoolean()) {
                sb.append(" ");
                sb.append(param.getValue());
            }
            sb.append(" ");
        }

        return EXE + " " + mode + " " + sb.toString().trim();
    }

    @Override
    public AbstractErrorCorrector create(AbstractErrorCorrectorArgs args) {
        return new SickleV11Process(args);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
