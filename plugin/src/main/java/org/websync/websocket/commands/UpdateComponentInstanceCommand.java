package org.websync.websocket.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.react.dto.ComponentInstanceDto;

public class UpdateComponentInstanceCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public ComponentInstanceDto data;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) {
        ComponentInstanceDto data = ((Message) inputMessage).data;
        int lastDot = data.id.lastIndexOf('.');
        String className = data.id.substring(0, lastDot);
        String oldFieldName = data.id.substring(lastDot + 1);
        String newFieldName = data.name;
        getWebSyncService().updateComponentInstance(className, oldFieldName, newFieldName);
        return null;
    }
}
