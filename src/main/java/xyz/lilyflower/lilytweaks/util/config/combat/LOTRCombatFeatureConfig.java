package xyz.lilyflower.lilytweaks.util.config.combat;

import cpw.mods.fml.common.registry.GameRegistry;
import java.io.File;
import lotr.common.fac.LOTRFaction;
import lotr.common.item.LOTRWeaponStats;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaks;

public class LOTRCombatFeatureConfig {

    public static boolean DISABLE_ATTACK_TIMINGS = true;
    public static boolean ENABLE_GW_DEATH = false;
    public static boolean ENABLE_WAR_CRIMES = false;
    public static boolean ENABLE_OMNITARGET = false;

    private static String[] ADDITIONAL_COMBAT_ITEMS;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        DISABLE_ATTACK_TIMINGS = configuration.getBoolean("removeNewCombat", "lotr.combat", true, "Removes the 1.9-style combat changes.");
        ENABLE_GW_DEATH = configuration.getBoolean("enableGWDeath", "lotr.combat", false, "Allows the Grey Wanderer to die.");

        ENABLE_WAR_CRIMES = configuration.getBoolean("enableWarCrimes", "lotr.combat", false, "Makes all LOTR factions permit war crimes.");
        ENABLE_OMNITARGET = configuration.getBoolean("enableOmnitarget", "lotr.combat", false, "Makes all LOTR NPCs attack any mob, NPC or non-NPC.");

        ADDITIONAL_COMBAT_ITEMS = configuration.getStringList("additionalCombatItems", "lotr.combat", new String[]{},
                "List of items to add to the custom LOTR combat system.\n" +
                "Format: 'modid:item_name@speedMultiplier_reachMultiplier'\n" +
                "Example: 'minecraft:golden_axe@1.5_1.0"
        );

        if (ENABLE_WAR_CRIMES) {
            for (LOTRFaction faction : LOTRFaction.values()) {
                faction.approvesWarCrimes = true;
            }
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
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
}
