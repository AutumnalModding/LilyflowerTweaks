package xyz.lilyflower.lilytweaks.core;

import com.github.bsideup.jabel.Desugar;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.reflections.Reflections;
import xyz.lilyflower.lilytweaks.util.data.FakeTransformerExclusions;

@SuppressWarnings("unchecked")
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.SortingIndex(-1)
@IFMLLoadingPlugin.TransformerExclusions({"xyz.lilyflower.lilytweaks", "org.reflections", "org.slf4j", "javassist"})
public class LilyflowerTweaksASMSystem implements IFMLLoadingPlugin {
    public static final Logger LOGGER = LogManager.getLogger("Lilyflower Tweaks Class Transformer System");

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{TransformerLoadingSystem.class.getName()};
    }
    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    // CURSED REFLECTION MY BELOVED
    static {
        ClassLoader cl = LilyflowerTweaksASMSystem.class.getClassLoader();
        if (cl instanceof LaunchClassLoader loader) {
            try {
                Field field = loader.getClass().getDeclaredField("transformerExceptions");
                field.setAccessible(true);
                Object obj = field.get(loader);

                if (obj instanceof Set) {
                    Set<String> set = (Set<String>) obj;
                    FakeTransformerExclusions exclusions = new FakeTransformerExclusions();
                    exclusions.addAll(set);
                    field.set(loader, exclusions);
                }
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                LOGGER.fatal("// LAUNCH FAILURE //");
                exception.printStackTrace();
                FMLCommonHandler.instance().exitJava(1, true);
            }
        }
    }

    public interface LilyflowerTweaksClassTransformer {
        @Desugar
        record Data(ClassNode node, MethodNode method) {}
    }

    @SuppressWarnings({"deprecation", "CallToPrintStackTrace"})
    public static class TransformerLoadingSystem implements IClassTransformer {
        private static final HashMap<String, Class<? extends LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer>> TRANSFORMERS = new HashMap<>();
    
        @SuppressWarnings("unused")
        public static byte[] run(String name, byte[] bytes) {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(3);
            reader.accept(node, 0);
    
            try {
                String clazz = ClassUtils.getShortClassName(name).replaceAll("\\.", "");
                if (TRANSFORMERS.containsKey(clazz)) {
                    LOGGER.debug("Transforming class '{}'...", name);
                    Class<? extends LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer> transformer = TRANSFORMERS.get(clazz);
                    LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer instance = transformer.newInstance();
    
    
                    ArrayList<String> methods = new ArrayList<>();
                    for (Method method : transformer.getDeclaredMethods()) {
                        if (method.getName().startsWith("patch_")) {
                            methods.add(method.getName());
                        }
                    }
    
                    for (MethodNode method : node.methods) {
                        if (methods.contains("patch_" + method.name)) {
                            LOGGER.debug("Transforming method '{}'...", method.name);
                            Method patcher = transformer.getDeclaredMethod("patch_" + method.name, LilyflowerTweaksClassTransformer.Data.class);
                            patcher.setAccessible(true);
                            patcher.invoke(instance, new LilyflowerTweaksClassTransformer.Data(node, method));
                        }
                    }
    
                    node.accept(writer);
                    bytes = writer.toByteArray();
                }
            } catch (NoSuchMethodException | NullPointerException exception) {
                exception.printStackTrace();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                LOGGER.fatal("// LAUNCH FAILED //");
                exception.printStackTrace();
                FMLCommonHandler.instance().exitJava(1, true);
            }
    
            File clazz = new File("classes/" + ClassUtils.getShortClassName(name).replaceAll("\\.", "\\$") + ".class");
            try (FileOutputStream output = new FileOutputStream(clazz)) {
                output.write(bytes);
            } catch (IOException ignored) {}
    
            return bytes;
        }
    
        static {
            LOGGER.debug("Scanning class transformers...");
            Reflections reflections = new Reflections("xyz.lilyflower.lilytweaks.core.transformer");
            Set<Class<? extends LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer>> classes = reflections.getSubTypesOf(LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer.class);
    
            for (Class<? extends LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer> clazz : classes) {
                String name = clazz.getSimpleName();
                String transformer = name.substring(0, name.length() - 11); // remove "Transformer" in the name
                TRANSFORMERS.put(transformer, clazz);
            }
    
            TRANSFORMERS.forEach((transformer, clazz) -> LOGGER.debug("Added class transformer for {}", transformer));
        }
    
        @Override
        public byte[] transform(String name, String transformedName, byte[] basicClass) {
            return run(name, basicClass);
        }

    }

    @SuppressWarnings("unused")
    public static class ClassTransformerUtils {
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
            list.add(new LdcInsnNode((raw ? "" : "$APPLYPREFIX$") + target)); // An injection into GameData#addPrefix is used to make this work.
        }

        public static void CancelRegistrationForID(InsnList list, int index) {

        }
    }
}

