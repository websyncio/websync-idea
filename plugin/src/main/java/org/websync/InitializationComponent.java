package org.websync;

import org.websync.utils.DebugFileWatcher;
import org.websync.utils.FileParser;
import com.intellij.openapi.components.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class InitializationComponent implements BaseComponent {

    DebugFileWatcher debugFileWatcher = new DebugFileWatcher(new File("c:/temp/text.txt"), new FileParser());

    public void initComponent() {
        System.out.println("Initializing...");

        debugFileWatcher.start();

        System.out.println("Initialized.");
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
