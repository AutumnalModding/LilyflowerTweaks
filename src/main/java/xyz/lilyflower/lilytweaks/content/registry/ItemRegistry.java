package xyz.lilyflower.lilytweaks.content.registry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import net.minecraft.item.Item;
import xyz.lilyflower.lilytweaks.configuration.modules.CustomContentAdditionsConfiguration;
import xyz.lilyflower.lilytweaks.content.ContentRegistry;
import xyz.lilyflower.lilytweaks.util.CollectionUtils;
import xyz.lilyflower.lilytweaks.util.Pair;

public class ItemRegistry implements ContentRegistry<Item> {
    static final ArrayList<Pair<Item, String>> ITEMS = new ArrayList<>();

    @Override
    public ArrayList<Pair<Item, String>> contents() {
        return ITEMS;
    }

    public static final Item STONE_DUST = create("dust_stone");

    private static Item create(String name) {
        return CollectionUtils.AARO(ITEMS, new Pair<>(new Item(), name)).left();
    }

    @Override
    public void register(Pair<Item, String> pair) {
        pair.left().setUnlocalizedName("lilytweaks." + pair.right());
        pair.left().setTextureName("lilytweaks:" + pair.right());

        GameRegistry.registerItem(pair.left(), pair.right(), "lilytweaks");
    }

    @Override
    public boolean shouldRegister(String key) {
        return CustomContentAdditionsConfiguration.ENABLE_CONTENT;
    }

    @Override
    public boolean shouldRun(FMLStateEvent phase) {
        return phase instanceof FMLPreInitializationEvent;
    }
}
