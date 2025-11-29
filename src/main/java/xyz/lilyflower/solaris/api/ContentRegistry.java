package xyz.lilyflower.solaris.api;

import java.util.ArrayList;
import xyz.lilyflower.solaris.util.FifteenthCompetingStandard;

public interface ContentRegistry<T> {
    ArrayList<FifteenthCompetingStandard.Pair<T, String>> contents();
    void register(FifteenthCompetingStandard.Pair<T, String> pair);
    boolean valid(String key);
    boolean runnable();
}
