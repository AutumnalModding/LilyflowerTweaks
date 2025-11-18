package xyz.lilyflower.solaris.mixin.alfheim;

import alfheim.common.core.handler.AlfheimConfigHandler;
import alfheim.common.core.handler.CardinalSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CardinalSystem.TimeStopSystem.class)
public class EnableBossTimestop {
    @Redirect(method = "affected", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/IBossDisplayData"), remap = false)
    public boolean enable(Object instance, Class<?> type) {
        return !AlfheimConfigHandler.INSTANCE.getSuperSpellBosses();
    }
}
