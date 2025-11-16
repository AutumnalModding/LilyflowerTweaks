package xyz.lilyflower.lilytweaks.internal.bwiama;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import java.util.Arrays;
import java.util.List;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import xyz.lilyflower.lilytweaks.api.LilyflowerTweaksIntegrationModule;

public class ServerInitializationHooks implements LilyflowerTweaksIntegrationModule {
    @Override
    public void run(FMLStateEvent stage)
    {
//        FMLServerStartingEvent server = (FMLServerStartingEvent) stage;
//        WorldUtil.registerSpaceStations(server.getServer().worldServerForDimension(100).getSaveHandler().getMapFileFromName("dummy").getParentFile());
    }

    @Override
    public List<String> requiredMods() {
        return Arrays.asList("GalacticraftCore", "lotr");
    }

    @Override
    public boolean valid(FMLStateEvent stage) {
        return stage instanceof FMLServerStartingEvent;
    }
}
