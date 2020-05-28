package org.websync;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import lombok.Getter;
import org.websync.websession.JdiModulesProvider;
import org.websync.websession.PsiJdiModulesProvider;
import org.websync.websocket.BrowserConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebSyncService {
    @Getter
    private BrowserConnectionManager browserConnectionManager;
    @Getter
    private JdiModulesProvider provider;
    final private WebSyncPsiTreeChangeListener psiTreeChangeListener;

    public WebSyncService() {
        provider = new PsiJdiModulesProvider();

        browserConnectionManager = new BrowserConnectionManager(getPortFromConfig());

        psiTreeChangeListener = new WebSyncPsiTreeChangeListener(this);
    }

    public static int getPortFromConfig() {
        // TODO: get port from settings
        return 1804;
    }

    private Path createDebugFile(Path projectDir) {
        Path debugFilePath = Paths.get(projectDir + "/debug.txt");
        try {
            Files.write(debugFilePath, "command".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return debugFilePath;
    }

    private Path getProjectDir() {
        String path = WebSyncService.class.getResource("").getPath();
        return Paths.get(path
                .replace("file:/", "")
                .replaceFirst("[!].*", ""))
                .getParent();
    }

    public void dispose() throws IOException, InterruptedException {
        browserConnectionManager.stop();
    }

    public void addProject(Project project) {
        provider.addProject(project);
        PsiManager.getInstance(project).addPsiTreeChangeListener(psiTreeChangeListener);
    }

    public void removeProject(Project project) {
        provider.removeProject(project);
        PsiManager.getInstance(project).removePsiTreeChangeListener(psiTreeChangeListener);
    }
}