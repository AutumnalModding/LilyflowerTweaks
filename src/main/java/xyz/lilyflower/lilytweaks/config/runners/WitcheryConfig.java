package xyz.lilyflower.lilytweaks.config.runners;

import com.emoniph.witchery.util.IHandleDT;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;
import xyz.lilyflower.lilytweaks.config.ConfigRunner;
import xyz.lilyflower.lilytweaks.config.LilyflowerTweaksGameConfigurationSystem;

@SuppressWarnings({"unchecked", "unused"})
public class WitcheryConfig implements ConfigRunner {
    public static final Consumer<Configuration> COMBAT_TWEAKS = configuration -> {
        String[] caps = configuration.getStringList("witcheryDamageCaps", "witchery", new String[]{},
                """
                        Class-cap mapping for Witchery bosses. Format: <EntityName>:<DamageCap>
                        Use -1 for no cap. Valid entity names:\s
                          BabaYaga,\s
                          Death,\s
                          GoblinGulg,\s
                          GoblinMog,\s
                          HornedHuntsman,\s
                          Leonard,\s
                          Lilith,\s
                          LordOfTorment,\s
                          Reflection,\s
                          Vampire"""
        );
        for (String cap : caps) {
            String[] split = cap.split(":");
            String classname = "com.emoniph.witchery.entity.Entity" + split[0];
            float value = Float.parseFloat(split[1]);

            try {
                // all witchery bosses implement this
                LilyflowerTweaksGameConfigurationSystem.WITCHERY_DAMAGE_CAPS.put((Class<? extends IHandleDT>) Class.forName(classname), value < 0 ? Float.MAX_VALUE : value);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public void init() {
        LilyflowerTweaksGameConfigurationSystem.add("witchery", COMBAT_TWEAKS);
    }
}
