package xyz.lilyflower.solaris.mixin.quiverbow;

import com.domochevsky.quiverbow.net.NetHelper;
import com.domochevsky.quiverbow.projectiles.SoulShot;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import xyz.lilyflower.solaris.configuration.modules.SolarisQuiverbow;
import xyz.lilyflower.solaris.api.EggLogic;

@Mixin(SoulShot.class)
@SuppressWarnings("UnusedMixin")
public class SoulShotMixin {
    @Unique
    private static final HashMap<String, EggLogic> solaris$logic = new HashMap<>();

    /**
     * @author Fractalflower
     * @reason Holy shit, the original code is so stupid. The entity class is hardcoded.
     * It's literally a bunch of nested if-else instanceof statements. Why. Just why.
     * So I'm rewriting it to work properly with modded entities, and also hopefully
     * improve the performance a bit (due to using a modern compiler).
     */
    @Overwrite(remap = false)
    public void onImpact(MovingObjectPosition target) {
        SoulShot shot = (SoulShot) (Object) this;
        Entity entity = target.entityHit;

        boolean allowed = (entity instanceof EntityLivingBase);
        for (String blacklisted : SolarisQuiverbow.ENTITY_BLACKLIST) {
            if (!allowed) break;
            try {
                allowed = !entity.getClass().isAssignableFrom(Class.forName(blacklisted));
            } catch (NoClassDefFoundError | ClassNotFoundException ignored) {}
        }

        if (allowed) {
            EntityLivingBase living = (EntityLivingBase) entity;

            String classname = entity.getClass().getName();
            Optional<EggLogic> logic = solaris$logic.entrySet()
                    .stream()
                    .filter(entry -> classname.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst();

            ItemStack stack = logic.isPresent() ? logic.get().egg(living.getClass()) : new ItemStack(Items.spawn_egg, 1, EntityList.getEntityID(living));
            if (stack.getDisplayName().equals("Spawn")) {
                stack = new ItemStack(Items.diamond);
                stack.setStackDisplayName("Invalid Spawn Egg Diamond Refund - Sorry! :(");
                if (shot.shootingEntity instanceof EntityPlayerMP player) {
                    player.addChatMessage(new ChatComponentTranslation("solaris.invalid_spawn_egg.1").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                    player.addChatMessage(new ChatComponentTranslation("solaris.invalid_spawn_egg.2").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                    player.addChatMessage(new ChatComponentTranslation("solaris.invalid_spawn_egg.3").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                    player.addChatMessage(new ChatComponentTranslation("solaris.invalid_spawn_egg.4").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                }
            } else {
                entity.setDead();
            }

            EntityItem dropped = new EntityItem(shot.worldObj,
                    shot.shootingEntity == null ? shot.posX : shot.shootingEntity.posX,
                    shot.shootingEntity == null ? shot.posY : shot.shootingEntity.posY,
                    shot.shootingEntity == null ? shot.posZ : shot.shootingEntity.posZ,
            stack);

            dropped.delayBeforeCanPickup = 10;
            shot.worldObj.spawnEntityInWorld(dropped);
            NetHelper.sendParticleMessageToAllPlayers(shot.worldObj, shot.getEntityId(), (byte) 11, (byte) 4);
            shot.setDead();
        }
    }

    static {
        solaris$logic.put("alfheim.common.entity", new EggLogic.AlfheimEggLogic());
        solaris$logic.put("lotr.common.entity", new EggLogic.GenericEggLogic("lotr", "item.spawnEgg"));
        solaris$logic.put("twilightforest.entity", new EggLogic.GenericEggLogic("TwilightForest", "item.tfspawnegg"));
        solaris$logic.put("com.gildedgames.the_aether.entities", new EggLogic.GenericEggLogic("aether_legacy", "aether_spawn_egg"));
    }
}
