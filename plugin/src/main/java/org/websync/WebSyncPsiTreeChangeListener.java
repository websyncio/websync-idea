package org.websync;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.websync.connection.dto.*;
import org.websync.connection.messages.idea.UpdateComponentTypeMessage;
import org.websync.connection.messages.idea.UpdatePageTypeMessage;
import org.websync.connection.messages.idea.UpdateWebSiteMessage;
import org.websync.psi.models.PsiComponentType;
import org.websync.psi.models.PsiPageType;
import org.websync.psi.models.PsiWebsite;
import org.websync.utils.Debouncer;
import org.websync.utils.LoggerUtils;

import static org.websync.utils.PsiUtil.*;

public class WebSyncPsiTreeChangeListener extends PsiTreeChangeAdapter {
    private final Debouncer debouncer;
    private WebSyncService webSyncService;

    public WebSyncPsiTreeChangeListener(WebSyncService webSyncService) {
        this.webSyncService = webSyncService;
        this.debouncer = new Debouncer(200);
    }

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent event) {
        handle(event);
    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent event) {
        handle(event);
    }

//    @Override
//    public void childReplaced(@NotNull PsiTreeChangeEvent event) {
    // looks like a duplication for childrenChanged()
//        handle(event);
//    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent event) {
        handle(event);
    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent event) {
        handle(event);
    }

    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent event) {
        handle(event);
    }

    private void handle(@NotNull PsiTreeChangeEvent event) {
        Project project = ((PsiManager) event.getSource()).getProject();
        PsiFile psiFile = event.getFile();
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
