package xyz.lilyflower.solaris.mixin.galacticraft;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import xyz.lilyflower.solaris.integration.galacticraft.StarRegistry;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WorldUtil.class, remap = false)
public class StarSupportMixin {
    @Inject(method = "getReachableCelestialBodiesForDimensionID", at = @At(value = "INVOKE", target = "Lmicdoodle8/mods/galacticraft/api/galaxies/GalaxyRegistry;getRegisteredPlanets()Ljava/util/Map;"))
    private static void addStars(int id, CallbackInfoReturnable<CelestialBody> cir, @Local List<CelestialBody> bodies) {
        bodies.addAll(StarRegistry.STARS);
    }

    @Inject(method = "getReachableCelestialBodiesForName", at = @At(value = "INVOKE", target = "Lmicdoodle8/mods/galacticraft/api/galaxies/GalaxyRegistry;getRegisteredPlanets()Ljava/util/Map;"))
    private static void addStars(String name, CallbackInfoReturnable<CelestialBody> cir, @Local List<CelestialBody> bodies) {
        bodies.addAll(StarRegistry.STARS);
    }
}
