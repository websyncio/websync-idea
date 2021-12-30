package org.websync.connection.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.ThrowableComputable;
import org.websync.psi.SeleniumProjectsProvider;

import java.util.List;

public class GetProjectsCommand extends CommandBase {
    public GetProjectsCommand(SeleniumProjectsProvider projectsProvider) {
        super(projectsProvider);
    }

    @Override
    public Object execute(String data) {
        return ReadAction.compute(() -> projectsProvider.getModuleNames());
    }
}
