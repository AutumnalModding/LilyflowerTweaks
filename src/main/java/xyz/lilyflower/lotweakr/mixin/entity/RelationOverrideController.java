package xyz.lilyflower.lotweakr.mixin.entity;

import lotr.common.fac.LOTRFaction;
import lotr.common.fac.LOTRFactionRelations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lotweakr.util.config.loader.FactionRelationLoader;

@Mixin(LOTRFactionRelations.class)
public class RelationOverrideController {
    @Inject(method = "getRelations", at = @At("HEAD"), cancellable = true, remap = false)
    private static void override(LOTRFaction f1, LOTRFaction f2, CallbackInfoReturnable<LOTRFactionRelations.Relation> cir) {
        LOTRFactionRelations.FactionPair pair = new LOTRFactionRelations.FactionPair(f1, f2);
        if (FactionRelationLoader.RELATIONS.containsKey(pair)) {
            cir.setReturnValue(FactionRelationLoader.RELATIONS.get(pair));
        }
    }
}