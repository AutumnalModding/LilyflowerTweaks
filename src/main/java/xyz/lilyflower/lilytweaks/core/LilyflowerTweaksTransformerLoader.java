package xyz.lilyflower.lilytweaks.core;

import com.github.bsideup.jabel.Desugar;
import cpw.mods.fml.common.FMLCommonHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.reflections.Reflections;

@SuppressWarnings({"deprecation", "CallToPrintStackTrace"})
public class LilyflowerTweaksTransformerLoader {
    private static final HashMap<String, Class<? extends LilyflowerTweaksClassTransformer>> TRANSFORMERS = new HashMap<>();

    @SuppressWarnings("unused")
    public static byte[] run(String name, byte[] bytes) {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(3);
        reader.accept(node, 0);

        try {
            String clazz = ClassUtils.getShortClassName(name).replaceAll("\\.", "");
            if (TRANSFORMERS.containsKey(clazz)) {
                LilyflowerTweaksCoremodLoader.LOGGER.debug("Transforming class '{}'...", name);
                Class<? extends LilyflowerTweaksClassTransformer> transformer = TRANSFORMERS.get(clazz);
                LilyflowerTweaksClassTransformer instance = transformer.newInstance();


                ArrayList<String> methods = new ArrayList<>();
                for (Method method : transformer.getDeclaredMethods()) {
                    if (method.getName().startsWith("patch_")) {
                        methods.add(method.getName());
                    }
                }

                for (MethodNode method : node.methods) {
                    if (methods.contains("patch_" + method.name)) {
                        LilyflowerTweaksCoremodLoader.LOGGER.debug("Transforming method '{}'...", method.name);
                        Method patcher = transformer.getDeclaredMethod("patch_" + method.name, ClassTransformerData.class);
                        patcher.setAccessible(true);
                        patcher.invoke(instance, new ClassTransformerData(node, method));
                    }
                }

                node.accept(writer);
                bytes = writer.toByteArray();
            }
        } catch (NoSuchMethodException | NullPointerException exception) {
            exception.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            LilyflowerTweaksCoremodLoader.LOGGER.fatal("// LAUNCH FAILED //");
            exception.printStackTrace();
            FMLCommonHandler.instance().exitJava(1, true);
        }

        File clazz = new File("classes/" + ClassUtils.getShortClassName(name).replaceAll("\\.", "\\$"));
        try (FileOutputStream output = new FileOutputStream(clazz)) {
            output.write(bytes);
        } catch (IOException ignored) {}

        return bytes;
    }

    static {
        LilyflowerTweaksCoremodLoader.LOGGER.debug("Scanning class transformers...");
        Reflections reflections = new Reflections("xyz.lilyflower.psc.asm.transformers");
        Set<Class<? extends LilyflowerTweaksClassTransformer>> classes = reflections.getSubTypesOf(LilyflowerTweaksClassTransformer.class);

        for (Class<? extends LilyflowerTweaksClassTransformer> clazz : classes) {
            String name = clazz.getSimpleName();
            String transformer = name.substring(0, name.length() - 11); // remove "Transformer" in the name
            TRANSFORMERS.put(transformer, clazz);
        }

        TRANSFORMERS.forEach((transformer, clazz) -> LilyflowerTweaksCoremodLoader.LOGGER.debug("Added class transformer for {}", transformer));
    }

    public interface LilyflowerTweaksClassTransformer {}

    @Desugar
    public record ClassTransformerData(ClassNode node, MethodNode method) {}

    @SuppressWarnings("unused")
    public static class ClassTransformerUtils {
        public static void NoopClassTransformerMethod(ClassTransformerData data) {
            InsnList instructions = new InsnList();

            instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
            instructions.add(new InsnNode(Opcodes.ARETURN));

            data.method.instructions.insert(instructions);
        }

        public static void LogMessage(ClassTransformerData data, String level, String message) {
            InsnList instructions = new InsnList();

            instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/lilyflower/psc/loader/asm/StardustCoreASMLoader", "LOGGER", "Lorg/apache/logging/log4j/Logger;"));
            instructions.add(new LdcInsnNode(message));
            instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", level, "(Ljava/lang/String;)V", true));

            data.method.instructions.insert(instructions);
        }

        public static void KillJVM(ClassTransformerData data) {
            InsnList instructions = new InsnList();

            instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "cpw/mods/fml/common/FMLCommonHandler", "instance", "()Lcpw/mods/fml/common/FMLCommonHandler;", false));
            instructions.add(new InsnNode(Opcodes.ICONST_0));
            instructions.add(new InsnNode(Opcodes.ICONST_1));
            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/FMLCommonHandler", "exitJava", "(IZ)V", false));

            data.method.instructions.insert(instructions);
        }
    }
}
