package xyz.lilyflower.solaris.mixin.bandaid;

import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.solaris.configuration.modules.SolarisGalacticraft;

@Mixin(Planet.class)
public class PlanetMixin {
    @Inject(method = "setParentSolarSystem", at = @At("HEAD"), cancellable = true, remap = false)
    public void disable(SolarSystem system, CallbackInfoReturnable<Planet> cir) {
        if (SolarisGalacticraft.DISABLED_CELESTIAL_BODIES.contains(system.getName())) cir.setReturnValue((Planet) (Object) this);
    }
}
