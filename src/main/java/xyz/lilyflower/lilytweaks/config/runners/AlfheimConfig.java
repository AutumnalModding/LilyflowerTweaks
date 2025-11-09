package xyz.lilyflower.lilytweaks.config.runners;

import alfheim.common.core.handler.AlfheimConfigHandler;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

@SuppressWarnings("unused")
public class AlfheimConfig implements ConfigRunner {
    public static final Consumer<Configuration> GENERIC_TWEAKS = configuration -> {
        LilyflowerTweaksConfigSystem.Alfheim.ESM_TELEPORT_DIMENSION = configuration.getInt("esmTeleportDimension", "alfheim", AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim(), Integer.MIN_VALUE, Integer.MAX_VALUE, "ESM dimension ID for teleportation.");
        LilyflowerTweaksConfigSystem.Alfheim.DISABLE_TPDIM = configuration.getBoolean("disableTPDim", "alfheim", false, "Disable /tpdim command.");
        LilyflowerTweaksConfigSystem.Alfheim.ENABLE_ESM_RACES = configuration.getBoolean("esmRacesToggle", "alfheim", true, "Enable ESM races. Useful if you want to have MMO without ESM.");
        LilyflowerTweaksConfigSystem.Alfheim.ENABLE_ESM_FLIGHT = configuration.getBoolean("esmFlightToggle", "alfheim", true, "Enable ESM flight.");
    };

    public void init() {
        LilyflowerTweaksConfigSystem.add("alfheim", GENERIC_TWEAKS);
    }
}
