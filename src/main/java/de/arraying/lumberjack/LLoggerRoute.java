package de.arraying.lumberjack;

/**
 * LLoggerRoute is an interface for a logger route.
 * It just handles a formatted log entry and deals with the output.
 */
public interface LLoggerRoute {

    /**
     * Should log the message.
     * The default implementation of this returns an immediate void.
     * @param message The message, fully formatted, and ready to be output.
     */
    default void log(String message) throws Exception {}

    /**
     * Should log the message.
     * This entry should not be given a custom format, please specify a {@link LLoggerFormatter} for this instead.
     * It only exists to provide information the message does not.
     * @param entry The entry, before it has been formatted.
     * @param message The message, fully formatted, and ready to be output.
     */
    default void log(LLogEntry entry, String message) throws Exception {}
}
