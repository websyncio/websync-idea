package org.websync.connection.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.search.GlobalSearchScope;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.dto.WebsiteDto;
import org.websync.connection.messages.browser.WebSiteMessage;
import org.websync.frameworks.jdi.JdiAttribute;

public class UpdateWebsiteCommand extends CommandWithDataBase<WebSiteMessage> {
    public UpdateWebsiteCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        WebSiteMessage commandData = parseCommandData(commandDataString, WebSiteMessage.class);
        return execute(commandData);
    }

    @Override
    public Object execute(WebSiteMessage commandData) throws WebSyncException {
        WebsiteDto webSite = commandData.webSite;
        String className = webSite.id;
        final Module module = webSyncService.getProvider().findByFullName(commandData.projectName);
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
