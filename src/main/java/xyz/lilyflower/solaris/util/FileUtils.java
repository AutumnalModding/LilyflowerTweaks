package xyz.lilyflower.solaris.util;

import java.io.File;

public class FileUtils {
    public static String basename(String name) { return name.replaceAll("\\." + extension(name), ""); }
    public static String extension(String name) { return (name.lastIndexOf('.') == -1) ? "" : name.substring(name.lastIndexOf('.') + 1); }

}
