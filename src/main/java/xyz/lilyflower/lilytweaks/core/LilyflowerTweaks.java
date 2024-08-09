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
import xyz.lilyflower.lilytweaks.util.config.alfheim.AlfheimMiscConfig;
import xyz.lilyflower.lilytweaks.util.config.combat.GenericCombatFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.combat.LOTRCombatFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.generic.BandaidFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.generic.LOTRGenericFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.interop.LOTRIntegrationFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.lotr.LOTRTravelFeatureConfig;
import xyz.lilyflower.lilytweaks.util.config.opencomputers.OpencomputersMiscFeatureConfig;
import xyz.lilyflower.lilytweaks.util.lotr.loader.LOTRCustomDataLoader;
import xyz.lilyflower.lilytweaks.util.lotr.debug.LTRDebuggerCommand;

@Mod(modid = LilyflowerTweaks.MODID, version = LilyflowerTweaks.VERSION, dependencies = "after:lotr;after:Thaumcraft")
public class LilyflowerTweaks
{
    public static final String MODID = "lilytweaks";
    public static final String VERSION = "2.1";

    public static final Logger LOGGER = LogManager.getLogger("LilyflowerTweaks");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File config = event.getSuggestedConfigurationFile();

        if (Loader.isModLoaded("lotr")) {
            LOTRGenericFeatureConfig.synchronizeConfiguration(config);
            LOTRCombatFeatureConfig.synchronizeConfiguration(config);
            LOTRTravelFeatureConfig.synchronizeConfiguration(config);
            LOTRIntegrationFeatureConfig.synchronizeConfiguration(config);

            LOTRTime.DAY_LENGTH = (int) (LOTRGenericFeatureConfig.TIME_BASE * LOTRGenericFeatureConfig.TIME_MULTIPLIER);

            LOTRCustomDataLoader.runAll();
        }

        BandaidFeatureConfig.synchronizeConfiguration(config);
        GenericCombatFeatureConfig.synchronizeConfiguration(config);

        if (Loader.isModLoaded("OpenComputers")) {
            OpencomputersMiscFeatureConfig.synchronizeConfiguration(config);
        }

        if (Loader.isModLoaded("alfheim")) {
            AlfheimMiscConfig.synchronizeConfiguration(config);
        }

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
