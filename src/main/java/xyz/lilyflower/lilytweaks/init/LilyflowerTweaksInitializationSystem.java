package xyz.lilyflower.lilytweaks.init;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import lotr.common.LOTRMod;
import lotr.common.LOTRTime;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.lilyflower.lilytweaks.configuration.ConfigurationModule;
import xyz.lilyflower.lilytweaks.configuration.LilyflowerTweaksGameConfigurationSystem;
import xyz.lilyflower.lilytweaks.configuration.modules.GalacticraftIntegrationConfiguration;
import xyz.lilyflower.lilytweaks.configuration.modules.LOTRModIntegrationConfiguration;
import xyz.lilyflower.lilytweaks.content.LilyflowerTweaksContentSystem;
import xyz.lilyflower.lilytweaks.debug.LTRDebuggerCommand;
import xyz.lilyflower.lilytweaks.integration.GalacticraftIntegration;
import xyz.lilyflower.lilytweaks.util.loader.CustomDataLoader;
import org.reflections.Reflections;
import java.util.Set;

@Mod(modid = LilyflowerTweaksInitializationSystem.MODID, version = LilyflowerTweaksInitializationSystem.VERSION, dependencies = "after:GalacticraftCore;before:lotr")
public class LilyflowerTweaksInitializationSystem {
    private static final Reflections DATA = new Reflections("xyz.lilyflower.lilytweaks.util.loader");
    private static final Reflections CONFIGURATION = new Reflections("xyz.lilyflower.lilytweaks.configuration.modules");

    public static final String MODID = "lilytweaks";
    public static final String VERSION = "3.0";

    public static final Logger LOGGER = LogManager.getLogger("LilyflowerTweaks");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LilyflowerTweaksIntegrationModule.add(new GalacticraftIntegration(), !GalacticraftIntegrationConfiguration.MODDED_PLANET_INTEGRATION.isEmpty());

        Set<Class<? extends ConfigurationModule>> configs = CONFIGURATION.getSubTypesOf(ConfigurationModule.class);
        configs.forEach(config -> {
            try {
                Constructor<? extends ConfigurationModule> constructor = config.getConstructor();
                ConfigurationModule module = constructor.newInstance();
                LOGGER.info("Loading configuration module {}", config.getName());
                module.init();
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
                LOGGER.fatal("Failed to load configuration module {}! Reason: {}", config.getName(), exception.getMessage());
                throw new RuntimeException(exception);
            }
        });

        Set<Class<? extends CustomDataLoader>> loaders = DATA.getSubTypesOf(CustomDataLoader.class);
        loaders.forEach(data -> {
            try {
                Constructor<? extends CustomDataLoader> constructor = data.getConstructor();
                CustomDataLoader loader = constructor.newInstance();
                LOGGER.info("Executing data loader {}", data.getName());
                loader.run();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException exception) {
                LOGGER.error("Failed to run custom data loader {}! Reason: {}", data.getName(), exception.getMessage());
            } catch (NoClassDefFoundError ignored) {}
        });

        LilyflowerTweaksGameConfigurationSystem.load(event.getSuggestedConfigurationFile());
        LilyflowerTweaksContentSystem.initialize(event);
        LilyflowerTweaksIntegrationModule.init(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (LOTRModIntegrationConfiguration.FIX_ORE_DICTIONARY && Loader.isModLoaded("lotr")) {
            OreDictionary.registerOre("dustSulfur", LOTRMod.sulfur);
            OreDictionary.registerOre("ingotMithril", LOTRMod.mithril);
            OreDictionary.registerOre("oreMithril", LOTRMod.oreMithril);
            OreDictionary.registerOre("nuggetMithril", LOTRMod.mithrilNugget);
        }

        LilyflowerTweaksIntegrationModule.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("lotr")) {
            LOTRModIntegrationConfiguration.registerModdedWeapons();
            LOTRTime.DAY_LENGTH = (int) (LOTRModIntegrationConfiguration.TIME_BASE * LOTRModIntegrationConfiguration.TIME_MULTIPLIER);
        }

        LilyflowerTweaksIntegrationModule.init(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Loader.isModLoaded("lotr")) {
            event.registerServerCommand(new LTRDebuggerCommand());
        }

        LilyflowerTweaksIntegrationModule.init(event);
    }
}
