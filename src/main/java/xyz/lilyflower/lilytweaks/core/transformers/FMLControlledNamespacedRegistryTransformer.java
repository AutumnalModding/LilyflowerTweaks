package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;

@SuppressWarnings("unused")
public class FMLControlledNamespacedRegistryTransformer implements LilyflowerTweaksBootstrapTransformer {
    void patch_addObjectRaw(TargetData data) {
        data.method().access = Opcodes.ACC_PUBLIC;
    }

    void patch_validateContent(TargetData data) {
        data.method().instructions.insert(new InsnNode(Opcodes.RETURN));
    }
}
