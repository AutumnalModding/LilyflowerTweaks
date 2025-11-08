package xyz.lilyflower.lilytweaks.core.transformer;

import xyz.lilyflower.lilytweaks.core.LilyflowerTweaksTransformerLoader;

@SuppressWarnings("unused")
public class LOTRClassTransformerTransformer implements LilyflowerTweaksTransformerLoader.LilyflowerTweaksClassTransformer {
    void patch_patchBlockFire(LilyflowerTweaksTransformerLoader.ClassTransformerData data) {
        LilyflowerTweaksTransformerLoader.ClassTransformerUtils.NoopClassTransformerMethod(data);
    }

    void patch_patchSpawnerAnimals(LilyflowerTweaksTransformerLoader.ClassTransformerData data) {
        LilyflowerTweaksTransformerLoader.ClassTransformerUtils.NoopClassTransformerMethod(data);
    }
}