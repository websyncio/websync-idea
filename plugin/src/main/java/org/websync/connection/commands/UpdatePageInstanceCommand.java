package org.websync.connection.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.dto.PageInstanceDto;
import org.websync.connection.messages.browser.PageInstanceMessage;
import org.websync.frameworks.jdi.JdiAttribute;

public class UpdatePageInstanceCommand<T> extends CommandWithDataBase<PageInstanceMessage> {
    public UpdatePageInstanceCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        PageInstanceMessage commandData = parseCommandData(commandDataString, PageInstanceMessage.class);
        return execute(commandData);
    }

    @Override
    public Object execute(PageInstanceMessage commandData) throws WebSyncException {
        PageInstanceDto pageInstance = commandData.pageInstance;
        int lastDot = pageInstance.id.lastIndexOf('.');
        String className = pageInstance.id.substring(0, lastDot);
        int fieldIndex = Integer.parseInt(pageInstance.id.substring(lastDot + 1));
        final Module module = webSyncService.getProvider().findByFullName(commandData.projectName);
        updatePageInstanceUrl(module, className, fieldIndex, pageInstance.url);
        return null;
    }

    public void updatePageInstanceUrl(Module module, String className, int fieldIndex, String url) throws WebSyncException {
        JdiAttribute urlAttribute = JdiAttribute.JDI_URL;
        WriteAction.runAndWait(() -> {
            PsiField psiField = findPsiField(module, className, fieldIndex);
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

