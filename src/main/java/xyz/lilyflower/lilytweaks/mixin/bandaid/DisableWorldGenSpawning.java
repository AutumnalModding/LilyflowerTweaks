package xyz.lilyflower.lilytweaks.mixin.bandaid;

import java.util.Random;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.lilytweaks.util.config.generic.BandaidFeatureConfig;

@Mixin(SpawnerAnimals.class)
public class DisableWorldGenSpawning {
    @Inject(method = "performWorldGenSpawning", at = @At("HEAD"), cancellable = true)
    private static void disableSpawning(World world, BiomeGenBase biome, int idont, int knowwhat, int theseare, Random random, CallbackInfo info) {
        if (BandaidFeatureConfig.DISABLE_WORLDGEN_SPAWNING) {
            info.cancel();
        }
    }
}
