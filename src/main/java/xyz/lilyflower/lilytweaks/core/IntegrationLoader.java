package xyz.lilyflower.lilytweaks.core;

import cpw.mods.fml.common.Loader;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.IntegrationThaumcraft;
import xyz.lilyflower.lilytweaks.util.lotr.config.LOTRIntegrationFeatureConfig;

public interface IntegrationLoader {
    Map<IntegrationLoader, Boolean> LOADERS = new HashMap<>();

    List<String> requiredMods();
    void runPre();
    void run();
    void runPost();

    static void add(IntegrationLoader loader, boolean enabled) {
        LOADERS.put(loader, enabled);
    }

    static void runAllPre() {
        try {
            add(new IntegrationThaumcraft(), LOTRIntegrationFeatureConfig.THAUMCRAFT_ENABLED);
        } catch (NoClassDefFoundError ignored) {}

        LOADERS.forEach((loader, enabled) -> {
            StringBuilder name = new StringBuilder();
            boolean shouldLoad = true;
            for (String mod : loader.requiredMods()) {
                if (!Loader.isModLoaded(mod)) {
                    shouldLoad = false;
                } else {
                    name.append(mod).append("-");
                }
            }
            if (shouldLoad && enabled) {
                String integration = name.substring(0, name.length() - 1);
                LilyflowerTweaks.LOGGER.info("Enabling {} integration!", integration);
                loader.runPre();
            }
        });
    }

    static void runAll() {
        LOADERS.forEach((loader, enabled) -> {
            boolean shouldLoad = true;
            for (String mod : loader.requiredMods()) {
                if (!Loader.isModLoaded(mod)) {
                    shouldLoad = false;
                }
            }
            if (shouldLoad && enabled) {
                loader.run();
            }
        });
    }

    static void runAllPost() {
        LOADERS.forEach((loader, enabled) -> {
            boolean shouldLoad = true;
            for (String mod : loader.requiredMods()) {
                if (!Loader.isModLoaded(mod)) {
                    shouldLoad = false;
                }
            }
            if (shouldLoad && enabled) {
                loader.runPost();
            }
        });
    }
}
