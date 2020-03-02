package org.websync;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;
import org.websync.debbuger.DebugFileWatcher;
import org.websync.debbuger.FileParser;

import java.io.File;

public class InitializationStartupActivity implements StartupActivity {
    DebugFileWatcher tempFileWatcher = new DebugFileWatcher(new File("c:/temp/text.txt"), new FileParser());

    @Override
    public void runActivity(@NotNull Project project) {
        DumbService.getInstance(project).runWhenSmart(() -> {

        });
    }
}
