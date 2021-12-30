package org.websync.connection.commands;

import com.intellij.openapi.application.ReadAction;
import org.websync.exceptions.DumbProjectException;
import org.websync.exceptions.WebSyncException;
import org.websync.connection.messages.ProjectMessage;
import org.websync.psi.SeleniumProjectsProvider;

public class GetProjectCommand extends CommandWithDataBase<ProjectMessage> {
    public GetProjectCommand(SeleniumProjectsProvider projectsProvider) {
        super(projectsProvider);
    }

    @Override
    public Object execute(ProjectMessage commandData) throws WebSyncException, DumbProjectException {
        return ReadAction.compute(() ->
                projectsProvider.getProject(commandData.projectName).getDto());
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException, DumbProjectException {
        return execute(parseCommandData(commandDataString, ProjectMessage.class));
    }
}
