package org.websync.browserConnection;

import org.websync.browserConnection.commands.GetWebSessionCommand;
import org.websync.websession.WebSessionProvider;

public class CommandHandler {
    final String CMD_GET_PAGEOBJECTS = "get-web-session";

    WebSessionProvider webSessionProvider;
    WebSessionSerializer webSessionSerializer;

    public CommandHandler(WebSessionProvider webSessionProvider, WebSessionSerializer webSessionSerializer) {
        this.webSessionProvider = webSessionProvider;
        this.webSessionSerializer = webSessionSerializer;
    }

    public String handle(String command) {
        switch (command) {
            case CMD_GET_PAGEOBJECTS:
                return new GetWebSessionCommand(webSessionProvider, webSessionSerializer).execute();
        }
        return null;
    }
}
