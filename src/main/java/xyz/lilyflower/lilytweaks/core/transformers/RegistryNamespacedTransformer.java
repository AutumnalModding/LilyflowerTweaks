package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.tree.InsnList;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformerTools;
import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;

@SuppressWarnings("unused")
public class RegistryNamespacedTransformer implements LilyflowerTweaksBootstrapTransformer {
    void addObject(TargetData data) {
        if (LilyflowerTweaksTransformerSettingsSystem.Stability.GROSS_REGISTRY_HACKS) {
            InsnList list = new InsnList();
            LilyflowerTweaksBootstrapTransformerTools.CancelRegistrationForID(list, 422);
            data.method().instructions.insert(list);
        }
    }

    @Override
    public String lilyflower$anticlobber() {
        return "net/minecraft/util/RegistryNamespaced";
    }
}
