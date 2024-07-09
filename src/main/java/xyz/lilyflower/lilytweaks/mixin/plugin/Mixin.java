package xyz.lilyflower.lilytweaks.mixin.plugin;

import cpw.mods.fml.relauncher.FMLLaunchHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public enum Mixin {

    //
    // IMPORTANT: Do not make any references to any mod from this file. This file is loaded quite early on and if
    // you refer to other mods you load them as well. The consequence is: You can't inject any previously loaded classes!
    // Exception: Tags.java, as long as it is used for Strings only!
    //

    COSMETIC_UNLOCKER("lotr.misc.CosmeticUnlockerMixin", TargetedMod.LOTR),
    FT_WAYPOINT_OVERRIDES("lotr.travel.FastTravelWaypointOverrideController", TargetedMod.LOTR),
    OMNITARGET("lotr.entity.OmnitargetHelper", TargetedMod.LOTR),
    ATTACK_TIMINGS_CLIENT("lotr.client.ClientSideAttackTimingsRemoval", TargetedMod.LOTR),
    ATTACK_TIMINGS_SERVER("lotr.entity.ServerSideAttackTimingsRemoval", TargetedMod.LOTR),
    ENABLE_GW_DEATH("lotr.entity.EnableGreyWandererDeath", TargetedMod.LOTR),
    INVASION_ENUM_FIXER("lotr.misc.InvasionEnumFixer", TargetedMod.LOTR),
    FACTION_RELATION_OVERRIDES("lotr.entity.RelationOverrideController", TargetedMod.LOTR),
    RENDER_SCRAP_TRADERS_PROPERLY("lotr.client.FixScrapTraderRenderer", TargetedMod.LOTR),

    // Interop
    //FIX_VAMPIRE_RITUAL("lotr.interop.witchery.FixVampireRitual", TargetedMod.LOTR, TargetedMod.WITCHERY),
    SAFE_VAMPIRE_BIOMES("lotr.interop.witchery.SafeVampireBiomes", TargetedMod.LOTR, TargetedMod.WITCHERY),
    ENTITY_RENDERER_PATCH("lotr.interop.witchery.EntityRendererPatch", TargetedMod.LOTR, TargetedMod.WITCHERY),

    RPLE_COMPUTRONICS_LAMP("rple.RPLEComputronicsLamp", TargetedMod.RPLE, TargetedMod.COMPUTRONICS),

    // Witchery
    DAMAGE_CAP_REMOVER("witchery.entity.DamageCapRemover$RegularCapRemover", TargetedMod.WITCHERY),
    CAP_MOG_GULG("witchery.entity.DamageCapRemover$WhyAreYouTwoSpecialDamnit", TargetedMod.WITCHERY),

    // Backhand
    FAKE_PLAYER_COMPAT("backhand.FakePlayerCompat", TargetedMod.BACKHAND),

    // Vanilla
    REMOVE_IFRAMES("vanilla.RemoveImmunityFrames", TargetedMod.VANILLA),

    ;

    public final String mixinClass;
    public final List<TargetedMod> targetedMods;
    private final Side side;

    Mixin(String mixinClass, Side side, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.targetedMods = Arrays.asList(targetedMods);
        this.side = side;
    }

    Mixin(String mixinClass, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.targetedMods = Arrays.asList(targetedMods);
        this.side = Side.BOTH;
    }

    public boolean shouldLoad(List<TargetedMod> loadedMods) {
        return (side == Side.BOTH
                || side == Side.SERVER && FMLLaunchHandler.side().isServer()
                || side == Side.CLIENT && FMLLaunchHandler.side().isClient())
                && new HashSet<>(loadedMods).containsAll(targetedMods);
    }
}

enum Side {
    BOTH,
    CLIENT,
    SERVER
}