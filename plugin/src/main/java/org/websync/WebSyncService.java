package org.websync;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.websync.debugger.DebugFileWatcher;
import org.websync.debugger.FileParser;
import org.websync.logger.Logger;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.WebSessionProvider;
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
    private WebSessionProvider provider;
    private DebugFileWatcher debugFileWatcher;

    public WebSyncService() {
        init();
    }

    public void init() {
        // .init provider
        this.provider = new PsiWebSessionProvider();

        // .init browser connection
        this.browserConnection = createBrowserConnection();

        // .init debug file watcher
        this.debugFileWatcher = createDebugFileWatcher();

        // .start
        this.browserConnection.start();
        this.debugFileWatcher.start();
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
        Logger.print(String.format("Project directory is '%s'.", projectDir));
        Logger.print(String.format("Debug file path is '%s'.", debugFilePath));
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
        this.provider.addProject(project);
    }

    public void removeProject(Project project) {
        this.provider.removeProject(project);
    }

    public void updateComponentInstance(String className, String oldFieldName, String newFieldName) {
        final Project project = provider.getProjects().get(0);

        ApplicationManager.getApplication().runWriteAction(() -> {
            JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
            GlobalSearchScope allScope = GlobalSearchScope.allScope(project);
            PsiClass componentPsiClass = javaPsiFacade.findClass(className, allScope);
            if (componentPsiClass == null) {
                return;
            }
            PsiField psiField = componentPsiClass.findFieldByName(oldFieldName, false);
            if (psiField != null) {
                psiField.setName(newFieldName);
            }
        });
    }
}