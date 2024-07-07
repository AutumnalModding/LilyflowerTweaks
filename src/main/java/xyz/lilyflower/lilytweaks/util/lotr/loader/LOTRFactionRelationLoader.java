package xyz.lilyflower.lilytweaks.util.lotr.loader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.stream.Stream;
import lotr.common.fac.LOTRFaction;
import lotr.common.fac.LOTRFactionRelations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LOTRFactionRelationLoader implements LOTRCustomDataLoader {
    private static final Logger LOGGER = LogManager.getLogger("LOTweakR Faction Relation Loader");

    public static final HashMap<LOTRFactionRelations.FactionPair, LOTRFactionRelations.Relation> RELATIONS = new HashMap<>();

    @Override
    public void run() {
        File relations = new File(System.getProperty("user.dir") + "/config/lilytweaks/lotr/relations.txt");

        if (relations.exists()) {
            try (Stream<String> stream = Files.lines(relations.toPath(), StandardCharsets.UTF_8)) {
                stream.forEach(line -> {
                    String[] split = line.split(" ");
                    if (split.length > 2) {
                        LOTRFactionRelations.FactionPair pair = new LOTRFactionRelations.FactionPair(LOTRFaction.forName(split[0]), LOTRFaction.forName(split[2]));
                        LOTRFactionRelations.Relation relation = LOTRFactionRelations.Relation.forName(split[1]);

                        if (pair.getLeft() == null || pair.getRight() == null ) {
                            LOGGER.error("Entry has invalid faction name in relations file! Skipping.");
                        } else if (relation == null) {
                            LOGGER.error("Entry has invalid relation in relations file! Skipping.");
                        } else {
                            RELATIONS.put(pair, relation);
                        }
                    } else {
                        LOGGER.error("Entry has invalid line length in relations file! Skipping.");
                    }
                });
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
