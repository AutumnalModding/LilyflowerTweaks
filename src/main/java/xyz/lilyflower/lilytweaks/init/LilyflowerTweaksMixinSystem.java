package xyz.lilyflower.lilytweaks.init;

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
import xyz.lilyflower.lilytweaks.util.FileUtils;
import xyz.lilyflower.lilytweaks.debug.LoggingHelper;

@SuppressWarnings({"deprecation", ""})
public class LilyflowerTweaksMixinSystem implements IMixinConfigPlugin {
    public static final Logger LOGGER = LogManager.getLogger("Lilyflower Tweaks Mixin System");
    private static final Path MODS_DIRECTORY_PATH = new File(Launch.minecraftHome, "mods/").toPath();

    @Override
    public void onLoad(String location) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String target, String mixin) { return true; }

    @Override
    public void acceptTargets(Set<String> ours, Set<String> theirs) {}

    @Override
    public List<String> getMixins() {
        final boolean isDevelopmentEnvironment = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        List<MixinTarget> loadedMods = Arrays.stream(MixinTarget.values())
                .filter(target -> target == MixinTarget.VANILLA
                        || (target.development && isDevelopmentEnvironment)
                        || load(target))
                .collect(Collectors.toList());

        for (MixinTarget target : MixinTarget.values()) {
            if(loadedMods.contains(target)) {
                LOGGER.info("Found {}! Integrating now...", target.name);
            }
            else {
                LOGGER.info("Could not find {}! Skipping integration....", target.name);
            }
        }

        List<String> mixins = new ArrayList<>();
        for (MixinRegistry mixin : MixinRegistry.values()) {
            if (mixin.shouldLoad(loadedMods)) {
                mixins.add(mixin.mixin);
                LOGGER.debug("Loading mixin: {}", mixin.mixin);
            }
        }
        return mixins;
    }

    private boolean load(final MixinTarget target) {
        try {
            File jar = locate(target);
            if(jar == null) {
                LOGGER.info("Jar not found for {}", target);
                return false;
            }

            LOGGER.info("Attempting to add {} to the URL Class Path", jar);
            if(!jar.exists()) {
                throw new FileNotFoundException(jar.toString());
            }
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

    @Override
    public void preApply(String target, ClassNode clazz, String mixin, IMixinInfo info) {

    }

    @Override
    public void postApply(String target, ClassNode clazz, String mixin, IMixinInfo info) {

    }

    public enum MixinRegistry {
        // LOTR
        COSMETIC_UNLOCKER("lotr.misc.CosmeticUnlocker", MixinTarget.LOTR),
        OMNITARGET_HELPER("lotr.entity.OmnitargetHelper", MixinTarget.LOTR),
        INVASION_ENUM_FIXER("lotr.misc.InvasionEnumFixer", MixinTarget.LOTR),
        SHUT_THE_FUCK_UP("lotr.misc.BiomeVariantShutterUpper", MixinTarget.LOTR),
        ATTACK_TIMINGS_SERVER("lotr.entity.ServerSideAttackTimingsRemoval", MixinTarget.LOTR),
        FACTION_RELATION_OVERRIDES("lotr.entity.RelationOverrideController", MixinTarget.LOTR),
        WAYPOINT_OVERRIDES("lotr.travel.FastTravelWaypointOverrideController", MixinTarget.LOTR),
        REFLECTION_COMPATIBILITY_PATCHES("lotr.bug.LOTRReflectionCompatibilityPatches", MixinTarget.LOTR),
        RENDER_SCRAP_TRADERS_PROPERLY("lotr.client.FixScrapTraderRenderer", Side.CLIENT, MixinTarget.LOTR),
        ATTACK_TIMINGS_CLIENT("lotr.client.ClientSideAttackTimingsRemoval", Side.CLIENT, MixinTarget.LOTR),
        ENTITY_RENDERER_PATCH("lotr.bug.EntityRendererPatch", Side.CLIENT, MixinTarget.LOTR, MixinTarget.WITCHERY),

        // Interop,
        //SAFE_VAMPIRE_BIOMES("lotr.interop.witchery.SafeVampireBiomes", MixinTarget.LOTR, MixinTarget.WITCHERY),

        // RPLE mixins - usually these get merged to upstream quickly
        RPLE_OPENLIGHT("rple.RPLEOpenLight", MixinTarget.RPLE, MixinTarget.OPENLIGHTS),

        // Witchery
        FIX_VAMPIRE_RITUAL("witchery.FixVampireRitual", MixinTarget.WITCHERY),
        CAP_VAMPIRE("witchery.entity.DamageCapRemover$VampireDTCapRemover", MixinTarget.WITCHERY),
        DAMAGE_CAP_REMOVER("witchery.entity.DamageCapRemover$RegularCapRemover", MixinTarget.WITCHERY),
        CAP_MOG_GULG("witchery.entity.DamageCapRemover$WhyAreYouTwoSpecialDamnit", MixinTarget.WITCHERY),

        // Backhand
        FAKE_PLAYER_COMPAT("backhand.FakePlayerCompat", MixinTarget.BACKHAND),

        // Vanilla
        REMOVE_IFRAMES("vanilla.RemoveImmunityFrames", MixinTarget.VANILLA),

        // Bandaids
        FIX_NULL_ENTITY_MAP("bandaid.FixNullEntityMap", MixinTarget.VANILLA),
        DISABLE_SNOW_UPDATES("bandaid.DisableSnowUpdates", MixinTarget.VANILLA),
        DISABLE_WORLDGEN_SPAWNING("bandaid.DisableWorldgenSpawning", MixinTarget.VANILLA),

        // Alfheim
        DISABLE_DIMTP("alfheim.DisableTPDIM", MixinTarget.ALFHEIM),
        DISABLE_FLIGHT("alfheim.ESMFlightDisabler", MixinTarget.ALFHEIM),
        ESM_TELEPORT_REWIRE("alfheim.ESMTeleportRewire", MixinTarget.ALFHEIM),
        ENABLE_BOSS_TIMESTOP("alfheim.EnableBossTimestop", MixinTarget.ALFHEIM),

        // Advanced Rocketry
        PROPERTY_ENHANCEMENTS("advrocketry.ARPlanetDataHelper", MixinTarget.ADVANCED_ROCKETRY),
        LOADER_ENHANCEMENTS("advrocketry.ARPlanetLoaderEnhancements", MixinTarget.ADVANCED_ROCKETRY),

        // Vic's Modern Warfare
        DISABLE_ZOMBIES("vics.DisableVicsMobs", MixinTarget.VICS_MW),
        FIX_TEXTURE_NAMES("vics.FixBadTextureNames", MixinTarget.VICS_MW),

        // Galacticraft
        DISABLE_BODIES("galacticraft.CelestialBodyDisabler", MixinTarget.GALACTICRAFT),
        DISABLE_UNREACHABLE_PLANETS("galacticraft.DisableUnreachablePlanets", MixinTarget.GALACTICRAFT),
        DISABLE_MAKING_UNREACHABLE_BODIES("galacticraft.DisableUnreachablePlanets$DisableMakingBodiesUnreachable", MixinTarget.GALACTICRAFT),

        // QuiverBow
        FIX_SOUL_CAIRN_STUPIDITY("quiverbow.SoulCairnDeStupidifier", MixinTarget.QUIVERBOW)
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
            final String location = path.toString();
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
