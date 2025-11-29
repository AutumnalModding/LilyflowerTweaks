package xyz.lilyflower.solaris.mixin.bandaid;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @SuppressWarnings({"UnresolvedMixinReference", "DataFlowIssue"})
    @Inject(method = "updateTimeLightAndEntities", at = @At(value = "INVOKE", target = "Ljava/util/Hashtable;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    public void makeSureNotNull(CallbackInfo info, @SuppressWarnings("LocalMayBeArgsOnly") @Local(name = "id") int dimension) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        server.worldTickTimes.putIfAbsent(dimension, new long[200]);
    }
}
