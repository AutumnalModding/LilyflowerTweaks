package xyz.lilyflower.solaris.mixin.galacticraft;

import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import java.util.ArrayList;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.solaris.integration.galacticraft.StarRegistry;

@Mixin(GalacticraftCore.class)
public class StarRegistrationHelper {
    @Inject(method = "serverStarting", at = @At(value = "INVOKE", target = "Lmicdoodle8/mods/galacticraft/api/galaxies/GalaxyRegistry;getRegisteredMoons()Ljava/util/Map;"), remap = false)
    public void addStars(FMLServerStartingEvent event, CallbackInfo ci, @Local ArrayList<CelestialBody> bodies) {
        bodies.addAll(StarRegistry.STARS);
    }

    @Inject(method = "postInit", at = @At(value = "INVOKE", target = "Lmicdoodle8/mods/galacticraft/api/galaxies/GalaxyRegistry;getRegisteredMoons()Ljava/util/Map;"), remap = false)
    public void addStars(FMLPostInitializationEvent event, CallbackInfo ci, @Local ArrayList<CelestialBody> bodies) {
        bodies.addAll(StarRegistry.STARS);
    }
}
