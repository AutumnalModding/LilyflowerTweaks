package xyz.lilyflower.lilytweaks.core.transformers;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;
import xyz.lilyflower.lilytweaks.core.settings.LilyflowerTweaksTransformerSettingsSystem;

@SuppressWarnings("unused") // THIS [[$4.99 COREMOD]] COULD'VE BEEN A MIXIN    !
public class ItemDataTransformer implements LilyflowerTweaksBootstrapTransformer {
    void createItemStack(TargetData data) {
        if (LilyflowerTweaksTransformerSettingsSystem.Stability.DISABLE_OPENCOMPUTERS_ROBOTS) {
            InsnList list = new InsnList();

            LabelNode label = new LabelNode(new Label());
            list.add(new VarInsnNode(Opcodes.ALOAD, 0)); // But it isn't, so who cares.
            list.add(new TypeInsnNode(Opcodes.INSTANCEOF, "li/cil/oc/common/item/data/RobotData"));
            list.add(new JumpInsnNode(Opcodes.IFEQ, label));
            list.add(new InsnNode(Opcodes.ACONST_NULL));
            list.add(new InsnNode(Opcodes.ARETURN));
            list.add(label);

            data.method().instructions.insert(list);
        }
    }

    @Override
    public String internal$transformerTarget() {
        return "li/cil/oc/common/item/data/ItemData";
    }
}
