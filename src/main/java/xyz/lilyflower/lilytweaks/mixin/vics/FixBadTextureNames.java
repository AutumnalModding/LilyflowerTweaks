package xyz.lilyflower.lilytweaks.mixin.vics;

import java.util.HashMap;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.lilyflower.lilytweaks.init.LilyflowerTweaksMixinSystem;

@Mixin(Item.class)
public class FixBadTextureNames {
    @Unique
    private static final HashMap<String, String> lilyflower$VMW_BAD_NAMES = new HashMap<>();

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyVariable(method = "setTextureName", at = @At("HEAD"), argsOnly = true)
    public String force(String name) {
        LilyflowerTweaksMixinSystem.LOGGER.debug("Fixing Vic's Modern Warfare's broken texture names...");
        if (lilyflower$VMW_BAD_NAMES.containsKey(name)) {
            name = name.toLowerCase();
        }
        return name.replaceAll(".png", "");
    }

    static {
        // Vic's Modern Warfare
        lilyflower$VMW_BAD_NAMES.put("mw:TinIngot", "mw:tiningot");
        lilyflower$VMW_BAD_NAMES.put("mw:LeadIngot", "mw:leadingot");
        lilyflower$VMW_BAD_NAMES.put("mw:SteelDust", "mw:steeldust");
        lilyflower$VMW_BAD_NAMES.put("mw:SteelIngot", "mw:steelingot");
        lilyflower$VMW_BAD_NAMES.put("mw:SulfurDust", "mw:sulfurdust");
        lilyflower$VMW_BAD_NAMES.put("mw:CarbonFiber", "mw:carbonfiber");
        lilyflower$VMW_BAD_NAMES.put("mw:CopperIngot", "mw:copperingot");
        lilyflower$VMW_BAD_NAMES.put("mw:PlasticPlate", "mw:plasticplate");
        lilyflower$VMW_BAD_NAMES.put("mw:GunmetalPlate", "mw:gunmetalplate");
        lilyflower$VMW_BAD_NAMES.put("mw:GraphiteChunk", "mw:graphitechunk");
        lilyflower$VMW_BAD_NAMES.put("mw:GunmetalIngot", "mw:gunmetalingot");
        lilyflower$VMW_BAD_NAMES.put("mw:CarbonComposite", "mw:carboncomposite");
        lilyflower$VMW_BAD_NAMES.put("mw:SyntheticPlastic", "mw:syntheticplastic");
        lilyflower$VMW_BAD_NAMES.put("mw:GunmetalComposite", "mw:gunmetalcomposite");
        lilyflower$VMW_BAD_NAMES.put("mw:SyntheticPolymerComposite", "mw:syntheticpolymercomposite");
    }
}
