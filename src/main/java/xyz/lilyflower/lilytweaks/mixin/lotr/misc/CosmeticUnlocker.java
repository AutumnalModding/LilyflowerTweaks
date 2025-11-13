package xyz.lilyflower.lilytweaks.mixin.lotr.misc;

import lotr.common.playerdetails.ExclusiveGroup;
import lotr.common.playerdetails.PlayerDetails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.configuration.modules.LOTRModIntegrationConfiguration;

@Mixin(PlayerDetails.class)
public class CosmeticUnlocker {
    @Inject(method = "hasExclusiveGroup", at = @At("HEAD"), cancellable = true, remap = false)
    public void unlock(ExclusiveGroup group, CallbackInfoReturnable<Boolean> cir) {
        if (LOTRModIntegrationConfiguration.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hasAnyExclusiveGroup", at = @At("HEAD"), cancellable = true, remap = false)
    public void unlock(ExclusiveGroup[] groups, CallbackInfoReturnable<Boolean> cir) {
        if (LOTRModIntegrationConfiguration.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isReceivedFromApi", at = @At("HEAD"), cancellable = true, remap = false)
    public void noRefetch(CallbackInfoReturnable<Boolean> cir) {
        if (LOTRModIntegrationConfiguration.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }
}
