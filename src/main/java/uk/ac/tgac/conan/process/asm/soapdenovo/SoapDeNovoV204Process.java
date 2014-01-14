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
package uk.ac.tgac.conan.process.asm.soapdenovo;

import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.fgpt.conan.service.ConanProcessService;
import uk.ac.tgac.conan.process.asm.AbstractAssembler;
import uk.ac.tgac.conan.process.asm.AbstractAssemblerArgs;

import java.io.File;
import java.io.IOException;

/**
 * User: maplesod
 * Date: 16/04/13
 * Time: 15:57
 */
@MetaInfServices(uk.ac.tgac.conan.process.asm.AssemblerCreator.class)
public class SoapDeNovoV204Process extends AbstractAssembler {

    private static Logger log = LoggerFactory.getLogger(SoapDeNovoV204Process.class);


    public static final String EXE = "SOAPdenovo-127mer all";
    public static final String NAME = "SoapDeNovo_V2.04";

    public SoapDeNovoV204Process() {
        this(new SoapDeNovoV204Args());
    }

    public SoapDeNovoV204Process(AbstractAssemblerArgs args) {
        super(EXE, args, new SoapDeNovoV204Params());
    }

    protected SoapDeNovoV204Args getSoapDeNovoArgs() {
        return (SoapDeNovoV204Args)this.getArgs();
    }

    @Override
    public boolean makesUnitigs() {
        return false;
    }

    @Override
    public File getUnitigsFile() {
        return null;
    }

    @Override
    public File getContigsFile() {
        return new File(this.getArgs().getOutputDir(), this.getSoapDeNovoArgs().getOutputPrefix() + ".contig");
    }

    @Override
    public File getScaffoldsFile() {
        return new File(this.getArgs().getOutputDir(), this.getSoapDeNovoArgs().getOutputPrefix() + ".scafSeq");
    }


    @Override
    public void initialise() throws IOException {

        String pwdFull = new File(".").getAbsolutePath();
        String pwd = pwdFull.substring(0, pwdFull.length() - 2);

        // Create the SOAP lib configuration file from the library list
        SoapDeNovoV204Args args = this.getSoapDeNovoArgs();

        this.addPreCommand("cd " + args.getOutputDir().getAbsolutePath());
        this.addPostCommand("cd " + pwd);


        if (args.getConfigFile() == null && args.getLibraries() != null) {

            File configFile = new File(args.getOutputDir(), "soap.libs");
            log.debug("Config file not defined but libraries available.  Creating config file from libraries at " + configFile.getAbsolutePath());
            args.setConfigFile(configFile);
            args.createLibraryConfigFile(args.getLibraries(), configFile);
        }
        else if (args.getConfigFile() == null && args.getLibraries() == null) {
            throw new IOException("Cannot run SOAP without libraries or config file");
        }
        else if (args.getConfigFile() != null && args.getLibraries() != null) {
            log.warn("SOAP denovo found both a config file and libraries.  Assuming config file was intended to be used.");
        }
    }

    @Override
    public AbstractAssembler create(AbstractAssemblerArgs args, ConanProcessService conanProcessService) {

        SoapDeNovoV204Process proc = new SoapDeNovoV204Process(args);
        proc.setConanProcessService(conanProcessService);

        return proc;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
