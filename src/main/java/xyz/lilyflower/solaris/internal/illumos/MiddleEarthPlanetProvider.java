package xyz.lilyflower.solaris.internal.illumos;

import lotr.common.world.LOTRWorldProviderMiddleEarth;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import xyz.lilyflower.solaris.integration.galacticraft.PlanetProvider;

public class MiddleEarthPlanetProvider extends LOTRWorldProviderMiddleEarth implements PlanetProvider {
    @Override
    public CelestialBody getCelestialBody() {
        return PlanetSetup.ARDA;
    }
}
