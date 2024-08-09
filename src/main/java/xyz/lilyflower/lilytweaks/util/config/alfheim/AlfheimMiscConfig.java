package xyz.lilyflower.lilytweaks.util.config.alfheim;

import alfheim.common.core.handler.AlfheimConfigHandler;
import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class AlfheimMiscConfig {
    public static int ESM_TELEPORT_DIMENSION = AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim();

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        ESM_TELEPORT_DIMENSION = configuration.getInt("esmTeleportDimension", "alfheim", AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim(), 0, Integer.MAX_VALUE, "ESM dimension ID for teleportation.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static int getETD() {
        return ESM_TELEPORT_DIMENSION;
    }
}
