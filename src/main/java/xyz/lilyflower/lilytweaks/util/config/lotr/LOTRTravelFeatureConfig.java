package xyz.lilyflower.lilytweaks.util.config.lotr;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import lotr.common.world.map.LOTRWaypoint;
import net.minecraftforge.common.config.Configuration;

public class LOTRTravelFeatureConfig {
    public static boolean UNLOCK_WAYPOINTS = false;
    public static boolean NO_WAYPOINT_LOCKING = false;
    private static List<String> DISABLED_WAYPOINTS;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
        DISABLED_WAYPOINTS = Arrays.asList(configuration.getStringList("disabledWaypoints", "lotr.travel", new String[]{},
                "List of waypoints to disable.\n" +
                "Format: Internal waypoint name - run `/ltdebug dumpWaypoints` for a list.\n" +
                "Example: 'MORANNON' would disable the Black Gate waypoint (display names and internal names often do not match!)"
        ));

        LOTRTravelFeatureConfig.UNLOCK_WAYPOINTS = configuration.getBoolean("unlockAllWaypoints", "lotr.travel", false, "Unlocks all fast travel waypoints.");
        LOTRTravelFeatureConfig.NO_WAYPOINT_LOCKING = configuration.getBoolean("disableWaypointLocking", "lotr.travel", false, "Disables alignment-based waypoint locking.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static boolean isWaypointDisabled(LOTRWaypoint waypoint) {
        return DISABLED_WAYPOINTS.contains(waypoint.name());
    }
}
