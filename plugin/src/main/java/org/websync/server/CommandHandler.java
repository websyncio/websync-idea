package org.websync.server;

import org.java_websocket.WebSocket;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.server.command.RetrieveWebSession;
import org.websync.websession.WebSessionPovider;

public class CommandHandler {
    final String CMD_GIVE_ME_PAGEOBECT = "give me page object";
    WebSessionPovider webSessionPovider;
    WebSessionSerializer webSessionSerializer;

    public CommandHandler(WebSessionPovider webSessionPovider, WebSessionSerializer webSessionSerializer) {
        this.webSessionPovider = webSessionPovider;
        this.webSessionSerializer = webSessionSerializer;
    }

    public String handle(String command) {
        switch (command) {
            case CMD_GIVE_ME_PAGEOBECT:
                return new RetrieveWebSession(webSessionPovider, webSessionSerializer).execute();
        }
        return null;
    }
}
