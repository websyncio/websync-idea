package org.websync;

import com.intellij.openapi.components.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.websync.debugger.DebugFileWatcher;
import org.websync.debugger.FileParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InitializationComponent implements BaseComponent {

    String projectDir;

    {
        String path = InitializationComponent.class.getResource("").getPath();
        projectDir = Paths.get(path
                .replace("file:/", "")
                .replaceFirst("[!].*", ""))
                .getParent().toString();
    }

    DebugFileWatcher debugFileWatcher;

    BrowserConnection browserConnection = new BrowserConnection("localhost", 1804);

    public InitializationComponent() {
    }

    public void initComponent() {
        System.out.println("Initializing...");

        Path debugFilePath = Paths.get(projectDir + "/debug.txt");
        try {
            Files.write(debugFilePath, "command".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        debugFileWatcher = new DebugFileWatcher(new File(debugFilePath.toString()), new FileParser());
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
