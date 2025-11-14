package xyz.lilyflower.lilytweaks.init;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.spongepowered.libraries.com.google.common.io.Files;
import ru.timeconqueror.spongemixins.MinecraftURLClassPath;

import static java.nio.file.Files.walk;

@SuppressWarnings({"deprecation", "CallToPrintStackTrace"})
public class LilyflowerTweaksMixinSystem implements IMixinConfigPlugin {
    public static final Logger LOGGER = LogManager.getLogger("Lilyflower Tweaks Mixin System");
    private static final Path MODS_DIRECTORY_PATH = new File(Launch.minecraftHome, "mods/").toPath();

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) { return true; }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        final boolean isDevelopmentEnvironment = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        List<MixinTarget> loadedMods = Arrays.stream(MixinTarget.values())
                .filter(mod -> mod == MixinTarget.VANILLA
                        || (mod.loadInDevelopment && isDevelopmentEnvironment)
                        || loadJarOf(mod))
                .collect(Collectors.toList());

        for (MixinTarget mod : MixinTarget.values()) {
            if(loadedMods.contains(mod)) {
                LOGGER.info("Found {}! Integrating now...", mod.modName);
            }
            else {
                LOGGER.info("Could not find {}! Skipping integration....", mod.modName);
            }
        }

        List<String> mixins = new ArrayList<>();
        for (MixinRegistry mixin : MixinRegistry.values()) {
            if (mixin.shouldLoad(loadedMods)) {
                mixins.add(mixin.mixinClass);
                LOGGER.debug("Loading mixin: {}", mixin.mixinClass);
            }
        }
        return mixins;
    }

    private boolean loadJarOf(final MixinTarget mod) {
        try {
            File jar = findJarOf(mod);
            if(jar == null) {
                LOGGER.info("Jar not found for {}", mod);
                return false;
            }

            LOGGER.info("Attempting to add {} to the URL Class Path", jar);
            if(!jar.exists()) {
                throw new FileNotFoundException(jar.toString());
            }
            MinecraftURLClassPath.addJar(jar);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("resource")
    public static File findJarOf(final MixinTarget mod) {
        try {
            return walk(MODS_DIRECTORY_PATH)
                    .filter(mod::isMatchingJar)
                    .map(Path::toFile)
                    .findFirst()
                    .orElse(null);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    public enum MixinRegistry {
        // LOTR

        COSMETIC_UNLOCKER("lotr.misc.CosmeticUnlocker", MixinTarget.LOTR),
        OMNITARGET_HELPER("lotr.entity.OmnitargetHelper", MixinTarget.LOTR),
        INVASION_ENUM_FIXER("lotr.misc.InvasionEnumFixer", MixinTarget.LOTR),
        ATTACK_TIMINGS_CLIENT("lotr.client.ClientSideAttackTimingsRemoval", MixinTarget.LOTR),
        ATTACK_TIMINGS_SERVER("lotr.entity.ServerSideAttackTimingsRemoval", MixinTarget.LOTR),
        RENDER_SCRAP_TRADERS_PROPERLY("lotr.client.FixScrapTraderRenderer", MixinTarget.LOTR),
        FACTION_RELATION_OVERRIDES("lotr.entity.RelationOverrideController", MixinTarget.LOTR),
        WAYPOINT_OVERRIDES("lotr.travel.FastTravelWaypointOverrideController", MixinTarget.LOTR),

        // Interop
        FIX_VAMPIRE_RITUAL("lotr.interop.witchery.FixVampireRitual", MixinTarget.WITCHERY),
        //SAFE_VAMPIRE_BIOMES("lotr.interop.witchery.SafeVampireBiomes", MixinTarget.LOTR, MixinTarget.WITCHERY),
        ENTITY_RENDERER_PATCH("lotr.interop.witchery.EntityRendererPatch", MixinTarget.LOTR, MixinTarget.WITCHERY),

        // RPLE mixins - usually these get merged to upstream quickly
        RPLE_OPENLIGHT("rple.RPLEOpenLight", MixinTarget.RPLE, MixinTarget.OPENLIGHTS),

        // Witchery
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

        // QuiverBow
        FIX_SOUL_CAIRN_STUPIDITY("quiverbow.SoulCairnDeStupidifier", MixinTarget.QUIVERBOW)
        ;

        public final String mixinClass;
        public final List<MixinTarget> targets;
        private final Side side;

        MixinRegistry(String mixinClass, Side side, MixinTarget... targets) {
            this.mixinClass = mixinClass;
            this.targets = Arrays.asList(targets);
            this.side = side;
        }

        MixinRegistry(String mixinClass, MixinTarget... targets) {
            this.mixinClass = mixinClass;
            this.targets = Arrays.asList(targets);
            this.side = Side.BOTH;
        }

        public boolean shouldLoad(List<MixinTarget> loadedMods) {
            return (side == Side.BOTH
                    || side == Side.SERVER && FMLLaunchHandler.side().isServer()
                    || side == Side.CLIENT && FMLLaunchHandler.side().isClient())
                    && new HashSet<>(loadedMods).containsAll(targets);
        }

        enum Side {
            BOTH,
            CLIENT,
            SERVER
        }
    }

    public enum MixinTarget {
        RPLE("rple", "rple", false),
        LOTR("The Lord of the Rings Mod", "LOTRMod", true),
        ALFHEIM("alfheim", "Alfheim", false),
        VANILLA("Minecraft", "unused", true),
        WITCHERY("Witchery", "Witchery", true),
        BACKHAND("backhand", "backhand", true),
        OPENCOMPUTERS("OpenComputers", "OpenComputers", true),
        OPENLIGHTS("openlights", "OpenLights", true),
        ENDLESSIDS("endlessids", "endlessids", false),
        ADVANCED_ROCKETRY("advancedRocketry", "AdvancedRocketry", true),
        VICS_MW("mw", "mw_", true),
        GALACTICRAFT("GalacticraftCore", "Galacticraft", true),
        QUIVERBOW("quiverchevsky", "QuiverBow", true);

        public final String modName;
        public final String jarNamePrefixLowercase;
        public final boolean loadInDevelopment;

        MixinTarget(String modName, String jarNamePrefix, boolean loadInDevelopment) {
            this.modName = modName;
            this.jarNamePrefixLowercase = jarNamePrefix.toLowerCase();
            this.loadInDevelopment = loadInDevelopment;
        }

        public boolean isMatchingJar(Path path) {
            final String pathString = path.toString();
            final String nameLowerCase = Files.getNameWithoutExtension(pathString).toLowerCase();
            final String fileExtension = Files.getFileExtension(pathString);

            return nameLowerCase.startsWith(jarNamePrefixLowercase) && "jar".equals(fileExtension);
        }

        @Override
        public String toString() {
            return "MixinTarget{" +
                    "modName='" + modName + '\'' +
                    ", jarNamePrefixLowercase='" + jarNamePrefixLowercase + '\'' +
                    '}';
        }
    }
}
