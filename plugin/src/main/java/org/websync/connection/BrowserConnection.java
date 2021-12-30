package org.websync.connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intellij.openapi.project.DumbService;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.NotNull;
import org.websync.connection.messages.Message;
import org.websync.connection.messages.ResponseMessage;
import org.websync.exceptions.DumbProjectException;
import org.websync.utils.LoggerUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class BrowserConnection {
    private final WebSocketServer server;
    private CommandsHandler commandsHandler;

    public BrowserConnection(int port) {
        server = createServer(port);
        server.start();
    }

    @NotNull
    private WebSocketServer createServer(int port) {
        // https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ChatServer.java
        return new WebSocketServer(new InetSocketAddress(port)) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                conn.send("Welcome to the server!"); //This method sends a message to the new client
                broadcast("new connection: " + handshake.getResourceDescriptor()); //This method sends a message to all clients connected
                LoggerUtils.print("new connection to " + conn.getRemoteSocketAddress());
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                LoggerUtils.print("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                LoggerUtils.print("received message from " + conn.getRemoteSocketAddress() + ": " + message);
                handleCommand(message);
            }

            @Override
            public void onMessage(WebSocket conn, ByteBuffer message) {
                LoggerUtils.print("received ByteBuffer from " + conn.getRemoteSocketAddress());
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
            }

            @Override
            public void onStart() {
                LoggerUtils.print("server started successfully");
            }

            @Override
            public void broadcast(String text) {
                LoggerUtils.print("broadcasting message: " + text);
                if (getConnections().isEmpty()) {
                    LoggerUtils.print("No connections, message ignored");
                } else {
                    super.broadcast(text);
                }
            }
        };
    }

    void handleCommand(String message) {
        if (commandsHandler == null) {
            return;
        }
        try{
            ResponseMessage responseMessage = commandsHandler.handle(message);
            send(responseMessage);
        }catch (DumbProjectException e){
            DumbService.getInstance(e.project).smartInvokeLater(()->{
                ResponseMessage responseMessage = commandsHandler.handle(message);
                send(responseMessage);
            });
        }
    }

    public void send(Message message) {
        server.broadcast(serialize(message));
    }

    private String serialize(Object message) {
        ObjectMapper serializer = new ObjectMapper();
        serializer.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            return serializer.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            LoggerUtils.print(e.getMessage());
            throw new RuntimeException("Unable to serialize message: " + message, e);
        }
    }

    public void stop() throws IOException, InterruptedException {
        server.stop();
    }

    public void setCommandsHandler(CommandsHandler commandsHandler) {
        this.commandsHandler=commandsHandler;
    }
}
