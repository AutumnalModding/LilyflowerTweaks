package xyz.lilyflower.lilytweaks.mixin.endlessids.hbm;

import com.falsepattern.endlessids.mixin.helpers.ChunkBiomeHook;
import com.hbm.packet.PacketDispatcher;
import com.hbm.world.WorldUtil;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xyz.lilyflower.lilytweaks.util.fixes.ExtendedBiomeSyncPacket;

@Mixin(value = WorldUtil.class, remap = false)
public class HBMCraterFix {
    /**
     * @author Lilyflower
     * @reason EndlessIDs compat
     */
    @Overwrite
    public static void setBiome(World world, int x, int z, BiomeGenBase biome) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        short[] array = ((ChunkBiomeHook) chunk).getBiomeShortArray();
        array[(z & 15) << 4 | x & 15] = (short) biome.biomeID;
        chunk.isModified = true;
    }

    /**
     * @author Lilyflower
     * @reason EndlessIDs compat
     */
    @Overwrite
    public static void syncBiomeChange(World world, int x, int z) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        short[] array = ((ChunkBiomeHook) chunk).getBiomeShortArray();
        PacketDispatcher.wrapper.sendToAllAround(new ExtendedBiomeSyncPacket(x >> 4, z >> 4, array), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, 128.0, z, 1024.0));
    }
}