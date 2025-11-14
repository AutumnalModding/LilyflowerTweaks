package xyz.lilyflower.lilytweaks.mixin.lotr.interop.witchery;

import com.emoniph.witchery.item.ItemGlassGoblet;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.lilyflower.lilytweaks.configuration.modules.WitcheryIntegrationConfiguration;

@Mixin(ItemGlassGoblet.class)
public class FixVampireRitual {
    @Redirect(method = "onItemRightClick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/WorldProvider;dimensionId:I"))
    public int allowOtherDimensions(WorldProvider instance) {
        return WitcheryIntegrationConfiguration.ALLOWED_RITUAL_DIMENSIONS.contains(instance.dimensionId) ? 0 : Integer.MAX_VALUE;
    }
}