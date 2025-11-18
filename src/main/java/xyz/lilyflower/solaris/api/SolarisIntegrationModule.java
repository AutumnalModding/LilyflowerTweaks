package xyz.lilyflower.solaris.api;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLStateEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import xyz.lilyflower.solaris.init.Solaris;

public interface SolarisIntegrationModule {
    Map<SolarisIntegrationModule, Boolean> LOADERS = new HashMap<>();

    void run(FMLStateEvent stage);
    List<String> requiredMods();
    boolean valid(FMLStateEvent stage);

    static void add(SolarisIntegrationModule loader, boolean enabled) {
        LOADERS.put(loader, enabled);
    }

    static void init(FMLStateEvent stage) {
        LOADERS.forEach((loader, enabled) -> {
            StringBuilder name = new StringBuilder();
            boolean shouldLoad = loader.valid(stage);
            for (String mod : loader.requiredMods()) {
                if (!Loader.isModLoaded(mod)) {
                    shouldLoad = false;
                } else {
                    name.append(mod).append("-");
                }
            }
            if (shouldLoad && enabled) {
                String integration = name.substring(0, name.length() - 1);
                Solaris.LOGGER.info("Enabling {} integration!", integration);
                loader.run(stage);
            }
        });
    }
}
