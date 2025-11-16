package xyz.lilyflower.lilytweaks.mixin.lotr.bug;

import lotr.common.LOTRReflection;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.core.settings.modules.LOTRTransformerSettings;

@Mixin(LOTRReflection.class)
public class LOTRReflectionCompatibilityPatches {
    @Inject(method = "canPistonPushBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cancel(Block block, World world, int i, int j, int k, boolean flag, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(LOTRTransformerSettings.CAN_PISTONS_PUSH);
    }
}
