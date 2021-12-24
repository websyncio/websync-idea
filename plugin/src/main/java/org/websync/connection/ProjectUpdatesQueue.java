package org.websync.connection;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.websync.connection.dto.ComponentTypeDto;
import org.websync.connection.dto.PageTypeDto;
import org.websync.connection.dto.WebsiteDto;
import org.websync.connection.messages.idea.UpdateComponentTypeMessage;
import org.websync.connection.messages.idea.UpdatePageTypeMessage;
import org.websync.connection.messages.idea.UpdateProjectMessage;
import org.websync.connection.messages.idea.UpdateWebSiteMessage;
import org.websync.models.JdiProject;
import org.websync.psi.JdiProjectsProvider;
import org.websync.psi.models.PsiComponentType;
import org.websync.psi.models.PsiPageType;
import org.websync.psi.models.PsiWebsite;
import org.websync.utils.Debouncer;
import org.websync.utils.LoggerUtils;
import org.websync.utils.ModuleStructureUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.websync.utils.PsiUtils.*;

public class ProjectUpdatesQueue {
    private BrowserConnection browserConnection;
    private JdiProjectsProvider projectsProvider;
    private final Debouncer debouncer;
    private List<Project> capturedProjects;

    public ProjectUpdatesQueue(BrowserConnection browserConnection, JdiProjectsProvider projectsProvider) {
        this.browserConnection = browserConnection;
        this.projectsProvider = projectsProvider;
        this.debouncer = new Debouncer();
        capturedProjects = new ArrayList<>();
    }

    public void addStructureUpdate(PsiFile psiFile) {
        if(isProjectStateCaptured(psiFile.getProject())){
            return;
        }
        // .use parent directory to define module
        // because if file is removed then it does not belong to module anymore
        // and directory still does belong
        //Module module = ModuleUtil.findModuleForFile(file.getParent().getVirtualFile(), project);
        if (ModuleStructureUtils.isFileInMainModule(psiFile)) {
            addStructureUpdate(psiFile.getProject());
        }
    }

    public void addStructureUpdate(Project project) {
        if(isProjectStateCaptured(project)){
            return;
        }
        // Change occurred in main module
        if (!DumbService.isDumb(project)) {
            debouncer.debounce(project, () -> {
                ApplicationManager.getApplication().runReadAction(() -> {
                    JdiProject jdiProject = projectsProvider.getJdiProject(project.getName());
                    sendProjectUpdate(jdiProject);
                });
            }, 300, TimeUnit.MILLISECONDS);
        }
    }

    private void sendProjectUpdate(JdiProject jdiProject) {
        LoggerUtils.logeTreeChangeEvent("Send project update");
        UpdateProjectMessage requestMessage = new UpdateProjectMessage(jdiProject.getDto());
        browserConnection.send(requestMessage);
    }

    public void addContentUpdate(PsiFile psiFile) {
        if (psiFile == null) {
            LoggerUtils.print("PsiFile is null for event");
            return;
        }
        if(isProjectStateCaptured(psiFile.getProject())){
            return;
        }
        if (!DumbService.isDumb(psiFile.getProject())) {
            debouncer.debounce(psiFile,
                    () -> sendUpdateFor(psiFile),
                    300,TimeUnit.MILLISECONDS);
        }
    }

    private void sendUpdateFor(PsiFile psiFile) {
        LoggerUtils.logeTreeChangeEvent("Send update for " + psiFile.getName());
        PsiClass psiClass = findPsiClass(psiFile);

        if (psiClass == null) {
            return;
        }

        if (isWebsite(psiClass)) {
            PsiWebsite website = new PsiWebsite(psiClass);
            website.fill();
            sendUpdateForWebSite(new WebsiteDto(website));
            return;
        }
        if (isPage(psiClass)) {
            PsiPageType pageType = new PsiPageType(psiClass);
            pageType.fill();
            sendUpdateForPageType(new PageTypeDto(pageType));
            return;
        }
        if (isComponent(psiClass)) {
            PsiComponentType component = new PsiComponentType(psiClass);
            component.fill();
            sendUpdateForComponentType(new ComponentTypeDto(component));
        }
    }

    private void sendUpdateForWebSite(WebsiteDto websiteDto) {
        UpdateWebSiteMessage requestMessage = new UpdateWebSiteMessage(websiteDto);
        browserConnection.send(requestMessage);
    }

    private void sendUpdateForPageType(PageTypeDto pageType) {
        UpdatePageTypeMessage requestMessage = new UpdatePageTypeMessage(pageType);
        browserConnection.send(requestMessage);
    }

    private void sendUpdateForComponentType(ComponentTypeDto componentTypeDto) {
        UpdateComponentTypeMessage requestMessage = new UpdateComponentTypeMessage(componentTypeDto);
        browserConnection.send(requestMessage);
    }

    private boolean isProjectStateCaptured(Project project) {
        return capturedProjects.contains(project);
    }

    public void captureProjectState(Project project) {
        if (!isProjectStateCaptured(project)) {
            capturedProjects.add(project);
        }
    }

    public void releaseProjectState(Project project) {
        if (isProjectStateCaptured(project)) {
            capturedProjects.remove(project);
            addStructureUpdate(project);
        }
    }
}
