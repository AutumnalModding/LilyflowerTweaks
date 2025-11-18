package xyz.lilyflower.solaris.mixin.galacticraft;

import com.llamalad7.mixinextras.sugar.Local;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.solaris.configuration.modules.SolarisGalacticraft;

@Mixin(value = GuiCelestialSelection.class, remap = false)
public class SolarSystemChangeHelper {
    @Shadow protected Object selectedParent;
    @Shadow protected CelestialBody selectedBody;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void setMainSolarSystem(CallbackInfo ci) {
        this.selectedParent = GalaxyRegistry.getRegisteredSolarSystems().get(SolarisGalacticraft.MAIN_SOLAR_SYSTEM);
    }

    @Inject(method = "mouseClicked", at = @At(value = "FIELD", target = "Lmicdoodle8/mods/galacticraft/core/GalacticraftCore;solarSystemSol:Lmicdoodle8/mods/galacticraft/api/galaxies/SolarSystem;", shift = At.Shift.AFTER))
    public void setMainSolarSystem(int x, int y, int button, CallbackInfo ci, @Local Object selected) {
        selected = GalaxyRegistry.getRegisteredSolarSystems().get(SolarisGalacticraft.MAIN_SOLAR_SYSTEM);
    }

    @Redirect(method = "getCelestialBodyPosition", at = @At(value = "INVOKE", target = "Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;getUnlocalizedName()Ljava/lang/String;"))
    public String setMainSolarSystem(CelestialBody instance) {
        SolarSystem main = GalaxyRegistry.getRegisteredSolarSystems().get(SolarisGalacticraft.MAIN_SOLAR_SYSTEM);
        return main.getMainStar() == instance ? "star.sol" : instance.getUnlocalizedName();
    }
}
