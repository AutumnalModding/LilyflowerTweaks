package xyz.lilyflower.lilytweaks.core.transformer;

import org.objectweb.asm.Opcodes;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksASMSystem;

@SuppressWarnings("unused")
public class FMLControlledNamespacedRegistryTransformer implements LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer {
    void patch_addObjectRaw(Data data) {
        data.method().access = Opcodes.ACC_PUBLIC;
    }
}
