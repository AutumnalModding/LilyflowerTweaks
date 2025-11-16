package xyz.lilyflower.lilytweaks.core.transformers.gross;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.StartupQuery;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import xyz.lilyflower.lilytweaks.api.LilyflowerTweaksBootstrapTransformer;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapSystem;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksTransformerLoadingSystem;
import xyz.lilyflower.lilytweaks.core.settings.modules.StabilityTransformerSettings;
import xyz.lilyflower.lilytweaks.util.TransformerMacros;

// "The world state is utterly corrupted and this save is NOT loadable"? Ha! Yeah, right.
public class GrossStabilityHacks$FMLContainer implements LilyflowerTweaksBootstrapTransformer {
    @Override
    public String internal$transformerTarget() {
        return "cpw/mods/fml/common/FMLContainer";
    }

    void readData(TargetData data) {
        if (StabilityTransformerSettings.STABILITY_OVERRIDES) {
            if (LilyflowerTweaksTransformerLoadingSystem.DEBUG_ENABLED) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Attempting to find calls to FMLLog & StartupQuery......");
            data.method().instructions.iterator().forEachRemaining(node -> {
                if (node instanceof MethodInsnNode method && node.getOpcode() == Opcodes.INVOKESTATIC) {
                    if (TransformerMacros.CheckStaticCall(StartupQuery.class, "abort", new Class<?>[]{}, method)) {
                        if (LilyflowerTweaksTransformerLoadingSystem.DEBUG_ENABLED) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Found StartupQuery#abort call. Removing.");
                        data.method().instructions.remove(method);
                    }

                    if (TransformerMacros.CheckStaticCall(StartupQuery.class, "notify", new Class<?>[]{String.class}, method)) {
                        if (LilyflowerTweaksTransformerLoadingSystem.DEBUG_ENABLED) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Found StartupQuery#notify call. Removing.");
                        data.method().instructions.insertBefore(method, new InsnNode(Opcodes.POP));
                        data.method().instructions.remove(method);
                    }

                    if (TransformerMacros.CheckStaticCall(FMLLog.class, "log", new Class<?>[]{Level.class, Throwable.class, String.class, Object[].class}, method)) {
                        if (LilyflowerTweaksTransformerLoadingSystem.DEBUG_ENABLED) LilyflowerTweaksBootstrapSystem.LOGGER.debug("Found FMLLog#log call. Removing.");
                        data.method().instructions.insertBefore(method, new InsnNode(Opcodes.POP));
                        data.method().instructions.insertBefore(method, new InsnNode(Opcodes.POP));
                        data.method().instructions.insertBefore(method, new InsnNode(Opcodes.POP));
                        data.method().instructions.insertBefore(method, new InsnNode(Opcodes.POP));
                        data.method().instructions.remove(method);
                    }
                }
            });
        }
    }
}
