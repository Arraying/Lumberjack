package de.arraying.lumberjack;

/**
 * Represents a logging level.
 * The levels are defined in ascending order, and contain all levels below.
 * For example {@link #DEBUG} contains logs for {@link #FATAL} and {@link #INFO} as well.
 */
public enum LLogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    FATAL,
    NONE
}
