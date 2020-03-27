package org.websync.websession;

import com.intellij.openapi.project.Project;
import org.websync.websession.models.WebSession;

import java.util.List;

public interface WebSessionProvider {
    List<WebSession> getWebSessions(boolean useCache);

    void removeProject(Project project);

    void addProject(Project project);

    List<Project> getProjects();
}
