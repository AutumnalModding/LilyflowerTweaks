package xyz.lilyflower.lilytweaks.mixin.alfheim;

import alfheim.api.entity.EnumRace;
import alfheim.common.block.tile.TileRaceSelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

@Mixin(value = TileRaceSelector.class, remap = false)
public class ESMTeleportRewire {
    @ModifyArg(method = "teleport", at = @At(value = "INVOKE", target = "Lalexsocol/asjlib/ASJUtilities;sendToDimensionWithoutPortal(Lnet/minecraft/entity/Entity;IDDD)V"))
    public int sendToSpecificDimension(Entity target, int i, double server, double worldTo, double dimFrom) {
        if (LilyflowerTweaksConfigSystem.DISABLE_ESM_RACES && target instanceof EntityPlayer) {
            ((TileRaceSelector) (Object) this).selectRace((EntityPlayer) target, EnumRace.HUMAN);
        }

        return LilyflowerTweaksConfigSystem.getETD();
    }
}
