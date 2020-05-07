package org.websync;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.InheritanceUtil;
import org.jetbrains.annotations.NotNull;
import org.websync.jdi.JdiElement;
import org.websync.react.dto.ComponentTypeDto;
import org.websync.react.dto.ComponentsContainerDto;
import org.websync.react.dto.PageTypeDto;
import org.websync.websession.psimodels.PsiComponentType;
import org.websync.websession.psimodels.PsiPageType;

import java.util.Arrays;

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
        // looks like duplication for childrenChanged()
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
        if (!DumbService.isDumb(project) && psiFile instanceof PsiJavaFile) {
            sendUpdateFor((PsiJavaFile) psiFile);
        }
    }

    private void sendUpdateFor(PsiJavaFile psiFile) {

        PsiClass psiClass = Arrays.stream(psiFile.getClasses())
                .filter(c -> c.getModifierList() != null && c.getModifierList().hasModifierProperty(PsiModifier.PUBLIC))
                .findFirst().orElse(null);

        if (psiClass == null) {
            return;
        }

        if (isPage(psiClass)) {
            PsiPageType pageType = new PsiPageType(psiClass);
            pageType.fill();
            sendUpdateFor(new PageTypeDto(pageType));
            return;
        }
        if (isComponent(psiClass)) {
            PsiComponentType component = new PsiComponentType(psiClass);
            component.fill();
            sendUpdateFor(new ComponentTypeDto(component));
        }
    }

    private void sendUpdateFor(ComponentsContainerDto container) {
        try {
            webSyncService.getBrowserConnection().sendUpdateComponent(container);
        } catch (WebSyncException e) {
            e.printStackTrace();
        }
    }

    private boolean isComponent(PsiClass psiClass) {
        return Arrays.stream(psiClass.getSuperTypes())
                .anyMatch(s -> InheritanceUtil.isInheritor(s, JdiElement.JDI_UI_BASE_ELEMENT.className));
    }

    private boolean isPage(PsiClass psiClass) {
        return Arrays.stream(psiClass.getSuperTypes())
                .anyMatch(s -> InheritanceUtil.isInheritor(s, JdiElement.JDI_WEB_PAGE.className));
    }

}
