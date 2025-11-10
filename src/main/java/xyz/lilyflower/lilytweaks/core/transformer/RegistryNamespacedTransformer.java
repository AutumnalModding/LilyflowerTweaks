package xyz.lilyflower.lilytweaks.core.transformer;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.InsnList;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksASMSystem;

@SuppressWarnings("unused")
public class RegistryNamespacedTransformer implements LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer {
    void patch_addObject(Data data) {
        if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) { // TODO: add earlyconfig
            InsnList list = new InsnList();
            LilyflowerTweaksASMSystem.ClassTransformerUtils.CancelRegistrationForID(list, 422);
            data.method().instructions.insert(list);
        }
    }
}
