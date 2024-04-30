package xyz.lilyflower.lotweakr.mixin.client;

import lotr.client.LOTRAttackTiming;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.lotweakr.util.config.CombatFeatureConfig;

@Mixin(LOTRAttackTiming.class)
public abstract class ClientSideAttackTimingsRemoval {
    @Inject(method = "doAttackTiming", at = @At("HEAD"), cancellable = true, remap = false)
    private static void removeTimingUpdates(CallbackInfo ci) {
        if (CombatFeatureConfig.DISABLE_ATTACK_TIMINGS) {
            LOTRAttackTiming.reset();
            ci.cancel();
        }
    }

    @Inject(method = "renderAttackMeter", at = @At("HEAD"), cancellable = true, remap = false)
    private static void ceaseRendering(ScaledResolution resolution, float partialTicks, CallbackInfo ci) {
        if (CombatFeatureConfig.DISABLE_ATTACK_TIMINGS) {
            ci.cancel();
        }
    }
}
