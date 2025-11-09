package xyz.lilyflower.lilytweaks.core.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksASMSystem;

@SuppressWarnings("unused")
public class LOTRClassTransformerTransformer implements LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer {
    void patch_patchBlockFire(Data data) {
        InsnList instructions = new InsnList();

        instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instructions.add(new InsnNode(Opcodes.ARETURN));

        data.method().instructions.insert(instructions);
    }

    void patch_patchSpawnerAnimals(Data data) {
        InsnList instructions = new InsnList();

        instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instructions.add(new InsnNode(Opcodes.ARETURN));

        data.method().instructions.insert(instructions);
    }
}