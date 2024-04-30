package xyz.lilyflower.lotweakr.integration.thaumcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemLTRWandCap extends Item {
    public IIcon[] icon = new IIcon[1];

    public ItemLTRWandCap() {
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("lotweakr.lotr_wand_cap");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon[0] = ir.registerIcon("lotweakr:wand_cap_mithril");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon[meta];
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(this, 1, 0));
    }

    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
