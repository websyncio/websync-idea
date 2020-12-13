package org.websync.websocket.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GetProjectsCommand extends WebSyncCommand {
    @Nullable
    @Override
    protected Object execute(@NotNull Message inputMessage) {
        Object[] result = new Object[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            result[0] = getWebSyncService().getProvider().getJdiModuleNames();
        });
        return result[0];
    }

}
