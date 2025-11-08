package xyz.lilyflower.lilytweaks.util.loader;

import java.util.EnumSet;
import lotr.common.LOTRDimension;
import lotr.common.fac.LOTRFaction;
import lotr.common.fac.LOTRMapRegion;
import lotr.common.world.spawning.LOTRInvasions;

@SuppressWarnings("rawtypes")
public class EnumHelperMappings {
    public static final Class[][] LOTR_EH_MAPPINGS = {
            {LOTRInvasions.class, LOTRFaction.class},
            {LOTRFaction.class, int.class, LOTRDimension.class, LOTRDimension.DimensionRegion.class, LOTRMapRegion.class, EnumSet.class},
    };
}
