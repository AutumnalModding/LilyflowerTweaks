package xyz.lilyflower.solaris.mixin.lotr.entity;

import lotr.common.item.LOTRWeaponStats;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.solaris.configuration.modules.SolarisLOTR;

@Mixin(LOTRWeaponStats.class)
public class ServerSideAttackTimingsRemoval {
    @Inject(method = "getAttackTimePlayer", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fixCombatPlayer(ItemStack itemstack, CallbackInfoReturnable<Integer> cir) {
        if (SolarisLOTR.DISABLE_ATTACK_TIMINGS) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "getAttackTimeMob", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fixCombatMobs(ItemStack itemstack, CallbackInfoReturnable<Integer> cir) {
        if (SolarisLOTR.DISABLE_ATTACK_TIMINGS) {
            cir.setReturnValue(0);
        }
    }
}