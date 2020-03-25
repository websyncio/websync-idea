package org.websync;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.websync.browserConnection.BrowserConnection;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.debugger.DebugFileWatcher;
import org.websync.debugger.FileParser;
import org.websync.react.ReactSerializer;
import org.websync.browserConnection.CommandHandler;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.WebSessionPovider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebSyncService {
    BrowserConnection browserConnection;
    WebSessionPovider provider;
    DebugFileWatcher debugFileWatcher;

    public WebSyncService() {
        init();
    }

    public void init() {
        // .init provider
        this.provider = new PsiWebSessionProvider();

        // .init browser connection
        WebSessionSerializer serializer = new ReactSerializer();
        CommandHandler commandHandler = new CommandHandler(this.provider, serializer);
        this.browserConnection = createBrowserConnection(commandHandler);

        // .init debug file watcher
        this.debugFileWatcher = createDebugFileWatcher();

        // .start
        this.browserConnection.start();
        this.debugFileWatcher.start();
    }

    private BrowserConnection createBrowserConnection(CommandHandler commandHandler) {
        // TODO: get port from settings
        return new BrowserConnection( 1804, commandHandler);
    }

    @NotNull
    private DebugFileWatcher createDebugFileWatcher() {
        Path projectDir = getProjectDir();
        Path debugFilePath = createDebugFile(projectDir);
        System.out.println(String.format("Project directory is '%s'.", projectDir));
        System.out.println(String.format("Debug file path is '%s'.", debugFilePath));
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

    public void dispose() throws IOException,InterruptedException {
        browserConnection.stop();
        debugFileWatcher.stop();
    }

    public void addProject(Project project) {
        this.provider.addProject(project);
    }

    public void removeProject(Project project) {
        this.provider.removeProject(project);
    }
}