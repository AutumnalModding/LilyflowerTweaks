package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;

public class EntitiesTransformer implements LilyflowerTweaksBootstrapTransformer {
    void init(TargetData data) {
        data.method().instructions.insert(new InsnNode(Opcodes.RETURN));
    }

    @Override
    public String lilyflower$anticlobber() {
        return "com/vicmatskiv/mw/CommonProxy";
    }
}
