package org.websync.connection.commands;

import org.websync.WebSyncService;

public abstract class CommandBase implements Command  {
    protected WebSyncService webSyncService;
    public CommandBase(WebSyncService webSyncService){
        this.webSyncService = webSyncService;
    }
}
