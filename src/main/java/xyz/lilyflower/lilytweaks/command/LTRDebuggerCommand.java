package xyz.lilyflower.lilytweaks.command;

import java.util.Set;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRPlayerData;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.spawning.LOTRInvasions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.reflections.Reflections;

public class LTRDebuggerCommand extends CommandBase {
    private static final Reflections NPC_SCANNER = new Reflections("lotr.common.entity.npc");

    @Override
    public String getCommandName() {
        return "ltdebug";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /ltdebug <verb> [arguments]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer player) {

            if (args.length >= 1) {
                switch (args[0]) {

                    case "dumpWaypoints":
                        sendWaypointDebugInfo(player);
                        break;

                    case "dumpNpcSubclasses":
                        sendNpcClasses(player);
                        break;

                    case "dumpInvasions":
                        sendChatMessage(player, "=== DUMPING VALID INVASION VALUES ===");
                        for (LOTRInvasions invasion : LOTRInvasions.values()) {
                            sendChatMessage(player, "Found invasion '" + invasion.name() + "' - faction " + invasion.invasionFaction + ".");
                        }
                        sendChatMessage(player, "===== DUMP FINISHED, CHECK LOGS =====");
                        break;

                    case "invasionExists":
                        String invasion = (args.length == 1 || args[1] == null ? "thisInvasionNameDoesNotAndWillNotEverExistLmao" : args[1]);
                        sendChatMessage(player, Boolean.toString(LOTRInvasions.forName(invasion) != null));
                        break;

                    case "lastKnownBiome":
                        LOTRPlayerData data = LOTRLevelData.getData(player);
                        sendChatMessage(player, "Last known LOTR biome: " + data.getLastKnownBiome().getBiomeDisplayName());
                        break;

                    case "help":
                    default:
                        sendCommandHelp(player);
                        break;
                }
            } else {
                sendCommandHelp(player);
            }
        }
    }

    private void sendNpcClasses(EntityPlayer player) {
        sendChatMessage(player, "====== DUMPING VALID NPC NAMES ======");
        Set<Class<? extends LOTREntityNPC>> npcs = NPC_SCANNER.getSubTypesOf(LOTREntityNPC.class);
        npcs.forEach(npc -> sendChatMessage(player, "Found NPC class: " + npc.getCanonicalName().replace("lotr.common.entity.npc.", "")));
        sendChatMessage(player, "===== DUMP FINISHED, CHECK LOGS =====");
    }

    private void sendCommandHelp(EntityPlayer player) {
        sendChatMessage(player, "Available LTDEBUG verbs: ");
        sendChatMessage(player, "    dumpWaypoints");
        sendChatMessage(player, "    dumpNpcSubclasses");
        sendChatMessage(player, "    dumpInvasions");
        sendChatMessage(player, "    invasionExists <INVASION>");
        sendChatMessage(player, "    lastKnownBiome");
    }

    private void sendWaypointDebugInfo(EntityPlayer player) {
        sendChatMessage(player, "====== DUMPING VALID WAYPOINTS ======");
        for (LOTRWaypoint.Region region : LOTRWaypoint.Region.values()) {
            sendChatMessage(player, "Listing region '" + region.name() + "' waypoint coordinates:");

            region.waypoints.forEach(waypoint -> sendChatMessage(player, ("Waypoint '"
                    + waypoint.name()
                    + "' coordinates: "
                    + waypoint.getXCoord()
                    + " "
                    + waypoint.getZCoord())
            ));
        }

        sendChatMessage(player, "===== DUMP FINISHED, CHECK LOGS =====");
    }
    
    private void sendChatMessage(EntityPlayer player, String text) {
        player.addChatMessage(new ChatComponentText(text));
    }
}
