package org.websync.browserConnection;

import com.intellij.openapi.application.ApplicationManager;
import org.java_websocket.WebSocket;
import org.websync.browserConnection.commands.GetWebSessionCommand;
import org.websync.websession.WebSessionPovider;

public class CommandHandler {
    final String CMD_GIVE_ME_PAGEOBECT = "give me page object";
    final String CMD_GET_PAGEOBJECTS = "get page objects";

    WebSessionPovider webSessionPovider;
    WebSessionSerializer webSessionSerializer;

    public CommandHandler(WebSessionPovider webSessionPovider, WebSessionSerializer webSessionSerializer) {
        this.webSessionPovider = webSessionPovider;
        this.webSessionSerializer = webSessionSerializer;
    }

    public String handle(String command) {
        switch (command) {
            case CMD_GIVE_ME_PAGEOBECT:
            case CMD_GET_PAGEOBJECTS:
                return new GetWebSessionCommand(webSessionPovider, webSessionSerializer).execute();
        }
        return null;
    }
}
