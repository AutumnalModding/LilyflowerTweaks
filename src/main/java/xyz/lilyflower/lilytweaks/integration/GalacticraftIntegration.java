package xyz.lilyflower.lilytweaks.integration;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import java.util.Arrays;
import java.util.List;
import xyz.lilyflower.lilytweaks.configuration.modules.GalacticraftConfiguration;
import xyz.lilyflower.lilytweaks.init.LilyflowerTweaksIntegrationModule;

public class GalacticraftIntegration implements LilyflowerTweaksIntegrationModule {
    @Override
    public void run() {
        for (String target : GalacticraftConfiguration.MODDED_PLANET_INTEGRATION) {
            switch (target) {

            }
        }
    }

    @Override
    public List<String> requiredMods() {
        return Arrays.asList("String");
    }

    @Override
    public boolean valid(FMLStateEvent stage) {
        return stage instanceof FMLInitializationEvent;
    }
}
