package io.freefair.network;

import java.util.Locale;

/**
 * @author Lars Grefer
 */
public class OS {

    private static final String NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

    public static boolean isMac() {
        return NAME.contains("mac") || NAME.contains("darwin");
    }

    public static boolean isWindows() {
        return NAME.contains("win");
    }
}
