package xyz.lilyflower.lilytweaks.core;

import alfheim.common.core.handler.AlfheimConfigHandler;
import com.emoniph.witchery.util.IHandleDT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import lotr.common.fac.LOTRFaction;
import net.minecraftforge.common.config.Configuration;

public class LTConfigRunners {
    public static final class Vanilla {
        public static final Consumer<Configuration> COMBAT_TWEAKS = configuration -> {
            LTConfig.NO_IFRAME_DAMAGETYPES = new ArrayList<>(Arrays.asList(configuration.getStringList("noImmunityDamageTypes", "damage", new String[]{},
                    "List of damage sources for which iframes aren't applied."
            )));

            LTConfig.NO_IFRAME_PROJECTILES = configuration.getBoolean("noImmunityForProjectiles", "damage", false, "Make projectiles ignore iframes.");
        };

        public static final Consumer<Configuration> BANDAID_FIXES = configuration -> {
            LTConfig.DISABLE_SNOW_UPDATES = configuration.getBoolean("disableSnowUpdates", "bandaid", false, "Disables snow sheet blocks from sending neighbour updates.\nCan stop StackOverflowExceptions in some cases.");
            LTConfig.DISABLE_WORLDGEN_SPAWNING = configuration.getBoolean("disableWorldgenSpawning", "bandaid", false, "Disables animals spawning during worldgen.\nCan fix 'this.entitiesByUuid is null' crashes during world creation.");
        };

        public static void init() {
            LTConfig.add("lilytweaks", COMBAT_TWEAKS);
            LTConfig.add("lilytweaks", BANDAID_FIXES);
        }
    }

    public static final class LOTR {
        public static final Consumer<Configuration> COMBAT_TWEAKS = configuration -> {
            LTConfig.DISABLE_ATTACK_TIMINGS = configuration.getBoolean("removeNewCombat", "lotr.combat", true, "Removes the 1.9-style combat changes.");
            LTConfig.ENABLE_WANDERER_DEATH = configuration.getBoolean("enableWandererDeath", "lotr.combat", false, "Allows the Grey Wanderer to die.");

            LTConfig.SHORTCIRCUIT_WAR_CRIMES = configuration.getBoolean("shortcircuitWarCrimes", "lotr.combat", false, "Makes all LOTR factions permit war crimes.");
            LTConfig.SHORTCIRCUIT_TARGET_SELECTOR = configuration.getBoolean("shortcircuitTargetSelector", "lotr.combat", false, "Makes all LOTR NPCs attack any mob, NPC or non-NPC.");

            LTConfig.ADDITIONAL_COMBAT_ITEMS = configuration.getStringList("additionalCombatItems", "lotr.combat", new String[]{},
                    "List of items to add to the custom LOTR combat system.\n" +
                            "Format: 'modid:item_name@speedMultiplier_reachMultiplier'\n" +
                            "Example: 'minecraft:golden_axe@1.5_1.0"
            );

            if (LTConfig.SHORTCIRCUIT_WAR_CRIMES) {
                for (LOTRFaction faction : LOTRFaction.values()) {
                    faction.approvesWarCrimes = true;
                }
            }
        };

        public static final Consumer<Configuration> GENERIC_TWEAKS = configuration -> {
            LTConfig.UNLOCK_COSMETICS = configuration.getBoolean("unlockCosmetics", "lotr.misc", false, "Unlocks all player-specific LOTR cosmetics.");
            LTConfig.FIX_ORE_DICTIONARY = configuration.getBoolean("fixOreDictionary", "lotr.misc", true, "Registers LOTR ores to the Forge ore dictionary.");
            LTConfig.TIME_MULTIPLIER = configuration.getFloat("timeMultiplier", "lotr.misc", 1.0F, Float.MIN_VALUE, Float.MAX_VALUE, "Day length multiplier for LOTR days.");
            LTConfig.ALLOW_SCRAP_TRADER_SCREENSHOTS = configuration.getBoolean("allowOddmentScreenshots", "lotr.misc", false, "Allow taking screenshots of the Oddment Collector.");
            LTConfig.TIME_BASE = configuration.getInt("timeBase", "lotr.misc", 48000, 1, Integer.MAX_VALUE, "LOTR day length (base)");
        };

        public static final Consumer<Configuration> CROSS_MOD_INTEGRATION = configuration -> {
            LTConfig.THAUMCRAFT_ENABLED = configuration.getBoolean("enableThaumIntegration", "lotr.integration", false, "Enables Thaumcraft integration (unfinished!).");
            LTConfig.FIX_RITUAL = configuration.getBoolean("fixVampireRitual", "lotr.integration", true, "Allow Witchery's vampire ritual in the LOTR dimension.");
            LTConfig.SAFE_BIOMES = Arrays.asList(configuration.getStringList("safeBiomes", "lotr.integration", new String[]{}, "Safe LOTR biomes for vampires. Use display names, like 'Mordor' or 'Gorgoroth'."));
        };

        public static final Consumer<Configuration> FAST_TRAVEL_TWEAKS = configuration -> {
            LTConfig.DISABLED_WAYPOINTS = Arrays.asList(configuration.getStringList("disabledWaypoints", "lotr.travel", new String[]{},
                    "List of waypoints to disable.\n" +
                            "Format: Internal waypoint name - run `/ltdebug dumpWaypoints` for a list.\n" +
                            "Example: 'MORANNON' would disable the Black Gate waypoint (display names and internal names often do not match!)"
            ));

            LTConfig.UNLOCK_WAYPOINTS = configuration.getBoolean("unlockAllWaypoints", "lotr.travel", false, "Unlocks all fast travel waypoints.");
            LTConfig.NO_WAYPOINT_LOCKING = configuration.getBoolean("disableWaypointLocking", "lotr.travel", false, "Disables alignment-based waypoint locking.");
        };

        public static void init() {
            LTConfig.add("lotr", COMBAT_TWEAKS);
            LTConfig.add("lotr", GENERIC_TWEAKS);
            LTConfig.add("lotr", CROSS_MOD_INTEGRATION);
            LTConfig.add("lotr", FAST_TRAVEL_TWEAKS);
        }
    }

    public static final class Witchery {
        public static final Consumer<Configuration> COMBAT_TWEAKS = configuration -> {
            String[] caps = configuration.getStringList("witcheryDamageCaps", "damage", new String[]{},
                    "Class-cap mapping for Witchery bosses. Format: <EntityName>:<DamageCap>\n" +
                            "Valid entity names: see https://i.imgur.com/CbQZ1ko.png (exclude semicolons!)"
            );
            for (String cap : caps) {
                String[] split = cap.split(":");
                String classname = "com.emoniph.witchery.entity.Entity " + split[0];
                float value = Float.parseFloat(split[1]);

                try {
                    // all witchery bosses implement this
                    LTConfig.WITCHERY_DAMAGE_CAPS.put((Class<? extends IHandleDT>) Class.forName(classname), value);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        public static void init() {
            LTConfig.add("witchery", COMBAT_TWEAKS);
        }
    }

    public static final class Alfheim {
        public static final Consumer<Configuration> GENERIC_TWEAKS = configuration -> {
            LTConfig.ESM_TELEPORT_DIMENSION = configuration.getInt("esmTeleportDimension", "alfheim", AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim(), Integer.MIN_VALUE, Integer.MAX_VALUE, "ESM dimension ID for teleportation.");
            LTConfig.DISABLE_TPDIM = configuration.getBoolean("disableTPDim", "alfheim", false, "Disable /tpdim command.");
            LTConfig.DISABLE_ESM_RACES = configuration.getBoolean("esmRacesToggle", "alfheim", false, "Disable ESM races. Useful if you want to have MMO without ESM.");
        };

        public static void init() {
            LTConfig.add("alfheim", GENERIC_TWEAKS);
        }
    }
}
