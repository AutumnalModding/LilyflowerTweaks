package xyz.lilyflower.lotweakr.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class LOTweakRTransformer implements IClassTransformer {
    public LOTweakRTransformer() {}

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (name.contains("LOTRClassTransformer")) {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(basicClass);
            ClassWriter writer = new ClassWriter(3);
            reader.accept(node, 0);

            for (MethodNode method : node.methods) {
                if (method.name.equals("patchBlockFire")) {
                    InsnList instructions = new InsnList();
                    instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    instructions.add(new InsnNode(Opcodes.ARETURN));

                    method.instructions.insert(instructions);
                    node.accept(writer);
                    return writer.toByteArray();
                }
            }
        }

        return basicClass;
    }
}