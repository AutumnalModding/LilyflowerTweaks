package xyz.lilyflower.lilytweaks.core.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;

public class LilyflowerTweaksTransformerSettingsSystem {
    private static final ArrayList<Consumer<Configuration>> CONFIG_RUNNERS = new ArrayList<>();

    public static class Alfheim {
        public static int ESM_TELEPORT_DIMENSION = -105;
        public static boolean DISABLE_TPDIM = false;
        public static boolean ENABLE_ESM_RACES = true;
        public static boolean ENABLE_ESM_FLIGHT = true;
    }

    // No 'mod' parameter -- too early to be depending on that!
    public static void add(Consumer<Configuration> runner) {
        CONFIG_RUNNERS.add(runner);
    }

    public static void synchronizeConfiguration(File config) {
        Configuration settings = new Configuration(config);

        CONFIG_RUNNERS.forEach(runner -> {
            runner.accept(settings);
        });

        if (settings.hasChanged()) {
            settings.save();
        }
    }

    public static class Stability {
        public static boolean STABILITY_OVERRIDES = false;
        public static boolean GROSS_REGISTRY_HACKS = false;
    }
}
