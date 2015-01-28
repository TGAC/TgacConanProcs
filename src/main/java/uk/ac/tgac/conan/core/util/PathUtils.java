package uk.ac.tgac.conan.core.util;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 16/01/14
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */
public class PathUtils {

    public static String joinAbsolutePaths(File[] files, String separator) {

        StringBuilder sb = new StringBuilder();

        if (files != null && files.length > 0) {
            sb.append(files[0].getAbsolutePath());

            for(int i = 1; i < files.length; i++) {
                sb.append(separator).append(files[1].getAbsolutePath());
            }
        }
        else {
            return "";
        }

        return sb.toString();
    }

    public static File[] splitPaths(String paths, String separator) {

        String[] parts = paths.split(separator);

        File[] files = new File[parts.length];


        for(int i = 0; i < parts.length; i++) {
            files[i] = new File(parts[i]);
        }

        return files;
    }

    public static void cleanDirectory(File dir) {
        if (dir.exists()) {
            dir.delete();
        }
        dir.mkdirs();
    }
}
