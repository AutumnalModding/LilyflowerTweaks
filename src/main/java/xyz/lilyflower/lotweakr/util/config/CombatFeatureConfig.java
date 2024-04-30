package xyz.lilyflower.lotweakr.util.config;

import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import lotr.common.fac.LOTRFaction;
import lotr.common.item.LOTRWeaponStats;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lotweakr.core.LOTweakR;

/**
 * <strong>NOTE:</strong> FO here stands for "Faction Override".
 */
public class CombatFeatureConfig {

    public static boolean DISABLE_ATTACK_TIMINGS = false;
    public static boolean ENABLE_GW_DEATH = false;
    public static boolean ENABLE_WAR_CRIMES = false;
    public static boolean ENABLE_OMNITARGET = false;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        DISABLE_ATTACK_TIMINGS = configuration.getBoolean("removeNewCombat", "combat", false, "Removes the 1.9-style combat changes.");
        ENABLE_GW_DEATH = configuration.getBoolean("enableGWDeath", "combat", false, "Allows the Grey Wanderer to die.");

        ENABLE_WAR_CRIMES = configuration.getBoolean("enableWarCrimes", "combat", false, "Makes all LOTR factions permit war crimes.");
        ENABLE_OMNITARGET = configuration.getBoolean("enableOmnitarget", "combat", false, "Makes all LOTR NPCs attack any mob, NPC or non-NPC.");

        String[] items = configuration.getStringList("additionalCombatItems", "combat", new String[]{},
                "List of items to add to the custom LOTR combat system.\n" +
                "Format: 'modid:item_name@speedMultiplier_reachMultiplier'\n" +
                "Example: 'minecraft:golden_axe@1.5_1.0"
        );

        registerModdedWeapons(items);

        if (ENABLE_WAR_CRIMES) {
            for (LOTRFaction faction : LOTRFaction.values()) {
                faction.approvesWarCrimes = true;
            }
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private static void registerModdedWeapons(String[] items) {
        for (String entry : items) {
            float speed = Float.parseFloat(entry.replaceAll(".*@", "").replaceAll("_.*", ""));
            float reach = Float.parseFloat(entry.replaceAll(".*_", ""));

            String modID = entry.replaceAll(":.*", "");
            String itemID = entry.replaceAll(".*:", "").replaceAll("@.*", "");

            Item item = GameRegistry.findItem(modID, itemID);

            if (item != null) {
                LOTweakR.LOGGER.debug("Registering item '" + modID + ":" + itemID + "' to the LOTR combat system (speed " + speed + "x, reach " + reach + "x)...");

                LOTRWeaponStats.registerMeleeSpeed(item, speed);
                LOTRWeaponStats.registerMeleeReach(item, reach);
            }
        }
    }
}
