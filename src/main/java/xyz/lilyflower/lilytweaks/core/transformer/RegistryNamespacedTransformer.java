package xyz.lilyflower.lilytweaks.core.transformer;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.VarInsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksASMSystem;

public class RegistryNamespacedTransformer implements LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer {
    void patch_addObject(Data data) {
        InsnList list = new InsnList();
        LilyflowerTweaksASMSystem.ClassTransformerUtils.CancelRegistrationForID(list, 422);
        data.method().instructions.insert(list);
    }
}
