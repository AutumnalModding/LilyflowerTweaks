package xyz.lilyflower.solaris.mixin.alfheim;

import alfheim.common.core.handler.ESMHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.solaris.core.settings.modules.AlfheimTransformerSettings;

@Mixin(ESMHandler.class)
public class ESMHandlerMixin {
    @Inject(method = "isAbilityDisabled", at = @At("HEAD"), cancellable = true, remap = false)
    public void disable(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!AlfheimTransformerSettings.ENABLE_ESM_FLIGHT);
    }
}
