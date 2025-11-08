package xyz.lilyflower.lilytweaks.mixin.bandaid;

import java.util.Random;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;
import xyz.lilyflower.lilytweaks.init.LilyflowerTweaksModLoader;

@Mixin(SpawnerAnimals.class)
public class DisableWorldgenSpawning {
    @SuppressWarnings("UnresolvedMixinReference") // it is literally right there
    @Inject(method = "performWorldGenSpawning", at = @At("HEAD"), cancellable = true)
    private static void disableSpawning(World world, BiomeGenBase biome, int what, int even, int are, int these, Random random, CallbackInfo info) {
        if (LilyflowerTweaksConfigSystem.DISABLE_WORLDGEN_SPAWNING) {
            LilyflowerTweaksModLoader.LOGGER.debug("Stopping chunkgen-time animal spawn");
            info.cancel();
        }
    }
}
