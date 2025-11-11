package xyz.lilyflower.lilytweaks.config.runners;

import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksGameConfigurationSystem;

public class ContentConfig implements ConfigRunner {
    @Override
    public void init() {
        LilyflowerTweaksGameConfigurationSystem.add("lilytweaks", configuration -> {
            LilyflowerTweaksGameConfigurationSystem.Content.ENABLE_CONTENT = configuration.getBoolean("globalContentToggle", "content", false, "Enables or disables all Lilyflower Tweaks content. You probably want to leave this disabled.");
        });
    }
}
