package org.websync.connection.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.messages.browser.ProjectMessage;

public class GetProjectCommand extends CommandWithDataBase<ProjectMessage> {
    public GetProjectCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(ProjectMessage commandData) throws WebSyncException {
        // hack to assign variable from read action
        Object[] result = new Object[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            result[0] = webSyncService.getModulesProvider().getJdiProject(commandData.projectName).getDto();
        });
        return result[0];
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, ProjectMessage.class));
    }
}
