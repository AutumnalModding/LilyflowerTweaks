package xyz.lilyflower.lilytweaks.configuration.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import xyz.lilyflower.lilytweaks.configuration.ConfigurationModule;
import xyz.lilyflower.lilytweaks.configuration.LilyflowerTweaksGameConfigurationSystem;

public class GalacticraftIntegrationConfiguration implements ConfigurationModule {
    public static boolean DISABLE_UNREACHABLE_PLANETS = false;
    public static List<String > DISABLED_CELESTIAL_BODIES = new ArrayList<>();
    public static List<String> MODDED_PLANET_INTEGRATION = new ArrayList<>();

    @Override
    public void init() {
        LilyflowerTweaksGameConfigurationSystem.add("GalacticraftCore", configuration -> {
            DISABLE_UNREACHABLE_PLANETS = configuration.getBoolean("disableUnreachablePlanets", "galacticraft", false, "Disables the creation of unreachable planets. Useful to avoid clutter.");
            MODDED_PLANET_INTEGRATION = Arrays.asList(configuration.getStringList("additionalModdedPlanets", "galacticraft", new String[]{}, "List of mods to register Galacticraft integration for."));
            DISABLED_CELESTIAL_BODIES = Arrays.asList(configuration.getStringList("disabledBodies", "galacticraft", new String[]{}, "List of celestial body IDs to disable. Works for moons, satellites, planets, and solar systems."));
        });
    }
}
