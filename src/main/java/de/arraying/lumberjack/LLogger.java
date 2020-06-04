package de.arraying.lumberjack;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * LLogger is the logger definition.
 * The logger itself is very flexible and can be implemented in various ways.
 */
public interface LLogger {

    /**
     * Gets the name of the logger.
     * @return The logger name, never null.
     */
    String getName();

    /**
     * Logs a message.
     * @param level The log level, must not be null and cannot be {@link LLogLevel#NONE}.
     * @param message The message, can be null.
     * @param format The formatting objects, can be null.
     * @return A future of the log entry when the log has been logged.
     */
    ListenableFuture<LLogEntry> log(LLogLevel level, String message, Object... format);

    /**
     * Alias for {@link #log(LLogLevel, String, Object...)} with level {@link LLogLevel#TRACE}.
     */
    ListenableFuture<LLogEntry> trace(String message, Object... format);

    /**
     * Alias for {@link #log(LLogLevel, String, Object...)} with level {@link LLogLevel#DEBUG}.
     */
    ListenableFuture<LLogEntry> debug(String message, Object... format);

    /**
     * Alias for {@link #log(LLogLevel, String, Object...)} with level {@link LLogLevel#INFO}.
     */
    ListenableFuture<LLogEntry> info(String message, Object... format);

    /**
     * Alias for {@link #log(LLogLevel, String, Object...)} with level {@link LLogLevel#WARN
     */
    ListenableFuture<LLogEntry> warn(String message, Object... format);

    /**
     * Alias for {@link #log(LLogLevel, String, Object...)} with level {@link LLogLevel#FATAL}.
     */
    ListenableFuture<LLogEntry> fatal(String message, Object... format);
}
