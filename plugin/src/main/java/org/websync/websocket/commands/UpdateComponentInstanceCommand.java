package org.websync.websocket.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.impl.source.tree.ChildRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.jdi.JdiAttribute;
import org.websync.logger.LoggerUtils;
import org.websync.react.dto.AnnotationDto;
import org.websync.react.dto.ComponentInstanceDto;

public class UpdateComponentInstanceCommand extends WebSyncCommand {
    static class Message extends WebSyncCommand.Message {
        public String projectName;
        public ComponentInstanceDto data;
    }

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message inputMessage) throws WebSyncException {
        ComponentInstanceDto data = ((Message) inputMessage).data;
        String moduleName = ((Message) inputMessage).projectName;
        int lastDot = data.id.lastIndexOf('.');
        String className = data.id.substring(0, lastDot);
        int fieldIndex = Integer.parseInt(data.id.substring(lastDot + 1));
        String newFieldName = data.fieldName;
        String newFieldTypeName = data.componentType.substring(data.componentType.lastIndexOf('.') + 1);
        updateComponentInstance(moduleName, className, fieldIndex, newFieldTypeName, newFieldName);
        if (data.initializationAttribute.getParameters().size() > 1) {
            String message = "Changed annotation has more than one parameters. Processing of that case is not implemented.";
            LoggerUtils.print(message);
            throw new WebSyncException(message);
        }
        updateComponentInstanceWithSingleAttribute(moduleName, className, fieldIndex, data.initializationAttribute);
        return "Attribute was changed.";
    }

    public void updateComponentInstance(String moduleName, String className, int fieldIndex, String newFieldType, String newFieldName) throws WebSyncException {
        final Module module = getWebSyncService().getProvider().findByFullName(moduleName);

        WriteAction.runAndWait(() -> {
            PsiFieldImpl psiField = (PsiFieldImpl) findPsiField(module, className, fieldIndex);
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiField.getManager().getProject());
            PsiTypeElement typeElement = (PsiTypeElement) psiField.getNode().findChildByRoleAsPsiElement(ChildRole.TYPE);
            PsiTypeElement newTypeElement = elementFactory.createTypeElementFromText(newFieldType,null);
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    className + ": rename field with index'" + fieldIndex + "' to '" + newFieldName + "'",
                    "WebSyncAction",
                    () -> {
                        typeElement.replace(newTypeElement);
                        psiField.setName(newFieldName);
                    });
        });
    }

    public void updateComponentInstanceWithSingleAttribute(String moduleName, String className, int fieldIndex,
                                                           AnnotationDto annotationDto) throws WebSyncException {
        final Module module = getWebSyncService().getProvider().findByFullName(moduleName);

        WriteAction.runAndWait(() -> {
            PsiField psiField = findPsiField(module, className, fieldIndex);
            String attributeShortName = annotationDto.getName();
            String attributeQualifiedName = JdiAttribute.getQualifiedNameByShortName(attributeShortName);
            if (attributeQualifiedName == null) {
                return;
            }

            Object param = annotationDto.getParameters().get(0).getValues().get(0);
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    className + ": update single annotation of field with index '" + fieldIndex +
                            "' with name '" + attributeShortName + "' and value '" + param + "'",
                    "WebSyncAction",
                    () -> {
                        PsiAnnotation psiAnnotation = psiField.getAnnotation(attributeQualifiedName);

                        PsiManager manager = psiAnnotation.getManager();
                        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(manager.getProject());

                        annotationDto.getParameters().forEach(p -> {
                            PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(
                                    "@" + attributeShortName + "(\"" + param + "\")",
                                    null);

                            psiAnnotation.replace(newAnnotation);
                        });
                    });
        });
    }
}
