package xyz.lilyflower.lotweakr.core;

import cpw.mods.fml.common.Loader;
import java.util.Map;
import java.util.HashMap;
import xyz.lilyflower.lotweakr.integration.thaumcraft.LTRIntegrationThaumcraft;
import xyz.lilyflower.lotweakr.integration.witchery.LTRIntegrationWitchery;
import xyz.lilyflower.lotweakr.util.config.IntegrationFeatureConfig;

public interface LTRIntegrationLoader {
    Map<LTRIntegrationLoader, Boolean> LOADERS = new HashMap<>();

    String requiredMod();
    void runPre();
    void run();
    void runPost();

    static void add(LTRIntegrationLoader loader, boolean enabled) {
        LOADERS.put(loader, enabled);
    }

    static void runAllPre() {
        try {
            add(new LTRIntegrationThaumcraft(), IntegrationFeatureConfig.THAUMCRAFT_ENABLED);
            add(new LTRIntegrationWitchery(), IntegrationFeatureConfig.WITCHERY_ENABLED);
        } catch (NoClassDefFoundError ignored) {

        }

        LOADERS.forEach((loader, enabled) -> {
            boolean shouldLoad = Loader.isModLoaded(loader.requiredMod());
            if (shouldLoad && enabled) {
                LOTweakR.LOGGER.info("Enabling {} integration!", loader.requiredMod());
                loader.runPre();
            }
        });
    }

    static void runAll() {
        LOADERS.forEach((loader, enabled) -> {
            boolean shouldLoad = Loader.isModLoaded(loader.requiredMod());
            if (shouldLoad && enabled) {
                loader.run();
            }
        });
    }

    static void runAllPost() {
        LOADERS.forEach((loader, enabled) -> {
            boolean shouldLoad = Loader.isModLoaded(loader.requiredMod());
            if (shouldLoad && enabled) {
                loader.runPost();
            }
        });
    }
}
