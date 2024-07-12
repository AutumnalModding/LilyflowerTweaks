package xyz.lilyflower.lilytweaks.util.config.generic;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class BandaidFeatureConfig {
    public static boolean DISABLE_SNOW_UPDATES = false;
    public static boolean DISABLE_WORLDGEN_SPAWNING = false;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        DISABLE_SNOW_UPDATES = configuration.getBoolean("disableSnowUpdates", "bandaid", false, "Disables snow sheet blocks from sending neighbour updates.\nCan stop StackOverflowExceptions in some cases.");
        DISABLE_WORLDGEN_SPAWNING = configuration.getBoolean("disableWorldgenSpawning", "bandaid", false, "Disables animals spawning during worldgen.\nCan fix 'this.entitiesByUuid is null' crashes.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
