package xyz.lilyflower.lilytweaks.mixin.lotr.bug;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.common.gameevent.TickEvent;
import lotr.client.LOTRTickHandlerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LOTRTickHandlerClient.class)
public class EntityRendererPatch {
    @Inject(method = "onClientTick", at = @At("HEAD"), cancellable = true, remap = false)
    public void fixRenderer(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        EntityPlayer entity = Minecraft.getMinecraft().thePlayer;
        if (entity != null) {
            ExtendedPlayer player = ExtendedPlayer.get(entity);

            if (player.getCreatureType() == TransformCreature.BAT && event.phase == TickEvent.Phase.START) {
                LOTRTickHandlerClient.clientTick++;
                ci.cancel();
            }
        }
    }
}
