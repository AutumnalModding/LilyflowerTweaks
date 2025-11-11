package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;
import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;

@SuppressWarnings("unused")
public class AlfheimConfigHandlerTransformer implements LilyflowerTweaksBootstrapTransformer {
    void patch_getEnableElvenStory(TargetData data) {
        if (!LilyflowerTweaksTransformerSettingsSystem.Alfheim.ENABLE_ESM_FLIGHT && !LilyflowerTweaksTransformerSettingsSystem.Alfheim.ENABLE_ESM_RACES) {
            InsnList list = new InsnList();
            LabelNode jump = new LabelNode(new Label());
            list.add(new InsnNode(Opcodes.ICONST_0));
            list.add(new InsnNode(Opcodes.IRETURN));

            list.add(jump);
            data.method().instructions.insert(list);
        }
    }
}
