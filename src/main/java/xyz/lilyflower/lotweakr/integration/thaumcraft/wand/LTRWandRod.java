package xyz.lilyflower.lotweakr.integration.thaumcraft.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.api.wands.WandRod;

public class LTRWandRod extends WandRod {
    private String research;

    public LTRWandRod(String tag, int capacity, ItemStack item, int craftCost, ResourceLocation texture) {
        super(tag, capacity, item, craftCost, texture);
    }

    public LTRWandRod(String tag, int capacity, ItemStack item, int craftCost, IWandRodOnUpdate onUpdate, ResourceLocation texture) {
        super(tag, capacity, item, craftCost, onUpdate, texture);
    }

    public LTRWandRod research(String research) {
        this.research = research;
        return this;
    }

    public LTRWandRod glowing() {
        this.setGlowing(true);
        return this;
    }

    @Override
    public String getResearch() {
        return this.research == null ? super.getResearch() : this.research;
    }
}
