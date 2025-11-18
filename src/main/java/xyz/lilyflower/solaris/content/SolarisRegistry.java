package xyz.lilyflower.solaris.content;

import cpw.mods.fml.common.event.FMLStateEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.lilyflower.solaris.api.ContentRegistry;
import xyz.lilyflower.solaris.util.ClasspathScanning;
import xyz.lilyflower.solaris.util.FifteenthCompetingStandard;

public class SolarisRegistry {
    public static final Logger LOGGER = LogManager.getLogger("Solaris Registry");

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void initialize(FMLStateEvent phase) {
        List<Class<ContentRegistry>> REGISTRIES = ClasspathScanning.GetAllImplementations(ContentRegistry.class);
        for (Class<? extends ContentRegistry> clazz : REGISTRIES) {
            try {
                LOGGER.info("Found content registry {}, attempting to load it!", clazz.getName());
                Constructor<? extends ContentRegistry> constructor = clazz.getConstructor();
                ContentRegistry registry = constructor.newInstance();
                if (registry.shouldRun(phase)) {
                    registry.contents().forEach(pair -> {
                        FifteenthCompetingStandard.Pair<?, String> content = (FifteenthCompetingStandard.Pair<?, String>) pair;
                        if (registry.shouldRegister(content.right())) {
                            registry.register(content);
                            LOGGER.debug("Registering key {} on {}", content.right(), clazz.getName());
                        }
                    });
                }
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
                LOGGER.fatal("!!! WARNING WARNING WARNING !!!");
                LOGGER.fatal("FAILED TO LOAD REGISTRY: {}!", clazz.getName());
                LOGGER.fatal("LOADING MAY BE UNSTABLE, PROCEED AT YOUR OWN RISK");
                LOGGER.fatal("DUMPING STACKTRACE TO LOGS:");
                for (StackTraceElement element : exception.getStackTrace()) {
                    LOGGER.fatal(element.toString());
                }
                LOGGER.fatal("!!! WARNING WARNING WARNING !!!");
            }
        }
    }
}
