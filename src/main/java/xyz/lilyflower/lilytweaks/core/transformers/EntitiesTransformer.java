package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;

public class EntitiesTransformer implements LilyflowerTweaksBootstrapTransformer {
    void patch_init(TargetData data) {
        // Other mods may use this classname, so we have to be exact.
        if (data.method().signature.equals("(Lcom/vicmatskiv/mw/CommonProxy;)V")) {
            data.method().instructions.insert(new InsnNode(Opcodes.RETURN));
        }
    }
}
