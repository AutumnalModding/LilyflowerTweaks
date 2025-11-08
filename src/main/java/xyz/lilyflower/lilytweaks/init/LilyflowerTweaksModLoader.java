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
import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;
import xyz.lilyflower.lilytweaks.debug.LTRDebuggerCommand;
import xyz.lilyflower.lilytweaks.util.loader.CustomDataLoader;
import org.reflections.Reflections;
import java.util.Set;

@Mod(modid = LilyflowerTweaksModLoader.MODID, version = LilyflowerTweaksModLoader.VERSION, dependencies = "before:lotr")
public class LilyflowerTweaksModLoader {
    private static final Reflections DATA = new Reflections("xyz.lilyflower.lilytweaks.util.loader");
    private static final Reflections CONFIGURATION = new Reflections("xyz.lilyflower.lilytweaks.config.runners");

    public static final String MODID = "lilytweaks";
    public static final String VERSION = "2.1";

    public static final Logger LOGGER = LogManager.getLogger("LilyflowerTweaks");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        IntegrationLoader.runAllPre();

        Set<Class<? extends ConfigRunner>> configs = CONFIGURATION.getSubTypesOf(ConfigRunner.class);
        configs.forEach(config -> {
            try {
                Constructor<? extends ConfigRunner> constructor = config.getConstructor();
                ConfigRunner runner = constructor.newInstance();
                LOGGER.info("Loading config runner {}", runner.getClass().getSimpleName());
                runner.init();
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
                LOGGER.fatal("Failed to load config class {}! Reason: {}", config.getCanonicalName(), exception.getMessage());
                throw new RuntimeException(exception);
            }
        });

        Set<Class<? extends CustomDataLoader>> loaders = DATA.getSubTypesOf(CustomDataLoader.class);
        loaders.forEach(data -> {
            try {
                Constructor<? extends CustomDataLoader> constructor = data.getConstructor();
                CustomDataLoader loader = constructor.newInstance();
                loader.run();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException exception) {
                LOGGER.error("Failed to run custom data loader {}! Reason: {}", data.getCanonicalName(), exception.getMessage());
            } catch (NoClassDefFoundError ignored) {}
        });

        LilyflowerTweaksConfigSystem.synchronizeConfiguration(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (LilyflowerTweaksConfigSystem.FIX_ORE_DICTIONARY && Loader.isModLoaded("lotr")) {
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
            LilyflowerTweaksConfigSystem.registerModdedWeapons();
            LOTRTime.DAY_LENGTH = (int) (LilyflowerTweaksConfigSystem.TIME_BASE * LilyflowerTweaksConfigSystem.TIME_MULTIPLIER);
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (Loader.isModLoaded("lotr")) {
            event.registerServerCommand(new LTRDebuggerCommand());
        }
    }
}
