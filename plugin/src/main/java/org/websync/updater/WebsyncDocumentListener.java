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
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.websync.WebSyncService;
import org.websync.jdi.JdiElement;
import org.websync.logger.LoggerUtils;
import org.websync.websocket.BrowserConnection;

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
        LoggerUtils.print(String.format("documentChanged: '%s'", file.getPath()));
        Project project = ProjectUtil.guessProjectForFile(file);
        if(project == null) {
            return;
        }
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        LoggerUtils.print(String.format("psiFile of this file: '%s'", psiFile));

        PsiClass psiClass = getPsiClassFromPsiFile(psiFile);
        boolean isPage = this.isPage(psiClass);
        boolean isComponent = this.isComponent(psiClass);
        LoggerUtils.print(String.format("isPage: %s, isComponent: %s", isPage, isComponent));

        if (!isPage && !isComponent) {
            return;
        }
        execute();
    }

    public void execute() {
        WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);
        BrowserConnection browserConnection = webSyncService.getBrowserConnection();
        WebSocket webSocket = browserConnection.getConnections().stream().findFirst().orElse(null);
        if (webSocket == null) {
            return;
        }

        String message = "{\"command\": \"get-modules\"}";
        browserConnection.onMessage(webSocket, message);
    }

//    public void execute(String projectName) {
//        WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);
//
//        final String[] json = new String[1];
//        int index = webSyncService.getProvider().getProjects().stream().map(Project::getName).collect(Collectors.toList()).indexOf(projectName);
//        if (index < 0) {
//            return;
//        }
//        ApplicationManager.getApplication().runReadAction(() -> {
//            json[0] = webSyncService.getSerializer().serialize(webSyncService.getProvider().getWebSession(
//                    webSyncService.getProvider().getProjects().get(index)));
//        });
//        json[0] = json[0].replaceFirst("\\{", String.format("{\"module\": \"%s\",", projectName));
//        webSyncService.getBrowserConnection().broadcast(json[0]);
//    }

    private boolean isComponent(PsiClass psiClass) {
        return Arrays.asList(psiClass.getSuperTypes()).stream()
                .anyMatch(s -> InheritanceUtil.isInheritor(s, JdiElement.JDI_UI_BASE_ELEMENT.className));
    }

    private boolean isPage(PsiClass psiClass) {
        return Arrays.asList(psiClass.getSuperTypes()).stream()
                .anyMatch(s -> InheritanceUtil.isInheritor(s, JdiElement.JDI_WEB_PAGE.className));
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