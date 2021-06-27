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
import org.websync.react.dto.AnnotationDto;
import org.websync.react.dto.ComponentInstanceDto;

public class UpdateComponentInstanceCommand extends ComponentInstanceCommand {

    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message genericMessage) throws WebSyncException {
        ComponentInstanceCommand.Message message = ((ComponentInstanceCommand.Message)genericMessage);
        ComponentInstanceDto componentInstance = message.data;
        String className = message.getContainerClassName();
        int fieldIndex = message.getFieldIndex();
        String newFieldName = componentInstance.fieldName;
        String newFieldTypeName = getNameFromTypeId(componentInstance.componentType);
        final Module module = getWebSyncService().getProvider().findByFullName(message.projectName);

        updateComponentInstance(module, className, fieldIndex, newFieldTypeName, newFieldName);
        if (componentInstance.initializationAttribute.getParameters().size() > 1) {
            throw new WebSyncException("Changed annotation has more than one parameters. Processing of that case is not implemented.");
        }
        updateComponentInstanceWithSingleAttribute(module, className, fieldIndex, componentInstance.initializationAttribute);
        return "Attribute was changed.";
    }

    public void updateComponentInstance(Module module, String className, int fieldIndex, String newFieldType, String newFieldName) throws WebSyncException {
        WriteAction.runAndWait(() -> {
            PsiFieldImpl psiField = (PsiFieldImpl) findPsiField(module, className, fieldIndex);
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiField.getManager().getProject());
            PsiTypeElement typeElement = (PsiTypeElement) psiField.getNode().findChildByRoleAsPsiElement(ChildRole.TYPE);

            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    className + ": rename field with index'" + fieldIndex + "' to '" + newFieldName + "'",
                    "WebSyncAction",
                    () -> {
                        if(!typeElement.getText().equals(newFieldType)) {
                            PsiTypeElement newTypeElement = elementFactory.createTypeElementFromText(newFieldType, null);
                            typeElement.replace(newTypeElement);
                        }
                        if(!psiField.getName().equals(newFieldName)) {
                            psiField.setName(newFieldName);
                        }
                    });
        });
    }

    public void updateComponentInstanceWithSingleAttribute(
            Module module,
            String className,
            int fieldIndex,
            AnnotationDto annotationDto) throws WebSyncException {
        WriteAction.runAndWait(() -> {
            PsiField psiField = findPsiField(module, className, fieldIndex);
            String attributeShortName = annotationDto.getName();
            String attributeQualifiedName = JdiAttribute.getQualifiedNameByName(attributeShortName);
            if (attributeQualifiedName == null) {
                return;
            }

            // .we use only first parameters so far and ignore others
            Object firstParameterValue = annotationDto.getParameters().get(0).getValues().get(0);
            WriteCommandAction.runWriteCommandAction(module.getProject(),
                className + ": update single annotation of field with index '" + fieldIndex +
                        "' with name '" + attributeShortName + "' and value '" + firstParameterValue + "'",
                "WebSyncAction",
                () -> {
                    PsiAnnotation psiAnnotation = psiField.getAnnotation(attributeQualifiedName);
                    PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(module.getProject());
                    String annotationText = "@" + attributeShortName + "(\"" + firstParameterValue + "\")";
                    PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(annotationText, null);
                    psiAnnotation.replace(newAnnotation);
                });
        });
    }
}
