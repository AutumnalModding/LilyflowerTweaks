package xyz.lilyflower.solaris.internal.illumos;

import com.gildedgames.the_aether.world.AetherWorldProvider;
import com.teammetallurgy.atum.world.AtumWorldProvider;
import java.util.Arrays;
import java.util.List;
import lotr.common.world.LOTRWorldProviderMiddleEarth;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeOverworld;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import net.minecraft.util.ResourceLocation;
import xyz.lilyflower.solaris.api.LoadStage;
import xyz.lilyflower.solaris.api.SolarisIntegrationModule;
import xyz.lilyflower.solaris.init.Solaris;
import xyz.lilyflower.solaris.integration.galacticraft.StarRegistry;
import xyz.lilyflower.solaris.integration.galacticraft.TeleportTypeBalloons;
import xyz.lilyflower.solaris.integration.galacticraft.TeleportTypeDropPod;
import xyz.lilyflower.solaris.util.SolarisExtensions;

public class PlanetSetup implements SolarisIntegrationModule {
    public static final SolarSystem MEDIAKORIA = new SolarSystem("mediakoria", "milkyWay")
            .setMapPosition(new Vector3(0, 0, 0));

    public static final Star EYE_OF_VELZIE = (Star) new Star("eov")
            .setParentSolarSystem(MEDIAKORIA)
            .setTierRequired(2)
            .setBodyIcon(BODY_TEXTURE("neptune"))
            .atmosphereComponent(IAtmosphericGas.OXYGEN)
            .atmosphereComponent(IAtmosphericGas.HELIUM)
            .atmosphereComponent(IAtmosphericGas.HYDROGEN)
            .atmosphereComponent(IAtmosphericGas.NITROGEN)
            .setDimensionInfo(32765, AetherPlanetProvider.class)
            .setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.0F, 0.0F));

    public static final Planet ARDA = (Planet) new Planet("arda")
            .setParentSolarSystem(MEDIAKORIA)
            .setTierRequired(1)
            .setRelativeOrbitTime(2.0F)
            .setBodyIcon(BODY_TEXTURE("earth"))
            .atmosphereComponent(IAtmosphericGas.ARGON)
            .atmosphereComponent(IAtmosphericGas.OXYGEN)
            .atmosphereComponent(IAtmosphericGas.WATER)
            .atmosphereComponent(IAtmosphericGas.NITROGEN)
            .setDimensionInfo(32767, MiddleEarthPlanetProvider.class);

    public static final Planet ERIS = (Planet) new Planet("eris")
            .setParentSolarSystem(MEDIAKORIA)
            .setTierRequired(1)
            .setRelativeOrbitTime(20.0F)
            .setBodyIcon(BODY_TEXTURE("moon"))
            .setDimensionInfo(-28, WorldProviderMoon.class)
            .setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.15F, 1.15F));

    public static final Planet KV62 = (Planet) new Planet("KV62")
            .setParentSolarSystem(MEDIAKORIA)
            .setTierRequired(1)
            .setRelativeSize(1.45F)
            .setRelativeOrbitTime(35.0F)
            .setBodyIcon(BODY_TEXTURE("venus"))
            .atmosphereComponent(IAtmosphericGas.CO2)
            .atmosphereComponent(IAtmosphericGas.OXYGEN)
            .atmosphereComponent(IAtmosphericGas.METHANE)
            .setDimensionInfo(32766, AtumPlanetProvider.class)
            .setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.375F, 1.375F));

    public static final Planet SEDNA = (Planet) new Planet("sedna")
            .setParentSolarSystem(MEDIAKORIA)
            .setTierRequired(2)
            .setPhaseShift(0.1667F)
            .setRelativeSize(0.535F)
            .setRelativeOrbitTime(1.95F)
            .setBodyIcon(BODY_TEXTURE("mars"))
            .atmosphereComponent(IAtmosphericGas.CO2)
            .atmosphereComponent(IAtmosphericGas.ARGON)
            .atmosphereComponent(IAtmosphericGas.NITROGEN)
            .setDimensionInfo(-29, WorldProviderMars.class)
            .setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.25F, 1.25F));

    public static final Planet CERUE = (Planet) new Planet("cerue")
            .setParentSolarSystem(MEDIAKORIA)
            .setTierRequired(3)
            .setRelativeOrbitTime(45.0F)
            .setBodyIcon(BODY_TEXTURE("asteroid"))
            .setPhaseShift(SolarisExtensions.TAU) // 2π
            .setDimensionInfo(-30, WorldProviderAsteroids.class)
            .setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.75F, 1.75F));

    @Override
    public void run() {
        Solaris.LOGGER.info("Registering Galacticraft planets...");
        MEDIAKORIA.setMainStar(EYE_OF_VELZIE);
        GalaxyRegistry.registerSolarSystem(MEDIAKORIA);
        GalaxyRegistry.registerPlanet(ARDA);
        GalaxyRegistry.registerPlanet(ERIS);
        GalaxyRegistry.registerPlanet(KV62);
        GalaxyRegistry.registerPlanet(SEDNA);
        GalaxyRegistry.registerPlanet(CERUE);
        StarRegistry.add(EYE_OF_VELZIE);

        GalacticraftRegistry.registerTeleportType(AtumWorldProvider.class, new TeleportTypeBalloons());
        GalacticraftRegistry.registerTeleportType(AtumPlanetProvider.class, new TeleportTypeBalloons());
        GalacticraftRegistry.registerTeleportType(AetherWorldProvider.class, new TeleportTypeDropPod());
        GalacticraftRegistry.registerTeleportType(AetherPlanetProvider.class, new TeleportTypeDropPod());
        GalacticraftRegistry.registerTeleportType(MiddleEarthPlanetProvider.class, new TeleportTypeOverworld());
        GalacticraftRegistry.registerTeleportType(LOTRWorldProviderMiddleEarth.class, new TeleportTypeOverworld());

        GalacticraftRegistry.registerRocketGui(AtumWorldProvider.class, new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/gui/marsRocketGui.png"));
        GalacticraftRegistry.registerRocketGui(LOTRWorldProviderMiddleEarth.class, new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/overworldRocketGui.png"));
    }

    private static ResourceLocation BODY_TEXTURE(String body) {
        return new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/" + body + ".png");
    }

    @Override
    public List<String> requiredMods() {
        return Arrays.asList("GalacticraftCore", "lotr");
    }

    @Override
    public boolean valid() {
        return Solaris.STATE == LoadStage.RUNNING;
    }
}
