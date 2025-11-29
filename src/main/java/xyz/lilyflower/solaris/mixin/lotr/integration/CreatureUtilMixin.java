package xyz.lilyflower.solaris.mixin.lotr.integration;

import com.emoniph.witchery.util.CreatureUtil;
import lotr.common.LOTRDimension;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRPlayerData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.solaris.configuration.modules.SolarisWitchery;

@Mixin(CreatureUtil.class)
public class CreatureUtilMixin {
    @Inject(method = "isInSunlight", at = @At("HEAD"), cancellable = true, remap = false)
    private static void isBiomeSafe(EntityLivingBase entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof EntityPlayer && entity.worldObj.provider.dimensionId == LOTRDimension.MIDDLE_EARTH.dimensionID) {
            LOTRPlayerData data = LOTRLevelData.getData((EntityPlayer) entity);
            if (SolarisWitchery.isBiomeSafe(data.getLastKnownBiome().getBiomeDisplayName())) {
                cir.setReturnValue(false);
            }
        }
    }
}
