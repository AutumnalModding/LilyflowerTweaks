package xyz.lilyflower.lilytweaks.mixin.alfheim;

import alfheim.common.core.handler.ESMHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

@Mixin(value = ESMHandler.class, remap = false)
public class ESMFlightDisabler {
    @Inject(method = "isAbilityDisabled", at = @At("HEAD"), cancellable = true)
    public void disable(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(LilyflowerTweaksConfigSystem.DISABLE_ESM_RACES);
    }
}
