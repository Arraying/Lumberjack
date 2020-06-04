package de.arraying.lumberjack;

/**
 * LLoggerRouteWrapper is a wrapper for a route and a specific level.
 */
public class LLoggerRouteWrapper {
    private final LLogLevel level;
    private final LLoggerRoute route;

    /**
     * Creates a new wrapper.
     * @param level The level, may not be null.
     * @param route The route, may not be null.
     */
    public LLoggerRouteWrapper(LLogLevel level, LLoggerRoute route) {
        if (level == null) {
            throw new IllegalArgumentException("level is null");
        }
        if (route == null) {
            throw new IllegalArgumentException("route is null");
        }
        this.level = level;
        this.route = route;
    }

    /**
     * Gets the level.
     * @return The level, not null.
     */
    public LLogLevel getLevel() {
        return level;
    }

    /**
     * Gets the route.
     * @return The route, not null.
     */
    public LLoggerRoute getRoute() {
        return route;
    }

    /**
     * Pretty print the wrapper.
     * @return The pretty print.
     */
    @Override
    public String toString() {
        return "LLoggerRouteWrapper{" +
            "level=" + level +
            ", route=" + route +
            '}';
    }
}
