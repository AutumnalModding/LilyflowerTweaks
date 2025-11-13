package xyz.lilyflower.lilytweaks.configuration.modules;

import xyz.lilyflower.lilytweaks.configuration.ConfigurationModule;
import xyz.lilyflower.lilytweaks.configuration.LilyflowerTweaksGameConfigurationSystem;

public class ContentConfiguration implements ConfigurationModule {
    public static boolean ENABLE_CONTENT = false;

    @Override
    public void init() {
        LilyflowerTweaksGameConfigurationSystem.add("lilytweaks", configuration -> {
            ENABLE_CONTENT = configuration.getBoolean("globalContentToggle", "content", false, "Enables or disables all Lilyflower Tweaks content. You probably want to leave this disabled.");
        });
    }
}
