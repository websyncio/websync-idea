package org.websync;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import lombok.Getter;
import org.websync.connection.BrowserConnection;
import org.websync.connection.CommandsHandler;
import org.websync.connection.ProjectUpdatesQueue;
import org.websync.connection.messages.idea.ProjectClosedMessage;
import org.websync.connection.messages.idea.ProjectOpenedMessage;
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
    private JdiProjectsProvider projectsProvider;
    final private WebSyncPsiTreeChangeListener psiTreeChangeListener;
    private ProjectUpdatesQueue projectUpdatesQueue;

    public WebSyncService() {
        projectsProvider = new PsiJdiProjectsProvider();
        browserConnection = new BrowserConnection(getPortFromConfig());
        projectUpdatesQueue = new ProjectUpdatesQueue(browserConnection, projectsProvider);
        CommandsHandler commandsHandler = new CommandsHandler(projectsProvider, projectUpdatesQueue);
        browserConnection.setCommandsHandler(commandsHandler);
        psiTreeChangeListener = new WebSyncPsiTreeChangeListener(projectUpdatesQueue);
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
        projectsProvider.addProject(project);
        PsiManager.getInstance(project).addPsiTreeChangeListener(psiTreeChangeListener);
        browserConnection.send(new ProjectOpenedMessage(project.getName()));
    }

    public void removeProject(Project project) {
        projectsProvider.removeProject(project);
        PsiManager.getInstance(project).removePsiTreeChangeListener(psiTreeChangeListener);
        browserConnection.send(new ProjectClosedMessage(project.getName()));
    }
}