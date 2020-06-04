package de.arraying.lumberjack.internal;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.arraying.lumberjack.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultLogger implements LLogger {
    private final String name;
    private final Map<LLogLevel, Set<LLoggerRoute>> routes;
    private final LLoggerFormatter formatter;
    private final ListeningExecutorService executorService;
    private final ExecutorService transformer;

    public DefaultLogger(String name, Set<LLoggerRouteWrapper> routes, LLoggerFormatter formatter, int threadPool) {
        this.name = name;
        this.routes = new HashMap<>(); // Doesn't need to be thread safe; read only.
        this.formatter = formatter;
        this.executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threadPool)); // Concurrency!
        this.transformer = Executors.newSingleThreadExecutor();
        // Populate the appropriate log levels.
        for (LLogLevel value : LLogLevel.values()) {
            if (value == LLogLevel.NONE) {
                continue; // Ignore NONE.
            }
            routes.stream()
                // Get all the appropriate levels.
                .filter(routeWrapper -> routeWrapper.getLevel().ordinal() <= value.ordinal())
                .forEach(routeWrapper ->
                    // Add the entry.
                    DefaultLogger.this.routes.computeIfAbsent(value, x -> new HashSet<>()).add(routeWrapper.getRoute())
                );
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public ListenableFuture<LLogEntry> log(LLogLevel level, String message, Object... format) {
        if (level == LLogLevel.NONE) {
            throw new IllegalArgumentException("invalid log level " + level);
        }
        Set<LLoggerRoute> routes = this.routes.get(level);
        if (routes == null) {
            return Futures.immediateFailedFuture(new IllegalStateException("no route available for level"));
        }
        DefaultEntry entry = new DefaultEntry(
            System.currentTimeMillis(),
            level,
            Thread.currentThread(),
            message,
            format
        );
        String formatted = formatter.format(entry);
        //noinspection unchecked
        ListenableFuture<Void>[] futures = routes.stream()
            .map(route -> { // Create a callable for each route.
                Callable<Void> callable = () -> {
                    try {
                        route.log(formatted);
                        route.log(entry, formatted);
                    } catch (LException lException) { // Show internal exceptions as warning.
                        warn(lException.getMessage());
                    } catch (Exception exception) { // Show a stacktrace.
                        exception.printStackTrace();
                        throw exception;
                    }
                    // Satisfy the future.
                    return null;
                };
                return executorService.submit(callable);
            })
            .toArray(ListenableFuture[]::new); // To array.
        ListenableFuture<List<Void>> futuresBatch = Futures.allAsList(futures); // Wait for batch completion.
        return Futures.transform(futuresBatch, voids -> entry, transformer); // Return data upon completion.
    }

    @Override
    public ListenableFuture<LLogEntry> trace(String message, Object... format) {
        return log(LLogLevel.TRACE, message, format);
    }

    @Override
    public ListenableFuture<LLogEntry> debug(String message, Object... format) {
        return log(LLogLevel.DEBUG, message, format);
    }

    @Override
    public ListenableFuture<LLogEntry> info(String message, Object... format) {
        return log(LLogLevel.INFO, message, format);
    }

    @Override
    public ListenableFuture<LLogEntry> warn(String message, Object... format) {
        return log(LLogLevel.WARN, message, format);
    }

    @Override
    public ListenableFuture<LLogEntry> fatal(String message, Object... format) {
        return log(LLogLevel.FATAL, message, format);
    }
}
