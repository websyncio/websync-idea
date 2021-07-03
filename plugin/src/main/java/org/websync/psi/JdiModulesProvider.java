package org.websync.psi;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.websync.models.JdiModule;

import java.util.List;

public interface JdiModulesProvider {
    List<String> getJdiModuleNames();

    JdiModule getJdiModule(String name);

    void removeProject(Project project);

    void addProject(Project project);

    Module findByFullName(String fullName);
}
