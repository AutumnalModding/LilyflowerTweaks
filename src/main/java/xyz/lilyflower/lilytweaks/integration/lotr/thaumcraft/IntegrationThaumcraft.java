package xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.items.wands.WandRodPrimalOnUpdate;
import xyz.lilyflower.lilytweaks.core.IntegrationLoader;
import xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.item.ItemLTRRobeArmour;
import xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.item.ItemLTRWandCap;
import xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.item.ItemLTRWandRod;
import xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.wand.LTRStaffRod;
import xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.wand.LTRWandCap;
import xyz.lilyflower.lilytweaks.integration.lotr.thaumcraft.wand.LTRWandRod;

public class IntegrationThaumcraft implements IntegrationLoader {
    public static final Item LOTR_WAND_CAP = new ItemLTRWandCap();
    public static final Item LOTR_WAND_ROD = new ItemLTRWandRod();

    public static WandCap CAP_MORGUL;
    public static WandCap CAP_MITHRIL;

    public static WandRod ROD_MALLORN;
    public static WandRod ROD_MIRKOAK;
    public static WandRod ROD_CHARRED;

    public static StaffRod STAFF_MALLORN;

    public static ResearchItem RESEARCH_CAP_MORGUL;
    public static ResearchItem RESEARCH_CAP_MITHRIL;
    public static ResearchItem RESEARCH_ROD_ELVEN;
    public static ResearchItem RESEARCH_ROD_CHARRED;
    public static ResearchItem RESEARCH_STAFF_MALLORN;

    public static Item MITHRIL_ROBE_BODY;
    public static Item MITHRIL_ROBE_LEGS;
    public static Item MITHRIL_ROBE_BOOTS;

    @Override
    public List<String> requiredMods() {
        ArrayList<String> mods = new ArrayList<>();
        mods.add("lotr");
        mods.add("Thaumcraft");
        return mods;
    }

    @Override
    public void runPre() {
        MITHRIL_ROBE_BODY = new ItemLTRRobeArmour(1, 1).setUnlocalizedName("lilytweaks.mithril_robe_body");
        MITHRIL_ROBE_LEGS = new ItemLTRRobeArmour(2, 2).setUnlocalizedName("lilytweaks.mithril_robe_legs");
        MITHRIL_ROBE_BOOTS = new ItemLTRRobeArmour(1, 3).setUnlocalizedName("lilytweaks.mithril_robe_boots");

        GameRegistry.registerItem(LOTR_WAND_CAP, "lotr_wand_cap", "LOTweakR");
        GameRegistry.registerItem(LOTR_WAND_ROD, "lotr_wand_rod", "LOTweakR");

        GameRegistry.registerItem(MITHRIL_ROBE_BODY, "mithril_robe_body", "LOTweakR");
        GameRegistry.registerItem(MITHRIL_ROBE_LEGS, "mithril_robe_legs", "LOTweakR");
        GameRegistry.registerItem(MITHRIL_ROBE_BOOTS, "mithril_robe_boots", "LOTweakR");

        CAP_MITHRIL = new LTRWandCap("mithril", 0.65F, new ItemStack(LOTR_WAND_CAP, 1, 0), 9).texture(new ResourceLocation("lilytweaks", "textures/items/cap_mithril.png"));
        CAP_MORGUL = new LTRWandCap("morgul", 0.8F, Lists.newArrayList(Aspect.FIRE, Aspect.ENTROPY), 0.45F, new ItemStack(LOTR_WAND_CAP, 1, 1), 7).texture(new ResourceLocation("lilytweaks", "textures/items/cap_morgul.png"));

        ROD_MALLORN = new LTRWandRod("mallorn", 150, new ItemStack(LOTR_WAND_ROD, 1, 0), 18, new ResourceLocation("lilytweaks", "textures/items/rod_mallorn.png")).research("ROD_elven");
        ROD_CHARRED = new LTRWandRod("charred", 100, new ItemStack(LOTR_WAND_ROD, 1, 2), 9, new WandRodPrimalOnUpdate(Aspect.FIRE), new ResourceLocation("lilytweaks", "textures/items/rod_charred.png")).glowing();
        ROD_MIRKOAK = new LTRWandRod("mirkoak", 100, new ItemStack(LOTR_WAND_ROD, 1, 1), 12, new WandRodPrimalOnUpdate(Aspect.EARTH), new ResourceLocation("lilytweaks", "textures/items/rod_mirkoak.png")).research("ROD_elven");

        STAFF_MALLORN = new LTRStaffRod("mallorn", 280, new ItemStack(LOTR_WAND_ROD, 1, 3), 24, new ResourceLocation("lilytweaks", "textures/items/staff_mallorn.png")).runic();
    }

    public void run() {
    }

    @Override
    public void runPost() {
        ResearchCategories.registerCategory("lilytweaks", new ResourceLocation("lilytweaks", "textures/items/cap_mithril.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));
        
        RESEARCH_CAP_MITHRIL = new ResearchItem("CAP_mithril", "lilytweaks",
                new AspectList()
                        .add(Aspect.AURA, 3)
                        .add(Aspect.TOOL, 1)
                        .add(Aspect.MAGIC, 2)
                        .add(Aspect.GREED, 2),
                0, 5, 1,
                new ResourceLocation("lilytweaks", "textures/items/cap_mithril.png"))
                .registerResearchItem();

        RESEARCH_CAP_MORGUL = new ResearchItem("CAP_morgul", "lilytweaks",
                new AspectList()
                        .add(Aspect.MAGIC, 2)
                        .add(Aspect.TOOL, 1)
                        .add(Aspect.DARKNESS, 3)
                        .add(Aspect.DEATH, 3),
                0, -5, 1,
                new ResourceLocation("lilytweaks", "textures/items/cap_morgul.png"))
                .registerResearchItem();

        RESEARCH_ROD_ELVEN = new ResearchItem("ROD_elven", "lilytweaks",
                new AspectList()
                        .add(Aspect.MAGIC, 3)
                        .add(Aspect.TREE, 2)
                        .add(Aspect.LIFE, 3)
                        .add(Aspect.TOOL, 1),
                5, 0, 2,
                new ResourceLocation("lilytweaks", "textures/items/rod_mallorn.png"))
                .registerResearchItem();

        RESEARCH_ROD_CHARRED = new ResearchItem("ROD_charred", "lilytweaks",
                new AspectList()
                        .add(Aspect.MAGIC, 2)
                        .add(Aspect.TREE, 2)
                        .add(Aspect.DEATH, 3)
                        .add(Aspect.TOOL, 1)
                        .add(Aspect.DARKNESS, 1),
                -5, 0, 1,
                new ResourceLocation("lilytweaks", "textures/items/rod_charred.png"))
                .registerResearchItem();

        RESEARCH_STAFF_MALLORN = new ResearchItem("ROD_mallorn_staff", "lilytweaks",
                new AspectList()
                        .add(Aspect.MAGIC, 2)
                        .add(Aspect.TREE, 5)
                        .add(Aspect.LIFE, 3)
                        .add(Aspect.TOOL, 1)
                        .add(Aspect.AURA, 3),
                0, 0, 3,
                new ResourceLocation("lilytweaks", "textures/items/staff_mallorn.png"))
                .registerResearchItem();
    }
}