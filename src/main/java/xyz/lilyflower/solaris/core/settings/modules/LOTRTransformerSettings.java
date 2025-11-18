package xyz.lilyflower.solaris.core.settings.modules;

import xyz.lilyflower.solaris.api.TransformerSettingsModule;
import xyz.lilyflower.solaris.core.settings.SolarisTransformerSettings;

public class LOTRTransformerSettings implements TransformerSettingsModule {
    public static boolean CAN_PISTONS_PUSH = true;

    @Override
    public void init() {
        SolarisTransformerSettings.add("lotr", configuration -> {
            CAN_PISTONS_PUSH = configuration.getBoolean("canPistonsPush", "lotr", true, "Can pistons push blocks? Set this to whatever is correct for your modpack.");
        });
    }
}
