package de.arraying.lumberjack;

/**
 * LLogEntry is an entry for a log.
 */
public interface LLogEntry {

    /**
     * Gets the time in milliseconds when this entry was created.
     * @return A greater than 0 long.
     */
    long getTime();

    /**
     * Gets the log level.
     * @return The level, never null.
     */
    LLogLevel getLevel();

    /**
     * Gets the thread the log was invoked on.
     * @return The thread.
     */
    Thread getThread();

    /**
     * Gets the raw unformatted message.
     * @return The unformatted message.
     */
    String getMessage();

    /**
     * Gets the formatting objects.
     * @return An array of possibly null formatting objects.
     */
    Object[] getFormattingObjects();
}
