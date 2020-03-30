package org.websync.browserConnection;


import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ChatServer.java
public class BrowserConnection extends WebSocketServer {
    private CommandHandler commandHandler;

    public BrowserConnection(int port, CommandHandler commandHandler) {
        super(new InetSocketAddress(port));
        this.commandHandler = commandHandler;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //This method sends a message to the new client
        broadcast("new connection: " + handshake.getResourceDescriptor()); //This method sends a message to all clients connected
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
        if (null != commandHandler) {
            String response = commandHandler.handle(message);
            if (null != response) {
                System.out.println("send response");
                conn.send(response);
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        System.out.println("received ByteBuffer from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }


    public static void main(String[] args) throws IOException {
        int port = 1804;

        BrowserConnection s = new BrowserConnection( port, null);
        s.start();
        System.out.println( "ChatServer started on port: " + s.getPort() );

        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
        while ( true ) {
            //String in = sysin.readLine();
            //s.broadcast( in );
//            if( in.equals( "exit" ) ) {
//                //s.stop(1000);
//                break;
//            }
        }
    }
}
