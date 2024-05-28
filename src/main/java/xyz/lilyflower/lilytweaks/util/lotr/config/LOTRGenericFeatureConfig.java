package xyz.lilyflower.lilytweaks.util.lotr.config;

import cpw.mods.fml.common.Loader;
import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class LOTRGenericFeatureConfig {
    public static boolean UNLOCK_COSMETICS = false;
    public static boolean FIX_ORE_DICTIONARY = Loader.isModLoaded("lotr");
    public static float TIME_MULTIPLIER = 1;
    public static boolean ALLOW_SCRAP_TRADER_SCREENSHOTS = false;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        UNLOCK_COSMETICS = configuration.getBoolean("unlockCosmetics", "misc", false, "Unlocks all player-specific LOTR cosmetics.");
        FIX_ORE_DICTIONARY = configuration.getBoolean("fixOreDictionary", "misc", true, "Registers LOTR ores to the Forge ore dictionary.");
        TIME_MULTIPLIER = configuration.getFloat("timeMultiplier", "misc", 1.0F, Float.MIN_VALUE, Float.MAX_VALUE, "Day length multiplier for LOTR days.");
        ALLOW_SCRAP_TRADER_SCREENSHOTS = configuration.getBoolean("allowOddmentScreenshots", "misc", false, "Allow taking screenshots of the Oddment Collector.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
