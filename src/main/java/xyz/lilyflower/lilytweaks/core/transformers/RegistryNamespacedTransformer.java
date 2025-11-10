package xyz.lilyflower.lilytweaks.core.transformers;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.InsnList;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformerTools;

@SuppressWarnings("unused")
public class RegistryNamespacedTransformer implements LilyflowerTweaksBootstrapTransformer {
    void patch_addObject(TargetData data) {
        if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) { // TODO: add earlyconfig
            InsnList list = new InsnList();
            LilyflowerTweaksBootstrapTransformerTools.CancelRegistrationForID(list, 422);
            data.method().instructions.insert(list);
        }
    }
}
