package org.websync.updater;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.InheritanceUtil;
import org.jetbrains.annotations.NotNull;
import org.websync.WebSyncService;
import org.websync.browserConnection.CommandHandler;
import org.websync.jdi.JdiElement;
import org.websync.websession.PsiWebSessionProvider;

import java.util.Arrays;

public class WebsyncDocumentListener implements DocumentListener {

    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        Document document = event.getDocument();
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        VirtualFile file = fileDocumentManager.getFile(document);
        if(file == null) {
            return;
        }

        WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);
        PsiWebSessionProvider provider = (PsiWebSessionProvider) webSyncService.getProvider();
        Project project = provider.getProjects().get(0);

        System.out.println(String.format("documentChanged: '%s'", file.getPath()));

        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        System.out.println(String.format("psiFile of this file: '%s'", psiFile));

        PsiClass psiClass = getPsiClassFromPsiFile(psiFile);
        boolean isPage = this.isPage(psiClass);
        boolean isComponent = this.isComponent(psiClass);
        System.out.println(String.format("isPage: %s, isComponent: %s", isPage, isComponent));

        webSyncService.getCommandHandler().handle(CommandHandler.CMD_GET_PAGEOBJECTS);
    }

    private boolean isComponent(PsiClass psiClass) {
        return Arrays.asList(psiClass.getSuperTypes()).stream()
                .anyMatch(s -> InheritanceUtil.isInheritor(s, JdiElement.JDI_UI_BASE_ELEMENT.value));
    }

    private boolean isPage(PsiClass psiClass) {
        return Arrays.asList(psiClass.getSuperTypes()).stream()
                .anyMatch(s -> InheritanceUtil.isInheritor(s, JdiElement.JDI_WEB_PAGE.value));
    }

    private PsiClass getPsiClassFromPsiFile(PsiFile psiFile) {
        if (!(psiFile instanceof PsiJavaFile)) {
            return null;
        }
        PsiClass clazz = Arrays.asList(((PsiJavaFile) psiFile).getClasses()).stream()
                .filter(c -> c.getModifierList().hasModifierProperty(PsiModifier.PUBLIC)).findFirst().get();
        return clazz;
    }
}