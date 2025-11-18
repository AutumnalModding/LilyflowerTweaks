package xyz.lilyflower.solaris.core.settings.modules;

import xyz.lilyflower.solaris.core.settings.SolarisTransformerSettings;
import xyz.lilyflower.solaris.api.TransformerSettingsModule;

@SuppressWarnings("unused")
public class AlfheimTransformerSettings implements TransformerSettingsModule {
    public static int ESM_TELEPORT_DIMENSION = -105;
    public static boolean DISABLE_TPDIM = false;
    public static boolean ENABLE_ESM_RACES = true;
    public static boolean ENABLE_ESM_FLIGHT = true;

    public static int getETD() {
        return ESM_TELEPORT_DIMENSION;
    }

    public void init() {
        SolarisTransformerSettings.add("alfheim", configuration -> {
            ESM_TELEPORT_DIMENSION = configuration.getInt("esmTeleportDimension", "alfheim", -105, Integer.MIN_VALUE, Integer.MAX_VALUE, "ESM dimension ID for teleportation.");
            DISABLE_TPDIM = configuration.getBoolean("disableTPDim", "alfheim", false, "Disable /tpdim command.");
            ENABLE_ESM_RACES = configuration.getBoolean("esmRacesToggle", "alfheim", true, "Enable ESM races. Useful if you want to have MMO without ESM.");
            ENABLE_ESM_FLIGHT = configuration.getBoolean("esmFlightToggle", "alfheim", true, "Enable ESM flight.");
        });
    }
}
