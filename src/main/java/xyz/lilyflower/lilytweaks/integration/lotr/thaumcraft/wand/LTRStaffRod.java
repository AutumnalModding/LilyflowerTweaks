package xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.api.wands.StaffRod;

public class LTRStaffRod extends StaffRod {
    private String research;

    public LTRStaffRod(String tag, int capacity, ItemStack item, int craftCost, ResourceLocation texture) {
        super(tag, capacity, item, craftCost, texture);
    }

    public LTRStaffRod(String tag, int capacity, ItemStack item, int craftCost, IWandRodOnUpdate onUpdate, ResourceLocation texture) {
        super(tag, capacity, item, craftCost, onUpdate, texture);
    }

    public LTRStaffRod research(String research) {
        this.research = research;
        return this;
    }

    public LTRStaffRod runic() {
        this.setRunes(true);
        return this;
    }

    @Override
    public String getResearch() {
        return this.research == null ? super.getResearch() : this.research;
    }
}
