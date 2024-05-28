package xyz.lilyflower.lilytweaks.util.lotr.config.loader;

import java.util.ArrayList;
import java.util.List;

public interface LOTRCustomDataLoader {
    List<LOTRCustomDataLoader> LOADERS = new ArrayList<>();

    void run();

    static void add(LOTRCustomDataLoader loader) {
        LOADERS.add(loader);
    }

    static void runAll() {
        add(new LOTRCustomFactionLoader());
        add(new LOTRCustomInvasionLoader());
        add(new LOTRFactionRelationLoader());

        LOADERS.forEach(LOTRCustomDataLoader::run);
    }
}
