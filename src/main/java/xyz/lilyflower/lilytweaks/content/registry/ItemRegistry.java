package xyz.lilyflower.lilytweaks.content.registry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.minecraft.item.Item;
import xyz.lilyflower.lilytweaks.configuration.modules.CustomContentAdditionsConfiguration;
import xyz.lilyflower.lilytweaks.api.ContentRegistry;
import xyz.lilyflower.lilytweaks.content.LilyflowerTweaksContentSystem;
import xyz.lilyflower.lilytweaks.debug.LoggingHelper;
import xyz.lilyflower.lilytweaks.util.Pair;

public class ItemRegistry implements ContentRegistry<Item> {
    static final ArrayList<Pair<Item, String>> ITEMS = new ArrayList<>();

    @Override
    public ArrayList<Pair<Item, String>> contents() {
        return ITEMS;
    }

    public static final Item STONE_DUST = create("dust_stone", Item.class, new Class<?>[]{});

    private static Item create(String name, Class<? extends Item> clazz, Class<?>[] types, Object... arguments) {
        try {
            Constructor<? extends Item> constructor = clazz.getConstructor(types);
            Item instance = constructor.newInstance(arguments);
            ITEMS.add(new Pair<>(instance, name));
            return instance;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            LoggingHelper.oopsie(LilyflowerTweaksContentSystem.LOGGER, "FAILED INITIALIZING ITEM CLASS: " + clazz.getName(), exception);
        }

        return null;
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
