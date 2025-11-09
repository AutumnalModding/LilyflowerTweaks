package xyz.lilyflower.lilytweaks.config.runners;

import java.util.Arrays;
import java.util.function.Consumer;
import lotr.common.fac.LOTRFaction;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

@SuppressWarnings("unused")
public class LotrConfig implements ConfigRunner {
    public static final Consumer<Configuration> COMBAT_TWEAKS = configuration -> {
        LilyflowerTweaksConfigSystem.LOTR.DISABLE_ATTACK_TIMINGS = configuration.getBoolean("removeNewCombat", "lotr.combat", true, "Removes the 1.9-style combat changes.");
        LilyflowerTweaksConfigSystem.LOTR.ENABLE_WANDERER_DEATH = configuration.getBoolean("enableWandererDeath", "lotr.combat", false, "Allows the Grey Wanderer to die.");

        LilyflowerTweaksConfigSystem.LOTR.SHORTCIRCUIT_WAR_CRIMES = configuration.getBoolean("shortcircuitWarCrimes", "lotr.combat", false, "Makes all LOTR factions permit war crimes.");
        LilyflowerTweaksConfigSystem.LOTR.SHORTCIRCUIT_TARGET_SELECTOR = configuration.getBoolean("shortcircuitTargetSelector", "lotr.combat", false, "Makes all LOTR NPCs attack any mob, NPC or non-NPC.");

        LilyflowerTweaksConfigSystem.LOTR.ADDITIONAL_COMBAT_ITEMS = configuration.getStringList("additionalCombatItems", "lotr.combat", new String[]{},
                "List of items to add to the custom LOTR combat system.\n" +
                        "Format: 'modid:item_name@speedMultiplier_reachMultiplier'\n" +
                        "Example: 'minecraft:golden_axe@1.5_1.0"
        );

        if (LilyflowerTweaksConfigSystem.LOTR.SHORTCIRCUIT_WAR_CRIMES) {
            for (LOTRFaction faction : LOTRFaction.values()) {
                faction.approvesWarCrimes = true;
            }
        }
    };

    public static final Consumer<Configuration> GENERIC_TWEAKS = configuration -> {
        LilyflowerTweaksConfigSystem.LOTR.UNLOCK_COSMETICS = configuration.getBoolean("unlockCosmetics", "lotr.misc", false, "Unlocks all player-specific LOTR cosmetics.");
        LilyflowerTweaksConfigSystem.LOTR.FIX_ORE_DICTIONARY = configuration.getBoolean("fixOreDictionary", "lotr.misc", true, "Registers LOTR ores to the Forge ore dictionary.");
        LilyflowerTweaksConfigSystem.LOTR.TIME_MULTIPLIER = configuration.getFloat("timeMultiplier", "lotr.misc", 1.0F, Float.MIN_VALUE, Float.MAX_VALUE, "Day length multiplier for LOTR days.");
        LilyflowerTweaksConfigSystem.LOTR.ALLOW_SCRAP_TRADER_SCREENSHOTS = configuration.getBoolean("allowOddmentScreenshots", "lotr.misc", false, "Allow taking screenshots of the Oddment Collector.");
        LilyflowerTweaksConfigSystem.LOTR.TIME_BASE = configuration.getInt("timeBase", "lotr.misc", 48000, 1, Integer.MAX_VALUE, "LOTR day length (base)");
    };

    public static final Consumer<Configuration> CROSS_MOD_INTEGRATION = configuration -> {
        LilyflowerTweaksConfigSystem.FIX_RITUAL = configuration.getBoolean("fixVampireRitual", "lotr.integration", true, "Allow Witchery's vampire ritual in the LOTR dimension.");
        LilyflowerTweaksConfigSystem.SAFE_BIOMES = Arrays.asList(configuration.getStringList("safeBiomes", "lotr.integration", new String[]{}, "Safe LOTR biomes for vampires. Use display names, like 'Mordor' or 'Gorgoroth'."));
    };

    public static final Consumer<Configuration> FAST_TRAVEL_TWEAKS = configuration -> {
        LilyflowerTweaksConfigSystem.LOTR.DISABLED_WAYPOINTS = Arrays.asList(configuration.getStringList("disabledWaypoints", "lotr.travel", new String[]{},
                "List of waypoints to disable.\n" +
                        "Format: Internal waypoint name - run `/ltdebug dumpWaypoints` for a list.\n" +
                        "Example: 'MORANNON' would disable the Black Gate waypoint (display names and internal names often do not match!)"
        ));

        LilyflowerTweaksConfigSystem.LOTR.UNLOCK_WAYPOINTS = configuration.getBoolean("unlockAllWaypoints", "lotr.travel", false, "Unlocks all fast travel waypoints.");
        LilyflowerTweaksConfigSystem.LOTR.NO_WAYPOINT_LOCKING = configuration.getBoolean("disableWaypointLocking", "lotr.travel", false, "Disables alignment-based waypoint locking.");
    };

    public void init() {
        LilyflowerTweaksConfigSystem.add("lotr", COMBAT_TWEAKS);
        LilyflowerTweaksConfigSystem.add("lotr", GENERIC_TWEAKS);
        LilyflowerTweaksConfigSystem.add("lotr", CROSS_MOD_INTEGRATION);
        LilyflowerTweaksConfigSystem.add("lotr", FAST_TRAVEL_TWEAKS);
    }
}
