package xyz.lilyflower.lilytweaks.mixin.lotr.misc;

import xyz.lilyflower.lilytweaks.core.LTConfig;
import lotr.common.playerdetails.ExclusiveGroup;
import lotr.common.playerdetails.PlayerDetails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerDetails.class)
public class CosmeticUnlockerMixin {
    @Inject(method = "hasExclusiveGroup", at = @At("HEAD"), cancellable = true, remap = false)
    public void unlock(ExclusiveGroup group, CallbackInfoReturnable<Boolean> cir) {
        if (LTConfig.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hasAnyExclusiveGroup", at = @At("HEAD"), cancellable = true, remap = false)
    public void unlock(ExclusiveGroup[] groups, CallbackInfoReturnable<Boolean> cir) {
        if (LTConfig.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isReceivedFromApi", at = @At("HEAD"), cancellable = true, remap = false)
    public void noRefetch(CallbackInfoReturnable<Boolean> cir) {
        if (LTConfig.UNLOCK_COSMETICS) {
            cir.setReturnValue(true);
        }
    }
}
