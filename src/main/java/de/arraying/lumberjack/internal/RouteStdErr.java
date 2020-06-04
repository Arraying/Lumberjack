package de.arraying.lumberjack.internal;

import de.arraying.lumberjack.LLoggerRoute;

public class RouteStdErr implements LLoggerRoute {
    @Override
    public void log(String message) {
        System.err.println(message);
    }
}
