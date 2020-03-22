package org.websync;

import com.intellij.openapi.components.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.websync.debugger.DebugFileWatcher;
import org.websync.debugger.FileParser;
import org.websync.server.BrowserConnection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// TODO: get rid of this class
public class InitializationComponent implements BaseComponent {
    DebugFileWatcher debugFileWatcher;

    public InitializationComponent() {
    }

    public void initComponent() {
        InitDebugFileWatcher();
    }

    @NotNull
    private Path InitDebugFileWatcher() {
        String projectDir = GetProjectDir();
        Path debugFilePath = createDebugFile(projectDir);
        debugFileWatcher = new DebugFileWatcher(new File(debugFilePath.toString()), new FileParser());
        debugFileWatcher.start();
        System.out.println(String.format("Project directory is '%s'.", projectDir));
        System.out.println(String.format("Debug file path is '%s'.", debugFilePath));
        return debugFilePath;
    }


    private Path createDebugFile(String projectDir) {
        Path debugFilePath = Paths.get(projectDir + "/debug.txt");
        try {
            Files.write(debugFilePath, "command".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return debugFilePath;
    }

    private String GetProjectDir() {
        String path = InitializationComponent.class.getResource("").getPath();
        return Paths.get(path
                .replace("file:/", "")
                .replaceFirst("[!].*", ""))
                .getParent().toString();
    }

    public void disposeComponent() {
        System.out.println("Disposing...");
        debugFileWatcher.stop();
        System.out.println("Disposed.");
    }

    @NotNull
    public String getComponentName() {
        return "InitializationComponent";
    }
}
