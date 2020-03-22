package org.websync;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Listener to detect project open and close.
 * Depends on org.intellij.sdk.maxOpenProjects.ProjectCountingService
 */
public class ProjectOpenCloseListener implements ProjectManagerListener {
    /**
     * Invoked on project open.
     *
     * @param project opening project
     */
    @Override
    public void projectOpened(@NotNull Project project) {
        ServiceManager.getService(WebSyncService.class).addProject(project);
    }

    /**
     * Invoked on project close.
     *
     * @param project closing project
     */
    @Override
    public void projectClosed(@NotNull Project project) {
        ServiceManager.getService(WebSyncService.class).removeProject(project);
    }
}