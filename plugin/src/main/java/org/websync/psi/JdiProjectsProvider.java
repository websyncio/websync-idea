package org.websync.psi;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.websync.models.JdiProject;

import java.util.List;

public interface JdiProjectsProvider {
    List<String> getJdiModuleNames();

    JdiProject getJdiProject(String projectName);

    void removeProject(Project project);

    void addProject(Project project);

    Module findProject(String projectName);
}
