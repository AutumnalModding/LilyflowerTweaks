package xyz.lilyflower.solaris.init;

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
import xyz.lilyflower.solaris.api.SolarisIntegrationModule;
import xyz.lilyflower.solaris.api.ConfigurationModule;
import xyz.lilyflower.solaris.configuration.SolarisConfigurationLoader;
import xyz.lilyflower.solaris.configuration.modules.SolarisContent;
import xyz.lilyflower.solaris.configuration.modules.SolarisGalacticraft;
import xyz.lilyflower.solaris.configuration.modules.SolarisLOTR;
import xyz.lilyflower.solaris.content.SolarisRegistry;
import xyz.lilyflower.solaris.command.LTRDebuggerCommand;
import xyz.lilyflower.solaris.integration.galacticraft.PlanetRegistrationHook;
import xyz.lilyflower.solaris.api.CustomDataLoader;
import xyz.lilyflower.solaris.internal.illumos.PlanetSetup;
import xyz.lilyflower.solaris.util.ClasspathScanning;

@Mod(modid = Solaris.MODID, version = Solaris.VERSION, dependencies = "before:GalacticraftCore;after:lotr")
public class Solaris {

    public static final String MODID = "solaris";
    public static final String VERSION = "3.0";

    public static final Logger LOGGER = LogManager.getLogger("Solaris");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        List<Class<ConfigurationModule>> modules = ClasspathScanning.interfaces(ConfigurationModule.class);
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

        List<Class<CustomDataLoader>> loaders = ClasspathScanning.interfaces(CustomDataLoader.class);
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

        SolarisConfigurationLoader.load(event.getSuggestedConfigurationFile());
        SolarisRegistry.initialize(event);
        SolarisIntegrationModule.init(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (SolarisLOTR.FIX_ORE_DICTIONARY && Loader.isModLoaded("lotr")) {
            OreDictionary.registerOre("dustSulfur", LOTRMod.sulfur);
            OreDictionary.registerOre("ingotMithril", LOTRMod.mithril);
            OreDictionary.registerOre("oreMithril", LOTRMod.oreMithril);
            OreDictionary.registerOre("nuggetMithril", LOTRMod.mithrilNugget);
        }

        SolarisIntegrationModule.add(new PlanetSetup(), SolarisContent.MODPACK_IDENTIFIER.equals("solaris"));
        SolarisIntegrationModule.add(new PlanetRegistrationHook(), !SolarisGalacticraft.MODDED_PLANET_INTEGRATION.isEmpty());
        SolarisIntegrationModule.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("lotr")) {
            SolarisLOTR.registerModdedWeapons();
            LOTRTime.DAY_LENGTH = (int) (SolarisLOTR.TIME_BASE * SolarisLOTR.TIME_MULTIPLIER);
        }

        SolarisIntegrationModule.init(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Loader.isModLoaded("lotr")) {
            event.registerServerCommand(new LTRDebuggerCommand());
        }

        SolarisIntegrationModule.init(event);
    }
}
