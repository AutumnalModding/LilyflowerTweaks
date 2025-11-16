package xyz.lilyflower.lilytweaks.configuration.modules;

import xyz.lilyflower.lilytweaks.api.ConfigurationModule;
import xyz.lilyflower.lilytweaks.configuration.LilyflowerTweaksGameConfigurationSystem;

public class CustomContentAdditionsConfiguration implements ConfigurationModule {
    public static boolean ENABLE_CONTENT = false;
    public static String MODPACK_IDENTIFIER = "";

    @Override
    public void init() {
        LilyflowerTweaksGameConfigurationSystem.add("lilytweaks", configuration -> {
            ENABLE_CONTENT = configuration.getBoolean("globalContentToggle", "content", false, "Enables or disables all Lilyflower Tweaks content. You probably want to leave this disabled.");
            MODPACK_IDENTIFIER = configuration.getString("modpackIdentifier", "content", "", "Modpack identifier for internal content. Don't touch this.");
        });
    }
}
