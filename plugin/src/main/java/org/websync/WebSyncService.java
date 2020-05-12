package org.websync;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeListener;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.websync.debugger.DebugFileWatcher;
import org.websync.debugger.FileParser;
import org.websync.logger.LoggerUtils;
import org.websync.websession.PsiJdiModulesProvider;
import org.websync.websession.JdiModulesProvider;
import org.websync.websocket.BrowserConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebSyncService {
    @Getter
    private BrowserConnection browserConnection;
    @Getter
    private JdiModulesProvider provider;
    private DebugFileWatcher debugFileWatcher;
    final private WebSyncPsiTreeChangeListener psiTreeChangeListener;

    public WebSyncService() {
        // .init provider
        provider = new PsiJdiModulesProvider();

        // .init browser connection
        browserConnection = createBrowserConnection();

        // .init debug file watcher
        debugFileWatcher = createDebugFileWatcher();

        // .start
        browserConnection.start();
        debugFileWatcher.start();
        psiTreeChangeListener = new WebSyncPsiTreeChangeListener(this);
    }

    private BrowserConnection createBrowserConnection() {
        return new BrowserConnection(getPortFromConfig());
    }

    public static int getPortFromConfig() {
        // TODO: get port from settings
        return 1804;
    }

    @NotNull
    private DebugFileWatcher createDebugFileWatcher() {
        Path projectDir = getProjectDir();
        Path debugFilePath = createDebugFile(projectDir);
        LoggerUtils.print(String.format("Project directory is '%s'.", projectDir));
        LoggerUtils.print(String.format("Debug file path is '%s'.", debugFilePath));
        return new DebugFileWatcher(new File(debugFilePath.toString()), new FileParser());
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
        debugFileWatcher.stop();
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