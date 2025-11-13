package xyz.lilyflower.lilytweaks.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class LilyflowerTweaksBootstrapTransformerTools {
    public static void LogMessage(InsnList list, String level, String message) {
        InsnList instructions = new InsnList();

        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/lilyflower/lilytweaks/core/LilyflowerTweaksASMSystem", "LOGGER", "Lorg/apache/logging/log4j/Logger;"));
        instructions.add(new LdcInsnNode(message));
        instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", level, "(Ljava/lang/String;)V", true));

        list.add(instructions);
    }

    public static void KillJVM(InsnList list, int code) {
        InsnList instructions = new InsnList();

        instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "cpw/mods/fml/common/FMLCommonHandler", "instance", "()Lcpw/mods/fml/common/FMLCommonHandler;", false));
        instructions.add(new IntInsnNode(Opcodes.SIPUSH, code));
        instructions.add(new InsnNode(Opcodes.ICONST_1));
        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/FMLCommonHandler", "exitJava", "(IZ)V", false));

        list.add(instructions);
    }

    public static void PrepareItemForRegister(InsnList list, LabelNode exit, String target, String original, String owner, boolean raw) {
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, owner, original, "Lnet/minecraft/item/Item;"));
        list.add(new JumpInsnNode(Opcodes.IF_ACMPNE, exit));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new LdcInsnNode((raw ? "" : "$APPLYPREFIX$") + target)); // see GameDataTransformer
    }

    // TODO: actually implement this
    public static void CancelRegistrationForID(InsnList list, int index) {}
}
