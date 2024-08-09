package xyz.lilyflower.lilytweaks.mixin.lotr.interop.witchery;

import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.item.ItemGlassGoblet;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xyz.lilyflower.lilytweaks.core.LTConfig;

@Mixin(value = ItemGlassGoblet.class)
public abstract class FixVampireRitual {
    @Shadow(remap = false) protected abstract boolean hasBloodType(ItemStack stack, ItemGlassGoblet.BloodSource source);

    @Shadow(remap = false) public abstract void setBloodOwner(ItemStack stack, ItemGlassGoblet.BloodSource source);

    @Shadow(remap = false) protected abstract boolean isRitual(World world, int x, int y, int z);

    @Shadow(remap = false) public abstract int getMaxItemUseDuration(ItemStack stack);

    @Shadow(remap = false) protected abstract boolean isElleNear(World world, double x, double y, double z, double range);

    /**
     * @author Lilyflower
     * @reason Allow vampire ritual in LOTR dim
     */
    @Overwrite
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        MovingObjectPosition mop = InfusionOtherwhere.raytraceBlocks(world, player, true, 2.0);
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && world.getBlock(mop.blockX, mop.blockY, mop.blockZ) == Blocks.skull) {
            TileEntitySkull skull = BlockUtil.getTileEntity(world, mop.blockX, mop.blockY, mop.blockZ, TileEntitySkull.class);
            if (!world.isRemote && skull != null && skull.func_145904_a() == 0) {
                if (this.hasBloodType(stack, ItemGlassGoblet.BloodSource.CHICKEN) && (world.provider.dimensionId == 0 || LTConfig.FIX_RITUAL && world.provider.dimensionId == 100) && this.isRitual(world, mop.blockX, mop.blockY, mop.blockZ) && world.canBlockSeeTheSky(mop.blockX, mop.blockY, mop.blockZ) && !world.isDaytime() && Config.instance().allowVampireRitual && !this.isElleNear(world, mop.blockX, mop.blockY - 1, mop.blockZ, 32.0)) {
                    this.setBloodOwner(stack, ItemGlassGoblet.BloodSource.EMPTY);
                    EntityLightningBolt bolt = new EntityLightningBolt(world, 0.5 + (double)mop.blockX, (double)mop.blockY + 0.05, 0.5 + (double)mop.blockZ);
                    world.setBlockToAir(mop.blockX, mop.blockY, mop.blockZ);
                    world.addWeatherEffect(bolt);
                    EntityFollower follower = new EntityFollower(world);
                    follower.setFollowerType(0);
                    follower.func_110163_bv();
                    follower.setPositionAndRotation(0.5 + (double)mop.blockX, (double)mop.blockY + 1.05, 0.5 + (double)mop.blockZ, 0.0F, 0.0F);
                    follower.setOwner(player);
                    ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_MOB_LILITH_TALK, world, 0.5 + (double)mop.blockX, (double)mop.blockY + 1.05, 0.5 + (double)mop.blockZ, 1.0, 2.0, 16);
                    ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.lilithquest");
                    world.spawnEntityInWorld(follower);
                } else if (!world.isRemote) {
                    ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:glassgoblet.seemswrong");
                    SoundEffect.NOTE_SNARE.playOnlyTo(player);
                }
            }

        } else {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        }
        return stack;
    }
}
