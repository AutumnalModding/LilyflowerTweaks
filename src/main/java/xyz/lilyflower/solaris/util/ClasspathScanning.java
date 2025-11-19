package xyz.lilyflower.solaris.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.List;

public class ClasspathScanning {
    public static <T> List<Class<T>> interfaces(Class<T> clazz) {
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

    public static <T> List<Class<T>> subclasses(Class<T> clazz) {
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
            return result.getSubclasses(clazz).loadClasses(clazz);
        }
    }
}
