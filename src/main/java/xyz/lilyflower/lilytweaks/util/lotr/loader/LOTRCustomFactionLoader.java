package xyz.lilyflower.lilytweaks.util.lotr.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import lotr.common.LOTRDimension;
import lotr.common.fac.LOTRControlZone;
import lotr.common.fac.LOTRFaction;
import lotr.common.fac.LOTRFactionRank;
import lotr.common.fac.LOTRFactionRelations;
import lotr.common.fac.LOTRMapRegion;
import lotr.common.world.map.LOTRWaypoint;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.lilyflower.lilytweaks.core.LilyflowerTweaks;
import xyz.lilyflower.lilytweaks.util.lotr.data.CustomFactionRank;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class LOTRCustomFactionLoader implements LOTRCustomDataLoader {
    private static final Logger LOGGER = LogManager.getLogger("LOTweakR Custom Factions Loader");

    @Override
    public void run() {
        File customFactionList = new File(System.getProperty("user.dir") + "/config/lilytweaks/lotr/factions/");

        if (!customFactionList.exists()) {
            customFactionList.mkdirs();
        }

        if (!customFactionList.isDirectory()) {
            customFactionList.delete();
            customFactionList.mkdirs();
        }

        for (String path : customFactionList.list()) {
            try (Stream<String> stream = Files.lines(Paths.get(customFactionList + "/" + path), StandardCharsets.UTF_8)) {
                Object[] array = stream.toArray();
                if (array.length >= 8) {
                    int lineNo = 1;

                    String name = "PLACEHOLDER_NAME_THIS_SHOULD_NEVER_APPEAR";
                    int colour = 0xFFFFFF;
                    LOTRDimension.DimensionRegion region = LOTRDimension.DimensionRegion.WEST;
                    LOTRMapRegion map = null;
                    EnumSet<LOTRFaction.FactionType> types = EnumSet.noneOf(LOTRFaction.FactionType.class);
                    boolean warCrimes = true;
                    boolean isolation = false;
                    boolean canGetRank = true;

                    ArrayList<LOTRControlZone> controlZones = new ArrayList<>();
                    ArrayList<CustomFactionRank> ranks = new ArrayList<>();
                    HashMap<LOTRFactionRelations.Relation, ArrayList<LOTRFaction>> relations = new HashMap<>();

                    for (Object obj : array) {
                        String line = (String) obj;

                        switch (lineNo++) {
                            case 1:
                                name = line;
                                break;

                            case 2:
                                colour = Integer.parseInt(line, 16);
                                break;

                            case 3:
                                region = LOTRDimension.DimensionRegion.valueOf(line);
                                break;

                            case 4:
                                String[] mapRegionParams = line.split(" ");
                                if (mapRegionParams.length >= 3) {
                                    int x = Integer.parseInt(mapRegionParams[0]);
                                    int y = Integer.parseInt(mapRegionParams[1]);
                                    int r = Integer.parseInt(mapRegionParams[2]);
                                    
                                    map = new LOTRMapRegion(x, y, r);
                                }

                                break;

                            case 5:
                                String[] factionTypes = line.split(" ");
                                for (String type : factionTypes) {
                                    LOTRFaction.FactionType parsed = LOTRFaction.FactionType.valueOf("TYPE_" + type.toUpperCase());
                                    types.add(parsed);
                                }

                                break;

                            case 6:
                                warCrimes = Boolean.parseBoolean(line);
                                break;

                            case 7:
                                isolation = Boolean.parseBoolean(line);
                                break;

                            case 8:
                                canGetRank = Boolean.parseBoolean(line);
                                break;
                        }

                        if (line.startsWith("CONTROL ")) {
                            String desc = line.replace("CONTROL ", "");
                            String[] split = desc.split(" ");
                            if (split.length >= 2) {
                                LOTRWaypoint waypoint = LOTRWaypoint.valueOf(split[0].toUpperCase());
                                int radius = Integer.parseInt(split[1]);

                                controlZones.add(new LOTRControlZone(waypoint, radius));
                            }
                        }

                        if (line.startsWith("RANK ")) {
                            String desc = line.replace("RANK ", "");
                            String[] split = desc.split(" ");
                            if (split.length >= 5) {
                                String title = split[0];
                                int alignment = Integer.parseInt(split[1]);
                                boolean needsPledge = Boolean.parseBoolean(split[2]);
                                boolean makeChatTitle = Boolean.parseBoolean(split[3]);
                                boolean makeAchievement = Boolean.parseBoolean(split[4]);

                                ranks.add(new CustomFactionRank(title, alignment, needsPledge, makeChatTitle, makeAchievement));
                            }
                        }

                        if (line.startsWith("RELATION ")) {
                            String desc = line.replace("RELATION ", "");
                            String[] split = desc.split(" ");
                            if (split.length >= 2) {
                                LOTRFaction target = LOTRFaction.valueOf(split[0]);
                                LOTRFactionRelations.Relation relation = LOTRFactionRelations.Relation.valueOf(split[1]);

                                ArrayList<LOTRFaction> factions = relations.getOrDefault(relation, new ArrayList<>());
                                factions.add(target);
                                relations.put(relation, factions);
                            }
                        }
                    }

                    LOTRFaction faction = EnumHelper.addEnum(EnumHelperMappings.LOTR_EH_MAPPINGS, LOTRFaction.class, name, colour, LOTRDimension.MIDDLE_EARTH, region, map, types);
                    faction.approvesWarCrimes = warCrimes;
                    faction.isolationist = isolation;
                    faction.allowPlayer = canGetRank;

                    relations.forEach((relation, factions) -> factions.forEach(fac -> LOTRFactionRelations.setDefaultRelations(faction, fac, relation)));

                    Field controlZoneList = LOTRFaction.class.getDeclaredField("controlZones");
                    controlZoneList.setAccessible(true);
                    List<LOTRControlZone> zones = (List<LOTRControlZone>) controlZoneList.get(faction);
                    zones.addAll(controlZones);

                    Field ranksPrivate = LOTRFaction.class.getDeclaredField("ranksSortedDescending");
                    ranksPrivate.setAccessible(true);
                    List<LOTRFactionRank> rankList = (List<LOTRFactionRank>) ranksPrivate.get(faction);

                    boolean setPledgeRank = false;

                    for (CustomFactionRank rank : ranks) {
                        LOTRFactionRank factionRank = new LOTRFactionRank(faction, rank.alignment, rank.title, false);

                        // TODO fix this

//                        if (rank.makeAchievement) {
//                            factionRank = factionRank.makeAchievement();
//                        }

                        if (rank.makeChatTitle) {
                            factionRank = factionRank.makeTitle();
                        }

                        rankList.add(factionRank);

                        if (!setPledgeRank && rank.needsPledge) {
                            faction.setPledgeRank(factionRank);
                            setPledgeRank = true;
                        }
                    }

                    Collections.sort(rankList);

                    LilyflowerTweaks.LOGGER.info("Added faction '{}'", name);
                } else {
                    LOGGER.error("File {} is invalid! Ignoring.", path);
                }

            } catch (IOException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
