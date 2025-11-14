package xyz.lilyflower.lilytweaks.mixin.galacticraft;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.Satellite;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.configuration.modules.GalacticraftIntegrationConfiguration;

@Mixin(GalaxyRegistry.class)
public class CelestialBodyDisabler {
    @Inject(method = "registerMoon", at = @At("HEAD"), cancellable = true, remap = false)
    private static void disableMoon(Moon moon, CallbackInfoReturnable<Boolean> cir) {
        if (GalacticraftIntegrationConfiguration.DISABLED_CELESTIAL_BODIES.contains(moon.getName())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "registerPlanet", at = @At("HEAD"), cancellable = true, remap = false)
    private static void disablePlanet(Planet planet, CallbackInfoReturnable<Boolean> cir) {
        if (GalacticraftIntegrationConfiguration.DISABLED_CELESTIAL_BODIES.contains(planet.getName())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "registerSatellite", at = @At("HEAD"), cancellable = true, remap = false)
    private static void disableSatellite(Satellite satellite, CallbackInfoReturnable<Boolean> cir) {
        if (GalacticraftIntegrationConfiguration.DISABLED_CELESTIAL_BODIES.contains(satellite.getName())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "registerSolarSystem", at = @At("HEAD"), cancellable = true, remap = false)
    private static void disableSolarSystem(SolarSystem system, CallbackInfoReturnable<Boolean> cir) {
        if (GalacticraftIntegrationConfiguration.DISABLED_CELESTIAL_BODIES.contains(system.getName())) {
            cir.setReturnValue(false);
        }
    }
}
