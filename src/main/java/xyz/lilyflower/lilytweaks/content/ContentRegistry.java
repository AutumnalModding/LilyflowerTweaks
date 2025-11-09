package xyz.lilyflower.lilytweaks.content;

import cpw.mods.fml.common.event.FMLStateEvent;
import java.util.ArrayList;
import xyz.lilyflower.lilytweaks.util.Pair;

public interface ContentRegistry<T> {
    ArrayList<Pair<T, String>> contents();
    void register(Pair<T, String> pair);
    boolean shouldRegister(String key);
    boolean shouldRun(FMLStateEvent phase);
}
