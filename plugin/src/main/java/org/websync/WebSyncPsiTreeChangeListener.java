package org.websync;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.websync.react.dto.*;
import org.websync.websession.psimodels.PsiComponentType;
import org.websync.websession.psimodels.PsiPageType;
import org.websync.websession.psimodels.PsiWebsite;

import static org.websync.PsiUtil.*;

public class WebSyncPsiTreeChangeListener extends PsiTreeChangeAdapter {
    private WebSyncService webSyncService;

    public WebSyncPsiTreeChangeListener(WebSyncService webSyncService) {
        this.webSyncService = webSyncService;
    }

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent event) {
        handle(event);
    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent event) {
        handle(event);
    }

    @Override
    public void childReplaced(@NotNull PsiTreeChangeEvent event) {
        // looks like a duplication for childrenChanged()
//        handle(event);
    }

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
            sendUpdateFor(psiFile);
        }
    }

    private void sendUpdateFor(PsiFile psiFile) {

        PsiClass psiClass = findPsiClass(psiFile);

        if (psiClass == null) {
            return;
        }

        if (isWebsite(psiClass)) {
            PsiWebsite website = new PsiWebsite(psiClass);
            website.fill();
            sendUpdateFor("site", new WebsiteDto(website));
            return;
        }
        if (isPage(psiClass)) {
            PsiPageType pageType = new PsiPageType(psiClass);
            pageType.fill();
            sendUpdateFor("page", new PageTypeDto(pageType));
            return;
        }
        if (isComponent(psiClass)) {
            PsiComponentType component = new PsiComponentType(psiClass);
            component.fill();
            sendUpdateFor("component", new ComponentTypeDto(component));
        }
    }

    private void sendUpdateFor(String type, BaseDto container) {
        webSyncService.getBrowserConnectionManager().sendUpdate(type, container);
    }
}
