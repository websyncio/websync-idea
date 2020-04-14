package org.websync.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.logger.Logger;
import org.websync.websocket.commands.WebSyncCommand;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

// https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ChatServer.java
public class BrowserConnection extends WebSocketServer {

    public BrowserConnection(int port) {
        super(new InetSocketAddress(port));
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
        Object replyObject;
        WebSyncCommand command = WebSyncCommand.createByText(message);
        if(command == null) {
            String msg = message.replace('"', '\'');
            broadcast("{\"status\":254,\"error\":\"Unknown message: " + msg + "\"");
            return;
        }
        try {
            Object result = command.execute(message);
            replyObject = new OkayReply(result);
        } catch (WebSyncException e) {
            e.printStackTrace();
            replyObject = new ErrorReply(100, e.getMessage());
        }
        try {
            broadcast(serialize(replyObject));
        } catch (JsonProcessingException e) {
            Logger.print("CANNOT serialize reply: " + e.getMessage());
            broadcast("{\"status\":255,\"error\":\"CANNOT serialize reply\"");
        }
    }

    @Nullable
    public String serialize(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper.writeValueAsString(o);
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

    static class ErrorReply {
        public int status;
        public String error;

        public ErrorReply(int status, String error) {
            if(status == 0) throw new IllegalArgumentException("must not be 0");
            this.status = status;
            this.error = error;
        }
    }

    static class OkayReply {
        public int status = 0;
        public Object data;

        public OkayReply(Object data) {
            this.data = data;
        }
    }
}
