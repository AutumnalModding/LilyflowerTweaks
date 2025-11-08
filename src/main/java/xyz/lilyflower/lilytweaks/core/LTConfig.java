package xyz.lilyflower.lilytweaks.core;

import com.emoniph.witchery.util.IHandleDT;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import lotr.common.item.LOTRWeaponStats;
import lotr.common.world.map.LOTRWaypoint;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class LTConfig {
    private static final HashMap<String, ArrayList<Consumer<Configuration>>> CONFIG_RUNNERS = new HashMap<>();

    public static boolean UNLOCK_WAYPOINTS = false;
    public static boolean NO_WAYPOINT_LOCKING = false;
    public static HashMap<Class<? extends IHandleDT>, Float> WITCHERY_DAMAGE_CAPS = new HashMap<>();
    public static ArrayList<String> NO_IFRAME_DAMAGETYPES;
    public static boolean NO_IFRAME_PROJECTILES = false;
    public static boolean DISABLE_ATTACK_TIMINGS = false;
    public static boolean ENABLE_WANDERER_DEATH = false;
    public static boolean SHORTCIRCUIT_WAR_CRIMES = false;
    public static boolean SHORTCIRCUIT_TARGET_SELECTOR = false;
    public static boolean UNLOCK_COSMETICS = false;
    public static boolean FIX_ORE_DICTIONARY = Loader.isModLoaded("lotr");
    public static float TIME_MULTIPLIER = 1;
    public static boolean ALLOW_SCRAP_TRADER_SCREENSHOTS = false;
    public static int TIME_BASE = 48000;
    public static boolean THAUMCRAFT_ENABLED = false;
    public static boolean FIX_RITUAL = true;
    public static boolean DISABLE_SNOW_UPDATES = false;
    public static boolean DISABLE_WORLDGEN_SPAWNING = false;
    public static int ESM_TELEPORT_DIMENSION = Loader.isModLoaded("alfheim") ? -105 : 0;
    public static boolean DISABLE_TPDIM = false;
    public static boolean DISABLE_ESM_RACES = false;
    static List<String> DISABLED_WAYPOINTS;
    public static String[] ADDITIONAL_COMBAT_ITEMS;
    public static List<String> SAFE_BIOMES;
    public static HashSet<Integer> MILLENAIRE_ALLOWED_DIMENSIONS = new HashSet<>();

    public static void add(String mod, Consumer<Configuration> runner) {
        ArrayList<Consumer<Configuration>> runners = CONFIG_RUNNERS.getOrDefault(mod, new ArrayList<>());
        runners.add(runner);
        CONFIG_RUNNERS.put(mod, runners);
    }

    public static void synchronizeConfiguration(File config) {
        Configuration cfg = new Configuration(config);

        CONFIG_RUNNERS.forEach((mod, runners) -> {
            if (Loader.isModLoaded(mod)) {
                for (Consumer<Configuration> runner : runners) {
                    runner.accept(cfg);
                }
            }
        });

        if (cfg.hasChanged()) {
            cfg.save();
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
                LTInit.LOGGER.debug("Registering item '{}:{}' to the LOTR combat system (speed {}x, reach {}x)...", modID, itemID, speed, reach);

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
