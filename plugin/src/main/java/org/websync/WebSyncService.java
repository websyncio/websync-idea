package org.websync;
import com.intellij.openapi.project.Project;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.react.ReactSerializer;
import org.websync.server.BrowserConnection;
import org.websync.server.CommandHandler;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.WebSessionPovider;

public class WebSyncService {
    BrowserConnection browserConnection;
    WebSessionPovider provider;

    public WebSyncService(){
        init();
    }

    public void init() {
        // .init
        this.provider = new PsiWebSessionProvider();
        WebSessionSerializer serializer = new ReactSerializer();
        CommandHandler commandHandler = new CommandHandler(this.provider, serializer);
        this.browserConnection = createBrowserConnection(commandHandler);

        // .start
        this.browserConnection.start();
    }

    private BrowserConnection createBrowserConnection(CommandHandler commandHandler) {
        // TODO: get port from settings
        return new BrowserConnection("localhost", 1804, commandHandler);
    }

    public void dispose() {
        browserConnection.disposeConnection();
    }

    public void addProject(Project project) {
        this.provider.addProject(project);
    }

    public void removeProject(Project project) {
        this.provider.removeProject(project);
    }
}