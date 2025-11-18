package xyz.lilyflower.solaris.debug;

import org.apache.logging.log4j.Logger;

public class LoggingHelper {
    public static void oopsie(Logger logger, String message, Throwable cause) {
        logger.fatal("/// SOMEBODY SET US UP THE BOMB ///");
        logger.fatal(message);
        logger.fatal("CAUSE: {}: {}", cause.getClass().getName().replaceAll("\\[", ""), cause.getMessage());
        logger.fatal("DUMPING STACKTRACE...");
        for (StackTraceElement element : cause.getStackTrace()) {
            logger.fatal(element.toString());
        }
        logger.fatal("/// SOMEBODY SET US UP THE BOMB ///");
    }
}
