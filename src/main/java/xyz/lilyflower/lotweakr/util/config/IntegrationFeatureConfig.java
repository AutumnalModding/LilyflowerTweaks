package xyz.lilyflower.lotweakr.util.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import net.minecraftforge.common.config.Configuration;

public class IntegrationFeatureConfig {
    public static boolean THAUMCRAFT_ENABLED = true;
    public static boolean WITCHERY_ENABLED = true;

    public static boolean FIX_RITUAL = true;
    private static List<String> SAFE_BIOMES;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        THAUMCRAFT_ENABLED = configuration.getBoolean("enableThaumIntegration", "thaumcraft", true, "Enables Thaumcraft integration.");
        WITCHERY_ENABLED = configuration.getBoolean("enableWitcheryIntegration", "witchery", true, "Enables Witchery integration.");

        FIX_RITUAL = configuration.getBoolean("fixVampireRitual", "witchery", true, "Allow Witchery's vampire ritual in the LOTR dimension.");
        SAFE_BIOMES = Arrays.asList(configuration.getStringList("safeBiomes", "witchery", new String[]{}, "Safe LOTR biomes for vampires. Use display names, like 'Mordor' or 'Gorgoroth'."));

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static boolean isBiomeSafe(String biome) {
        return SAFE_BIOMES.contains(biome);
    }
}
