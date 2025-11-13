package xyz.lilyflower.lilytweaks.mixin.witchery.entity;

import com.emoniph.witchery.entity.EntityBabaYaga;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityGoblinGulg;
import com.emoniph.witchery.entity.EntityGoblinMog;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.EntityLeonard;
import com.emoniph.witchery.entity.EntityLilith;
import com.emoniph.witchery.entity.EntityLordOfTorment;
import com.emoniph.witchery.entity.EntityReflection;
import com.emoniph.witchery.entity.EntityVampire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static xyz.lilyflower.lilytweaks.configuration.modules.WitcheryIntegrationConfiguration.WITCHERY_DAMAGE_CAPS;

public class DamageCapRemover {
    @Mixin({
            EntityBabaYaga.class,
            EntityDeath.class,
            EntityHornedHuntsman.class,
            EntityLeonard.class,
            EntityLilith.class,
            EntityLordOfTorment.class,
            EntityReflection.class,
    })
    public static class RegularCapRemover {
        @ModifyArg(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"), index = 1)
        public float removeCaps(float cap) {
            return WITCHERY_DAMAGE_CAPS.getOrDefault(getClass(), cap);
        }

        @Inject(method = "getCapDT", at = @At("HEAD"), cancellable = true, remap = false)
        public void removeDTCap(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
            cir.setReturnValue(WITCHERY_DAMAGE_CAPS.getOrDefault(getClass(), damage));
        }
    }

    /**
     * For some Velzie-forsaken reason, these two call {@code Math;min(DD)D} instead of {@code Math;min(FF)F}. <br/>
     * Whatever the FUCK Eminoph was on when writing this, I want it.
     */
    @Mixin({EntityGoblinMog.class, EntityGoblinGulg.class})
    public static class WhyAreYouTwoSpecialDamnit {
        @ModifyArg(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(DD)D"), index = 1)
        public double removeCaps(double cap) {
            return WITCHERY_DAMAGE_CAPS.getOrDefault(getClass(), (float) cap).doubleValue();
        }

        @Inject(method = "getCapDT", at = @At("HEAD"), cancellable = true, remap = false)
        public void removeDTCap(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
            cir.setReturnValue(WITCHERY_DAMAGE_CAPS.getOrDefault(getClass(), damage));
        }
    }

    /**
     * So it turns out DT is "Death's Touch", <br/>
     * In almost all instances, though, it's the same as the real damage cap - except for two: <br/>
     * Reflections have a cap of 2.0, but the real cap is 6.0; <br>
     * and Leonard has the same. Unless the damage source isn't from a player. Then it's zero.
     *
     * @see com.emoniph.witchery.util.IHandleDT#getCapDT(DamageSource, float)
     * @see com.emoniph.witchery.util.EntityUtil#touchOfDeath(Entity, EntityLivingBase, float) 
     */
    @Mixin(EntityVampire.class)
    public static class VampireDTCapRemover {
        @Inject(method = "getCapDT", at = @At("HEAD"), cancellable = true, remap = false)
        public void removeDTCap(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
            cir.setReturnValue(WITCHERY_DAMAGE_CAPS.getOrDefault(getClass(), damage));
        }
    }
}