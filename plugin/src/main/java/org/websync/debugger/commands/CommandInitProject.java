package org.websync.debugger.commands;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.models.WebSession;

public class CommandInitProject {
    public static void run() {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        WebSession webSession = PsiWebSessionProvider.getWebSession(project);
    }
}
