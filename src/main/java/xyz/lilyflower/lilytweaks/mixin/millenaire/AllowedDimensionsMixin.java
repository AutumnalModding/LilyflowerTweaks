package xyz.lilyflower.lilytweaks.mixin.millenaire;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.millenaire.client.MillClientUtilities;
import org.millenaire.client.forge.ClientTickHandler;
import org.millenaire.common.UserProfile;
import org.millenaire.common.WorldGenVillage;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.lilyflower.lilytweaks.core.LTConfig;

public class AllowedDimensionsMixin {
    @Debug(export = true)
    @Mixin(MillClientUtilities.class)
    public static class Keybinds {
        @ModifyExpressionValue(method = "handleKeyPress", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;dimension:I"))
        private static int allow(int original) {
            System.out.println("a");
            return 0;
        }
    }

    @Mixin(WorldGenVillage.class)
    public static class Generation {
        @ModifyExpressionValue(method = "generate", at = @At(value = "FIELD", target = "Lnet/minecraft/world/WorldProvider;dimensionId:I"))
        public int allow(int original) {
            System.out.println("b");
            return 0;
        }
    }

    @Mixin(ClientTickHandler.class)
    public static class Ticker {
        @ModifyExpressionValue(method = "tickStart", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;dimension:I"))
        public int allow(int original) {
            System.out.println("c");
            return 0;
        }
    }

    @Mixin(UserProfile.class)
    public static class Profile {
        @ModifyExpressionValue(method = "updateProfile", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;dimension:I"))
        public int allow(int original) {
            System.out.println("d");
            return 0;
        }
    }
}
