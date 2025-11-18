package xyz.lilyflower.solaris.mixin.vanilla;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.solaris.configuration.modules.SolarisVanilla;

@Mixin(EntityLivingBase.class)
public class RemoveImmunityFrames {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "attackEntityFrom", at = @At("TAIL"))
    public void removeCaps(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        EntityLivingBase living = (EntityLivingBase) (Object) this;
        if (living.hurtResistantTime >= living.maxHurtResistantTime) {
            if (source.isProjectile() && SolarisVanilla.NO_IFRAME_PROJECTILES) {
                living.hurtResistantTime = 0;
            }

            if (SolarisVanilla.NO_IFRAME_DAMAGETYPES.contains(source.getDamageType())) {
                living.hurtResistantTime = 0;
            }
        }
    }
}
