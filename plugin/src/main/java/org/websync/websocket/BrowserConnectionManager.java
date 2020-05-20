package org.websync.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.websync.WebSyncException;
import org.websync.logger.LoggerUtils;
import org.websync.react.dto.ComponentsContainerDto;
import org.websync.websocket.commands.WebSyncCommand;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class BrowserConnectionManager {
    private final WebSocketServer server;

    public BrowserConnectionManager(int port) {
// https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ChatServer.java
        server = new WebSocketServer(new InetSocketAddress(port)) {
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
        server.start();
    }

    void handleCommand(String message) {
        WebSyncCommand command = WebSyncCommand.createByText(message);
        if (command == null) {
            String error = "Unknown message: " + message.replace('"', '`');
            sendErrorReply(254, "internal-error", error);
            return;
        }
        try {
            send(new Message(command.getResponseType()) {
                public int status = 0;
                public Object data = command.execute(message);
            });
        } catch (WebSyncException e) {
            e.printStackTrace();
            sendErrorReply(100, command.getResponseType(), e.getMessage());
        }
    }

    private void sendErrorReply(int code, String responseType, String messageText) {
        if (code == 0) {
            throw new IllegalArgumentException("must not be 0");
        }
        send(new Message(responseType) {
            public int status = code;
            public String error = messageText;
        });
    }


    public void sendUpdate(String commandType, ComponentsContainerDto container) {
        send(new Message("update-" + commandType) {
            public Object data = container;
        });
    }

    public void sendShowInPageEditor(String commandType, String qualifiedName) {
        send(new Message("show-" + commandType) {
            public String className = qualifiedName;
        });
    }

    void send(Message messageObject) {
        ObjectMapper serializer = new ObjectMapper();
        serializer.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            String messageJson = serializer.writeValueAsString(messageObject);
            server.broadcast(messageJson);
        } catch (JsonProcessingException e) {
            LoggerUtils.print(e.getMessage());
            server.broadcast("{\"status\":255,\"type\":\"internal-error\",\"error\":\"CANNOT serialize reply\"");
            throw new RuntimeException("Cannot serialize " + messageObject, e);
        }
    }

    public void stop() throws IOException, InterruptedException {
        server.stop();
    }

    // serialization stuff, must not be used outside
    static class Message {
        Message(String type) {
            this.type = type;
        }

        public String type;
    }

}
