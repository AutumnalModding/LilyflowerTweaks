package xyz.lilyflower.lilytweaks.util.lotr.loader;

import cpw.mods.fml.common.FMLCommonHandler;
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
import xyz.lilyflower.lilytweaks.core.LTInit;
import xyz.lilyflower.lilytweaks.util.lotr.data.CustomFactionRank;

@SuppressWarnings({"ConstantConditions"})
public class LOTRCustomFactionLoader implements LOTRCustomDataLoader {
    public static final HashMap<LOTRFaction, ArrayList<LOTRFaction>> META_FACTIONS = new HashMap<>();
    public static final HashMap<LOTRFaction, LOTRFaction> MFRL = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger("LilyflowerTweaks Custom Factions Loader");

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
                int entries = 0;

                String name = "PLACEHOLDER_NAME_THIS_SHOULD_NEVER_APPEAR";
                int colour = Integer.MIN_VALUE;
                LOTRDimension.DimensionRegion region = null;
                LOTRMapRegion map = null;
                EnumSet<LOTRFaction.FactionType> types = null;
                boolean warCrimes = false;
                boolean isolation = false;
                boolean canGetRank = true;

                boolean ISMETA = false;
                ArrayList<LOTRFaction> metamembers = new ArrayList<>();

                ArrayList<LOTRControlZone> controlZones = new ArrayList<>();
                ArrayList<CustomFactionRank> ranks = new ArrayList<>();
                HashMap<LOTRFactionRelations.Relation, ArrayList<LOTRFaction>> relations = new HashMap<>();

                for (Object obj : array) {
                    String line = (String) obj;

                    String key = line.replaceAll(" .*", "");
                    String value = line.replace(key + " ", "");

                    switch (key) {
                        case "NAME":
                            if (name.equals("PLACEHOLDER_NAME_THIS_SHOULD_NEVER_APPEAR")) {
                                name = value;
                                entries++;
                            }
                            break;

                        case "COLOUR":
                            if (colour == Integer.MIN_VALUE) {
                                colour = Integer.parseInt(value.replace("0x", ""), 16);
                                entries++;
                            }
                            break;

                        case "REGION":
                            if (region == null) {
                                region = LOTRDimension.DimensionRegion.valueOf(value);
                                entries++;
                            }
                            break;

                        case "LOCATION":
                            if (map == null) {
                                String[] mapRegionParams = value.split(" ");
                                if (mapRegionParams.length >= 3) {
                                    int x = Integer.parseInt(mapRegionParams[0]);
                                    int y = Integer.parseInt(mapRegionParams[1]);
                                    int r = Integer.parseInt(mapRegionParams[2]);

                                    map = new LOTRMapRegion(x, y, r);
                                }
                                entries++;
                            }

                            break;

                        case "TYPE":
                            if (types == null) {
                                types = EnumSet.noneOf(LOTRFaction.FactionType.class);
                                String[] factionTypes = value.split(" ");
                                for (String type : factionTypes) {
                                    LOTRFaction.FactionType parsed = LOTRFaction.FactionType.valueOf("TYPE_" + type.toUpperCase());
                                    types.add(parsed);
                                }
                                entries++;
                            }

                            break;

                        case "WARCRIMES":
                            warCrimes = Boolean.parseBoolean(value);
                            break;

                        case "ISOLATIONIST":
                            isolation = Boolean.parseBoolean(value);
                            break;

                        case "JOINABLE":
                            canGetRank = Boolean.parseBoolean(value);
                            break;

                        case "CONTROL":
                            String[] zone = value.split(" ");
                            if (zone.length >= 2) {
                                LOTRWaypoint waypoint = LOTRWaypoint.valueOf(zone[0].toUpperCase());
                                int radius = Integer.parseInt(zone[1]);
                                System.out.println(waypoint + ", " + radius);

                                controlZones.add(new LOTRControlZone(waypoint, radius));
                            }
                            break;

                        case "RANK":
                            String[] rank = value.split(" ");
                            if (rank.length >= 5) {
                                String title = rank[0];
                                int alignment = Integer.parseInt(rank[1]);
                                boolean needsPledge = Boolean.parseBoolean(rank[2]);
                                boolean makeChatTitle = Boolean.parseBoolean(rank[3]);
                                boolean makeAchievement = Boolean.parseBoolean(rank[4]);

                                ranks.add(new CustomFactionRank(title, alignment, needsPledge, makeChatTitle, makeAchievement));
                            }
                            break;

                        case "RELATION":
                            String[] rel = value.split(" ");
                            if (rel.length >= 2) {
                                LOTRFaction target = LOTRFaction.valueOf(rel[0]);
                                LOTRFactionRelations.Relation relation = LOTRFactionRelations.Relation.valueOf(rel[1]);

                                ArrayList<LOTRFaction> factions = relations.getOrDefault(relation, new ArrayList<>());
                                factions.add(target);
                                relations.put(relation, factions);
                            }
                            break;

                        case "METAMEMBER":
                            ISMETA = true;
                            if (LOTRFaction.forName(value.toUpperCase()) != null) {
                                metamembers.add(LOTRFaction.forName(value.toUpperCase()));
                            }
                            break;
                    }
                }

                if (entries >= 5) {
                    LOTRFaction faction = EnumHelper.addEnum(EnumHelperMappings.LOTR_EH_MAPPINGS, LOTRFaction.class, name, colour, LOTRDimension.MIDDLE_EARTH, region, map, types);
                    faction.approvesWarCrimes = warCrimes;
                    faction.isolationist = isolation;
                    faction.allowPlayer = canGetRank;

                    if (ISMETA) {
                        META_FACTIONS.put(faction, metamembers);
                        for (LOTRFaction member : metamembers) {
                            MFRL.put(member, faction);
                        }
                    }

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

                    LTInit.LOGGER.info("Added faction '{}'", name);
                }

            } catch (IOException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
