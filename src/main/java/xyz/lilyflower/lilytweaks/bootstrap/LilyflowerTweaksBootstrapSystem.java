package xyz.lilyflower.lilytweaks.bootstrap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.List;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.lang3.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// How early can we go?
@SuppressWarnings("unused")
public class LilyflowerTweaksBootstrapSystem implements ITweaker {
    public static final Logger LOGGER = LogManager.getLogger("Lilyflower Tweaks Bootstreap System");

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
        Instrumentation agent = ByteBuddyAgent.install();
        agent.addTransformer(new ClassDumper(), true);
    }

    public static class ClassDumper implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String name, Class<?> target, ProtectionDomain domain, byte[] bytes) {
            String readable = ClassUtils.getShortClassName(name).replaceAll("/", "∕");
            File clazz = new File("classes/" + readable.replaceAll("\\.", "$") + ".class");
            try (FileOutputStream output = new FileOutputStream(clazz)) {
                output.write(bytes);
            } catch (IOException ignored) {}
            return bytes;
        }
    }
}
