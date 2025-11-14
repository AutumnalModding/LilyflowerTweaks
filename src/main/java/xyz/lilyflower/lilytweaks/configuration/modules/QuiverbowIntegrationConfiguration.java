package xyz.lilyflower.lilytweaks.configuration.modules;

import java.util.List;
import java.util.Arrays;
import xyz.lilyflower.lilytweaks.configuration.ConfigurationModule;
import xyz.lilyflower.lilytweaks.configuration.LilyflowerTweaksGameConfigurationSystem;

public class QuiverbowIntegrationConfiguration implements ConfigurationModule {
    public static List<String> ENTITY_BLACKLIST;

    @Override
    public void init() {
        LilyflowerTweaksGameConfigurationSystem.add("quiverchevsky", configuration -> {
            ENTITY_BLACKLIST = Arrays.asList(configuration.getStringList("soulCairnBlacklist", "quiverbow", new String[]{"net.minecraft.entity.boss.IBossDisplayData"}, "List of entities that are barred from the Soul Cairn. Uses fully qualified class names."));
        });
    }
}
