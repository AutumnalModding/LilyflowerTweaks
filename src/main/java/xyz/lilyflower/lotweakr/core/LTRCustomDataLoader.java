package xyz.lilyflower.lotweakr.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import xyz.lilyflower.lotweakr.util.config.loader.CustomFactionLoader;
import xyz.lilyflower.lotweakr.util.config.loader.CustomInvasionLoader;
import xyz.lilyflower.lotweakr.util.config.loader.FactionRelationLoader;

public interface LTRCustomDataLoader {
    List<LTRCustomDataLoader> LOADERS = new ArrayList<>();

    void run();

    static void add(LTRCustomDataLoader loader) {
        LOADERS.add(loader);
    }

    static void runAll() {
        add(new CustomFactionLoader());
        add(new CustomInvasionLoader());
        add(new FactionRelationLoader());

        LOADERS.forEach(LTRCustomDataLoader::run);
    }
}
