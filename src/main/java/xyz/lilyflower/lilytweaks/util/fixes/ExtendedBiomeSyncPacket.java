package xyz.lilyflower.lilytweaks.util.fixes;

import com.falsepattern.endlessids.mixin.helpers.ChunkBiomeHook;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ExtendedBiomeSyncPacket implements IMessage {
    int chunkX;
    int chunkZ;
    byte blockX;
    byte blockZ;
    short biome;
    short[] biomeArray;

    @SuppressWarnings("unused")
    public ExtendedBiomeSyncPacket() {}

    public ExtendedBiomeSyncPacket(int chunkX, int chunkZ, short[] biomeArray) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.biomeArray = biomeArray;
    }

    public ExtendedBiomeSyncPacket(int blockX, int blockZ, short biome) {
        this.chunkX = blockX >> 4;
        this.chunkZ = blockZ >> 4;
        this.blockX = (byte)(blockX & 15);
        this.blockZ = (byte)(blockZ & 15);
        this.biome = biome;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        if (this.biomeArray == null) {
            buf.writeBoolean(false);
            buf.writeShort(this.biome);
            buf.writeByte(this.blockX);
            buf.writeByte(this.blockZ);
        } else {
            buf.writeBoolean(true);

            for(int i = 0; i < 255; ++i) {
                buf.writeShort(this.biomeArray[i]);
            }
        }

    }

    public void fromBytes(ByteBuf buf) {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        if (!buf.readBoolean()) {
            this.biome = buf.readShort();
            this.blockX = buf.readByte();
            this.blockZ = buf.readByte();
        } else {
            this.biomeArray = new short[255];

            for(int i = 0; i < 255; ++i) {
                this.biomeArray[i] = buf.readShort();
            }
        }

    }

    public static class Handler implements IMessageHandler<ExtendedBiomeSyncPacket, IMessage> {
        public Handler() {
        }

        @SideOnly(Side.CLIENT)
        public IMessage onMessage(ExtendedBiomeSyncPacket m, MessageContext ctx) {
            World world = Minecraft.getMinecraft().theWorld;
            if (world.getChunkProvider().chunkExists(m.chunkX, m.chunkZ)) {
                Chunk chunk = world.getChunkFromChunkCoords(m.chunkX, m.chunkZ);
                chunk.isModified = true;
                if (m.biomeArray == null) {
                    ChunkBiomeHook hook = (ChunkBiomeHook) chunk;
                    hook.getBiomeShortArray()[(m.blockZ & 15) << 4 | m.blockX & 15] = m.biome;
                    world.markBlockRangeForRenderUpdate(m.chunkX << 4, 0, m.chunkZ << 4, m.chunkX << 4, 255, m.chunkZ << 4);
                } else {
                    for (int i = 0; i < 255; ++i) {
                        ChunkBiomeHook hook = (ChunkBiomeHook) chunk;
                        hook.getBiomeShortArray()[i] = m.biomeArray[i];
                        world.markBlockRangeForRenderUpdate(m.chunkX << 4, 0, m.chunkZ << 4, (m.chunkX << 4) + 15, 255, (m.chunkZ << 4) + 15);
                    }
                }

            }
            return null;
        }
    }
}
