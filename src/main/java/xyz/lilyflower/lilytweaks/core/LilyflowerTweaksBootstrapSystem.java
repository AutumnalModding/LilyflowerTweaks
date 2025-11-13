package xyz.lilyflower.lilytweaks.core;

import java.net.URL;
import java.io.File;
import java.util.Set;
import java.util.List;
import org.reflections.Reflections;
import java.net.URISyntaxException;
import java.lang.reflect.Constructor;
import org.apache.logging.log4j.Logger;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.apache.logging.log4j.LogManager;
import java.lang.instrument.Instrumentation;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.launchwrapper.ITweaker;
import cpw.mods.fml.relauncher.CoreModManager;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraft.launchwrapper.LaunchClassLoader;
import xyz.lilyflower.lilytweaks.core.settings.TransformerSettingsModule;
import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;

@SuppressWarnings("unused") // How early can we go?
public class LilyflowerTweaksBootstrapSystem implements ITweaker {
    public static final Logger LOGGER = LogManager.getLogger("Lilyflower Tweaks Bootstrap System");

    // The text engine calls this `whoSetUsUpTheBomb`.
    // Speaking of, that reminds me. TODO: Port text engine.
    public static void ohno(String message, Throwable cause) {
        LOGGER.fatal("/// CRITICAL CRITICAL CRITICAL ///");
        LOGGER.fatal(message);
        LOGGER.fatal("CAUSE: {}", cause.getMessage());
        LOGGER.fatal("EXCEPTION CLASS: {}", cause.getClass().getSimpleName());
        LOGGER.fatal("DUMPING STACKTRACE...");
        for (StackTraceElement element : cause.getStackTrace()) {
            LOGGER.fatal(element.toString());
        }
        LOGGER.fatal("/// CRITICAL CRITICAL CRITICAL ///");
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
        if (location == null) return;
        if (!"file".equals(location.getProtocol())) return;
        try {
            MixinBootstrap.getPlatform().addContainer(location.toURI());
            String mod = new File(location.toURI()).getName();
            CoreModManager.getReparseableCoremods().add(mod);
        } catch (URISyntaxException exception) {
            ohno("FAILED TO LOAD COREMOD", exception);
            FMLCommonHandler.instance().exitJava(400, true);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launch) {
        // You either die a FakeTransformerExclusions...
        // ...or live long enough to see yourself an LCL call.
        launch.addTransformerExclusion("com.unascribed.ears");
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

    static {
        LOGGER.info("Spinning up...");
        Instrumentation agent = ByteBuddyAgent.install();
        agent.addTransformer(new LilyflowerTweaksTransformerLoadingSystem(), true);

        String name = ManagementFactory.getRuntimeMXBean().getName();
        long pid = Long.parseLong(name.split("@")[0]);
        LOGGER.info("Process ID: {}", pid);

        Reflections reflections = new Reflections("xyz.lilyflower.lilytweaks.core.settings.modules");
        Set<Class<? extends TransformerSettingsModule>> runners = reflections.getSubTypesOf(TransformerSettingsModule.class);
        runners.forEach(runner -> {
            try {
                Constructor<? extends TransformerSettingsModule> constructor = runner.getConstructor();
                TransformerSettingsModule instance = constructor.newInstance();
                LOGGER.info("Registering settings runner {}...", instance.getClass().getSimpleName());
                instance.init();
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
                LOGGER.fatal("Failed to load transformer settings class {}! Reason: {}", runner.getCanonicalName(), exception.getMessage());
                throw new BootstrapSetupFailedError(exception.getMessage());
            }
        });

        File dir = new File("config/");
        if (dir.exists() || dir.mkdir()) {
            LilyflowerTweaksTransformerSettingsSystem.load(new File("config/lilytweaks-early.cfg"));
        }
    }

}
