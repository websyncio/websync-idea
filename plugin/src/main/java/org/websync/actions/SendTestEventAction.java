package org.websync.actions;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.vfs.VirtualFile;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.websync.WebSyncService;

import java.net.URI;
import java.net.URISyntaxException;

public class SendTestEventAction extends DumbAwareAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final FileChooserDescriptor desc = new FileChooserDescriptor(true, false, false, false, false, false);
        VirtualFile f = FileChooser.chooseFile(desc, e.getProject(), null);
        if(f == null) {
            System.out.println("No command file was chosen!");
            return;
        }
        try {
            String message = new String(f.contentsToByteArray());
            WebSocketClient client = createClient();
            client.connectBlocking();
            client.send(message);
            client.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private WebSocketClient createClient() throws URISyntaxException {
        URI uri = new URI("http://127.0.0.1:" + WebSyncService.getPortFromConfig());
        return new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
            //temporary comment
            /*TODO: implement
            /*
            not implemented yet
            attempt to fade out Codacy Warning:
            Document empty method bodyn. (PMD 3.4)
             */
            }
            @Override
            public void onMessage(String message) {
            //temporary comment
            /*TODO: implement
            /*
            not implemented yet
            attempt to fade out Codacy Warning:
            Document empty method bodyn. (PMD 3.4)
             */
            }
            @Override
            public void onClose(int code, String reason, boolean remote) {
            //temporary comment
            /*TODO: implement
            /*
            not implemented yet
            attempt to fade out Codacy Warning:
            Document empty method bodyn. (PMD 3.4)
             */
            }
            @Override
            public void onError(Exception ex) {
            //temporary comment
            /*TODO: implement
            /*
            not implemented yet
            attempt to fade out Codacy Warning:
            Document empty method bodyn. (PMD 3.4)
             */
            }
        };
    }
}
