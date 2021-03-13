package org.websync.websocket.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.jdi.JdiAttribute;
import org.websync.react.dto.WebsiteDto;
import com.intellij.openapi.module.Module;

public class UpdateWebsiteCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public String projectName;
        public WebsiteDto webSite;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) throws WebSyncException {
        UpdateWebsiteCommand.Message message = ((UpdateWebsiteCommand.Message) inputMessage);
        WebsiteDto webSite = message.webSite;
        String className = webSite.id;
        final Module module = getWebSyncService().getProvider().findByFullName(message.projectName);
        updateWebsiteUrl(module, className, webSite.url);
        return null;
    }

    public void updateWebsiteUrl(Module module, String className, String url) throws WebSyncException{
        JdiAttribute urlAttribute = JdiAttribute.JDI_JSITE;
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(module.getProject());

        WriteAction.runAndWait(() -> {
            PsiClass psiWebsiteClass = javaPsiFacade.findClass(className, GlobalSearchScope.moduleScope(module));
            if (psiWebsiteClass == null) {
                throw new WebSyncException("WebSite not found: " + className);
            }
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    () -> {
                        PsiAnnotation psiAnnotation = psiWebsiteClass.getAnnotation(urlAttribute.className);
                        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(module.getProject()).getElementFactory();
                        PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(
                                "@" + urlAttribute.name + "(\"" + url + "\")",
                                null);
                        psiAnnotation.replace(newAnnotation);
                    });
        });
    }
}
