package xyz.lilyflower.lilytweaks.core;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import java.io.File;
import lotr.common.LOTRMod;
import lotr.common.LOTRTime;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.lilyflower.lilytweaks.util.config.combat.GenericCombatFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.combat.LOTRCombatFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.lotr.LOTRGenericFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.interop.LOTRIntegrationFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.lotr.LOTRTravelFeatureConfig;
import xyz.lilyflower.lilytweaks.util.lotr.loader.LOTRCustomDataLoader;
import xyz.lilyflower.lilytweaks.util.lotr.debug.LTRDebuggerCommand;

@Mod(modid = LilyflowerTweaks.MODID, version = LilyflowerTweaks.VERSION, dependencies = "after:lotr;after:Thaumcraft")
public class LilyflowerTweaks
{
    public static final String MODID = "lilytweaks";
    public static final String VERSION = "2.1";

    public static final Logger LOGGER = LogManager.getLogger("LilyflowerTweaks");

    private static File getFeatureConfig(String mod, String base, String feature) {
        File config = new File(base + "/lilytweaks/" + mod + "/" + feature + ".cfg");
        if (!config.getParentFile().exists()) {
            config.getParentFile().mkdirs();
        }
        return config;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        String baseConfigPath = event.getSuggestedConfigurationFile().getParent();

        if (Loader.isModLoaded("lotr")) {
            LOTRGenericFeatureConfig.synchronizeConfiguration(getFeatureConfig("lotr", baseConfigPath, "misc"));
            LOTRCombatFeatureConfig.synchronizeConfiguration(getFeatureConfig("lotr", baseConfigPath, "combat"));
            LOTRTravelFeatureConfig.synchronizeConfiguration(getFeatureConfig("lotr", baseConfigPath, "travel"));
            LOTRIntegrationFeatureConfig.synchronizeConfiguration(getFeatureConfig("lotr", baseConfigPath, "integration"));

            LOTRTime.DAY_LENGTH = (int) (LOTRTime.DAY_LENGTH * LOTRGenericFeatureConfig.TIME_MULTIPLIER);

            LOTRCustomDataLoader.runAll();
        }

        GenericCombatFeatureConfig.synchronizeConfiguration(getFeatureConfig("generic", baseConfigPath, "combat"));

        IntegrationLoader.runAllPre();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (LOTRGenericFeatureConfig.FIX_ORE_DICTIONARY) {
            OreDictionary.registerOre("oreMithril", LOTRMod.oreMithril);
            OreDictionary.registerOre("oreMythril", LOTRMod.oreMithril);
            OreDictionary.registerOre("ingotMithril", LOTRMod.mithril);
            OreDictionary.registerOre("ingotMythril", LOTRMod.mithril);
            OreDictionary.registerOre("nuggetMithril", LOTRMod.mithrilNugget);
            OreDictionary.registerOre("nuggetMythril", LOTRMod.mithrilNugget);
        }

        IntegrationLoader.runAll();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        IntegrationLoader.runAllPost();

        if (Loader.isModLoaded("lotr")) {
            LOTRCombatFeatureConfig.registerModdedWeapons();
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Loader.isModLoaded("lotr")) {
            event.registerServerCommand(new LTRDebuggerCommand());
        }
    }
}
