package org.evlis.lunamatic.utilities.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class LogHandler extends ConsoleHandler {
    public LogHandler() {
        setFormatter(new LogFormatter());
        setLevel(Level.ALL); // Allow all log levels
        // Redirect output to System.out instead of System.err
        try {
            setOutputStream(System.out);
        } catch (Exception ignored) {}
    }
}
