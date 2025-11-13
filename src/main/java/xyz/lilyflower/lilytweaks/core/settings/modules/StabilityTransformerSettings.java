package xyz.lilyflower.lilytweaks.core.settings.modules;

import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;
import xyz.lilyflower.lilytweaks.core.settings.TransformerSettingsModule;

@SuppressWarnings("unused")
public class StabilityTransformerSettings implements TransformerSettingsModule {
    @Override
    public void init() {
        LilyflowerTweaksTransformerSettingsSystem.add("stability", configuration -> {
            LilyflowerTweaksTransformerSettingsSystem.Stability.GROSS_REGISTRY_HACKS = configuration.getBoolean("grossRegistryHacks", "stability", false, "Turns on gross registry hacks. Don't touch this if you don't know what you're doing.");
            LilyflowerTweaksTransformerSettingsSystem.Stability.STABILITY_OVERRIDES = configuration.getBoolean("overrideStabilityChecks", "stability", false, "Overrides all stability checks. Unless you're absolutely sure, do not enable this.");
            LilyflowerTweaksTransformerSettingsSystem.Stability.DISABLE_OPENCOMPUTERS_ROBOTS = configuration.getBoolean("disableOCRobots", "stability", false, "Disables OpenComputers robots. Enable this if you're using Beddium and are crashing.");
        });
    }
}
