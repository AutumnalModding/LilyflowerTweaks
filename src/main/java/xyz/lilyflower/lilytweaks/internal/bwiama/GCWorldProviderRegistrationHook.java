package xyz.lilyflower.lilytweaks.internal.bwiama;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.init.Items;
import xyz.lilyflower.lilytweaks.api.LilyflowerTweaksIntegrationModule;
import xyz.lilyflower.lilytweaks.debug.LoggingHelper;
import xyz.lilyflower.lilytweaks.init.LilyflowerTweaksInitializationSystem;

public class GCWorldProviderRegistrationHook implements LilyflowerTweaksIntegrationModule {
    private static final HashMap<Object, Integer> ARDA_STATION_RECIPE = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void run(FMLStateEvent stage) {}

    @Override
    public List<String> requiredMods() {
        return Arrays.asList("GalacticraftCore", "lotr");
    }

    @Override
    public boolean valid(FMLStateEvent stage) {
        return stage instanceof FMLPostInitializationEvent;
    }

    static {
        ARDA_STATION_RECIPE.put("ingotTin", 32);
        ARDA_STATION_RECIPE.put("ingotAluminum", 16);
        ARDA_STATION_RECIPE.put("waferAdvanced", 1);
        ARDA_STATION_RECIPE.put(Items.iron_ingot, 24);
    }
}
