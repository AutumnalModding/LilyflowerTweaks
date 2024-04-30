package xyz.lilyflower.lotweakr.integration.thaumcraft.wand;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandCap;

public class LTRWandCap extends WandCap {
    private String research;

    public LTRWandCap(String tag, float discount, ItemStack item, int craftCost) {
        super(tag, discount, item, craftCost);
    }

    public LTRWandCap(String tag, float discount, List<Aspect> specialAspects, float discountSpecial, ItemStack item, int craftCost) {
        super(tag, discount, specialAspects, discountSpecial, item, craftCost);
    }

    LTRWandCap research(String research) {
        this.research = research;
        return this;
    }

    public LTRWandCap texture(ResourceLocation texture) {
        super.setTexture(texture);
        return this;
    }

    @Override
    public String getResearch() {
        return this.research == null ? super.getResearch() : this.research;
    }
}
