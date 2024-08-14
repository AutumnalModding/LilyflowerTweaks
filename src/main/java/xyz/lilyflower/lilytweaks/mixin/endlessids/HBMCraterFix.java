package xyz.lilyflower.lilytweaks.mixin.endlessids;

import com.falsepattern.endlessids.mixin.helpers.ChunkBiomeHook;
import com.hbm.packet.PacketDispatcher;
import com.hbm.world.WorldUtil;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.lilytweaks.util.fixes.BiomeSyncPacketButNotShit;

@Mixin(value = WorldUtil.class, remap = false)
public class HBMCraterFix {
    @Inject(method = "setBiome", at = @At("HEAD"), cancellable = true)
    private static void fix(World world, int x, int z, BiomeGenBase biome, CallbackInfo ci) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        short[] array = ((ChunkBiomeHook) chunk).getBiomeShortArray();
        array[(z & 15) << 4 | x & 15] = (short) biome.biomeID;
        chunk.isModified = true;
        ci.cancel();
    }

    @Inject(method = "syncBiomeChange(Lnet/minecraft/world/World;II)V", at = @At("HEAD"), cancellable = true)
    private static void fix(World world, int x, int z, CallbackInfo ci) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        short[] array = ((ChunkBiomeHook) chunk).getBiomeShortArray();
        PacketDispatcher.wrapper.sendToAllAround(new BiomeSyncPacketButNotShit(x >> 4, z >> 4, array), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, 128.0, z, 1024.0));
        ci.cancel();
    }
}