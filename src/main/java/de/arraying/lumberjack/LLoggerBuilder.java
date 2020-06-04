package de.arraying.lumberjack;

import de.arraying.lumberjack.internal.*;

import java.util.HashSet;
import java.util.Set;

/**
 * LLoggerBuilder is the builder that can be used to build the default implementation.
 * Other implementations may use other builders.
 * This builder is not thread safe, any concurrent usage needs to be synchronized externally.
 */
public class LLoggerBuilder {
    private final String name;
    private final Set<LLoggerRouteWrapper> routes;
    private LLoggerFormatter formatter;
    private int threadPool;

    /**
     * Private constructor with name.
     * @param name The name of the logger, cannot be null.
     */
    private LLoggerBuilder(String name) {
        this.name = name;
        // Default values.
        this.routes = new HashSet<>();
        this.formatter = new DefaultFormatter();
        this.threadPool = 1;
    }

    public static LLoggerBuilder create(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is null or empty");
        }
        return new LLoggerBuilder(name);
    }

    public LLoggerBuilder withFormatter(LLoggerFormatter formatter) {
        if (formatter == null) {
            throw new IllegalArgumentException("formatter is null");
        }
        this.formatter = formatter;
        return this;
    }

    public LLoggerBuilder withThreadPoolSize(int threadPool) {
        if (threadPool <= 0) {
            throw new IllegalArgumentException("thread pool cannot be <= 0");
        }
        this.threadPool = threadPool;
        return this;
    }

    public LLoggerBuilder withRoute(LLogLevel level, LLoggerRoute route) {
        if (level == null) {
            throw new IllegalArgumentException("level is null");
        }
        if (route == null) {
            throw new IllegalArgumentException("route is null");
        }
        this.routes.add(new LLoggerRouteWrapper(level, route));
        return this;
    }

    public LLoggerBuilder withRouteStdOut(LLogLevel level) {
        return withRoute(level, new RouteStdOut());
    }

    public LLoggerBuilder withRouteStdErr(LLogLevel level) {
        return withRoute(level, new RouteStdErr());
    }

    public LLoggerBuilder withRouteFs(LLogLevel level, LFsRules rules) {
        return withRoute(level, new RouteFs(rules));
    }

    public LLogger build() {
        return new DefaultLogger(name, routes, formatter, threadPool);
    }
}