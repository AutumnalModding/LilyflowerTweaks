package xyz.lilyflower.lilytweaks.core;

import cpw.mods.fml.common.FMLCommonHandler;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import org.reflections.Reflections;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.security.ProtectionDomain;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationTargetException;

public class LilyflowerTweaksTransformerLoadingSystem implements ClassFileTransformer {
    private static final HashMap<String, Class<? extends LilyflowerTweaksBootstrapTransformer>> TRANSFORMERS = new HashMap<>();

    @Override
    @SuppressWarnings("deprecation") // 'since java 9' yeah good thing this is java 8 then
    public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes) {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(3);
        reader.accept(node, 0);

        name = reader.getClassName();
        boolean debug = Files.exists(Paths.get(".classes/"));
        if (debug) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Detected classload for {}", name);
        if (TRANSFORMERS.containsKey(name)) {
            try {
                boolean modified = false;
                Class<? extends LilyflowerTweaksBootstrapTransformer> transformer = TRANSFORMERS.get(name);
                LilyflowerTweaksBootstrapSystem.LOGGER.debug("Found class transformer {} - running it!!", transformer.getSimpleName());
                LilyflowerTweaksBootstrapTransformer instance = transformer.newInstance();
                ArrayList<String> methods = new ArrayList<>();
                if (debug) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Scanning available patches...");
                Arrays.stream(transformer.getDeclaredMethods()).iterator().forEachRemaining(method -> methods.add(method.getName()));

                if (debug) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Scanning class methods...");
                for (MethodNode method : node.methods) {
                    if (debug) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Trying method {}...", method.name);
                    if (methods.contains(method.name.replaceAll("<", "\\$").replaceAll(">", "\\$"))) {
                        if (debug) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Transforming method {}...", method.name);
                        modified |= invoke(transformer, instance, node, method);
                    }
                }

                if (methods.contains("metadata")) {
                    if (debug) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Transforming class metadata...");
                    modified |= invoke(transformer, instance, node, null);
                }

                if (modified) {
                    node.accept(writer);
                    bytes = writer.toByteArray();
                    if (debug) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Finished targeting class {}!", name);
                } else {
                    if (debug) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Did not modify class.");
                }
            } catch (Throwable exception) { // this is bad practice but fuck it, do it anyway
                LilyflowerTweaksBootstrapSystem.ohno("FAILED TRANSFORMING CLASS: " + name, exception);
            }
        }

        if (debug) {
            File dump = new File(".classes/" + name.replaceAll("/", "∕") + ".class");
            try (FileOutputStream output = new FileOutputStream(dump)) {
                output.write(bytes);
            } catch (IOException exception) {
                LilyflowerTweaksBootstrapSystem.ohno("FAILED DUMPING CLASS: " + name, exception);
            }
        }

        return bytes;
    }

    static {
        LilyflowerTweaksBootstrapSystem.LOGGER.debug("Scanning class transformers...");
        Reflections reflections = new Reflections("xyz.lilyflower.lilytweaks.core.transformers");
        Set<Class<? extends LilyflowerTweaksBootstrapTransformer>> classes = reflections.getSubTypesOf(LilyflowerTweaksBootstrapTransformer.class);

        for (Class<? extends LilyflowerTweaksBootstrapTransformer> clazz : classes) {
            try {
                Constructor<? extends LilyflowerTweaksBootstrapTransformer> constructor = clazz.getConstructor();
                LilyflowerTweaksBootstrapTransformer transformer = constructor.newInstance();
                String target = transformer.internal$transformerTarget();
                TRANSFORMERS.put(target, clazz);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException exception) {
                LilyflowerTweaksBootstrapSystem.ohno("FAILED LOADING CLASS TRANSFORMER: " + clazz.getSimpleName(), exception);
            }
        }

        TRANSFORMERS.forEach((target, clazz) -> LilyflowerTweaksBootstrapSystem.LOGGER.debug("Registered class transformer {} targeting {}!", clazz.getSimpleName(), target));
    }

    private static boolean invoke(Class<? extends LilyflowerTweaksBootstrapTransformer> transformer, LilyflowerTweaksBootstrapTransformer instance, ClassNode clazz, @Nullable MethodNode method) {
        try {
            Method patcher = transformer.getDeclaredMethod(method == null ? "metadata" : method.name, LilyflowerTweaksBootstrapTransformer.TargetData.class);
            patcher.setAccessible(true);

            Integer hash = null;
            Integer node = null;
            if (method != null) {
                hash = compute(method.instructions);
            } else {
                node = compute(clazz);
            }

            patcher.invoke(instance, new LilyflowerTweaksBootstrapTransformer.TargetData(clazz, method));
            if (hash != null) {
                int computed = compute(method.instructions);
                LilyflowerTweaksBootstrapSystem.LOGGER.debug("Hash, original: {}", hash);
                LilyflowerTweaksBootstrapSystem.LOGGER.debug("Hash, computed: {}", computed);
                return computed != hash;
            } else {
                return node != compute(clazz);
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException exception) {
            LilyflowerTweaksBootstrapSystem.ohno("FAILED TRANSFORMING CLASS" + (method == null ? " METADATA" : ":" + method.name), exception);
        }

        return false;
    }

    private static int compute(InsnList list) {
        AtomicInteger hash = new AtomicInteger(list.size());
        list.iterator().forEachRemaining(node -> hash.set((31 * hash.get()) + node.getOpcode()));
        return hash.get();
    }

    private static int compute(ClassNode node) {
        int hash = node.access;
        hash = 31 * hash + node.name.hashCode();
        hash = 31 * hash + (node.superName != null ? node.superName.hashCode() : 0);
        hash = 31 * hash + node.interfaces.hashCode();
        hash = 31 * hash + node.methods.size();
        hash = 31 * hash + node.fields.size();
        return hash;
    }
}
