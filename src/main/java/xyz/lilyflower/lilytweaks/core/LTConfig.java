package xyz.lilyflower.lilytweaks.core;

import alfheim.common.core.handler.AlfheimConfigHandler;
import com.emoniph.witchery.util.IHandleDT;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lotr.common.fac.LOTRFaction;
import lotr.common.item.LOTRWeaponStats;
import lotr.common.world.map.LOTRWaypoint;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class LTConfig {
    public static boolean UNLOCK_WAYPOINTS = false;
    public static boolean NO_WAYPOINT_LOCKING = false;
    public static HashMap<Class<? extends IHandleDT>, Float> WITCHERY_DAMAGE_CAPS = new HashMap<>();
    public static ArrayList<String> NO_IFRAME_DAMAGETYPES;
    public static boolean NO_IFRAME_PROJECTILES = false;
    public static boolean DISABLE_ATTACK_TIMINGS = true;
    public static boolean ENABLE_GW_DEATH = false;
    public static boolean ENABLE_WAR_CRIMES = false;
    public static boolean ENABLE_OMNITARGET = false;
    public static boolean UNLOCK_COSMETICS = false;
    public static boolean FIX_ORE_DICTIONARY = Loader.isModLoaded("lotr");
    public static float TIME_MULTIPLIER = 1;
    public static boolean ALLOW_SCRAP_TRADER_SCREENSHOTS = false;
    public static int TIME_BASE = 48000;
    public static boolean THAUMCRAFT_ENABLED = false;
    public static boolean FIX_RITUAL = true;
    public static boolean DISABLE_SNOW_UPDATES = false;
    public static boolean DISABLE_WORLDGEN_SPAWNING = false;
    public static int ESM_TELEPORT_DIMENSION = Loader.isModLoaded("alfheim") ? AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim() : 0;
    private static List<String> DISABLED_WAYPOINTS;

    private static Configuration configuration;
    public static String[] ADDITIONAL_COMBAT_ITEMS;
    public static List<String> SAFE_BIOMES;

    private static void config_combat() {
        LTConfig.NO_IFRAME_DAMAGETYPES = new ArrayList<>(Arrays.asList(configuration.getStringList("noImmunityDamageTypes", "damage", new String[]{},
                "List of damage sources for which iframes aren't applied."
        )));

        LTConfig.NO_IFRAME_PROJECTILES = configuration.getBoolean("noImmunityForProjectiles", "damage", false, "Make projectiles ignore iframes.");

        if (Loader.isModLoaded("witchery")) {
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
        }
    }
    private static void config_bandaid() {
        LTConfig.DISABLE_SNOW_UPDATES = configuration.getBoolean("disableSnowUpdates", "bandaid", false, "Disables snow sheet blocks from sending neighbour updates.\nCan stop StackOverflowExceptions in some cases.");
        LTConfig.DISABLE_WORLDGEN_SPAWNING = configuration.getBoolean("disableWorldgenSpawning", "bandaid", false, "Disables animals spawning during worldgen.\nCan fix 'this.entitiesByUuid is null' crashes during world creation.");
    }
    private static void config_alfheim() {
        LTConfig.ESM_TELEPORT_DIMENSION = configuration.getInt("esmTeleportDimension", "alfheim", AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim(), 0, Integer.MAX_VALUE, "ESM dimension ID for teleportation.");
    }
    private static void config_lotr_combat() {
        LTConfig.DISABLE_ATTACK_TIMINGS = configuration.getBoolean("removeNewCombat", "lotr.combat", true, "Removes the 1.9-style combat changes.");
        LTConfig.ENABLE_GW_DEATH = configuration.getBoolean("enableGWDeath", "lotr.combat", false, "Allows the Grey Wanderer to die.");

        LTConfig.ENABLE_WAR_CRIMES = configuration.getBoolean("enableWarCrimes", "lotr.combat", false, "Makes all LOTR factions permit war crimes.");
        LTConfig.ENABLE_OMNITARGET = configuration.getBoolean("enableOmnitarget", "lotr.combat", false, "Makes all LOTR NPCs attack any mob, NPC or non-NPC.");

        LTConfig.ADDITIONAL_COMBAT_ITEMS = configuration.getStringList("additionalCombatItems", "lotr.combat", new String[]{},
                "List of items to add to the custom LOTR combat system.\n" +
                        "Format: 'modid:item_name@speedMultiplier_reachMultiplier'\n" +
                        "Example: 'minecraft:golden_axe@1.5_1.0"
        );

        if (LTConfig.ENABLE_WAR_CRIMES) {
            for (LOTRFaction faction : LOTRFaction.values()) {
                faction.approvesWarCrimes = true;
            }
        }
    }
    private static void config_lotr_generic() {
        LTConfig.UNLOCK_COSMETICS = configuration.getBoolean("unlockCosmetics", "lotr.misc", false, "Unlocks all player-specific LOTR cosmetics.");
        LTConfig.FIX_ORE_DICTIONARY = configuration.getBoolean("fixOreDictionary", "lotr.misc", true, "Registers LOTR ores to the Forge ore dictionary.");
        LTConfig.TIME_MULTIPLIER = configuration.getFloat("timeMultiplier", "lotr.misc", 1.0F, Float.MIN_VALUE, Float.MAX_VALUE, "Day length multiplier for LOTR days.");
        LTConfig.ALLOW_SCRAP_TRADER_SCREENSHOTS = configuration.getBoolean("allowOddmentScreenshots", "lotr.misc", false, "Allow taking screenshots of the Oddment Collector.");
        LTConfig.TIME_BASE = configuration.getInt("timeBase", "lotr.misc", 48000, 1, Integer.MAX_VALUE, "LOTR day length (base)");
    }
    private static void config_lotr_integration() {
        LTConfig.THAUMCRAFT_ENABLED = configuration.getBoolean("enableThaumIntegration", "lotr.integration", false, "Enables Thaumcraft integration (unfinished!).");

        LTConfig.FIX_RITUAL = configuration.getBoolean("fixVampireRitual", "lotr.integration", true, "Allow Witchery's vampire ritual in the LOTR dimension.");
        LTConfig.SAFE_BIOMES = Arrays.asList(configuration.getStringList("safeBiomes", "lotr.integration", new String[]{}, "Safe LOTR biomes for vampires. Use display names, like 'Mordor' or 'Gorgoroth'."));
    }
    private static void config_lotr_travel() {
        DISABLED_WAYPOINTS = Arrays.asList(configuration.getStringList("disabledWaypoints", "lotr.travel", new String[]{},
                "List of waypoints to disable.\n" +
                        "Format: Internal waypoint name - run `/ltdebug dumpWaypoints` for a list.\n" +
                        "Example: 'MORANNON' would disable the Black Gate waypoint (display names and internal names often do not match!)"
        ));

        UNLOCK_WAYPOINTS = configuration.getBoolean("unlockAllWaypoints", "lotr.travel", false, "Unlocks all fast travel waypoints.");
        NO_WAYPOINT_LOCKING = configuration.getBoolean("disableWaypointLocking", "lotr.travel", false, "Disables alignment-based waypoint locking.");
    }

    public static void synchronizeConfiguration(File config) {
        configuration = new Configuration(config);

        config_combat();
        config_bandaid();

        if (Loader.isModLoaded("lotr")) {
            config_lotr_combat();
            config_lotr_generic();
            config_lotr_integration();
            config_lotr_travel();
        }

        if (Loader.isModLoaded("alfheim")) {
            config_alfheim();
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static boolean isWaypointDisabled(LOTRWaypoint waypoint) {
        return DISABLED_WAYPOINTS.contains(waypoint.name());
    }

    public static void registerModdedWeapons() {
        for (String entry : ADDITIONAL_COMBAT_ITEMS) {
            float speed = Float.parseFloat(entry.replaceAll(".*@", "").replaceAll("_.*", ""));
            float reach = Float.parseFloat(entry.replaceAll(".*_", ""));

            String modID = entry.replaceAll(":.*", "");
            String itemID = entry.replaceAll(".*:", "").replaceAll("@.*", "");

            Item item = GameRegistry.findItem(modID, itemID);

            if (item != null) {
                LilyflowerTweaks.LOGGER.debug("Registering item '{}:{}' to the LOTR combat system (speed {}x, reach {}x)...", modID, itemID, speed, reach);

                LOTRWeaponStats.registerMeleeSpeed(item, speed);
                LOTRWeaponStats.registerMeleeReach(item, reach);
            }
        }
    }

    public static boolean isBiomeSafe(String biome) {
        return SAFE_BIOMES.contains(biome);
    }

    public static int getETD() {
        return ESM_TELEPORT_DIMENSION;
    }
}
