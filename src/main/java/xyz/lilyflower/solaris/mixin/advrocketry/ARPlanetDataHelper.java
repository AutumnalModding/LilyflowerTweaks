package xyz.lilyflower.solaris.mixin.advrocketry;

import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.lilyflower.solaris.util.data.AREnhancedPlanetProperties;
import zmaster587.advancedRocketry.AdvancedRocketry;
import zmaster587.advancedRocketry.api.IAtmosphere;
import zmaster587.advancedRocketry.atmosphere.AtmosphereType;
import zmaster587.advancedRocketry.dimension.DimensionProperties;

@Mixin(DimensionProperties.class)
public class ARPlanetDataHelper implements AREnhancedPlanetProperties {
    @Unique
    public IAtmosphere definedAtmosphere = null;

    @Unique
    public String atmosphereName = "";

    @Override
    public IAtmosphere getDefinedAtmosphere() {
        return this.definedAtmosphere;
    }

    @Override
    public String getAtmosphereName() {
        return this.atmosphereName;
    }

    @Override
    public void setAtmosphereDirect(IAtmosphere type) {
        this.definedAtmosphere = type;
    }

    @Override
    public void setDefinedAtmosphere(String name) {
        this.atmosphereName = name;
        try {
            this.definedAtmosphere = (IAtmosphere) AtmosphereType.class.getDeclaredField(name).get(null);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            AdvancedRocketry.logger.warn("Invalid atmosphere type '{}'", name);
        }
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"), remap = false)
    public void readEnhanced(NBTTagCompound nbt, CallbackInfo ci) {
        if (nbt.hasKey("atmosType")) {
            String type = nbt.getString("atmosType").toUpperCase();
            this.setDefinedAtmosphere(type);
        }
    }

    @Inject(method = "writeToNBT", at = @At("TAIL"), remap = false)
    public void writeEnhanced(NBTTagCompound nbt, CallbackInfo ci) {
        if (this.definedAtmosphere != null) {
            nbt.setString("atmosType", this.atmosphereName);
        }
    }

    @Inject(method = "getAtmosphere", at = @At("HEAD"), cancellable = true, remap = false)
    public void getDefinedAtmosphere(CallbackInfoReturnable<IAtmosphere> cir) {
        if (this.definedAtmosphere != null) {
            cir.setReturnValue(this.definedAtmosphere);
        }
    }
}
