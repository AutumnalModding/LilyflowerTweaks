package xyz.lilyflower.solaris.content.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.minecraft.item.Item;
import xyz.lilyflower.solaris.api.LoadStage;
import xyz.lilyflower.solaris.configuration.modules.SolarisContent;
import xyz.lilyflower.solaris.api.ContentRegistry;
import xyz.lilyflower.solaris.content.SolarisRegistry;
import xyz.lilyflower.solaris.debug.LoggingHelper;
import xyz.lilyflower.solaris.init.Solaris;
import xyz.lilyflower.solaris.util.SolarisExtensions;

public class ItemRegistry implements ContentRegistry<Item> {
    static final ArrayList<SolarisExtensions.Pair<Item, String>> ITEMS = new ArrayList<>();

    @Override
    public ArrayList<SolarisExtensions.Pair<Item, String>> contents() {
        return ITEMS;
    }

    public static final Item STONE_DUST = create("dust_stone", Item.class, new Class<?>[]{});

    @SuppressWarnings("SameParameterValue")
    private static Item create(String name, Class<? extends Item> clazz, Class<?>[] types, Object... arguments) {
        try {
            Constructor<? extends Item> constructor = clazz.getConstructor(types);
            Item instance = constructor.newInstance(arguments);
            ITEMS.add(new SolarisExtensions.Pair<>(instance, name));
            return instance;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            LoggingHelper.oopsie(SolarisRegistry.LOGGER, "FAILED INITIALIZING ITEM CLASS: " + clazz.getName(), exception);
        }

        return null;
    }

    @Override
    public void register(SolarisExtensions.Pair<Item, String> pair) {
        pair.left().setUnlocalizedName("solaris." + pair.right());
        pair.left().setTextureName("solaris:" + pair.right());

        GameRegistry.registerItem(pair.left(), pair.right(), "solaris");
    }

    @Override
    public boolean valid(String key) {
        return SolarisContent.ENABLE_CONTENT;
    }

    @Override
    public boolean runnable() {
        return Solaris.STATE == LoadStage.PRELOADER;
    }
}
