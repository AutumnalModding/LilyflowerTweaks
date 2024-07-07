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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static xyz.lilyflower.lilytweaks.util.config.combat.GenericCombatFeatureConfig.WITCHERY_DAMAGE_CAPS;


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
    }

    @Mixin({EntityGoblinMog.class, EntityGoblinGulg.class})
    public static class WhyAreYouTwoSpecialDamnit {
        @ModifyArg(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(DD)D"), index = 1)
        public double removeCaps(double cap) {
            return WITCHERY_DAMAGE_CAPS.getOrDefault(getClass(), (float) cap).doubleValue();
        }
    }
}