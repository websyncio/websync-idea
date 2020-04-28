package com.epam.sha.intellij.locatorupdater;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiJavaFile;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.websync.WebSyncService;
import org.websync.websocket.BrowserConnection;

public class ButtonAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiJavaFile javaFile = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);
        String packageName = javaFile.getPackageName();
        String fileName = javaFile.getVirtualFile().getNameWithoutExtension();
        if (fileName == null) {
            System.out.println("There aren't any opened files");
            return;
        }
        try {
            execute(packageName + "." + fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void execute(String filename) {
        WebSyncService webSyncService = ServiceManager.getService(WebSyncService.class);
        BrowserConnection browserConnection = webSyncService.getBrowserConnection();
        WebSocket webSocket = browserConnection.getConnections().stream().findFirst().orElse(null);
        if (webSocket == null) {
            return;
        }
        String message = "{\"type\": \"PageType\",\"status\": 0,\"data\": \"" + filename + "\"}";
        browserConnection.broadcast(message);
    }
}
