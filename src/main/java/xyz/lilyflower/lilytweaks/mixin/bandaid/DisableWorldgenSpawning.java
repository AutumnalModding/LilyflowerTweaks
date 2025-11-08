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

@Mixin(SpawnerAnimals.class)
public class DisableWorldgenSpawning {
    @Inject(method = "performWorldGenSpawning", at = @At("HEAD"), cancellable = true)
    private static void disableSpawning(World world, BiomeGenBase biome, int idk, int what, int these, int are, Random random, CallbackInfo info) {
        if (LilyflowerTweaksConfigSystem.DISABLE_WORLDGEN_SPAWNING) {
            //LilyflowerTweaks.LOGGER.info("Stopping chunkgen-time animal spawn");
            info.cancel();
        }
    }
}
