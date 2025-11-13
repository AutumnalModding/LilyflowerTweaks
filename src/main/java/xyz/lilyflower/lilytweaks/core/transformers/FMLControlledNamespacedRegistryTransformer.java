package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;
import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;

@SuppressWarnings("unused")
public class FMLControlledNamespacedRegistryTransformer implements LilyflowerTweaksBootstrapTransformer {
    void addObjectRaw(TargetData data) {
        if (LilyflowerTweaksTransformerSettingsSystem.Stability.GROSS_REGISTRY_HACKS) {
            data.method().access = Opcodes.ACC_PUBLIC;
        }
    }

    void validateContent(TargetData data) {
        if (LilyflowerTweaksTransformerSettingsSystem.Stability.STABILITY_OVERRIDES) {
            data.method().instructions.insert(new InsnNode(Opcodes.RETURN));
        }
    }

    @Override
    public String internal$transformerTarget() {
        return "cpw/mods/fml/common/registry/FMLControlledNamespacedRegistry";
    }
}
