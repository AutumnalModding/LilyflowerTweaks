package xyz.lilyflower.lilytweaks.mixin.lotr.travel;

import lotr.common.world.map.LOTRWaypoint;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.lilytweaks.configuration.modules.LOTRModIntegrationConfiguration;

@Mixin(LOTRWaypoint.class)
public class FastTravelWaypointOverrideController {
    @Inject(method = "hasPlayerUnlocked", at = @At("HEAD"), cancellable = true, remap = false)
    public void modifyUnlockStatus(EntityPlayer entityplayer, CallbackInfoReturnable<Boolean> cir) {
        if (LOTRModIntegrationConfiguration.UNLOCK_WAYPOINTS) {
            cir.setReturnValue(true);
        }

        if (LOTRModIntegrationConfiguration.isWaypointDisabled((LOTRWaypoint) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isCompatibleAlignment", at = @At("HEAD"), cancellable = true, remap = false)
    public void noLocking(EntityPlayer entityplayer, CallbackInfoReturnable<Boolean> cir) {
        if (LOTRModIntegrationConfiguration.NO_WAYPOINT_LOCKING) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isHidden", at = @At("HEAD"), cancellable = true, remap = false)
    public void disableWaypoint(CallbackInfoReturnable<Boolean> cir) {
        if (LOTRModIntegrationConfiguration.isWaypointDisabled((LOTRWaypoint) (Object) this)) {
            cir.setReturnValue(true);
        }
    }
}
