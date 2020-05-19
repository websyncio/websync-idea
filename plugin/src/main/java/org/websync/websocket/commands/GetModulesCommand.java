package org.websync.websocket.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.websocket.ReplyObject;

public class GetModulesCommand extends WebSyncCommand {
    @Nullable
    @Override
    protected ReplyObject execute(@NotNull Message inputMessage) {
        Object[] result = new Object[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            result[0] = getWebSyncService().getProvider().getJdiModuleNames();
        });
        ReplyObject replyObject = new ReplyObject("modules", result[0]);
        return replyObject;
    }

}
