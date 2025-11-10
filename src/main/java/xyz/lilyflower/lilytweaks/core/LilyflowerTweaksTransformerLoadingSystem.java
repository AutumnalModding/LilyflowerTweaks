package xyz.lilyflower.lilytweaks.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.reflections.Reflections;

public class LilyflowerTweaksTransformerLoadingSystem implements ClassFileTransformer {
    private static final HashMap<String, Class<? extends LilyflowerTweaksBootstrapTransformer>> TRANSFORMERS = new HashMap<>();

    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes) {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(3);
        reader.accept(node, 0);

        String target = name.replaceAll(".*/", "").replaceAll("\\.", "$");
        if (TRANSFORMERS.containsKey(target)) {
            LilyflowerTweaksBootstrapSystem.LOGGER.debug("Transforming class '{}'...", name);
            try {
                Class<? extends LilyflowerTweaksBootstrapTransformer> transformer = TRANSFORMERS.get(target);
                LilyflowerTweaksBootstrapTransformer instance = transformer.newInstance();

                ArrayList<String> methods = new ArrayList<>();
                LilyflowerTweaksBootstrapSystem.LOGGER.debug("Scanning available patches...");
                for (Method method : transformer.getDeclaredMethods()) {
                    if (method.getName().startsWith("patch_")) {
                        methods.add(method.getName());
                    }
                }

                LilyflowerTweaksBootstrapSystem.LOGGER.debug("Scanning class methods...");
                for (MethodNode method : node.methods) {
                    LilyflowerTweaksBootstrapSystem.LOGGER.debug("Trying method {}...", method.name);
                    if (methods.contains("patch_" + method.name)) {
                        LilyflowerTweaksBootstrapSystem.LOGGER.debug("Transforming method '{}'...", method.name);
                        Method patcher = transformer.getDeclaredMethod("patch_" + method.name, LilyflowerTweaksBootstrapTransformer.TargetData.class);
                        patcher.setAccessible(true);
                        patcher.invoke(instance, new LilyflowerTweaksBootstrapTransformer.TargetData(node, method));
                    }
                }

                node.accept(writer);
                bytes = writer.toByteArray();
            } catch (NoSuchMethodException | NullPointerException | IllegalAccessException | InvocationTargetException | InstantiationException exception) {
                LilyflowerTweaksBootstrapSystem.LOGGER.fatal("/// FAILED TRANSFORMING CLASS: '{}' ///", name);
                for (StackTraceElement element : exception.getStackTrace()) {
                    LilyflowerTweaksBootstrapSystem.LOGGER.fatal(element.toString());
                }
            }
        }

        if (Files.exists(Paths.get(".classes/"))) {
            File dump = new File(".classes/" + name.replaceAll("/", "∕") + ".class");
            try (FileOutputStream output = new FileOutputStream(dump)) {
                output.write(bytes);
            } catch (IOException exception) {
                LilyflowerTweaksBootstrapSystem.LOGGER.fatal("/// FAILED DUMPING CLASS: '{}' ///", name);
                for (StackTraceElement element : exception.getStackTrace()) {
                    LilyflowerTweaksBootstrapSystem.LOGGER.fatal(element.toString());
                }
            }
        }

        return bytes;
    }

    static {
        LilyflowerTweaksBootstrapSystem.LOGGER.debug("Scanning class transformers...");
        Reflections reflections = new Reflections("xyz.lilyflower.lilytweaks.core.transformers");
        Set<Class<? extends LilyflowerTweaksBootstrapTransformer>> classes = reflections.getSubTypesOf(LilyflowerTweaksBootstrapTransformer.class);

        for (Class<? extends LilyflowerTweaksBootstrapTransformer> clazz : classes) {
            String name = clazz.getSimpleName();
            String transformer = name.substring(0, name.length() - 11); // remove "Transformer" in the name
            TRANSFORMERS.put(transformer, clazz);
        }

        TRANSFORMERS.forEach((transformer, clazz) -> LilyflowerTweaksBootstrapSystem.LOGGER.debug("Added class transformer for {}", transformer));
    }
}
