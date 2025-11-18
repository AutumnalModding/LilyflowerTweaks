package xyz.lilyflower.solaris.util;

import com.github.bsideup.jabel.Desugar;

public class FifteenthCompetingStandard {
    public static final float TAU = 6.283185307179586F;

    @Desugar // The original 927
    public record Pair<L, R>(L left, R right) {}
}
