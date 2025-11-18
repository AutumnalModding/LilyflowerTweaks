package xyz.lilyflower.solaris.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import xyz.lilyflower.solaris.core.SolarisBootstrap;
import xyz.lilyflower.solaris.core.SolarisTransformer;
import xyz.lilyflower.solaris.debug.LoggingHelper;

public class TransformerMacros {
    public static void LogMessage(InsnList list, String level, String message) {
        InsnList instructions = new InsnList();

        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/lilyflower/solaris/core/SolarisBootstrap", "LOGGER", "Lorg/apache/logging/log4j/Logger;"));
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

    public static void GetStaticField(Class<?> clazz, String name, InsnList list) {
        try {
            String owner = Type.getInternalName(clazz);
            Field field = clazz.getDeclaredField(name);
            String descriptor = Type.getDescriptor(field.getType());
            list.add(new FieldInsnNode(Opcodes.GETSTATIC, owner, name, descriptor));
        } catch (Throwable exception) {
            LoggingHelper.oopsie(SolarisBootstrap.LOGGER, "FAILED GETTING FIELD: " + name, exception);
        }
    }

    public static boolean CheckMethodCall(Class<?> clazz, String name, Class<?>[] arguments, MethodInsnNode node) {
        try {
            String owner = Type.getInternalName(clazz);
            Method method = clazz.getDeclaredMethod(name, arguments);
            String descriptor = Type.getMethodDescriptor(method);

            if (SolarisTransformer.DEBUG_ENABLED) SolarisBootstrap.LOGGER.debug("Validating {}#{}{} against {}#{}{}", owner, name, descriptor, node.owner, node.name, node.desc);
            return node.owner.equals(owner) && node.desc.equals(descriptor);
        } catch (Throwable exception) {
            LoggingHelper.oopsie(SolarisBootstrap.LOGGER, "FAILED VERFIYING CALL: " + name, exception);
        }

        return false;
    }

    public static void KillMethodCall(Class<?> clazz, String name, Class<?>[] arguments, InsnList list) {
        list.iterator().forEachRemaining(node -> {
            if (!(node instanceof MethodInsnNode method)) return;
            if (!CheckMethodCall(clazz, name, arguments, method)) return;
            Type descriptor = Type.getMethodType(method.desc);
            for (Type argument : descriptor.getArgumentTypes()) {
                list.insertBefore(method, new InsnNode(argument.getSize() == 2 ? Opcodes.POP2 : Opcodes.POP));
            }
            if (method.getOpcode() != Opcodes.INVOKESTATIC) list.insertBefore(method, new InsnNode(Opcodes.POP));
            if (SolarisTransformer.DEBUG_ENABLED) SolarisBootstrap.LOGGER.debug("Killing call to {}#{}{}", method.owner, method.name, method.desc);
            list.remove(method);
        });
    }

    public static MethodInsnNode AddNoopCall() {
        return new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(TransformerMacros.class), "__INTERNAL_NOOP", "()V", false);
    }

    public static void __INTERNAL_NOOP() {}

    // TODO: actually implement this
    public static void CancelRegistrationForID(InsnList list, int index) {}
}
