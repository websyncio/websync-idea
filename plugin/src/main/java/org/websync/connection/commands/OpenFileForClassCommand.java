package org.websync.connection.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import org.websync.exceptions.WebSyncException;
import org.websync.connection.messages.browser.OpenFileMessage;
import org.websync.psi.SeleniumProjectsProvider;
import org.websync.utils.PsiUtils;

import static com.intellij.util.OpenSourceUtil.navigate;

public class OpenFileForClassCommand extends CommandWithDataBase<OpenFileMessage> {
    public OpenFileForClassCommand(SeleniumProjectsProvider projectsProvider) {
        super(projectsProvider);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, OpenFileMessage.class));
    }

    @Override
    public Object execute(OpenFileMessage commandData) throws WebSyncException {
        final Module module = projectsProvider.findProject(commandData.projectName);
        ReadAction.run(() -> {
            PsiClass psiClass = PsiUtils.findClass(module, commandData.fullClassName);
            if (psiClass == null) {
                throw new WebSyncException("Class was not found: " + commandData.fullClassName);
            }
            VirtualFile classFile = psiClass.getContainingFile().getVirtualFile();
            OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(module.getProject(), classFile);
            ApplicationManager.getApplication().invokeLater(() -> {
                        openFileDescriptor.navigate(false);
                    },
                    ModalityState.defaultModalityState());
        });
        return null;
    }
}
