package org.websync.server;

import org.java_websocket.WebSocket;
import org.websync.server.command.CommandGiveMePageObject;

public class CommandHandler {

    final String CMD_GIVE_ME_PAGEOBECT = "give me page object";

    public void handle(WebSocket conn, String command) {
        if (command.contains(CMD_GIVE_ME_PAGEOBECT)) {
            CommandGiveMePageObject.run(conn);
        }
    }
}
