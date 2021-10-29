package org.websync;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import lombok.Getter;
import org.websync.connection.BrowserConnection;
import org.websync.connection.CommandsHandler;
import org.websync.psi.JdiProjectsProvider;
import org.websync.psi.PsiJdiProjectsProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebSyncService {
    @Getter
    private BrowserConnection browserConnection;
    @Getter
    private JdiProjectsProvider modulesProvider;
    final private WebSyncPsiTreeChangeListener psiTreeChangeListener;

    public WebSyncService() {
        modulesProvider = new PsiJdiProjectsProvider();
        CommandsHandler commandsHandler = new CommandsHandler(this);
        browserConnection = new BrowserConnection(getPortFromConfig(), commandsHandler);
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
        browserConnection.stop();
    }

    public void addProject(Project project) {
        modulesProvider.addProject(project);
        PsiManager.getInstance(project).addPsiTreeChangeListener(psiTreeChangeListener);
    }

    public void removeProject(Project project) {
        modulesProvider.removeProject(project);
        PsiManager.getInstance(project).removePsiTreeChangeListener(psiTreeChangeListener);
    }
}