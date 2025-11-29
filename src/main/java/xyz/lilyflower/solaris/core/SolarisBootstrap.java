package xyz.lilyflower.solaris.core;

import java.lang.reflect.Method;
import java.net.URL;
import java.io.File;
import java.util.List;
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
import xyz.lilyflower.solaris.api.TransformerSettingsModule;
import xyz.lilyflower.solaris.core.settings.SolarisTransformerSettings;
import xyz.lilyflower.solaris.debug.LoggingHelper;
import xyz.lilyflower.solaris.util.ClasspathScanning;

@SuppressWarnings("unused") // How early can we go?
public class SolarisBootstrap implements ITweaker {
    public static final Logger LOGGER = LogManager.getLogger("Solaris Bootstrap");

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
            LoggingHelper.oopsie(LOGGER, "FAILED TO LOAD COREMOD", exception);
            FMLCommonHandler.instance().exitJava(400, true);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launch) {
        // You either die a FakeTransformerExclusions...
        // ...or live long enough to see yourself an LCL call.
        launch.addTransformerExclusion("com.unascribed.ears");
        SolarisTransformer.LOADER = launch;
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
        try {
            Instrumentation agent = ByteBuddyAgent.install();
            agent.addTransformer(new SolarisTransformer(), true);
            throw new ExceptionInInitializerError();
        } catch (ExceptionInInitializerError error) { // JNA /should/ work but it might not? unsure.
            LoggingHelper.oopsie(LOGGER, "FAILED TO INITIALIZE AGENT -- CRASHING! (Try running with a JDK!)", error);
            try { // we have to call directly because of FML shenanigans
                Class<?> internal = Class.forName("java.lang.Shutdown");
                Method halt = internal.getDeclaredMethod("halt0", int.class);
                halt.setAccessible(true);
                halt.invoke(null, 1); // <- maybe we change the exit code?
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException exception) {
                LOGGER.fatal("Failed to kill the JVM - you're on your own!");
            }
        }

        String name = ManagementFactory.getRuntimeMXBean().getName();
        long pid = Long.parseLong(name.split("@")[0]);
        LOGGER.info("Process ID: {}", pid);

        List<Class<TransformerSettingsModule>> modules = ClasspathScanning.implementations(TransformerSettingsModule.class, false);
        modules.forEach(module -> {
            try {
                Constructor<? extends TransformerSettingsModule> constructor = module.getConstructor();
                TransformerSettingsModule instance = constructor.newInstance();
                LOGGER.info("Registering transformer settings module {}...", instance.getClass().getSimpleName());
                instance.init();
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
                LoggingHelper.oopsie(LOGGER, "FAILED TO LOAD TRANSFORMER SETTINGS: " + module.getSimpleName(), exception);
            }
        });

        File dir = new File("config/");
        if (dir.exists() || dir.mkdir()) {
            SolarisTransformerSettings.load(new File("config/solaris-early.cfg"));
        }
    }

}
