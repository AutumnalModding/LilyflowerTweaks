package xyz.lilyflower.lilytweaks.internal.bwiama;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import java.util.Arrays;
import java.util.List;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeOverworld;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import net.minecraft.util.ResourceLocation;
import xyz.lilyflower.lilytweaks.api.LilyflowerTweaksIntegrationModule;
import xyz.lilyflower.lilytweaks.init.LilyflowerTweaksInitializationSystem;

public class PlanetSetup implements LilyflowerTweaksIntegrationModule {
    public static final Planet ARDA = (Planet) new Planet("arda")
            .setParentSolarSystem(GalacticraftCore.solarSystemSol)
            .setTierRequired(1)
            .setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png"))
            .setDimensionInfo(32767, LOTRGalacticraftWorldProvider.class)
            .setRelativeOrbitTime(2.0F)
            .atmosphereComponent(IAtmosphericGas.NITROGEN)
            .atmosphereComponent(IAtmosphericGas.OXYGEN)
            .atmosphereComponent(IAtmosphericGas.ARGON)
            .atmosphereComponent(IAtmosphericGas.WATER);

    public static final Planet ERIS = (Planet) new Planet("eris")
            .setParentSolarSystem(GalacticraftCore.solarSystemSol)
            .setTierRequired(1)
            .setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/moon.png"))
            .setDimensionInfo(-28, WorldProviderMoon.class)
            .setRelativeOrbitTime(20.0F)
            .setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.15F, 1.15F));

    @Override
    public void run(FMLStateEvent stage) {
        LilyflowerTweaksInitializationSystem.LOGGER.info("Registering Galacticraft planets...");
        GalaxyRegistry.registerPlanet(ARDA);
        GalaxyRegistry.registerPlanet(ERIS);
        GalacticraftRegistry.registerTeleportType(LOTRGalacticraftWorldProvider.class, new TeleportTypeOverworld());
    }

    @Override
    public List<String> requiredMods() {
        return Arrays.asList("GalacticraftCore", "lotr");
    }

    @Override
    public boolean valid(FMLStateEvent stage) {
        return stage instanceof FMLInitializationEvent;
    }
}
