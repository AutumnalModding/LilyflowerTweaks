package xyz.lilyflower.solaris.integration.galacticraft;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import java.util.Arrays;
import java.util.List;
import xyz.lilyflower.solaris.api.LoadStage;
import xyz.lilyflower.solaris.configuration.modules.SolarisGalacticraft;
import xyz.lilyflower.solaris.api.SolarisIntegrationModule;
import xyz.lilyflower.solaris.init.Solaris;

public class PlanetRegistrationHook implements SolarisIntegrationModule {
    @Override
    public void run() {
        for (String target : SolarisGalacticraft.MODDED_PLANET_INTEGRATION) {
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
    public boolean valid() {
        return Solaris.STATE == LoadStage.RUNNING;
    }
}
