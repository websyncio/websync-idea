package org.websync;

import com.intellij.openapi.diagnostic.Logger;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;


public class BrowserConnection {
    private static final Logger log = Logger.getInstance(BrowserConnection.class);
    private ServerSocket serverSocket;
    private WebSocketServer server;
    private Thread listenerThread;
    private String host;
    private Integer port;
//    private String connectionName;

    public BrowserConnection(String host, Integer port) {
        this.host = host;
        this.port = port;
//        this.connectionName = connectionName;
    }

    public void initConnection() {
        server = new Server(new InetSocketAddress(host, port));
        server.run();
        log.info("Listening " + port);
    }

    public void disposeConnection() {
        try {
            if (listenerThread != null) {
                listenerThread.interrupt();
            }
            server.stop();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public String getComponentName() {
        return "BrowserConnection";
    }
}