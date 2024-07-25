package xyz.lilyflower.lilytweaks.util.config.opencomputers;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class OpencomputersMiscFeatureConfig {
    public static boolean TRANSPOSER_IGNORE_SIDEDNESS = false;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        TRANSPOSER_IGNORE_SIDEDNESS = configuration.getBoolean("transposerIgnoreSidedness", "opencomputers", false, "Ignore sidedness for transposer transfers.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
