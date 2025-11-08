package xyz.lilyflower.lilytweaks.mixin.advrocketry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import cpw.mods.fml.common.FMLCommonHandler;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.w3c.dom.Node;
import zmaster587.advancedRocketry.AdvancedRocketry;
import zmaster587.advancedRocketry.api.dimension.solar.StellarBody;
import zmaster587.advancedRocketry.dimension.DimensionProperties;
import zmaster587.advancedRocketry.util.AstronomicalBodyHelper;
import zmaster587.advancedRocketry.util.XMLPlanetLoader;

@Mixin(XMLPlanetLoader.class)
public class ActuallyRespectAverageTemperature {
    @WrapOperation(method = "readPlanetFromNode",
            at = @At(value = "INVOKE",
            target = "Lzmaster587/advancedRocketry/util/AstronomicalBodyHelper;getAverageTemperature(Lzmaster587/advancedRocketry/api/dimension/solar/StellarBody;II)I"
            ), remap = false)
    public int respectAvgTemp(StellarBody star, int distance, int pressure, Operation<Integer> original, @Local DimensionProperties properties) {
        return properties.averageTemperature == 100 ? AstronomicalBodyHelper.getAverageTemperature(star, distance, pressure) : properties.averageTemperature;
    }

    @Inject(method = "readPlanetFromNode", at = @At(value = "INVOKE", target = "Lorg/w3c/dom/Node;getNextSibling()Lorg/w3c/dom/Node;"), remap = false)
    public void readAvgTemp(Node planetNode, StellarBody star, CallbackInfoReturnable<List<DimensionProperties>> cir, @Local(ordinal = 1) Node planetPropertyNode, @Local DimensionProperties properties) {
        if (planetPropertyNode.getNodeName().equalsIgnoreCase("avgtemperature")) {
            try {
                properties.averageTemperature = Integer.parseInt(planetPropertyNode.getTextContent());
            } catch (NumberFormatException exception) {
                AdvancedRocketry.logger.warn("Invalid avgTemperature specified");
            }
        }
    }
}
