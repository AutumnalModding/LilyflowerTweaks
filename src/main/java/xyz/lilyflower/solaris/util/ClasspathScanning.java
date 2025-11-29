package xyz.lilyflower.solaris.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClasspathScanning {
    private static final HashMap<Class<?>, List<Class<?>>> CACHE = new HashMap<>();

    public static <T> List<Class<T>> implementations(Class<T> clazz, boolean concrete) {
        if (!CACHE.containsKey(clazz)) {
            try (ScanResult result = new ClassGraph()
                    .enableClassInfo()
                    .rejectPackages(
                            "java.*",
                            "javax.*",
                            "sun.*",
                            "com.sun.*",
                            "com.azul.*",
                            "jdk.*",
                            "org.spongepowered.*",
                            "org.objectweb.*"
                    )
                    .scan()) {
                ClassInfoList classes = concrete ? result.getSubclasses(clazz) : result.getClassesImplementing(clazz);
                __STORE(clazz, classes.loadClasses(clazz));
            }
        }

        return __LOAD(clazz);
    }

    private static <T> void __STORE(Class<T> superclass, List<Class<T>> implementations) {
        CACHE.put(superclass, new ArrayList<>(implementations));
    }

    @SuppressWarnings("unchecked")
    private static <T> List<Class<T>> __LOAD(Class<T> superclass) {
        return (List<Class<T>>) (List<?>) CACHE.get(superclass);
    }
}
