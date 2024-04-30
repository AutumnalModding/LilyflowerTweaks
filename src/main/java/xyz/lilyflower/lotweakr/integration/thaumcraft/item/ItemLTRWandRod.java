package xyz.lilyflower.lotweakr.integration.thaumcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemLTRWandRod extends Item {
    public IIcon[] icon = new IIcon[4];

    public ItemLTRWandRod() {
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("lotweakr.lotr_wand_rod");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon[0] = ir.registerIcon("lotweakr:wand_rod_mallorn");
        this.icon[1] = ir.registerIcon("lotweakr:wand_rod_mirkoak");
        this.icon[2] = ir.registerIcon("lotweakr:wand_rod_charred");
        this.icon[3] = ir.registerIcon("lotweakr:staff_rod_mallorn");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon[meta];
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
        list.add(new ItemStack(this, 1, 2));
    }

    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
