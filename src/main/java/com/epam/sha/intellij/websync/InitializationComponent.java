package com.epam.sha.intellij.websync;

import com.epam.sha.intellij.websync.utils.DebugFileWatcher;
import com.epam.sha.intellij.websync.utils.FileParser;
import com.intellij.openapi.components.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class InitializationComponent implements BaseComponent {

    DebugFileWatcher tempFileWatcher = new DebugFileWatcher(new File("c:/temp/text.txt"), new FileParser());

    public void initComponent() {
        System.out.println("Initializing...");

        tempFileWatcher.start();

        System.out.println("Initialized.");
    }

    public void disposeComponent() {
        System.out.println("Disposing...");

        tempFileWatcher.stop();

        System.out.println("Disposed.");
    }

    @NotNull
    public String getComponentName() {
        return "InitializationComponent";
    }
}
