package xyz.lilyflower.lilytweaks.mixin.lotr.entity;

import lotr.common.entity.ai.LOTRNPCTargetSelector;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

@Mixin(LOTRNPCTargetSelector.class)
public class OmnitargetHelper {
    @Inject(method = "isEntityApplicable", at = @At("HEAD"), remap = false, cancellable = true)
    public void enableBloodbathMode(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (LilyflowerTweaksConfigSystem.LOTR.SHORTCIRCUIT_TARGET_SELECTOR) {
            cir.setReturnValue(true);
        }
    }
}
