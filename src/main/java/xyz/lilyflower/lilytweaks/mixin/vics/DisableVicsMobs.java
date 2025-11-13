package xyz.lilyflower.lilytweaks.mixin.vics;

import com.vicmatskiv.mw.CommonProxy;
import com.vicmatskiv.mw.Entities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entities.class)
public class DisableVicsMobs {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true, remap = false)
    private static void clobberOverTheHead(CommonProxy commonProxy, CallbackInfo ci) {
        ci.cancel();
    }
}
