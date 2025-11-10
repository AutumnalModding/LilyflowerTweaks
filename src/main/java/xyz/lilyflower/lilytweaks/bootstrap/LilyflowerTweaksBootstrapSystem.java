package xyz.lilyflower.lilytweaks.bootstrap;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// How early can we go?
@SuppressWarnings("unused")
public class LilyflowerTweaksBootstrapSystem implements ITweaker {
    public static final Logger LOGGER = LogManager.getLogger("Lilyflower Tweaks Bootstrap System");

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {

    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader launch) {
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
        String name = ManagementFactory.getRuntimeMXBean().getName();
        long pid = Long.parseLong(name.split("@")[0]);
        LOGGER.info("Spinning up...");
        LOGGER.info("Process ID: {}", pid);
        LOGGER.info(LilyflowerTweaksBootstrapSystem.class.getClassLoader().getClass().getCanonicalName());
        System.exit(1);
    }
}
