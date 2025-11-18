package xyz.lilyflower.solaris.core.settings.modules;

import xyz.lilyflower.solaris.core.settings.SolarisTransformerSettings;
import xyz.lilyflower.solaris.api.TransformerSettingsModule;

@SuppressWarnings("unused")
public class StabilityTransformerSettings implements TransformerSettingsModule {
    public static boolean STABILITY_OVERRIDES = false;
    public static boolean GROSS_REGISTRY_HACKS = false;
    public static boolean DISABLE_OPENCOMPUTERS_ROBOTS = false;

    @Override
    public void init() {
        SolarisTransformerSettings.add("stability", configuration -> {
            GROSS_REGISTRY_HACKS = configuration.getBoolean("grossRegistryHacks", "stability", false, "Turns on gross registry hacks. Don't touch this if you don't know what you're doing.");
            STABILITY_OVERRIDES = configuration.getBoolean("overrideStabilityChecks", "stability", false, "Overrides all stability checks. Unless you're absolutely sure, do not enable this.");
            DISABLE_OPENCOMPUTERS_ROBOTS = configuration.getBoolean("disableOCRobots", "stability", false, "Disables OpenComputers robots. Enable this if you're using Beddium and are crashing.");
        });
    }
}
