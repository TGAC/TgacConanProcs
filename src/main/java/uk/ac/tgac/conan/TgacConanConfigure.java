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
package uk.ac.tgac.conan;

import org.apache.commons.io.FileUtils;
import uk.ac.tgac.conan.core.util.JarUtils;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

/**
 * User: maplesod
 * Date: 25/04/13
 * Time: 13:50
 */
public class TgacConanConfigure {

    public static final File SETTINGS_DIR = new File(System.getProperty("user.home") + "/.tgac/tgac-conan/");

    public static void initialise() throws IOException {

        if (!SETTINGS_DIR.exists()) {
            if (!SETTINGS_DIR.mkdirs()) {
                throw new IOException("Could not create settings directory at: " + SETTINGS_DIR.getAbsolutePath());
            }
        }

        File internalTools = FileUtils.toFile(TgacConanConfigure.class.getResource("/tools"));
        File externalTools = new File(SETTINGS_DIR, "tools");

        JarFile thisJar = JarUtils.jarForClass(TgacConanConfigure.class, null);

        if (thisJar == null) {
            FileUtils.copyDirectory(internalTools, externalTools);
        }
        else {
            JarUtils.copyResourcesToDirectory(thisJar, "tools", externalTools.getAbsolutePath());
        }
    }
}
