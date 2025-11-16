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
import java.util.List;
import lotr.common.LOTRMod;
import lotr.common.LOTRTime;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.lilyflower.lilytweaks.api.LilyflowerTweaksIntegrationModule;
import xyz.lilyflower.lilytweaks.api.ConfigurationModule;
import xyz.lilyflower.lilytweaks.configuration.LilyflowerTweaksGameConfigurationSystem;
import xyz.lilyflower.lilytweaks.configuration.modules.CustomContentAdditionsConfiguration;
import xyz.lilyflower.lilytweaks.configuration.modules.GalacticraftIntegrationConfiguration;
import xyz.lilyflower.lilytweaks.configuration.modules.LOTRModIntegrationConfiguration;
import xyz.lilyflower.lilytweaks.content.LilyflowerTweaksContentSystem;
import xyz.lilyflower.lilytweaks.command.LTRDebuggerCommand;
import xyz.lilyflower.lilytweaks.integration.GalacticraftIntegration;
import xyz.lilyflower.lilytweaks.api.CustomDataLoader;
import xyz.lilyflower.lilytweaks.internal.bwiama.PlanetSetup;
import xyz.lilyflower.lilytweaks.internal.bwiama.ServerInitializationHooks;
import xyz.lilyflower.lilytweaks.internal.bwiama.GCWorldProviderRegistrationHook;
import xyz.lilyflower.lilytweaks.util.ClasspathScanning;

@Mod(modid = LilyflowerTweaksInitializationSystem.MODID, version = LilyflowerTweaksInitializationSystem.VERSION, dependencies = "after:GalacticraftCore;after:lotr")
public class LilyflowerTweaksInitializationSystem {

    public static final String MODID = "lilytweaks";
    public static final String VERSION = "3.0";

    public static final Logger LOGGER = LogManager.getLogger("Lilyflower Tweaks");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        List<Class<ConfigurationModule>> modules = ClasspathScanning.GetAllImplementations(ConfigurationModule.class);
        modules.forEach(module -> {
            try {
                Constructor<? extends ConfigurationModule> constructor = module.getConstructor();
                ConfigurationModule instance = constructor.newInstance();
                LOGGER.info("Loading configuration module {}", module.getName());
                instance.init();
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
                LOGGER.fatal("Failed to load configuration module {}! Reason: {}", module.getName(), exception.getMessage());
                throw new RuntimeException(exception);
            }
        });

        List<Class<CustomDataLoader>> loaders = ClasspathScanning.GetAllImplementations(CustomDataLoader.class);
        loaders.forEach(loader -> {
            try {
                Constructor<? extends CustomDataLoader> constructor = loader.getConstructor();
                CustomDataLoader instance = constructor.newInstance();
                LOGGER.info("Executing data loader {}", loader.getName());
                instance.run();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException exception) {
                LOGGER.error("Failed to run custom data loader {}! Reason: {}", loader.getName(), exception.getMessage());
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

        LilyflowerTweaksIntegrationModule.add(new PlanetSetup(), CustomContentAdditionsConfiguration.MODPACK_IDENTIFIER.equals("bwiama"));
        LilyflowerTweaksIntegrationModule.add(new GalacticraftIntegration(), !GalacticraftIntegrationConfiguration.MODDED_PLANET_INTEGRATION.isEmpty());
        LilyflowerTweaksIntegrationModule.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("lotr")) {
            LOTRModIntegrationConfiguration.registerModdedWeapons();
            LOTRTime.DAY_LENGTH = (int) (LOTRModIntegrationConfiguration.TIME_BASE * LOTRModIntegrationConfiguration.TIME_MULTIPLIER);
        }

        LilyflowerTweaksIntegrationModule.add(new GCWorldProviderRegistrationHook(), CustomContentAdditionsConfiguration.MODPACK_IDENTIFIER.equals("bwiama"));
        LilyflowerTweaksIntegrationModule.init(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Loader.isModLoaded("lotr")) {
            event.registerServerCommand(new LTRDebuggerCommand());
        }

        LilyflowerTweaksIntegrationModule.add(new ServerInitializationHooks(), CustomContentAdditionsConfiguration.MODPACK_IDENTIFIER.equals("bwiama"));
        LilyflowerTweaksIntegrationModule.init(event);
    }
}
