package xyz.lilyflower.lilytweaks.config.runners;

import alfheim.common.core.handler.AlfheimConfigHandler;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

public class AlfheimConfig implements ConfigRunner {
    public static final Consumer<Configuration> GENERIC_TWEAKS = configuration -> {
        LilyflowerTweaksConfigSystem.ESM_TELEPORT_DIMENSION = configuration.getInt("esmTeleportDimension", "alfheim", AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim(), Integer.MIN_VALUE, Integer.MAX_VALUE, "ESM dimension ID for teleportation.");
        LilyflowerTweaksConfigSystem.DISABLE_TPDIM = configuration.getBoolean("disableTPDim", "alfheim", false, "Disable /tpdim command.");
        LilyflowerTweaksConfigSystem.DISABLE_ESM_RACES = configuration.getBoolean("esmRacesToggle", "alfheim", false, "Disable ESM races. Useful if you want to have MMO without ESM.");
    };

    public void init() {
        LilyflowerTweaksConfigSystem.add("alfheim", GENERIC_TWEAKS);
    }
}
