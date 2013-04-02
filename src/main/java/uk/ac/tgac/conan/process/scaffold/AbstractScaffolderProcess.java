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
package uk.ac.tgac.conan.process.scaffold;

import uk.ac.ebi.fgpt.conan.model.param.ProcessParams;
import uk.ac.tgac.conan.process.AbstractAmpProcess;

/**
 * User: maplesod
 * Date: 23/01/13
 * Time: 13:48
 */
public abstract class AbstractScaffolderProcess extends AbstractAmpProcess {

    protected AbstractScaffolderProcess(String executable, AbstractScaffolderArgs args, ProcessParams params) {
        super(executable, args, params);
    }

    public AbstractScaffolderArgs getScaffolderArgs() {
        return (AbstractScaffolderArgs)this.getProcessArgs();
    }
}
