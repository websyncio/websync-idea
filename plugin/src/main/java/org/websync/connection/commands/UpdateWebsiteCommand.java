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
import org.websync.connection.dto.WebsiteDto;
import org.websync.connection.messages.browser.WebSiteMessage;
import org.websync.frameworks.jdi.JdiAttribute;
import org.websync.psi.SeleniumProjectsProvider;

import javax.naming.OperationNotSupportedException;

public class UpdateWebsiteCommand extends CommandWithDataBase<WebSiteMessage> {
    public UpdateWebsiteCommand(SeleniumProjectsProvider projectsProvider) {
        super(projectsProvider);
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
        final Module module = projectsProvider.findProject(commandData.projectName);
        updateWebsiteUrl(module, className, webSite.url);
        return null;
    }

    public void updateWebsiteUrl(Module module, String className, String url) throws WebSyncException {
        JdiAttribute urlAttribute = JdiAttribute.JDI_JSITE;
        WriteAction.runAndWait(() -> {
            JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(module.getProject());
            PsiClass psiWebsiteClass = javaPsiFacade.findClass(className, GlobalSearchScope.moduleScope(module));
            if (psiWebsiteClass == null) {
                throw new WebSyncException("WebSite not found: " + className);
            }

            PsiElementFactory elementFactory = JavaPsiFacade.getInstance(module.getProject()).getElementFactory();
            PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(
                    "@" + urlAttribute.name + "(\"" + url + "\")",
                    null);

            PsiAnnotation psiAnnotation = psiWebsiteClass.getAnnotation(urlAttribute.className);
            if (psiAnnotation == null) {
                throw new WebSyncException("Could not find JSite annotation for website");
            } else {
                WriteCommandAction.runWriteCommandAction(module.getProject(),
                        "Update Website URL",
                        "WebSyncAction",
                        () -> psiAnnotation.replace(newAnnotation));
            }
        });
    }
}
