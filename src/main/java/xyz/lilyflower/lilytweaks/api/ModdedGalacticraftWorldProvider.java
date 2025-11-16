package xyz.lilyflower.lilytweaks.api;

import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;

public interface ModdedGalacticraftWorldProvider extends IGalacticraftWorldProvider, IExitHeight, ISolarLevel {
    @Override
    default double getYCoordinateToTeleport() {
        return 800.0;
    }

    @Override
    default float getGravity() {
        return 1.0F;
    }

    @Override
    default double getMeteorFrequency() {
        return 0.0D;
    }

    @Override
    default double getFuelUsageMultiplier() {
        return 1.0D;
    }

    @Override
    default boolean canSpaceshipTierPass(int tier) {
        return tier >= this.getCelestialBody().getTierRequirement();
    }

    @Override
    default float getFallDamageModifier() {
        return this.getGravity();
    }

    @Override
    default float getSoundVolReductionAmount() {
        return this.hasBreathableAtmosphere() ? 0.0F : 10.0F;
    }

    @Override
    default boolean hasBreathableAtmosphere() {
        return this.getCelestialBody().atmosphere.contains(IAtmosphericGas.OXYGEN);
    }

    @Override
    default boolean netherPortalsOperational() {
        return false;
    }

    @Override
    default boolean isGasPresent(IAtmosphericGas gas) {
        return this.getCelestialBody().atmosphere.contains(gas);
    }

    @Override
    default float getThermalLevelModifier() {
        return 0.0f;
    }

    @Override
    default float getWindLevel() {
        return 1.0f;
    }

    @Override
    default float getSolarSize() {
        return 1.0f;
    }

    @Override
    default double getSolarEnergyMultiplier() {
        return 1.0d;
    }
}
