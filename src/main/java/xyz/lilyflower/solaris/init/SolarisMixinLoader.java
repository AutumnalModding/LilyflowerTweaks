package xyz.lilyflower.solaris.init;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import ru.timeconqueror.spongemixins.MinecraftURLClassPath;
import xyz.lilyflower.solaris.util.FileUtils;
import xyz.lilyflower.solaris.debug.LoggingHelper;

@SuppressWarnings({"deprecation", ""})
public class SolarisMixinLoader implements IMixinConfigPlugin {
    public static final Logger LOGGER = LogManager.getLogger("Solaris Mixins");
    private static final Path MODS_DIRECTORY_PATH = new File(Launch.minecraftHome, "mods/").toPath();

    @Override public void onLoad(String location) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> ours, Set<String> theirs) {}
    @Override public boolean shouldApplyMixin(String target, String mixin) { return true; }

    @Override
    public List<String> getMixins() {
        final boolean isDevelopmentEnvironment = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        List<MixinTarget> loaded = Arrays.stream(MixinTarget.values())
                .filter(target -> target == MixinTarget.VANILLA
                        || (target.development && isDevelopmentEnvironment)
                        || load(target))
                .collect(Collectors.toList());

        LOGGER.info("Available targets: ");
        for (MixinTarget target : MixinTarget.values()) {
            LOGGER.info("  [{}] {}", loaded.contains(target) ? "X" : " ", target.name);
        }

        List<String> mixins = new ArrayList<>();
        for (MixinRegistry mixin : MixinRegistry.values()) {
            if (mixin.shouldLoad(loaded)) {
                mixins.add(mixin.mixin);
                LOGGER.debug("Loading mixin: {}", mixin.mixin);
            }
        }
        return mixins;
    }

    private boolean load(final MixinTarget target) {
        try {
            File jar = locate(target);
            if (jar == null) return false;
            if (!jar.exists()) throw new FileNotFoundException(jar.toString());

            MinecraftURLClassPath.addJar(jar);
            return true;
        }
        catch (Exception exception) {
            LoggingHelper.oopsie(LOGGER, "FAILED LOADING MIXIN TARGET: " + target, exception);
            return false;
        }
    }

    @SuppressWarnings("resource")
    public static File locate(final MixinTarget target) {
        try {
            return Files.walk(MODS_DIRECTORY_PATH)
                    .filter(target::isMatchingJar)
                    .map(Path::toFile)
                    .findFirst()
                    .orElse(null);
        }
        catch (IOException exception) {
            LoggingHelper.oopsie(LOGGER, "FAILED LOCATING MIXIN TARGET: " + target, exception);
            return null;
        }
    }

    @Override public void preApply(String target, ClassNode clazz, String mixin, IMixinInfo info) {}
    @Override public void postApply(String target, ClassNode clazz, String mixin, IMixinInfo info) {}

    public enum MixinRegistry {
        SoulShot("quiverbow.SoulShotMixin", MixinTarget.QUIVERBOW),
        LOTRTickHandlerClient("lotr.bug.LOTRTickHandlerClientMixin", Side.CLIENT, MixinTarget.LOTR, MixinTarget.WITCHERY),

        ;

        public final String mixin;
        public final List<MixinTarget> targets;
        private final Side side;

        MixinRegistry(String mixin, Side side, MixinTarget... targets) {
            this.mixin = mixin;
            this.targets = Arrays.asList(targets);
            this.side = side;
        }

        MixinRegistry(String mixin, MixinTarget... targets) {
            this.mixin = mixin;
            this.targets = Arrays.asList(targets);
            this.side = Side.BOTH;
        }

        public boolean shouldLoad(List<MixinTarget> loaded) {
            return (side == Side.BOTH
                    || side == Side.SERVER && FMLLaunchHandler.side().isServer()
                    || side == Side.CLIENT && FMLLaunchHandler.side().isClient())
                    && new HashSet<>(loaded).containsAll(targets);
        }

        enum Side {
            BOTH,
            CLIENT,
            SERVER
        }
    }

    public enum MixinTarget {
        ALFHEIM("Alfheim", "Alfheim", false),
        WITCHERY("Witchery", "Witchery", true),
        BACKHAND("Backhand", "backhand", true),
        QUIVERBOW("Quiverbow", "QuiverBow", true),
        VICS_MW("Vic's Modern Warfare", "mw_", true),
        VANILLA("Vanilla Minecraft", "unused", true),
        OPENLIGHTS("Openlights", "OpenLights", false),
        ENDLESSIDS("EndlessIDs", "endlessids", false),
        GALACTICRAFT("Galacticraft", "Galacticraft", true),
        LOTR("The Lord of the Rings Mod", "LOTRMod", true),
        RPLE("Right Proper Lighting Engine", "rple", false),
        OPENCOMPUTERS("OpenComputers", "OpenComputers", false),
        ADVANCED_ROCKETRY("Advanced Rocketry", "AdvancedRocketry", true);

        public final String name;
        public final String prefix;
        public final boolean development;

        MixinTarget(String name, String prefix, boolean development) {
            this.name = name;
            this.prefix = prefix.toLowerCase();
            this.development = development;
        }

        public boolean isMatchingJar(Path path) {
            final String location = path.toString().replaceAll(".*mods/", "");
            final String basename = FileUtils.basename(location).toLowerCase();
            final String extension = FileUtils.extension(location);

            return basename.startsWith(prefix) && "jar".equals(extension);
        }

        @Override
        public String toString() {
            return "MixinTarget{" +
                    "modName='" + name + '\'' +
                    ", jarNamePrefixLowercase='" + prefix + '\'' +
                    '}';
        }
    }
}
