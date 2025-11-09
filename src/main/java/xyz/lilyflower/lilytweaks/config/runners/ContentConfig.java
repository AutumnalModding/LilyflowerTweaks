package xyz.lilyflower.lilytweaks.config.runners;

import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

public class ContentConfig implements ConfigRunner {
    public static final Consumer<Configuration> CONTENT_SETTINGS = configuration -> {
        LilyflowerTweaksConfigSystem.Content.ENABLE_CONTENT = configuration.getBoolean("globalContentToggle", "content", false, "Enables or disables all Lilyflower Tweaks content. You probably want to leave this disabled.");
        LilyflowerTweaksConfigSystem.Content.ENABLE_SUBSTITUTIONS_ITEM = configuration.getBoolean("enableItemSubstitutions", "content", false, "Enables or disables item substitutions. Don't touch this if you don't know what you're doing.");
        LilyflowerTweaksConfigSystem.Content.ENABLE_SUBSTITUTIONS_BLOCK = configuration.getBoolean("enableBlockSubstitutions", "content", false, "Enables or disables block substitutions. Don't touch this if you don't know what you're doing.");
    };

    @Override
    public void init() {
        LilyflowerTweaksConfigSystem.add("lilytweaks", CONTENT_SETTINGS);
    }
}
