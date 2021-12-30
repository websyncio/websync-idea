package org.websync.connection.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.websync.psi.SeleniumProjectsProvider;

public class GetProjectsCommand extends CommandBase {
    public GetProjectsCommand(SeleniumProjectsProvider projectsProvider) {
        super(projectsProvider);
    }

    @Override
    public Object execute(String data) {
        Object[] result = new Object[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            result[0] = projectsProvider.getModuleNames();
        });
        return result[0];
    }
}
