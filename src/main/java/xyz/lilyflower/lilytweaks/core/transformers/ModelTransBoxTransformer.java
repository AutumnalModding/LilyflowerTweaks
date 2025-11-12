package xyz.lilyflower.lilytweaks.core.transformers;

import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksBootstrapTransformer;


@SuppressWarnings("unused")
public class ModelTransBoxTransformer implements LilyflowerTweaksBootstrapTransformer {
    void metadata(TargetData data) {
        data.node().superName = "net/minecraft/client/model/ModelBox";
    }

    @Override
    public String lilyflower$anticlobber() {
        return "com/unascribed/ears/ModelTransBox";
    }
}
