package org.websync.websocket.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;

public class DeleteComponentInstanceCommand extends ComponentInstanceCommand {
    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message genericMessage) throws WebSyncException {
        ComponentInstanceCommand.Message message = ((ComponentInstanceCommand.Message) genericMessage);
        final Module module = getWebSyncService().getProvider().findByFullName(message.projectName);
        String className = message.getContainerClassName();
        int fieldIndex = message.getFieldIndex();

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
