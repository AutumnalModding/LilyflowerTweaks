package xyz.lilyflower.lilytweaks.core;

import cpw.mods.fml.relauncher.CoreModManager;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.spongepowered.asm.launch.MixinBootstrap;
import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;
import xyz.lilyflower.lilytweaks.core.settings.SettingsRunner;

// How early can we go?
@SuppressWarnings("unused")
public class LilyflowerTweaksBootstrapSystem implements ITweaker {
    public static final Logger LOGGER = LogManager.getLogger("Lilyflower Tweaks Bootstrap System");

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
            exception.printStackTrace();
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launch) {}

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

        Reflections reflections = new Reflections("xyz.lilyflower.bootstrap.settings.runners");
        Set<Class<? extends SettingsRunner>> runners = reflections.getSubTypesOf(SettingsRunner.class);
        runners.forEach(runner -> {
            try {
                Constructor<? extends SettingsRunner> constructor = runner.getConstructor();
                SettingsRunner instance = constructor.newInstance();
                LOGGER.info("Launching settings runner {}", instance.getClass().getSimpleName());
                instance.init();
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
                LOGGER.fatal("Failed to load transformer settings class {}! Reason: {}", runner.getCanonicalName(), exception.getMessage());
                throw new BootstrapMethodError(exception); // Do not catch this.
            }
        });

        new File("config/").mkdir();
        LilyflowerTweaksTransformerSettingsSystem.synchronizeConfiguration(new File("config/lilytweaks-early.cfg"));
    }

}
