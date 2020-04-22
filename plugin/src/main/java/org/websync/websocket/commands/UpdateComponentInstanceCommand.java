package org.websync.websocket.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.jdi.JdiAttribute;
import org.websync.logger.Logger;
import org.websync.react.dto.AnnotationDto;
import org.websync.react.dto.ComponentInstanceDto;

import java.util.LinkedHashMap;

public class UpdateComponentInstanceCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public ComponentInstanceDto data;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) throws WebSyncException {
        ComponentInstanceDto data = ((Message) inputMessage).data;
        int lastDot = data.id.lastIndexOf('.');
        String className = data.id.substring(0, lastDot);
        String oldFieldName = data.id.substring(lastDot + 1);
        String newFieldName = data.name;
        updateComponentInstance(className, oldFieldName, newFieldName);
        if (data.initializationAttribute.getParameters().size() > 1) {
            Logger.print("Annotation has more than one parameters. Processing of that case is not implemented.");
            return null;
        }
        updateComponentInstanceWithSingleAttribute(className, oldFieldName, data.initializationAttribute);
        return null;
    }

    public void updateComponentInstance(String className, String oldFieldName, String newFieldName) throws WebSyncException {
        final Project project = getWebSyncService().getProvider().getProjects().get(0);

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);
        WriteAction.runAndWait(() -> {
            PsiClass componentPsiClass = javaPsiFacade.findClass(className, allScope);
            if (componentPsiClass == null) {
                throw new WebSyncException("Component not found: " + className);
            }
            PsiField psiField = componentPsiClass.findFieldByName(oldFieldName, false);
            if (psiField == null) {
                throw new WebSyncException("Field " + oldFieldName + " not found in component: " + className);
            }
            WriteCommandAction.runWriteCommandAction(project,
                    className + ": rename '" + oldFieldName + "' to '" + newFieldName + "'",
                    "WebSyncAction",
                    () -> {
                psiField.setName(newFieldName);
            });
        });
    }

    public void updateComponentInstanceWithSingleAttribute(String className, String fieldName,
                                                           AnnotationDto annotationDto) throws WebSyncException {
        final Project project = getWebSyncService().getProvider().getProjects().get(0);

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);
        WriteAction.runAndWait(() -> {
            PsiClass componentPsiClass = javaPsiFacade.findClass(className, allScope);
            if (componentPsiClass == null) {
                throw new WebSyncException("Component not found: " + className);
            }
            PsiField psiField = componentPsiClass.findFieldByName(fieldName, false);
            if (psiField == null) {
                throw new WebSyncException("Field " + fieldName + " not found in component: " + className);
            }
            String attributeShortName = annotationDto.getName();
            String attributeQualifiedName = JdiAttribute.getQualifiedNameByShortName(attributeShortName);
            if (attributeQualifiedName == null) {
                return;
            }

            LinkedHashMap params = ((LinkedHashMap) annotationDto.getParameters().get(0).getValues().get(0));
            WriteCommandAction.runWriteCommandAction(project,
                    className + ": update single annotation of field '" + fieldName +
                            "' with name '" + attributeShortName + "' and value '" + params.get("value") + "'",
                    "WebSyncAction",
                    () -> {
                        PsiAnnotation psiAnnotation = psiField.getAnnotation(attributeQualifiedName);

                        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();

                        annotationDto.getParameters().forEach(p -> {
                            PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(
                                    "@" + attributeShortName + "(\"" + params.get("value") + "\")",
                                    null);

                            psiAnnotation.replace(newAnnotation);
                        });
                    });
        });
    }
}
