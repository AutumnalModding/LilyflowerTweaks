package xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.ItemRobeArmor;

public class ItemLTRRobeArmour extends ItemRobeArmor {
    public ItemLTRRobeArmour(int slot, int index) {
        super(ThaumcraftApi.armorMatSpecial, slot, index);
    }

    @Override
    public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
        return this.armorType == 3 ? 3 : 7;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.rare;
    }
}
