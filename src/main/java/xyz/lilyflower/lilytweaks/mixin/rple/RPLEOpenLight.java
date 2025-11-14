package xyz.lilyflower.lilytweaks.mixin.rple;

import com.falsepattern.rple.api.common.ServerColorHelper;
import com.falsepattern.rple.api.common.block.RPLECustomBlockBrightness;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pcl.openlights.tileentity.OpenLightTE;

import static com.falsepattern.rple.api.common.color.LightValueColor.LIGHT_VALUE_0;

@Mixin(value = OpenLightTE.class, remap = false)
public abstract class RPLEOpenLight implements RPLECustomBlockBrightness {
    @Shadow public int color;
    @Shadow public int brightness;

    @Override
    public short rple$getCustomBrightnessColor() {
        return LIGHT_VALUE_0.rgb16();
    }

    @Override
    public short rple$getCustomBrightnessColor(int i) {
        return LIGHT_VALUE_0.rgb16();
    }

    @Override
    public short rple$getCustomBrightnessColor(@NotNull IBlockAccess access, int meta, int posX, int posY, int posZ) {
        int red = (((this.color >> 16) & 0x1f) >> 1) & this.brightness;
        int green = (((this.color >> 8) & 0x1f) >> 1) & this.brightness;
        int blue = ((this.color & 0x1f) >> 1) & this.brightness;

        return ServerColorHelper.RGB16FromRGBChannel4Bit(red, green, blue);
    }
}
