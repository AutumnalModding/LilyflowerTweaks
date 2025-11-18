package xyz.lilyflower.solaris.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import cpw.mods.fml.common.Loader;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.solaris.init.Solaris;

public class SolarisConfigurationLoader {
    private static final HashMap<String, ArrayList<Consumer<Configuration>>> MODULES = new HashMap<>();

    public static void add(String mod, Consumer<Configuration> module) {
        ArrayList<Consumer<Configuration>> modules = MODULES.getOrDefault(mod, new ArrayList<>());
        modules.add(module);
        MODULES.put(mod, modules);
    }

    public static void load(File file) {
        Configuration backing = new Configuration(file);

        MODULES.forEach((mod, modules) -> {
            if (Loader.isModLoaded(mod)) {
                for (Consumer<Configuration> module : modules) {
                    Solaris.LOGGER.debug("Loading configuration module for mod {}...", mod.toUpperCase());
                    module.accept(backing);
                }
            }
        });

        if (backing.hasChanged()) {
            backing.save();
        }
    }
}
