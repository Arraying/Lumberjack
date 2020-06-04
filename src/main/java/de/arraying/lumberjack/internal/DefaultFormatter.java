package de.arraying.lumberjack.internal;

import de.arraying.lumberjack.LLogEntry;
import de.arraying.lumberjack.LLoggerFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultFormatter implements LLoggerFormatter {
    @Override
    public String format(LLogEntry entry) {
        Date date = new Date(entry.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String timestamp = simpleDateFormat.format(date);
        String log = String.format(entry.getMessage(), entry.getFormattingObjects());
        return String.format("[%s] [%s] %s: %s", timestamp, entry.getThread().getName(), entry.getLevel(), log);
    }
}
