package xyz.lilyflower.lilytweaks.mixin.alfheim;

import alfheim.common.block.tile.TileRaceSelector;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.lilyflower.lilytweaks.util.config.alfheim.AlfheimMiscConfig;

@Mixin(value = TileRaceSelector.class, remap = false)
public class ESMTeleportRewire {
    @ModifyArg(method = "teleport", at = @At(value = "INVOKE", target = "Lalexsocol/asjlib/ASJUtilities;sendToDimensionWithoutPortal(Lnet/minecraft/entity/Entity;IDDD)V"))
    public int sendToSpecificDimension(Entity $i$a$_also_ASJUtilities$sendToDimensionWithoutPortal$1, int i, double server, double worldTo, double dimFrom) {
        return AlfheimMiscConfig.getETD();
    }
}
