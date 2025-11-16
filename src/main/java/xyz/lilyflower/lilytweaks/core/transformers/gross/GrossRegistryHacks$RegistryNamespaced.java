package xyz.lilyflower.lilytweaks.core.transformers.gross;

import org.objectweb.asm.tree.InsnList;
import xyz.lilyflower.lilytweaks.api.LilyflowerTweaksBootstrapTransformer;
import xyz.lilyflower.lilytweaks.core.settings.modules.StabilityTransformerSettings;
import xyz.lilyflower.lilytweaks.util.TransformerMacros;

@SuppressWarnings("unused")
public class GrossRegistryHacks$RegistryNamespaced implements LilyflowerTweaksBootstrapTransformer {
    void addObject(TargetData data) {
        if (StabilityTransformerSettings.GROSS_REGISTRY_HACKS) {
            InsnList list = new InsnList();
            TransformerMacros.CancelRegistrationForID(list, 422);
            data.method().instructions.insert(list);
        }
    }

    @Override
    public String internal$transformerTarget() {
        return "net/minecraft/util/RegistryNamespaced";
    }
}
