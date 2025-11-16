package xyz.lilyflower.lilytweaks.util;

import com.github.bsideup.jabel.Desugar;

@Desugar // The fifteenth competing standard
public record Pair<L, R>(L left, R right) {}
