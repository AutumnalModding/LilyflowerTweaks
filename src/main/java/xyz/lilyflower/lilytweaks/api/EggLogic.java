package xyz.lilyflower.lilytweaks.api;

import alfheim.common.item.AlfheimItems;
import alfheim.common.item.ItemSpawnEgg;
import com.github.bsideup.jabel.Desugar;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.stream.IntStream;
import kotlin.Triple;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface EggLogic {
    ItemStack egg(Class<? extends EntityLivingBase> clazz);

    @Desugar
    record GenericEggLogic(String modID, String itemName) implements EggLogic {
        @Override
        public ItemStack egg(Class<? extends EntityLivingBase> clazz) {
            Item item = GameRegistry.findItem(this.modID, this.itemName);
            EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(clazz, false);
            int index = registration.getModEntityId();
            return new ItemStack(item, 1, index);
        }
    }

    class AlfheimEggLogic implements EggLogic {
        @Override
        public ItemStack egg(Class<? extends EntityLivingBase> clazz) {
            ArrayList<Triple<Class<? extends Entity>, Integer, Integer>> mappings = ItemSpawnEgg.Companion.getMappings();
            int index = IntStream.range(0, mappings.size())
                    .filter(value -> mappings.get(value).getFirst() == clazz)
                    .findFirst()
                    .orElse(Short.MAX_VALUE);

            return new ItemStack(AlfheimItems.INSTANCE.getSpawnEgg(), 1, index);
        }
    }
}
