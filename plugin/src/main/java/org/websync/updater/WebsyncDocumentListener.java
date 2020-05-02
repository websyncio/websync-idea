package org.websync.updater;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.InheritanceUtil;
import org.jetbrains.annotations.NotNull;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.jdi.JdiElement;
import org.websync.logger.LoggerUtils;
import org.websync.react.dto.ComponentTypeDto;
import org.websync.react.dto.ComponentsContainerDto;
import org.websync.react.dto.PageTypeDto;
import org.websync.websession.psimodels.PsiComponentType;
import org.websync.websession.psimodels.PsiPageType;

import java.util.Arrays;

// TODO remove, replaced with WebSyncPsiTreeChangeListener
public class WebsyncDocumentListener implements DocumentListener {

    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        Document document = event.getDocument();
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        VirtualFile file = fileDocumentManager.getFile(document);
        if (file == null) {
            return;
        }
        LoggerUtils.print(String.format("documentChanged: '%s'", file.getPath()));
        Project project = ProjectUtil.guessProjectForFile(file);
        if (project == null) {
            return;
        }
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        LoggerUtils.print(String.format("psiFile of this file: '%s'", psiFile));

        PsiClass psiClass = getPsiClassFromPsiFile(psiFile);
        if (psiClass == null) {
            return;
        }
        boolean isPage = isPage(psiClass);
        boolean isComponent = isComponent(psiClass);
        LoggerUtils.print(String.format("isPage: %s, isComponent: %s", isPage, isComponent));

        if (isPage) {
            PsiPageType pageType = new PsiPageType(psiClass);
            pageType.fill();
            sendUpdate(new PageTypeDto(pageType));
            return;
        }
        if (isComponent) {
            PsiComponentType component = new PsiComponentType(psiClass);
            component.fill();
            sendUpdate(new ComponentTypeDto(component));
        }
    }

    private void sendUpdate(ComponentsContainerDto container) {
        WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);
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

    private PsiClass getPsiClassFromPsiFile(PsiFile psiFile) {
        if (!(psiFile instanceof PsiJavaFile)) {
            return null;
        }
        return Arrays.stream(((PsiJavaFile) psiFile).getClasses())
                .filter(c -> c.getModifierList() != null && c.getModifierList().hasModifierProperty(PsiModifier.PUBLIC))
                .findFirst().orElse(null);
    }
}