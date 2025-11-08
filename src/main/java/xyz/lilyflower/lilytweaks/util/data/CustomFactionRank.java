package xyz.lilyflower.lilytweaks.util.data;

// Poor man's record.
public class CustomFactionRank {
    public final String title;
    public final int alignment;
    public final boolean needsPledge;
    public final boolean makeChatTitle;
    public final boolean makeAchievement;

    public CustomFactionRank(String title, int alignment, boolean needsPledge, boolean makeChatTitle, boolean makeAchievement) {
        this.title = title;
        this.alignment = alignment;
        this.needsPledge = needsPledge;
        this.makeChatTitle = makeChatTitle;
        this.makeAchievement = makeAchievement;
    }
}
