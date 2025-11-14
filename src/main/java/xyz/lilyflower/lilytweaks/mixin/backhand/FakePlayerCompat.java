package xyz.lilyflower.lilytweaks.mixin.backhand;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xonin.backhand.api.core.BackhandUtils;

@Mixin(BackhandUtils.class)
public class FakePlayerCompat {
    @Inject(method = "getOffhandItem", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fakePlayerCompat(EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        if (player instanceof FakePlayer) {
            cir.setReturnValue(new ItemStack(new ItemBlock(Blocks.air)));
        }
    }
}
