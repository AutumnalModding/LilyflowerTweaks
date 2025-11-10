package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;

@SuppressWarnings("unused")
public class LOTRClassTransformerTransformer implements LilyflowerTweaksBootstrapTransformer {
    void patch_patchBlockFire(TargetData data) {
        InsnList instructions = new InsnList();

        instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instructions.add(new InsnNode(Opcodes.ARETURN));

        data.method().instructions.insert(instructions);
    }

    void patch_patchSpawnerAnimals(TargetData data) {
        InsnList instructions = new InsnList();

        instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instructions.add(new InsnNode(Opcodes.ARETURN));

        data.method().instructions.insert(instructions);
    }
}