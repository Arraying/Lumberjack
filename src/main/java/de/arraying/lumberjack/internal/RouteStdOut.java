package de.arraying.lumberjack.internal;

import de.arraying.lumberjack.LLoggerRoute;

public class RouteStdOut implements LLoggerRoute {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
