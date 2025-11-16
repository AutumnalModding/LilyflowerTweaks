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
import xyz.lilyflower.lilytweaks.api.TransformerSettingsModule;
import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;
import xyz.lilyflower.lilytweaks.debug.LoggingHelper;
import xyz.lilyflower.lilytweaks.util.ClasspathScanning;

@SuppressWarnings("unused") // How early can we go?
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
            LoggingHelper.oopsie(LOGGER, "FAILED TO LOAD COREMOD", exception);
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

        List<Class<TransformerSettingsModule>> modules = ClasspathScanning.GetAllImplementations(TransformerSettingsModule.class);
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
            LilyflowerTweaksTransformerSettingsSystem.load(new File("config/lilytweaks-early.cfg"));
        }
    }

}
