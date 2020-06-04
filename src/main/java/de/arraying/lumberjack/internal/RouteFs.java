package de.arraying.lumberjack.internal;

import de.arraying.lumberjack.LException;
import de.arraying.lumberjack.LFsRules;
import de.arraying.lumberjack.LLogEntry;
import de.arraying.lumberjack.LLoggerRoute;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.function.Function;
import java.util.function.Supplier;

public class RouteFs implements LLoggerRoute {
    private static class Appender {
        private final LFsRules rules;
        private final File file;
        private LLogEntry first;
        private long uid;

        @SuppressWarnings("ResultOfMethodCallIgnored")
        private Appender(LFsRules rules) throws IOException, IllegalStateException {
            this.rules = rules;
            rules.getDirectory().mkdirs();
            this.file = tryName(0);
            file.createNewFile();
        }

        private boolean ask() {
            if (first == null) {
                return true;
            }
            if (rules.getLineLimit() > 0) {
                if (uid >= rules.getLineLimit()) {
                    return false;
                }
            }
            if (rules.getTimeLimit() > 0) {
                long now = System.currentTimeMillis();
                return now - first.getTime() <= rules.getTimeLimit();
            }
            return true;
        }

        private void append(LLogEntry entry, String message) throws IOException {
            if (first == null) {
                first = entry;
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(message.getBytes());
            outputStream.write("\n".getBytes()); // Add a newline.
            Files.write(file.toPath(), outputStream.toByteArray(), StandardOpenOption.APPEND);
            uid++; // Everything is synchronized anyway.
        }

        private File tryName(long worker) {
            File file = new File(rules.getDirectory(), rules.formatFileName(System.currentTimeMillis(), worker));
            if (file.exists()) {
                // Try the next worker ID.
                return tryName(worker + 1);
            }
            return file;
        }
    }

    private static class InternalFsRules implements LFsRules {
        private Supplier<File> supplierDirectory;
        private Supplier<Integer> supplierLimitLine;
        private Supplier<Long> supplierLimitTime;
        private Function<ArgumentWrapper, String> formatter;
        private String error;

        @Override
        public File getDirectory() {
            return supplierDirectory.get();
        }

        @Override
        public int getLineLimit() {
            return supplierLimitLine.get();
        }

        @Override
        public long getTimeLimit() {
            return supplierLimitTime.get();
        }

        @Override
        public String formatFileName(long time, long uid) {
            return formatter.apply(new ArgumentWrapper(time, uid));
        }
    }

    private static class ArgumentWrapper {
        private final long time;
        private final long uid;

        private ArgumentWrapper(long time, long uid) {
            this.time = time;
            this.uid = uid;
        }
    }

    private final LFsRules rules;
    private LException nextException;
    private Appender appender;

    public RouteFs(LFsRules rules)  {
        InternalFsRules internalFsRules = generateValidRules(rules);
        this.rules = internalFsRules;
        this.nextException = internalFsRules.error == null ? null : new LException(internalFsRules.error);
    }

    @Override
    public synchronized void log(LLogEntry entry, String message) throws Exception {
        if (appender == null || !appender.ask()) {
            appender = new Appender(rules);
        }
        if (nextException != null) {
            LException throwing = nextException;
            nextException = null;
            throw throwing;
        }
        appender.append(entry, message);
    }

    private InternalFsRules generateValidRules(LFsRules current) {
        InternalFsRules rules = new InternalFsRules();
        // Ensure logs directory will always be up to date.
        if (current.getDirectory() == null) {
            rules.supplierDirectory = () -> new File("logs");
            rules.error = "Invalid logs directory. Falling back to ./logs/";
        } else {
            rules.supplierDirectory = current::getDirectory;
        }
        // Ensure that there will never be a stack overflow because the formatter doesn't use the uid.
        if (current.formatFileName(0, 0).equalsIgnoreCase(current.formatFileName(0, 1))) {
            rules.formatter = wrapper -> String.format("%d-%d.txt", wrapper.time, wrapper.uid);
            rules.error = "Mis-configured filename formatter, please include the UID. Falling back to default filenames.";
        } else {
            rules.formatter = wrapper -> current.formatFileName(wrapper.time, wrapper.uid);
        }
        rules.supplierLimitLine = current::getLineLimit;
        rules.supplierLimitTime = current::getTimeLimit;
        return rules;
    }
}
