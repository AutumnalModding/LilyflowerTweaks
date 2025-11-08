package xyz.lilyflower.lilytweaks.mixin.bandaid;

import net.minecraft.entity.Entity;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldServer.class)
public class FixNullEntityMap {
    @Shadow
    private IntHashMap entityIdMap;

    @Inject(method = "onEntityAdded", at = @At("HEAD"))
    public void fixNullMap(Entity entity, CallbackInfo ci) {
        if (this.entityIdMap == null) {
            this.entityIdMap = new IntHashMap();
        }
    }
}
