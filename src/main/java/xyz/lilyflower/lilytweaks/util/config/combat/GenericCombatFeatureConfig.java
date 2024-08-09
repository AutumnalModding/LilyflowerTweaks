package xyz.lilyflower.lilytweaks.util.config.combat;

import com.emoniph.witchery.util.IHandleDT;
import java.io.File;
import java.util.HashMap;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("unchecked")
public class GenericCombatFeatureConfig {
    public static HashMap<Class<? extends IHandleDT>, Float> WITCHERY_DAMAGE_CAPS = new HashMap<>();
    public static String[] NO_IFRAME_DAMAGETYPES;
    public static boolean NO_IFRAME_PROJECTILES = false;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        String[] caps = configuration.getStringList("witcheryDamageCaps", "damage", new String[]{},
                "Class-cap mapping for Witchery bosses. Format: <EntityName>:<DamageCap>\n" +
                "Valid entity names: see https://i.imgur.com/CbQZ1ko.png (exclude semicolons!)"
        );

        NO_IFRAME_DAMAGETYPES = configuration.getStringList("noImmunityDamageTypes", "damage", new String[]{},
                "List of damage sources for which iframes aren't applied."
        );

        NO_IFRAME_PROJECTILES = configuration.getBoolean("noImmunityForProjectiles", "damage", false, "Make projectiles ignore iframes.");

        for (String cap : caps) {
            String[] split = cap.split(":");
            String classname = "com.emoniph.witchery.entity.Entity " + split[0];
            float value = Float.parseFloat(split[1]);

            try {
                // all witchery bosses implement this
                WITCHERY_DAMAGE_CAPS.put((Class<? extends IHandleDT>) Class.forName(classname), value);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
