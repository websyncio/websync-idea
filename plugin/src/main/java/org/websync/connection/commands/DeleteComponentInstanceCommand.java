package org.websync.connection.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiField;
import org.websync.exceptions.WebSyncException;
import org.websync.connection.messages.browser.ComponentInstanceMessage;
import org.websync.psi.SeleniumProjectsProvider;

public class DeleteComponentInstanceCommand extends CommandWithDataBase<ComponentInstanceMessage> {
    public DeleteComponentInstanceCommand(SeleniumProjectsProvider projectsProvider) {
        super(projectsProvider);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, ComponentInstanceMessage.class));
    }

    @Override
    public Object execute(ComponentInstanceMessage commandData) throws WebSyncException {
        final Module module = projectsProvider.findProject(commandData.projectName);
        String className = commandData.componentInstance.parentId;
        int fieldIndex = commandData.componentInstance.fieldIndex;
        WriteAction.runAndWait(() -> {
            PsiField psiField = findPsiFieldByIndex(module, className, fieldIndex);
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    className + ": delete field with index'" + fieldIndex,
                    "WebSyncAction",
                    () -> {
                        psiField.delete();
                    });
        });
        return null;
    }
}
