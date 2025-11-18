package xyz.lilyflower.solaris.core.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.solaris.core.SolarisBootstrap;
import xyz.lilyflower.solaris.util.FifteenthCompetingStandard;

public class SolarisTransformerSettings {
    private static final ArrayList<FifteenthCompetingStandard.Pair<String, Consumer<Configuration>>> CONFIG_RUNNERS = new ArrayList<>();

    // No 'mod' parameter -- too early to be depending on that!
    public static void add(String identifier, Consumer<Configuration> runner) {
        CONFIG_RUNNERS.add(new FifteenthCompetingStandard.Pair<>(identifier, runner));
    }

    public static void load(File file) {
        // Make sure we don't load earlyconfig in a development environment. For some reason.
        if (System.getProperties().containsKey("net.minecraftforge.gradle.GradleStart.srgDir")) return;
        Configuration settings = new Configuration(file);
        SolarisBootstrap.LOGGER.info("Initializing transformer settings...");

        CONFIG_RUNNERS.forEach(runner -> {
            SolarisBootstrap.LOGGER.info("Parsing settings for '{}'!", runner.left().toUpperCase());
            runner.right().accept(settings);
        });

        if (settings.hasChanged()) {
            settings.save();
        }
    }
}
