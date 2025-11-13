package xyz.lilyflower.lilytweaks.core;

import com.github.bsideup.jabel.Desugar;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public interface LilyflowerTweaksBootstrapTransformer {
    @Desugar
    record TargetData(ClassNode node, MethodNode method) {}

    String internal$transformerTarget();
}
