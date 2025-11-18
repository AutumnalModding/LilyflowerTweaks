package xyz.lilyflower.solaris.integration.galacticraft;

import java.util.Random;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TeleportTypeLander implements ITeleportType, IExitHeight {
    @Override public boolean useParachute() { return false; }
    @Override public Vector3 getParaChestSpawnLocation(WorldServer server, EntityPlayerMP player, Random random) { return null; }

    public EntityLanderBase lander(EntityPlayerMP occupant) {
        return new EntityLander(occupant);
    }

    @Override
    public double getYCoordinateToTeleport() {
        return 900.0D;
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer server, EntityPlayerMP player) {
        if (player == null) return null;
        GCPlayerStats stats = GCPlayerStats.get(player);
        return new Vector3(stats.coordsTeleportedFromX, this.getYCoordinateToTeleport(), stats.coordsTeleportedFromZ);
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer server, Entity entity) {
        return entity == null ? null : new Vector3(entity.posX, this.getYCoordinateToTeleport(), entity.posZ);
    }

    @Override
    public void onSpaceDimensionChanged(World world, EntityPlayerMP player, boolean auto) {
        if (auto || player == null) return;
        GCPlayerStats stats = GCPlayerStats.get(player);
        if (stats.teleportCooldown > 0) return;
        player.capabilities.isFlying = false;
        stats.teleportCooldown = 10;
        if (world.isRemote) return;
        EntityLanderBase lander = lander(player);
        world.spawnEntityInWorld(lander);
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player) {}
}
