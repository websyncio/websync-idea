package org.websync;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.websync.connection.dto.ComponentTypeDto;
import org.websync.connection.dto.PageTypeDto;
import org.websync.connection.dto.WebsiteDto;
import org.websync.connection.messages.idea.UpdateComponentTypeMessage;
import org.websync.connection.messages.idea.UpdatePageTypeMessage;
import org.websync.connection.messages.idea.UpdateProjectMessage;
import org.websync.connection.messages.idea.UpdateWebSiteMessage;
import org.websync.models.JdiProject;
import org.websync.psi.models.PsiComponentType;
import org.websync.psi.models.PsiPageType;
import org.websync.psi.models.PsiWebsite;
import org.websync.utils.Debouncer;
import org.websync.utils.LoggerUtils;
import org.websync.utils.ModuleNameUtils;

import static org.websync.utils.PsiUtils.*;

public class WebSyncPsiTreeChangeListener extends PsiTreeChangeAdapter {
    private final Debouncer debouncer;
    private WebSyncService webSyncService;

    public WebSyncPsiTreeChangeListener(WebSyncService webSyncService) {
        this.webSyncService = webSyncService;
        this.debouncer = new Debouncer(200);
    }

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent event) {
        LoggerUtils.print(event.toString());
        PsiElement child = event.getChild();
        if (child == null || child instanceof PsiFile) {
            // . we should ignore file adding event because
            // at this stage we can not get new version of project data
            // since file still does not exist
            return;
        }
        System.out.println("child added: " + child.getContainingFile().getName());
        handleStructureChange(((PsiManager) event.getSource()).getProject(), child.getContainingFile());
    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent event) {
        LoggerUtils.print(event.toString());
        PsiElement child = event.getChild();
        if (child == null || !(child instanceof PsiFile)) {
            // . we are interested only in file structure changes
            return;
        }
        System.out.println("child removed: " + child.getContainingFile().getName());
        handleStructureChange(((PsiManager) event.getSource()).getProject(), child.getContainingFile());
    }

//    @Override
//    public void childReplaced(@NotNull PsiTreeChangeEvent event) {
    // looks like a duplication for childrenChanged()
//        handle(event);
//    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent event) {
        LoggerUtils.print(event.toString());
        PsiElement child = event.getChild();
        if (child == null || !(child instanceof PsiFile)) {
            // . we are interested only in file structure changes
            return;
        }
        System.out.println("child moved: " + child.getContainingFile().getName());
        handleStructureChange(((PsiManager) event.getSource()).getProject(), child.getContainingFile());
    }

    private void handleStructureChange(Project project, PsiFile file) {
        // .use parent directory to define module
        // because if file is removed then it does not belong to module anymore
        // and directory still does belong
        Module module = ModuleUtil.findModuleForFile(file.getParent().getVirtualFile(), project);
        if (ModuleNameUtils.isMainModule(module.getName())) {
            // Change occured in main module
            if (!DumbService.isDumb(project)) {
                debouncer.execute(() -> {
                    ApplicationManager.getApplication().runReadAction(() -> {
                        JdiProject jdiProject = webSyncService.getModulesProvider().getJdiProject(project.getName());
                        sendProjectUpdate(jdiProject);
                    });
                });
            }
        }
    }

    private void sendProjectUpdate(JdiProject jdiProject) {
        UpdateProjectMessage requestMessage = new UpdateProjectMessage(jdiProject.getDto());
        webSyncService.getBrowserConnection().send(requestMessage);
    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent event) {
        LoggerUtils.print(event.toString());
        handleContentChange(((PsiManager) event.getSource()).getProject(), event.getFile());
    }

    // PsiFile is null for this event
//    @Override
//    public void propertyChanged(@NotNull PsiTreeChangeEvent event) {
//        handle(event);
//    }

    private void handleContentChange(Project project, @NotNull PsiFile psiFile) {
        if (psiFile == null) {
            LoggerUtils.print("PsiFile is null for event");
            return;
        }
        if (!DumbService.isDumb(project)) {
            debouncer.execute(() -> sendUpdateFor(psiFile));
        }
    }

    private void sendUpdateFor(PsiFile psiFile) {
        LoggerUtils.print("Send update for " + psiFile.getName());
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
        webSyncService.getBrowserConnection().send(requestMessage);
    }

    private void sendUpdateForPageType(PageTypeDto pageType) {
        UpdatePageTypeMessage requestMessage = new UpdatePageTypeMessage(pageType);
        webSyncService.getBrowserConnection().send(requestMessage);
    }

    private void sendUpdateForComponentType(ComponentTypeDto componentTypeDto) {
        UpdateComponentTypeMessage requestMessage = new UpdateComponentTypeMessage(componentTypeDto);
        webSyncService.getBrowserConnection().send(requestMessage);
    }
}
