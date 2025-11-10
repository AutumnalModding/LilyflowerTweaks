package xyz.lilyflower.lilytweaks.settings.runners;

import alfheim.common.core.handler.AlfheimConfigHandler;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksGameConfigurationSystem;
import xyz.lilyflower.lilytweaks.settings.LilyflowerTweaksTransformerSettingsSystem;
import xyz.lilyflower.lilytweaks.settings.SettingsRunner;

@SuppressWarnings("unused")
public class AlfheimConfig implements SettingsRunner {
    public static final Consumer<Configuration> GENERIC_TWEAKS = configuration -> {
        LilyflowerTweaksTransformerSettingsSystem.Alfheim.ESM_TELEPORT_DIMENSION = configuration.getInt("esmTeleportDimension", "alfheim", -105, Integer.MIN_VALUE, Integer.MAX_VALUE, "ESM dimension ID for teleportation.");
        LilyflowerTweaksTransformerSettingsSystem.Alfheim.DISABLE_TPDIM = configuration.getBoolean("disableTPDim", "alfheim", false, "Disable /tpdim command.");
        LilyflowerTweaksTransformerSettingsSystem.Alfheim.ENABLE_ESM_RACES = configuration.getBoolean("esmRacesToggle", "alfheim", true, "Enable ESM races. Useful if you want to have MMO without ESM.");
        LilyflowerTweaksTransformerSettingsSystem.Alfheim.ENABLE_ESM_FLIGHT = configuration.getBoolean("esmFlightToggle", "alfheim", true, "Enable ESM flight.");
    };

    public void init() {
        LilyflowerTweaksTransformerSettingsSystem.add(GENERIC_TWEAKS);
    }
}
