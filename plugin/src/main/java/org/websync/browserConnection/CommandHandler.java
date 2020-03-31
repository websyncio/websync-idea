package org.websync.browserConnection;

import org.websync.WebSyncService;
import org.websync.browserConnection.commands.GetWebSessionCommand;
import org.websync.websession.WebSessionPovider;

public class CommandHandler {
    public static final String CMD_GET_PAGEOBJECTS = "get-web-session";

    WebSyncService webSyncService;

    public CommandHandler(WebSyncService webSyncService) {
        this.webSyncService = webSyncService;
    }

    public void handle(String command) {
        switch (command) {
            case CMD_GET_PAGEOBJECTS:
                new GetWebSessionCommand(webSyncService).execute();
        }
    }
}
