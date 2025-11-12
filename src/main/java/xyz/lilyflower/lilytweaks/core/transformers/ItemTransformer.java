package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;

@SuppressWarnings("unused")
public class ItemTransformer implements LilyflowerTweaksBootstrapTransformer {
    void func_111206_d(TargetData data) {
        InsnList insns = new InsnList();

        insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insns.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;", false));
        insns.add(new LdcInsnNode("\\.png"));
        insns.add(new LdcInsnNode(""));
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "replaceAll", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false));
        insns.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/item/Item", "itemIcon", "Ljava/lang/String;"));
        insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insns.add(new InsnNode(Opcodes.ARETURN));

        data.method().instructions.insert(insns);
    }

    @Override
    public String lilyflower$anticlobber() {
        return "net/minecraft/item/Item";
    }
}
