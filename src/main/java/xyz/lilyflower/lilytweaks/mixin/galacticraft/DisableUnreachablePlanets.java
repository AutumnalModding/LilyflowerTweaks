package xyz.lilyflower.lilytweaks.mixin.galacticraft;

import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.configuration.modules.GalacticraftIntegrationConfiguration;

@Mixin(GalacticraftCore.class)
public class DisableUnreachablePlanets {
    @Inject(method = "makeUnreachablePlanet", at = @At("HEAD"), cancellable = true, remap = false)
    public void disable(String name, SolarSystem system, CallbackInfoReturnable<Planet> cir) {
        if (GalacticraftIntegrationConfiguration.DISABLE_UNREACHABLE_PLANETS) {
            cir.setReturnValue(null);
        }
    }
}
