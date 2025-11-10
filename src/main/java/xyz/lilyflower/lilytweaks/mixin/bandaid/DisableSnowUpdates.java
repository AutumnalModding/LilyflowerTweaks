package xyz.lilyflower.lilytweaks.mixin.bandaid;

import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksGameConfigurationSystem;

@Mixin(BlockSnow.class)
public class DisableSnowUpdates {
    /**
     * @author LilyflowerFDL
     * @reason got a stack overflow from this.
     */
    @Overwrite
    private boolean func_150155_m(World world, int x, int y, int z) {
        BlockSnow block = (BlockSnow) (Object) this;
        if (!block.canPlaceBlockAt(world, x, y, z)) {
            if (LilyflowerTweaksGameConfigurationSystem.DISABLE_SNOW_UPDATES) {
                world.setBlock(x, y, z, Blocks.air, 0, 2);
            } else {
                world.setBlockToAir(x, y, z);
            }

            return false;
        } else {
            return true;
        }
    }
}
