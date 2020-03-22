package org.websync.browserConnection;

import com.intellij.openapi.diagnostic.Logger;
import org.java_websocket.server.WebSocketServer;


public class BrowserConnection {
    private static final Logger log = Logger.getInstance(BrowserConnection.class);
    private final CommandHandler commandHandler;
    private WebsocketServer server;
    private String host;
    private Integer port;

    public BrowserConnection(String host, Integer port, CommandHandler commandHandler) {
        this.host = host;
        this.port = port;
        this.commandHandler = commandHandler;
    }

    public void start() {
        server = new WebsocketServer(port, commandHandler);
        server.start();
        log.info("Listening port:" + port);
    }

    public void stop() {
        try {
            server.stop(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}