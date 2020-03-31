package org.websync.browserConnection;

import org.websync.browserConnection.commands.GetWebSessionCommand;
import org.websync.websession.WebSessionPovider;

public class CommandHandler {
    public static final String CMD_GET_PAGEOBJECTS = "get-web-session";

    WebSessionPovider webSessionPovider;
    WebSessionSerializer webSessionSerializer;

    public CommandHandler(WebSessionPovider webSessionPovider, WebSessionSerializer webSessionSerializer) {
        this.webSessionPovider = webSessionPovider;
        this.webSessionSerializer = webSessionSerializer;
    }

    public String handle(String command) {
        switch (command) {
            case CMD_GET_PAGEOBJECTS:
                return new GetWebSessionCommand(webSessionPovider, webSessionSerializer).execute();
        }
        return null;
    }
}
