package xyz.lilyflower.lilytweaks.mixin.lotr.entity;

import lotr.common.entity.npc.LOTREntityGandalf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.util.config.combat.LOTRCombatFeatureConfig;

@Mixin(LOTREntityGandalf.class)
public abstract class EnableGreyWandererDeath extends EntityLivingBase {
    public EnableGreyWandererDeath(World world) {
        super(world);
    }

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true, remap = false)
    public void allowKillingGandalf(DamageSource damagesource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (LOTRCombatFeatureConfig.ENABLE_GW_DEATH) {
            System.out.println("damaging GW");
            cir.setReturnValue(super.attackEntityFrom(damagesource, f));
        }
    }
}
