package org.websync.connection.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.source.PsiFieldImpl;
import org.websync.WebSyncException;
import org.websync.connection.ProjectUpdatesQueue;
import org.websync.connection.dto.PageInstanceDto;
import org.websync.connection.messages.browser.PageInstanceMessage;
import org.websync.frameworks.jdi.JdiAttribute;
import org.websync.psi.SeleniumProjectsProvider;
import org.websync.utils.PsiUtils;

public class UpdatePageInstanceCommand<T> extends CommandWithDataBase<PageInstanceMessage> {
    private ProjectUpdatesQueue projectUpdatesQueue;

    public UpdatePageInstanceCommand(SeleniumProjectsProvider projectsProvider, ProjectUpdatesQueue projectUpdatesQueue) {
        super(projectsProvider);
        this.projectUpdatesQueue = projectUpdatesQueue;
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
        String fieldName = pageInstance.id.substring(lastDot + 1);
        final Module module = projectsProvider.findProject(commandData.projectName);
        updatePageInstanceUrl(module, className, fieldName, pageInstance.url);
        return null;
    }

    public void updatePageInstanceUrl(Module module, String className, String fieldName, String url) throws WebSyncException {
        try {
            this.projectUpdatesQueue.captureProjectState(module.getProject());

            JdiAttribute urlAttribute = JdiAttribute.JDI_URL;
            WriteAction.runAndWait(() -> {
                PsiField psiField = findPsiFieldByName(module, className, fieldName);
                if (psiField == null) {
                    throw new WebSyncException("Field '" + fieldName + "' not found in '" + className + "'");
                }
                PsiElementFactory elementFactory = JavaPsiFacade.getInstance(module.getProject()).getElementFactory();
                String annotationText = getAnnotationText(urlAttribute, url);
                PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(
                        annotationText,
                        null);
                PsiAnnotation annotation = psiField.getAnnotation(urlAttribute.className);
                WriteCommandAction.runWriteCommandAction(module.getProject(),
                        "Update Page URL",
                        "WebSyncAction",
                        () -> {

                            if (annotation == null) {
                                PsiUtils.addAnnotationToField(psiField, newAnnotation);
                            } else {
                                annotation.replace(newAnnotation);
                            }
                        });
            });
        } finally {
            this.projectUpdatesQueue.releaseProjectState(module.getProject());
        }
    }

    private String getAnnotationText(JdiAttribute urlAttribute, String url) {
        final String DYNAMIC_PARAMETER_TEMPLATE = "{0}";
        if (url.contains(DYNAMIC_PARAMETER_TEMPLATE)) {
            String template = url.replaceAll("\\{\\d+\\}", "([^\\/]+)");
            return "@" + urlAttribute.name + "(value = \"" + url + "\", template = \"" + template + "\")";
        } else {
            return "@" + urlAttribute.name + "(\"" + url + "\")";
        }
    }
}

