package org.websync.websocket.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.jdi.JdiAttribute;
import org.websync.react.dto.PageInstanceDto;

public class UpdatePageInstanceCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public String projectName;
        public PageInstanceDto pageInstance;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) throws WebSyncException {
        UpdatePageInstanceCommand.Message message = ((UpdatePageInstanceCommand.Message) inputMessage);
        PageInstanceDto pageInstance = message.pageInstance;
        int lastDot = pageInstance.id.lastIndexOf('.');
        String className = pageInstance.id.substring(0, lastDot);
        String fieldName = pageInstance.id.substring(lastDot + 1);
        final Module module = getWebSyncService().getProvider().findByFullName(message.projectName);
        updatePageInstanceUrl(module, className, fieldName, pageInstance.url);
        return null;
    }

    public void updatePageInstanceUrl(Module module, String className, String fieldName , String url) throws WebSyncException {
        JdiAttribute urlAttribute = JdiAttribute.JDI_URL;
        WriteAction.runAndWait(() -> {
            PsiField psiField = findPsiField(module, className, fieldName);
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    () -> {
                        PsiAnnotation psiAnnotation = psiField.getAnnotation(urlAttribute.className);
                        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(module.getProject()).getElementFactory();
                        PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(
                                "@" + urlAttribute.name + "(\"" + url + "\")",
                                null);
                        psiAnnotation.replace(newAnnotation);
                    });
        });
    }
}

