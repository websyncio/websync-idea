package org.websync.server;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.java_websocket.server.WebSocketServer;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.ember.ReactSerializer;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.WebSessionPovider;


public class BrowserConnection {
    private static final Logger log = Logger.getInstance(BrowserConnection.class);
    private Server server;
    private String host;
    private Integer port;

    public BrowserConnection(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        WebSessionPovider provider = GetWebSesstionProvider();
        WebSessionSerializer serializer = new ReactSerializer();
        server = new Server(port, new CommandHandler(provider, serializer));
        server.start();
        log.info("Listening port:" + port);
    }

    private PsiWebSessionProvider GetWebSesstionProvider() {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        return new PsiWebSessionProvider(project);
    }

    public void disposeConnection() {
        try {
            server.stop(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}