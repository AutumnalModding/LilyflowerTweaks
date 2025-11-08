package xyz.lilyflower.lilytweaks.config.runners;

import com.emoniph.witchery.util.IHandleDT;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksConfigSystem;

@SuppressWarnings("unchecked")
public class WitcheryConfig implements ConfigRunner {
    public static final Consumer<Configuration> COMBAT_TWEAKS = configuration -> {
        String[] caps = configuration.getStringList("witcheryDamageCaps", "damage", new String[]{},
                "Class-cap mapping for Witchery bosses. Format: <EntityName>:<DamageCap>\n" +
                        "Valid entity names: see https://i.imgur.com/CbQZ1ko.png (exclude semicolons!)"
        );
        for (String cap : caps) {
            String[] split = cap.split(":");
            String classname = "com.emoniph.witchery.entity.Entity " + split[0];
            float value = Float.parseFloat(split[1]);

            try {
                // all witchery bosses implement this
                LilyflowerTweaksConfigSystem.WITCHERY_DAMAGE_CAPS.put((Class<? extends IHandleDT>) Class.forName(classname), value);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public void init() {
        LilyflowerTweaksConfigSystem.add("witchery", COMBAT_TWEAKS);
    }
}
