package de.arraying.lumberjack;

import java.io.File;

/**
 * LFsRules is a set of file system rules the logger should use.
 */
public interface LFsRules {

    /**
     * Gets the logging directory.
     * @return The directory logs should go into, not null.
     */
    File getDirectory();

    /**
     * The line limit per log.
     * @return less than or equal to 0 for no line limit, otherwise the line limit.
     */
    int getLineLimit();

    /**
     * The time limit per log in milliseconds.
     * Time is started from the first log entry.
     * @return less than or equal to 0 for no time limit, otherwise the time limit.
     */
    long getTimeLimit();

    /**
     * Formats the file name.
     * @param time The current time in milliseconds.
     * @param uid The unique ID for the timestamp.
     * @return A formatted string, must not be null.
     */
    default String formatFileName(long time, long uid) {
        return String.format("%d-%d.txt", time, uid);
    }
}
