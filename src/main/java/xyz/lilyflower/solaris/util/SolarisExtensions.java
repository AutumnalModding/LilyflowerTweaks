package xyz.lilyflower.solaris.util;

import com.github.bsideup.jabel.Desugar;

public class SolarisExtensions {
    public static final float TAU = 6.283185307179586F;

    public static String basename(String name) { return name.replaceAll("\\." + extension(name), ""); }

    public static String extension(String name) { return (name.lastIndexOf('.') == -1) ? "" : name.substring(name.lastIndexOf('.') + 1); }

    @Desugar // The original 927
    public record Pair<L, R>(L left, R right) {}

}
