package xyz.lilyflower.lilytweaks.internal.bwiama;

import lotr.common.LOTRDimension;
import lotr.common.world.LOTRWorldProvider;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import xyz.lilyflower.lilytweaks.api.ModdedGalacticraftWorldProvider;

public class LOTRGalacticraftWorldProvider extends LOTRWorldProvider implements ModdedGalacticraftWorldProvider {
    @Override
    public LOTRDimension getLOTRDimension() {
        return LOTRDimension.MIDDLE_EARTH;
    }

    @Override
    public CelestialBody getCelestialBody() {
        return PlanetSetup.ARDA;
    }
}
