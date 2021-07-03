package org.websync.connection.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.impl.source.tree.ChildRole;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.dto.AnnotationDto;
import org.websync.connection.dto.ComponentInstanceDto;
import org.websync.connection.messages.browser.ComponentInstanceMessage;
import org.websync.frameworks.jdi.JdiAttribute;

public class UpdateComponentInstanceCommand extends CommandWithDataBase<ComponentInstanceMessage> {
    public UpdateComponentInstanceCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, ComponentInstanceMessage.class));
    }

    @Override
    public Object execute(ComponentInstanceMessage commandData) throws WebSyncException {
        ComponentInstanceDto componentInstance = commandData.componentInstance;
        String parentId = componentInstance.parentId;
        int fieldIndex = componentInstance.fieldIndex;
        String newFieldName = componentInstance.fieldName;
        String newFieldTypeName = getNameFromId(componentInstance.componentType);
        final Module module = webSyncService.getProvider().findByFullName(commandData.projectName);

        updateComponentInstance(module, parentId, fieldIndex, newFieldTypeName, newFieldName);
        if (componentInstance.initializationAttribute.getParameters().size() > 1) {
            throw new WebSyncException("Changed annotation has more than one parameters. Processing of that case is not implemented.");
        }
        updateComponentInstanceWithSingleAttribute(module, parentId, fieldIndex, componentInstance.initializationAttribute);
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
