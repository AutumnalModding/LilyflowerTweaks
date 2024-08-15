package xyz.lilyflower.lilytweaks.mixin.endlessids;

import com.hbm.packet.PacketDispatcher;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.lilyflower.lilytweaks.util.fixes.ExtendedBiomeSyncPacket;

@Mixin(value = PacketDispatcher.class, remap = false)
public class HBMPacketFix {
    @Shadow @Final public static SimpleNetworkWrapper wrapper;

    @Inject(method = "registerPackets", at = @At("TAIL"))
    private static void register(CallbackInfo ci) {
        wrapper.registerMessage(ExtendedBiomeSyncPacket.Handler.class, ExtendedBiomeSyncPacket.class, 36, Side.CLIENT);
    }
}
