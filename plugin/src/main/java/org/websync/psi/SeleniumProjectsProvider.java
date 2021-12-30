package org.websync.psi;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.websync.exceptions.DumbProjectException;
import org.websync.models.SeleniumProject;
import org.websync.models.WebSite;
import org.websync.psi.models.PsiWebsite;

import java.util.Collection;
import java.util.List;

public interface SeleniumProjectsProvider {
    List<String> getModuleNames();

    SeleniumProject getProject(String projectName) throws DumbProjectException;

    Collection<WebSite> getWebsites(String projectName);

    void removeProject(Project project);

    void addProject(Project project);

    Module findProject(String projectName);
}
