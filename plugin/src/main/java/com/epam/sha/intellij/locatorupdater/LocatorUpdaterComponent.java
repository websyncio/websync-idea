package com.epam.sha.intellij.locatorupdater;

import com.epam.sha.intellij.locatorupdater.handler.OpenFileMessageHandler;
import com.epam.sha.intellij.locatorupdater.notifier.RequestNotifier;
import com.epam.sha.intellij.locatorupdater.notifier.SocketNotifier;
import com.epam.sha.intellij.locatorupdater.settings.LocatorUpdaterSettings;
import com.epam.sha.intellij.locatorupdater.utils.FileNavigatorImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class LocatorUpdaterComponent implements BaseComponent {
    private static final Logger log = Logger.getInstance(LocatorUpdaterComponent.class);
    private final LocatorUpdaterSettings mySettings;

    private ServerSocket serverSocket;
    private Thread listenerThread;

    public LocatorUpdaterComponent(LocatorUpdaterSettings settings) {
        mySettings = settings;
    }

    public void initComponent() {
        final int port = mySettings.getPortNumber();
        final boolean allowRequestsFromLocalhostOnly = mySettings.isAllowRequestsFromLocalhostOnly();

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(allowRequestsFromLocalhostOnly ? "localhost" : "0.0.0.0", port));
            log.info("Listening " + port);
        } catch (IOException e) {
            ApplicationManager.getApplication().invokeLater(() -> Messages
                    .showMessageDialog("Can't bind with " + port + " port. LocatorUpdater plugin won't work", "LocatorUpdater Plugin Error",
                            Messages.getErrorIcon()));
            return;
        }

        RequestNotifier messageNotifier = new SocketNotifier(serverSocket);
        messageNotifier.addRequestHandler(new OpenFileMessageHandler(new FileNavigatorImpl()));
        listenerThread = new Thread(messageNotifier);
        listenerThread.start();
    }

    public void disposeComponent() {
        try {
            if (listenerThread != null) {
                listenerThread.interrupt();
            }
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public String getComponentName() {
        return "LocatorUpdaterComponent";
    }
}