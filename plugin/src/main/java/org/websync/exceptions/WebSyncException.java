package org.websync.exceptions;

public class WebSyncException extends Exception {

    public WebSyncException(String message) {
        super(message);
    }

    public WebSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
