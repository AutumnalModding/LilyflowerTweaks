package xyz.lilyflower.lilytweaks.core.transformer;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksASMSystem;

@SuppressWarnings("unused")
public class InterfaceInjectorTransformer implements LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer {
    void patch_transform(Data data) {
        InsnList insns = new InsnList();

        LabelNode jump = new LabelNode(new Label());
        insns.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insns.add(new LdcInsnNode("Reika."));
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
        insns.add(new JumpInsnNode(Opcodes.IFEQ, jump));
        insns.add(new VarInsnNode(Opcodes.ALOAD, 3));
        insns.add(new InsnNode(Opcodes.ARETURN));
        insns.add(jump);

        data.method().instructions.insert(insns);
    }
}