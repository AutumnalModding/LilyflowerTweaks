package xyz.lilyflower.lilytweaks.core.settings.modules;

import xyz.lilyflower.lilytweaks.api.TransformerSettingsModule;
import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;

public class LOTRTransformerSettings implements TransformerSettingsModule {
    public static boolean CAN_PISTONS_PUSH = true;

    @Override
    public void init() {
        LilyflowerTweaksTransformerSettingsSystem.add("lotr", configuration -> {
            CAN_PISTONS_PUSH = configuration.getBoolean("canPistonsPush", "lotr", true, "Can pistons push blocks? Set this to whatever is correct for your modpack.");
        });
    }
}
