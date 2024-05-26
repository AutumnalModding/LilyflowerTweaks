package xyz.lilyflower.lotweakr.core;

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
import xyz.lilyflower.lotweakr.util.config.CombatFeatureConfig;
import xyz.lilyflower.lotweakr.util.config.GenericFeatureConfig;
import xyz.lilyflower.lotweakr.util.config.IntegrationFeatureConfig;
import xyz.lilyflower.lotweakr.util.config.TravelFeatureConfig;
import xyz.lilyflower.lotweakr.util.debug.LTRDebuggerCommand;

@Mod(modid = LOTweakR.MODID, version = LOTweakR.VERSION, dependencies = "required-after:lotr;after:Thaumcraft")
public class LOTweakR
{
    public static final String MODID = "lotweakr";
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger("LOTweakR");

    private static File getFeatureConfig(String base, String feature) {
        return new File(base + "/lotweakr/" + feature + ".cfg");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        String baseConfigPath = event.getSuggestedConfigurationFile().getParent();

        GenericFeatureConfig.synchronizeConfiguration(getFeatureConfig(baseConfigPath, "misc"));
        CombatFeatureConfig.synchronizeConfiguration(getFeatureConfig(baseConfigPath, "combat"));
        TravelFeatureConfig.synchronizeConfiguration(getFeatureConfig(baseConfigPath, "travel"));
        IntegrationFeatureConfig.synchronizeConfiguration(getFeatureConfig(baseConfigPath, "integration"));

        LOTRTime.DAY_LENGTH = (int) (LOTRTime.DAY_LENGTH * GenericFeatureConfig.TIME_MULTIPLIER);

        LTRCustomDataLoader.runAll();
        LTRIntegrationLoader.runAllPre();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (GenericFeatureConfig.FIX_ORE_DICTIONARY) {
            OreDictionary.registerOre("oreMithril", LOTRMod.oreMithril);
            OreDictionary.registerOre("oreMythril", LOTRMod.oreMithril);
            OreDictionary.registerOre("ingotMithril", LOTRMod.mithril);
            OreDictionary.registerOre("ingotMythril", LOTRMod.mithril);
            OreDictionary.registerOre("nuggetMithril", LOTRMod.mithrilNugget);
            OreDictionary.registerOre("nuggetMythril", LOTRMod.mithrilNugget);
        }

        LTRIntegrationLoader.runAll();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LTRIntegrationLoader.runAllPost();
        CombatFeatureConfig.registerModdedWeapons();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new LTRDebuggerCommand());
    }
}
