package xyz.lilyflower.lotweakr.mixin.plugin;

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

    // Replace with your own mixins:

    COSMETIC_UNLOCKER("misc.CosmeticUnlockerMixin", TargetedMod.LOTR),
    FT_WAYPOINT_OVERRIDES("travel.FastTravelWaypointOverrideController", TargetedMod.LOTR),
    OMNITARGET("entity.OmnitargetHelper", TargetedMod.LOTR),
    ATTACK_TIMINGS_CLIENT("client.ClientSideAttackTimingsRemoval", TargetedMod.LOTR),
    ATTACK_TIMINGS_SERVER("entity.ServerSideAttackTimingsRemoval", TargetedMod.LOTR),
    ENABLE_GW_DEATH("entity.EnableGreyWandererDeath", TargetedMod.LOTR),
    INVASION_ENUM_FIXER("misc.InvasionEnumFixer", TargetedMod.LOTR),
    FACTION_RELATION_OVERRIDES("entity.RelationOverrideController", TargetedMod.LOTR),
    RENDER_SCRAP_TRADERS_PROPERLY("client.FixScrapTraderRenderer", TargetedMod.LOTR),

    // Interop
    FIX_VAMPIRE_RITUAL("interop.witchery.FixVampireRitual", TargetedMod.WITCHERY),
    SAFE_VAMPIRE_BIOMES("interop.witchery.SafeVampireBiomes", TargetedMod.WITCHERY),
    ENTITY_RENDERER_PATCH("interop.witchery.EntityRendererPatch", TargetedMod.WITCHERY)
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