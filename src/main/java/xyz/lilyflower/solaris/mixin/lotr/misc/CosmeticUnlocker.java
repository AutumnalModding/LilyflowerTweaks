package xyz.lilyflower.solaris.mixin.lotr.misc;

import lotr.common.playerdetails.ExclusiveGroup;
import lotr.common.playerdetails.PlayerDetails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.solaris.configuration.modules.SolarisLOTR;

@Mixin(PlayerDetails.class)
public class CosmeticUnlocker {
    @Inject(method = "hasExclusiveGroup", at = @At("HEAD"), cancellable = true, remap = false)
    public void unlock(ExclusiveGroup group, CallbackInfoReturnable<Boolean> cir) {
        if (SolarisLOTR.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hasAnyExclusiveGroup", at = @At("HEAD"), cancellable = true, remap = false)
    public void unlock(ExclusiveGroup[] groups, CallbackInfoReturnable<Boolean> cir) {
        if (SolarisLOTR.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isReceivedFromApi", at = @At("HEAD"), cancellable = true, remap = false)
    public void noRefetch(CallbackInfoReturnable<Boolean> cir) {
        if (SolarisLOTR.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }
}
