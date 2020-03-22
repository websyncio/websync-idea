package org.websync.server;

import com.intellij.openapi.diagnostic.Logger;
import org.java_websocket.server.WebSocketServer;


public class BrowserConnection {
    private static final Logger log = Logger.getInstance(BrowserConnection.class);
    private final CommandHandler commandHandler;
    private Server server;
    private String host;
    private Integer port;

    public BrowserConnection(String host, Integer port, CommandHandler commandHandler) {
        this.host = host;
        this.port = port;
        this.commandHandler = commandHandler;
    }

    public void start() {
        server = new Server(port, commandHandler);
        server.start();
        log.info("Listening port:" + port);
    }

    public void disposeConnection() {
        try {
            server.stop(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}