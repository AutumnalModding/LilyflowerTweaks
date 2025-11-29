package xyz.lilyflower.solaris.mixin.lotr.misc;

import lotr.common.world.biome.variant.LOTRBiomeVariantStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.lilyflower.solaris.util.TransformerMacros;

@Mixin(LOTRBiomeVariantStorage.class)
public class LOTRBiomeVariantStorageMixin {
    @Redirect(method = "sendChunkVariantsToPlayer", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/FMLLog;severe(Ljava/lang/String;[Ljava/lang/Object;)V", remap = false), remap = false)
    private static void ohMyGodShutTheFuckUp(String message, Object[] data) {
        TransformerMacros.__INTERNAL_NOOP();
    }
}
