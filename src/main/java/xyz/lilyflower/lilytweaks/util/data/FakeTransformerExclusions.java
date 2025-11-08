package xyz.lilyflower.lilytweaks.util.data;

import java.util.ArrayList;
import java.util.HashSet;

public class FakeTransformerExclusions extends HashSet<String> {
    static final ArrayList<String> DISALLOWED_PACKAGES = new ArrayList<>();

    @Override
    public boolean add(String exclusion) {
        for (String pkg : DISALLOWED_PACKAGES) {
            if (exclusion.contains(pkg)) {
                return false;
            }
        }
        return super.add(exclusion);
    }

    static {
        DISALLOWED_PACKAGES.add("lotr");
        DISALLOWED_PACKAGES.add("Reika");
    }
}
