package xyz.lilyflower.lilytweaks.mixin.alfheim;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.entity.EnumRace;
import alfheim.common.block.tile.TileRaceSelector;
import alfheim.common.core.handler.AlfheimConfigHandler;
import cpw.mods.fml.common.Loader;
import lotr.common.LOTRDimension;
import lotr.common.LOTRLevelData;
import lotr.common.world.LOTRTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksGameConfigurationSystem;
import xyz.lilyflower.lilytweaks.settings.LilyflowerTweaksTransformerSettingsSystem;

@Mixin(value = TileRaceSelector.class, remap = false)
public class ESMTeleportRewire {
    @Inject(method = "teleport", at = @At("HEAD"), cancellable = true)
    public void teleport(EntityPlayer player, CallbackInfo ci) {
        if (!LilyflowerTweaksTransformerSettingsSystem.Alfheim.ENABLE_ESM_RACES && player instanceof EntityPlayer) {
            ((TileRaceSelector) (Object) this).selectRace(player, EnumRace.HUMAN);
        }

        int dest = LilyflowerTweaksGameConfigurationSystem.getETD();
        if (dest != AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim()) {
            MinecraftServer server = MinecraftServer.getServer();
            WorldServer target = server.worldServerForDimension(dest);
            WorldServer source = server.worldServerForDimension(player.dimension);

            if (Loader.isModLoaded("lotr") && dest == LOTRDimension.MIDDLE_EARTH.dimensionID) {
                WorldServer overworld = server.worldServerForDimension(0);
                ChunkCoordinates location = overworld.getSpawnPoint();
                ASJUtilities.sendToDimensionWithoutPortal(player, 0, location.posX, location.posY, location.posZ);
                lilyflowerTweaks$teleportRaw(player, dest, source, target, new LOTRTeleporter(DimensionManager.getWorld(dest), false));
                LOTRLevelData.setMadeMiddleEarthPortal(0);
                try {
                    if (player instanceof EntityPlayerMP remote) {
                        remote.playerNetServerHandler.kickPlayerFromServer("Please reconnect/reload your world!");
                    }
                } catch (NoClassDefFoundError ignored) {}
            } else { // TODO: more branches for more mods
                ChunkCoordinates location = target.getSpawnPoint();
                ASJUtilities.sendToDimensionWithoutPortal(player, dest, location.posX, location.posY, location.posZ);
            }

            ci.cancel();
        }
    }

    @Unique
    private void lilyflowerTweaks$teleportRaw(EntityPlayer player, int dest, WorldServer source, WorldServer target, Teleporter teleporter) {
        int from = player.dimension;
        player.dimension = dest;
        player.worldObj.removeEntity(player);
        player.isDead = false;
        MinecraftServer.getServer().getConfigurationManager().transferEntityToWorld(player, from, source, target, teleporter);
        Entity there = EntityList.createEntityByName(EntityList.getEntityString(player), target);
        if (there != null) {
            there.copyDataFrom(player, true);
            target.spawnEntityInWorld(there);
        }
        player.isDead = true;
        source.resetUpdateEntityTick();
        target.resetUpdateEntityTick();
    }
}
