package xyz.lilyflower.solaris.integration.galacticraft;

import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;

public interface PlanetProvider extends IGalacticraftWorldProvider, IExitHeight, ISolarLevel {
    @Override default float getGravity() { return 0.0F; }
    @Override default float getWindLevel() { return 1.0F; }
    @Override default float getSolarSize() { return 1.0F; }
    @Override default double getMeteorFrequency() { return 0.0; }
    @Override default float getFallDamageModifier() { return 1.0F; }
    @Override default double getFuelUsageMultiplier() { return 1.0; }
    @Override default float getThermalLevelModifier() { return 0.0F; }
    @Override default double getSolarEnergyMultiplier() { return 1.0; }
    @Override default double getYCoordinateToTeleport() {return 800.0; }
    @Override default boolean netherPortalsOperational() { return false; }
    @Override default float getSoundVolReductionAmount() { return this.hasBreathableAtmosphere() ? 0.0F : 10.0F; }
    @Override default boolean isGasPresent(IAtmosphericGas gas) { return this.getCelestialBody().atmosphere.contains(gas); }
    @Override default boolean canSpaceshipTierPass(int tier) { return tier >= this.getCelestialBody().getTierRequirement(); }
    @Override default boolean hasBreathableAtmosphere() { return this.getCelestialBody().atmosphere.contains(IAtmosphericGas.OXYGEN); }
}
