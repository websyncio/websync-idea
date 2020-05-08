package org.websync.websocket.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.jdi.JdiAttribute;
import org.websync.logger.LoggerUtils;
import org.websync.react.dto.AnnotationDto;
import org.websync.react.dto.ComponentInstanceDto;
import org.websync.websocket.BrowserConnection;

import java.util.LinkedHashMap;

public class UpdateComponentInstanceCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public String moduleName;
        public ComponentInstanceDto data;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) throws WebSyncException {
        ComponentInstanceDto data = ((Message) inputMessage).data;
        String moduleName = ((Message) inputMessage).moduleName;
        int lastDot = data.id.lastIndexOf('.');
        String className = data.id.substring(0, lastDot);
        String oldFieldName = data.id.substring(lastDot + 1);
        String newFieldName = data.name;
        updateComponentInstance(moduleName, className, oldFieldName, newFieldName);
        if (data.initializationAttribute.getParameters().size() > 1) {
            String message = "Changed annotation has more than one parameters. Processing of that case is not implemented.";
            LoggerUtils.print(message);
            return new BrowserConnection.ErrorReply(101, message);
        }
        updateComponentInstanceWithSingleAttribute(moduleName, className, oldFieldName, data.initializationAttribute);
        return new BrowserConnection.OkayReply("Attribute was changed.");
    }

    public void updateComponentInstance(String moduleName, String className, String oldFieldName, String newFieldName) throws WebSyncException {
        final Module module = getWebSyncService().getProvider().findByFullName(moduleName);

        WriteAction.runAndWait(() -> {
            PsiField psiField = findPsiClassInModule(module, className, oldFieldName);
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    className + ": rename '" + oldFieldName + "' to '" + newFieldName + "'",
                    "WebSyncAction",
                    () -> psiField.setName(newFieldName));
        });
    }

    private static PsiField findPsiClassInModule(Module module, String className, String fieldName) throws WebSyncException {
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(module.getProject());
        PsiClass componentPsiClass = javaPsiFacade.findClass(className, GlobalSearchScope.moduleScope(module));
        if (componentPsiClass == null) {
            throw new WebSyncException("Component not found: " + className);
        }
        PsiField psiField = componentPsiClass.findFieldByName(fieldName, false);
        if (psiField == null) {
            throw new WebSyncException("Field " + fieldName + " not found in component: " + className);
        }
        return psiField;
    }

    public void updateComponentInstanceWithSingleAttribute(String moduleName, String className, String fieldName,
                                                           AnnotationDto annotationDto) throws WebSyncException {
        final Module module = getWebSyncService().getProvider().findByFullName(moduleName);

        WriteAction.runAndWait(() -> {
            PsiField psiField = findPsiClassInModule(module, className, fieldName);
            String attributeShortName = annotationDto.getName();
            String attributeQualifiedName = JdiAttribute.getQualifiedNameByShortName(attributeShortName);
            if (attributeQualifiedName == null) {
                return;
            }

            LinkedHashMap params = ((LinkedHashMap) annotationDto.getParameters().get(0).getValues().get(0));
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    className + ": update single annotation of field '" + fieldName +
                            "' with name '" + attributeShortName + "' and value '" + params.get("value") + "'",
                    "WebSyncAction",
                    () -> {
                        PsiAnnotation psiAnnotation = psiField.getAnnotation(attributeQualifiedName);

                        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(module.getProject()).getElementFactory();

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
