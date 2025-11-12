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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.reflections.Reflections;

public class LilyflowerTweaksTransformerLoadingSystem implements ClassFileTransformer {
    private static final HashMap<String, Class<? extends LilyflowerTweaksBootstrapTransformer>> TRANSFORMERS = new HashMap<>();

    @Override
    @SuppressWarnings("deprecation") // 'since java 9' yeah good thing this is java 8 then
    public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes) {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(3);
        reader.accept(node, 0);

        String target = name.replaceAll(".*/", "").replaceAll("\\.", "$");
        if (TRANSFORMERS.containsKey(target)) {
            try {
                LilyflowerTweaksBootstrapSystem.LOGGER.debug("Found possible target class '{}'...", name);
                Class<? extends LilyflowerTweaksBootstrapTransformer> transformer = TRANSFORMERS.get(target);
                LilyflowerTweaksBootstrapTransformer instance = transformer.newInstance();

                Method anticlobber = transformer.getDeclaredMethod("lilyflower$anticlobber");
                anticlobber.setAccessible(true);

                String result = (String) anticlobber.invoke(instance);
                LilyflowerTweaksBootstrapSystem.LOGGER.debug("Checking anticlobber for {} against {}!", name, result);
                if (result.equals(name)) {
                    ArrayList<String> methods = new ArrayList<>();
                    LilyflowerTweaksBootstrapSystem.LOGGER.debug("Anticlobber match success for {}. Hit it!", name);
                    LilyflowerTweaksBootstrapSystem.LOGGER.debug("Scanning available patches...");
                    Arrays.stream(transformer.getDeclaredMethods()).iterator().forEachRemaining(method -> methods.add(method.getName()));

                    LilyflowerTweaksBootstrapSystem.LOGGER.debug("Scanning class methods...");
                    for (MethodNode method : node.methods) {
                        LilyflowerTweaksBootstrapSystem.LOGGER.debug("Trying method {}...", method.name);
                        if (methods.contains(method.name.replaceAll("<", "$").replaceAll(">", "$"))) {
                            LilyflowerTweaksBootstrapSystem.LOGGER.debug("Transforming method {}...", method.name);
                            invoke(transformer, instance, node, method);
                        }
                    }

                    if (methods.contains("metadata")) {
                        invoke(transformer, instance, node, null);
                    }

                    node.accept(writer);
                    bytes = writer.toByteArray();
                    LilyflowerTweaksBootstrapSystem.LOGGER.debug("Finished targeting class {}!", name);
                }
            } catch (Throwable exception) { // this is bad practice but fuck it, do it anyway
                LilyflowerTweaksBootstrapSystem.ohno("FAILED TO TRANSFORM CLASS: " + name, exception);
            }
        }

        if (Files.exists(Paths.get(".classes/"))) {
            File dump = new File(".classes/" + name.replaceAll("/", "∕") + ".class");
            try (FileOutputStream output = new FileOutputStream(dump)) {
                output.write(bytes);
            } catch (IOException exception) {
                LilyflowerTweaksBootstrapSystem.ohno("FAILED TO DUMP CLASS: " + name, exception);
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

    private static void invoke(Class<? extends LilyflowerTweaksBootstrapTransformer> transformer, LilyflowerTweaksBootstrapTransformer instance, ClassNode clazz, @Nullable MethodNode method) {
        try {
            Method patcher = transformer.getDeclaredMethod(method == null ? "metadata" : method.name, LilyflowerTweaksBootstrapTransformer.TargetData.class);
            patcher.setAccessible(true);
            patcher.invoke(instance, new LilyflowerTweaksBootstrapTransformer.TargetData(clazz, method));
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException exception) {
            LilyflowerTweaksBootstrapSystem.ohno("FAILED TO TRANSFORM CLASS" + (method == null ? " METADATA" : ":" + method.name), exception);
        }
    }
}
