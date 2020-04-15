package org.websync;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class SendTestEventAction extends AnAction {

            /*
             * temporary
             @SuppressWarnings to fade out Codacy
             * warning Pattern:Document empty method body
             * //ToDO: add valuable comment
             * @param message
             */

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

    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    private WebSocketClient createClient() throws URISyntaxException {
        URI uri = new URI("http://127.0.0.1:" + WebSyncService.getPortFromConfig());
        return new WebSocketClient(uri) {
            @Override
            @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
            public void onOpen(ServerHandshake handshakedata) {

            }

            @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
            public void onMessage(String message) {

            }

            @Override
            @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
            public void onError(Exception ex) {

            }
        };
    }
}
