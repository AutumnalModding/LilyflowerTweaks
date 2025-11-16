package xyz.lilyflower.lilytweaks.integration;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import java.util.Arrays;
import java.util.List;
import xyz.lilyflower.lilytweaks.configuration.modules.GalacticraftIntegrationConfiguration;
import xyz.lilyflower.lilytweaks.api.LilyflowerTweaksIntegrationModule;

public class GalacticraftIntegration implements LilyflowerTweaksIntegrationModule {
    @Override
    public void run(FMLStateEvent stage) {
        for (String target : GalacticraftIntegrationConfiguration.MODDED_PLANET_INTEGRATION) {
            switch (target) {
                // TODO: actually implement this lmao
            }
        }
    }

    @Override
    public List<String> requiredMods() {
        return Arrays.asList("GalacticraftCore");
    }

    @Override
    public boolean valid(FMLStateEvent stage) {
        return stage instanceof FMLInitializationEvent;
    }
}
