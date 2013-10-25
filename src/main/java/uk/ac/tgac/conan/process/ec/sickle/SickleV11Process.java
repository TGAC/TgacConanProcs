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

import org.springframework.stereotype.Component;
import uk.ac.ebi.fgpt.conan.core.process.AbstractConanProcess;
import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.tgac.conan.process.ec.ErrorCorrector;
import uk.ac.tgac.conan.process.ec.ErrorCorrectorArgs;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 14:36
 */
@Component
public class SickleV11Process extends AbstractConanProcess implements ErrorCorrector {


    public enum JobType {

        SINGLE_END {
            @Override
            public ProcessParams getParameters() {
                return new SickleSeV11Params();
            }

            @Override
            public ErrorCorrectorArgs createArgs() {
                return new SickleSeV11Args();
            }

            @Override
            public String getExe() {
                return "sickle se";
            }
        },
        PAIRED_END {
            @Override
            public ProcessParams getParameters() {
                return new SicklePeV11Params();
            }

            @Override
            public ErrorCorrectorArgs createArgs() {
                return new SicklePeV11Args();
            }

            @Override
            public String getExe() {
                return "sickle pe";
            }
        };

        public abstract String getExe();

        public abstract ProcessParams getParameters();
        public abstract ErrorCorrectorArgs createArgs();
    }

    public SickleV11Process() {
        this(new SicklePeV11Args());
    }

    public SickleV11Process(ErrorCorrectorArgs args) {
        super(args.isPairedEnd() ? JobType.PAIRED_END.getExe() : JobType.SINGLE_END.getExe(),
                args,
                args.isPairedEnd() ? JobType.PAIRED_END.getParameters() : JobType.SINGLE_END.getParameters());
    }

    @Override
    public ErrorCorrectorArgs getArgs() {
        return (ErrorCorrectorArgs) this.getProcessArgs();
    }

    @Override
    public void initialise() {
    }

    @Override
    public String getCommand() {
        return this.getCommand(this.getProcessArgs(), false);
    }

    @Override
    public String getName() {
        return "Sickle_V1.1";
    }
}
