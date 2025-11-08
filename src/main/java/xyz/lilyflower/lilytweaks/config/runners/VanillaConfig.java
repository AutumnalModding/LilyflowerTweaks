package xyz.lilyflower.lilytweaks.config.runners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

@SuppressWarnings("unused")
public final class VanillaConfig implements ConfigRunner {
    public static final Consumer<Configuration> COMBAT_TWEAKS = configuration -> {
        LilyflowerTweaksConfigSystem.NO_IFRAME_DAMAGETYPES = new ArrayList<>(Arrays.asList(configuration.getStringList("noImmunityDamageTypes", "vanilla.damage", new String[]{},
                "List of damage sources for which iframes aren't applied."
        )));

        LilyflowerTweaksConfigSystem.NO_IFRAME_PROJECTILES = configuration.getBoolean("noImmunityForProjectiles", "vanilla.damage", false, "Make projectiles ignore iframes.");
    };

    public static final Consumer<Configuration> BANDAID_FIXES = configuration -> {
        LilyflowerTweaksConfigSystem.DISABLE_SNOW_UPDATES = configuration.getBoolean("disableSnowUpdates", "bandaid", false, "Disables snow sheet blocks from sending neighbour updates.\nCan stop StackOverflowExceptions in some cases.");
        LilyflowerTweaksConfigSystem.DISABLE_WORLDGEN_SPAWNING = configuration.getBoolean("disableWorldgenSpawning", "bandaid", false, "Disables animals spawning during worldgen.\nCan fix 'this.entitiesByUuid is null' crashes during world creation.");
    };

    public void init() {
        LilyflowerTweaksConfigSystem.add("lilytweaks", COMBAT_TWEAKS);
        LilyflowerTweaksConfigSystem.add("lilytweaks", BANDAID_FIXES);
    }
}
