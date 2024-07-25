package xyz.lilyflower.lilytweaks.mixin.opencomputers;

import li.cil.oc.util.InventoryUtils$;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.lilyflower.lilytweaks.util.config.opencomputers.OpencomputersMiscFeatureConfig;

@Mixin(InventoryUtils$.class)
public class TransposerIgnoreSidedness {
    @Redirect(method = "insertIntoInventorySlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/IInventory;isItemValidForSlot(ILnet/minecraft/item/ItemStack;)Z"))
    public boolean forceAllow(IInventory instance, int i, ItemStack stack) {
        return OpencomputersMiscFeatureConfig.TRANSPOSER_IGNORE_SIDEDNESS;
    }
}
