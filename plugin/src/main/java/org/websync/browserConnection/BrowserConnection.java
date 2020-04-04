package org.websync.browserConnection;

import lombok.Getter;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.websync.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

// https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ChatServer.java
public class BrowserConnection extends WebSocketServer {
    @Getter
    private CommandHandler commandHandler;

    public BrowserConnection(int port, CommandHandler commandHandler) {
        super(new InetSocketAddress(port));
        this.commandHandler = commandHandler;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //This method sends a message to the new client
        broadcast("new connection: " + handshake.getResourceDescriptor()); //This method sends a message to all clients connected
        Logger.print("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Logger.print("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Logger.print("received message from " + conn.getRemoteSocketAddress() + ": " + message);
        if (null != commandHandler) {
            commandHandler.handle(message);
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        Logger.print("received ByteBuffer from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        Logger.print("server started successfully");
    }


    public static void main(String[] args) throws IOException {
        int port = 1804;

        BrowserConnection s = new BrowserConnection(port, null);
        s.start();
        Logger.print("ChatServer started on port: " + s.getPort());

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            //String in = sysin.readLine();
            //s.broadcast( in );
//            if( in.equals( "exit" ) ) {
//                //s.stop(1000);
//                break;
//            }
        }
    }
}
