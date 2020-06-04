package de.arraying.lumberjack;

/**
 * LException is a specifc exception for the logger, which is only thrown internally.
 */
public class LException extends Exception {

    /**
     * Passes the message to the exception.
     * @param message The message.
     */
    public LException(String message) {
        super(message);
    }
}
