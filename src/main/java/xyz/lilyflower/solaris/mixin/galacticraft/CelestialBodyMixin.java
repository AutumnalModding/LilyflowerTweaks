package xyz.lilyflower.solaris.mixin.galacticraft;


import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.solaris.configuration.modules.SolarisGalacticraft;

@Mixin(CelestialBody.class)
public class CelestialBodyMixin {
    @Inject(method = "setUnreachable", at = @At("HEAD"), cancellable = true, remap = false)
    public void noYouDont(CallbackInfo ci) {
        if (SolarisGalacticraft.DISABLE_UNREACHABLE_PLANETS) {
            ci.cancel();
        }
    }
}
