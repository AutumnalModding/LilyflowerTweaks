package xyz.lilyflower.solaris.mixin.lotr.client;

import lotr.client.LOTRAttackTiming;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.solaris.configuration.modules.SolarisLOTR;

@Mixin(LOTRAttackTiming.class)
public class LOTRAttackTimingClientMixin {
    @Inject(method = "doAttackTiming", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cease(CallbackInfo ci) {
        if (SolarisLOTR.DISABLE_ATTACK_TIMINGS) {
            LOTRAttackTiming.reset();
            ci.cancel();
        }
    }

    @Inject(method = "renderAttackMeter", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cease(ScaledResolution resolution, float partialTicks, CallbackInfo ci) {
        if (SolarisLOTR.DISABLE_ATTACK_TIMINGS) {
            ci.cancel();
        }
    }
}
