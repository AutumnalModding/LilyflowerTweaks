package xyz.lilyflower.lilytweaks.core.settings.modules;

import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;
import xyz.lilyflower.lilytweaks.api.TransformerSettingsModule;

@SuppressWarnings("unused")
public class AlfheimTransformerSettings implements TransformerSettingsModule {
    public static int getETD() {
        return LilyflowerTweaksTransformerSettingsSystem.Alfheim.ESM_TELEPORT_DIMENSION;
    }

    public void init() {
        LilyflowerTweaksTransformerSettingsSystem.add("alfheim", configuration -> {
            LilyflowerTweaksTransformerSettingsSystem.Alfheim.ESM_TELEPORT_DIMENSION = configuration.getInt("esmTeleportDimension", "alfheim", -105, Integer.MIN_VALUE, Integer.MAX_VALUE, "ESM dimension ID for teleportation.");
            LilyflowerTweaksTransformerSettingsSystem.Alfheim.DISABLE_TPDIM = configuration.getBoolean("disableTPDim", "alfheim", false, "Disable /tpdim command.");
            LilyflowerTweaksTransformerSettingsSystem.Alfheim.ENABLE_ESM_RACES = configuration.getBoolean("esmRacesToggle", "alfheim", true, "Enable ESM races. Useful if you want to have MMO without ESM.");
            LilyflowerTweaksTransformerSettingsSystem.Alfheim.ENABLE_ESM_FLIGHT = configuration.getBoolean("esmFlightToggle", "alfheim", true, "Enable ESM flight.");
        });
    }
}
