package xyz.lilyflower.lilytweaks.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.List;

public class ClasspathScanning {
    public static <T> List<Class<T>> GetAllImplementations(Class<T> clazz) {
        try (ScanResult result = new ClassGraph()
            .enableClassInfo()
            .rejectPackages(
                "java.*",
                "javax.*",
                "sun.*",
                "jdk.*",
                "org.spongepowered.*",
                "org.objectweb.*"
            )
            .scan()) {
                return result.getClassesImplementing(clazz).loadClasses(clazz);
        }
    }
}
