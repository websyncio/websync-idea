package org.websync.connection.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.websync.WebSyncService;

public class GetProjectsCommand extends CommandBase {
    public GetProjectsCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(String data) {
        Object[] result = new Object[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            result[0] = webSyncService.getProvider().getJdiModuleNames();
        });
        return result[0];
    }
}
