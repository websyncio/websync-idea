package org.websync.websocket.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.react.dto.PageInstanceDto;

public class UpdatePageinstanceUrlCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public PageInstanceDto pageInstance;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) throws WebSyncException {
        PageInstanceDto pageInstance = ((UpdatePageinstanceUrlCommand.Message) inputMessage).pageInstance;
        updatePageInstanceUrl(pageInstance.url, pageInstance.id);
        return null;
    }

    public void updatePageInstanceUrl(String url, String id) {
        System.out.println("===========================    New url: " + url + id);
    }
}

