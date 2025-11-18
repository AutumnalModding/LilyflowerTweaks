package xyz.lilyflower.solaris.integration.galacticraft;

import java.util.ArrayList;
import micdoodle8.mods.galacticraft.api.galaxies.Star;

public class StarRegistry {
    public static final ArrayList<Star> STARS = new ArrayList<>();

    public static boolean add(Star star) {
        if (STARS.contains(star)) return false;
        STARS.add(star);
        return true;
    }
}
