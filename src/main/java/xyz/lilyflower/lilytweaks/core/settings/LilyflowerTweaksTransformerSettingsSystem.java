package xyz.lilyflower.lilytweaks.core.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapSystem;
import xyz.lilyflower.lilytweaks.util.Pair;

public class LilyflowerTweaksTransformerSettingsSystem {
    private static final ArrayList<Pair<String, Consumer<Configuration>>> CONFIG_RUNNERS = new ArrayList<>();

    public static class Alfheim {
        public static int ESM_TELEPORT_DIMENSION = -105;
        public static boolean DISABLE_TPDIM = false;
        public static boolean ENABLE_ESM_RACES = true;
        public static boolean ENABLE_ESM_FLIGHT = true;
    }

    // No 'mod' parameter -- too early to be depending on that!
    public static void add(String identifier, Consumer<Configuration> runner) {
        CONFIG_RUNNERS.add(new Pair<>(identifier, runner));
    }

    public static void load(File config) {
        Configuration settings = new Configuration(config);
        LilyflowerTweaksBootstrapSystem.LOGGER.info("Initializing early-config system...");

        CONFIG_RUNNERS.forEach(runner -> {
            LilyflowerTweaksBootstrapSystem.LOGGER.info("Parsing settings for '{}'!", runner.left().toUpperCase());
            runner.right().accept(settings);
        });

        if (settings.hasChanged()) {
            settings.save();
        }
    }

    public static class Stability {
        public static boolean STABILITY_OVERRIDES = false;
        public static boolean GROSS_REGISTRY_HACKS = false;
        public static boolean DISABLE_OPENCOMPUTERS_ROBOTS = false;
    }
}
