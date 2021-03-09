package org.websync.websocket.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;

public class UpdateWebsiteUrlCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public String url;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) throws WebSyncException {
        String newUrl = ((UpdateWebsiteUrlCommand.Message) inputMessage).url;

        updateWebsiteUrl(newUrl);

        return null;
    }

    public void updateWebsiteUrl(String url) {
        System.out.println("New url: " + url);
    }
}
