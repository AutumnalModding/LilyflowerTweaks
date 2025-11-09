package xyz.lilyflower.lilytweaks.core.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksASMSystem;

public class FMLControlledNamespacedRegistryTransformer implements LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer {
    void patch_addObjectRaw(Data data) {
        data.method().access = Opcodes.ACC_PUBLIC;
    }
}
