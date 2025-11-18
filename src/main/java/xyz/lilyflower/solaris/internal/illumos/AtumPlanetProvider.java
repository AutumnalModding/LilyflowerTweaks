package xyz.lilyflower.solaris.internal.illumos;

import com.teammetallurgy.atum.world.AtumWorldProvider;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import xyz.lilyflower.solaris.integration.galacticraft.PlanetProvider;

public class AtumPlanetProvider extends AtumWorldProvider implements PlanetProvider {
    @Override
    public CelestialBody getCelestialBody() {
        return PlanetSetup.KV62;
    }
}
