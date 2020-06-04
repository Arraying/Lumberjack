package de.arraying.lumberjack;

/**
 * LLoggerFormatter represents a formatter that formats the log entry.
 */
public interface LLoggerFormatter {

    /**
     * Formats the entry.
     * This will only be called if the log level complies with the logger's level.
     * @param entry The entry.
     * @return The format.
     */
    String format(LLogEntry entry);
}
