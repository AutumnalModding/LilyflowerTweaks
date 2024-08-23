package xyz.lilyflower.lilytweaks.mixin.alfheim;

import alexsocol.asjlib.command.CommandDimTP;
import net.minecraft.command.ICommandSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.core.LTConfig;

@Mixin(CommandDimTP.class)
public class DisableTPDIM {
    @Inject(method = "canCommandSenderUseCommand", at = @At("HEAD"), cancellable = true)
    public void disable(ICommandSender sender, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!LTConfig.DISABLE_TPDIM);
    }
}
