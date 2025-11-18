package xyz.lilyflower.solaris.util.data;

import zmaster587.advancedRocketry.api.IAtmosphere;

public interface AREnhancedPlanetProperties {
    IAtmosphere getDefinedAtmosphere();
    String getAtmosphereName();

    void setAtmosphereDirect(IAtmosphere type);
    void setDefinedAtmosphere(String name);
}
