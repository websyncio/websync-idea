package org.websync.connection.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiField;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.messages.browser.ComponentInstanceMessage;

public class DeleteComponentInstanceCommand extends CommandWithDataBase<ComponentInstanceMessage> {
    public DeleteComponentInstanceCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, ComponentInstanceMessage.class));
    }

    @Override
    public Object execute(ComponentInstanceMessage commandData) throws WebSyncException {
        final Module module = webSyncService.getModulesProvider().findProject(commandData.projectName);
        String className = commandData.componentInstance.parentId;
        int fieldIndex = commandData.componentInstance.fieldIndex;
        WriteAction.runAndWait(() -> {
            PsiField psiField = findPsiField(module, className, fieldIndex);
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
