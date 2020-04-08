package org.websync.debugger.commands;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

public class CommandInitProject {
    public static void run() {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
    }
}
