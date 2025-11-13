package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;

@SuppressWarnings("unused")
public class LOTRClassTransformerTransformer implements LilyflowerTweaksBootstrapTransformer {
    void patchBlockFire(TargetData data) {
        InsnList instructions = new InsnList();

        instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instructions.add(new InsnNode(Opcodes.ARETURN));

        data.method().instructions.insert(instructions);
    }

    void patchSpawnerAnimals(TargetData data) {
        InsnList instructions = new InsnList();

        instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instructions.add(new InsnNode(Opcodes.ARETURN));

        data.method().instructions.insert(instructions);
    }

    @Override
    public String internal$transformerTarget() {
        return "lotr/common/coremod/LOTRClassTransformer";
    }
}