package xyz.lilyflower.lilytweaks.mixin.lotr.misc;

import java.util.Map;
import lotr.common.LOTRPlayerData;
import lotr.common.fac.LOTRFaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.util.loader.lotr.LOTRCustomFactionLoader;

@Mixin(value = LOTRPlayerData.class, remap = false)
public class MetafactionFixes {
    @Shadow private LOTRFaction pledgeFaction;

    @Shadow private Map<LOTRFaction, Float> alignments;

    @Inject(method = "isPledgedTo", at = @At("HEAD"), cancellable = true)
    public void metafixPledge(LOTRFaction fac, CallbackInfoReturnable<Boolean> cir) {
        if (LOTRCustomFactionLoader.MFRL.containsKey(fac)) {
            System.out.println("Returning pledge for " + LOTRCustomFactionLoader.MFRL.get(fac) + " instead of " + fac);
            cir.setReturnValue(this.pledgeFaction == LOTRCustomFactionLoader.MFRL.get(fac));
        }
    }

    @Inject(method = "getAlignment", at = @At("HEAD"), cancellable = true)
    public void metafixAlignmentRetrieval(LOTRFaction fac, CallbackInfoReturnable<Float> cir) {
        if (LOTRCustomFactionLoader.MFRL.containsKey(fac)) {
            Float alignment = this.alignments.get(LOTRCustomFactionLoader.MFRL.get(fac));
            System.out.println("Returning alignment " + alignment + " for " + LOTRCustomFactionLoader.MFRL.get(fac) + " instead of " + fac);
            cir.setReturnValue(alignment != null ? alignment : 0.0F);
        }
    }

    @ModifyArg(method = "setAlignment", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0), index = 0)
    private Object metafixAlignmentModification(Object faction) {
        System.out.println("Modfying alignment for " + LOTRCustomFactionLoader.MFRL.getOrDefault((LOTRFaction) faction, (LOTRFaction) faction) + " instead of " + faction);
        return LOTRCustomFactionLoader.MFRL.getOrDefault((LOTRFaction) faction, (LOTRFaction) faction);
    }
}
