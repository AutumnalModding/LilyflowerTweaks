package xyz.lilyflower.lotweakr.mixin.entity;

import lotr.common.entity.npc.LOTREntityGandalf;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lotweakr.util.config.CombatFeatureConfig;

@Mixin(LOTREntityGandalf.class)
public abstract class EnableGreyWandererDeath extends EntityLivingBase {
    public EnableGreyWandererDeath(World world) {
        super(world);
    }

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true, remap = false)
    public void allowKillingGandalf(DamageSource damagesource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (CombatFeatureConfig.ENABLE_GW_DEATH) {
            System.out.println("damaging GW");
            cir.setReturnValue(super.attackEntityFrom(damagesource, f));
        }
    }
}
