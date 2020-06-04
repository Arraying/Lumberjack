package de.arraying.lumberjack.internal;

import de.arraying.lumberjack.LLogEntry;
import de.arraying.lumberjack.LLogLevel;

public class DefaultEntry implements LLogEntry {
    private final long time;
    private final LLogLevel level;
    private final Thread thread;
    private final String message;
    private final Object[] formatters;

    public DefaultEntry(long time, LLogLevel level, Thread thread, String message, Object[] formatters) {
        this.time = time;
        this.level = level;
        this.thread = thread;
        this.message = message;
        this.formatters = formatters;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public LLogLevel getLevel() {
        return level;
    }

    @Override
    public Thread getThread() {
        return thread;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Object[] getFormattingObjects() {
        return formatters;
    }
}
