package org.websync;

import com.intellij.openapi.components.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.websync.utils.DebugFileWatcher;
import org.websync.utils.FileParser;

import java.io.File;
import java.net.ServerSocket;

public class InitializationComponent implements BaseComponent {

    DebugFileWatcher debugFileWatcher = new DebugFileWatcher(new File("c:/temp/text.txt"), new FileParser());
    BrowserConnection browserConnection = new BrowserConnection("localhost", 1804);

    private ServerSocket serverSocket;
    private Thread listenerThread;

    public InitializationComponent() {
    }

    public void initComponent() {
        System.out.println("Initializing...");

        debugFileWatcher.start();
        browserConnection.initConnection();
        System.out.println("Initialized.");
    }

    public void disposeComponent() {
        System.out.println("Disposing...");

        debugFileWatcher.stop();
        browserConnection.disposeConnection();
        System.out.println("Disposed.");
    }

    @NotNull
    public String getComponentName() {
        return "InitializationComponent";
    }
}
