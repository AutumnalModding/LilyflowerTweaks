package xyz.lilyflower.lilytweaks.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import cpw.mods.fml.common.Loader;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.init.LilyflowerTweaksInitializationSystem;

public class LilyflowerTweaksGameConfigurationSystem {
    private static final HashMap<String, ArrayList<Consumer<Configuration>>> CONFIG_RUNNERS = new HashMap<>();

    public static void add(String mod, Consumer<Configuration> runner) {
        ArrayList<Consumer<Configuration>> runners = CONFIG_RUNNERS.getOrDefault(mod, new ArrayList<>());
        runners.add(runner);
        CONFIG_RUNNERS.put(mod, runners);
    }

    public static void load(File file) {
        Configuration backing = new Configuration(file);

        CONFIG_RUNNERS.forEach((mod, runners) -> {
            if (Loader.isModLoaded(mod)) {
                for (Consumer<Configuration> runner : runners) {
                    LilyflowerTweaksInitializationSystem.LOGGER.debug("Loading configuration runner for mod {}...", mod.toUpperCase());
                    runner.accept(backing);
                }
            }
        });

        if (backing.hasChanged()) {
            backing.save();
        }
    }
}
