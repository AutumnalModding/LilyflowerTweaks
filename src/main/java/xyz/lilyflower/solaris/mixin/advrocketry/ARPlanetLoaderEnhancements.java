package xyz.lilyflower.solaris.mixin.advrocketry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.w3c.dom.Node;
import xyz.lilyflower.solaris.util.data.AREnhancedPlanetProperties;
import zmaster587.advancedRocketry.AdvancedRocketry;
import zmaster587.advancedRocketry.api.dimension.solar.StellarBody;
import zmaster587.advancedRocketry.dimension.DimensionProperties;
import zmaster587.advancedRocketry.util.AstronomicalBodyHelper;
import zmaster587.advancedRocketry.util.XMLPlanetLoader;

@Mixin(XMLPlanetLoader.class)
public class ARPlanetLoaderEnhancements {
    @WrapOperation(method = "readPlanetFromNode",
            at = @At(value = "INVOKE",
            target = "Lzmaster587/advancedRocketry/util/AstronomicalBodyHelper;getAverageTemperature(Lzmaster587/advancedRocketry/api/dimension/solar/StellarBody;II)I"
            ), remap = false)
    public int readAverageTemperature(StellarBody star, int distance, int pressure, Operation<Integer> original, @Local DimensionProperties properties) {
        return properties.averageTemperature == 100 ? AstronomicalBodyHelper.getAverageTemperature(star, distance, pressure) : properties.averageTemperature;
    }

    @Inject(method = "readPlanetFromNode", at = @At(value = "INVOKE", target = "Lorg/w3c/dom/Node;getNextSibling()Lorg/w3c/dom/Node;"), remap = false)
    public void parse(Node planetNode, StellarBody star, CallbackInfoReturnable<List<DimensionProperties>> cir, @Local(ordinal = 1) Node planetPropertyNode, @Local DimensionProperties properties) {
        try {
            switch (planetPropertyNode.getNodeName().toLowerCase()) {
                case "avgtemperature" -> properties.averageTemperature = Integer.parseInt(planetPropertyNode.getTextContent());
                case "hasoxygen" -> {
                    String value = planetPropertyNode.getTextContent();
                    if (value != null && value.equalsIgnoreCase("true")) {
                        properties.hasOxygen = true;
                    }
                }
                case "atmospheretype" -> {
                    String value = planetPropertyNode.getTextContent();
                    if (value != null) {
                        ((AREnhancedPlanetProperties) properties).setDefinedAtmosphere(value);
                    }
                }
            }
        } catch (NumberFormatException exception) {
            AdvancedRocketry.logger.warn("Invalid {} specified", planetPropertyNode.getNodeName());
        }
    }

    @ModifyVariable(method = "writePlanet", at = @At(value = "INVOKE", target = "Lzmaster587/advancedRocketry/dimension/DimensionProperties;isGasGiant()Z"), ordinal = 0, remap = false)
    private static String write(String output, @Local(argsOnly = true) DimensionProperties properties, @Local(ordinal = 1) String tabLen) {
        if (properties.hasOxygen) {
            output = output + tabLen + "\t<hasOxygen>true</hasOxygen>\n";
        }

        AREnhancedPlanetProperties enhanced = (AREnhancedPlanetProperties) properties;
        if (enhanced.getDefinedAtmosphere() != null) {
            output = output + tabLen + "\t<atmosphereType>" + enhanced.getAtmosphereName() + "</atmosphereType>\n";
        }

        return output;
    }
}
