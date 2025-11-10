package xyz.lilyflower.lilytweaks.core.transformer;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksASMSystem;

@SuppressWarnings("unused")
public class AlfheimConfigHandlerTransformer implements LilyflowerTweaksASMSystem.LilyflowerTweaksClassTransformer {
    void patch_getEnableElvenStory(Data data) {
        InsnList list = new InsnList();
        LabelNode jump = new LabelNode(new Label());

        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/lilyflower/lilytweaks/settings/LilyflowerTweaksTransformerSettingsSystem$Alfheim", "ENABLE_ESM_FLIGHT", "Z"));
        list.add(new JumpInsnNode(Opcodes.IFNE, jump));

        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/lilyflower/lilytweaks/settings/LilyflowerTweaksTransformerSettingsSystem$Alfheim", "ENABLE_ESM_RACES", "Z"));
        list.add(new JumpInsnNode(Opcodes.IFNE, jump));

        list.add(new InsnNode(Opcodes.ICONST_0));
        list.add(new InsnNode(Opcodes.IRETURN));

        list.add(jump);
        data.method().instructions.insert(list);
    }
}
