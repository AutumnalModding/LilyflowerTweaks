package xyz.lilyflower.lilytweaks.mixin.vanilla;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksGameConfigurationSystem;

@SuppressWarnings("ALL") // TODO: replace with the correct inspections
@Mixin(EntityLivingBase.class)
public class RemoveImmunityFrames {
    @Inject(method = "attackEntityFrom", at = @At("TAIL"))
    public void removeCaps(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        EntityLivingBase living = (EntityLivingBase) (Object) this;
        if (living.hurtResistantTime >= living.maxHurtResistantTime) {
            if (source.isProjectile() && LilyflowerTweaksGameConfigurationSystem.NO_IFRAME_PROJECTILES) {
                living.hurtResistantTime = 0;
            }

            if (LilyflowerTweaksGameConfigurationSystem.NO_IFRAME_DAMAGETYPES.contains(source.getDamageType())) {
                living.hurtResistantTime = 0;
            }
        }
    }
}
