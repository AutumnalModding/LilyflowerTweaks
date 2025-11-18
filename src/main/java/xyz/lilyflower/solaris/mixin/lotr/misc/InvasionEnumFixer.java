package xyz.lilyflower.solaris.mixin.lotr.misc;

import lotr.common.world.spawning.LOTRInvasions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LOTRInvasions.class)
public class InvasionEnumFixer {
    @Inject(method = "forName", at = @At("HEAD"), cancellable = true, remap = false)
    private static void quickSearch(String name, CallbackInfoReturnable<LOTRInvasions> cir) {
        try {
            cir.setReturnValue(LOTRInvasions.valueOf(name));
        } catch (IllegalArgumentException ignored) {}
    }
}
