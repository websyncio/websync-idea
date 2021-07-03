package org.websync.connection.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.impl.source.tree.ChildRole;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.messages.browser.ComponentInstanceMessage;
import org.websync.frameworks.jdi.JdiAttribute;
import org.websync.utils.LoggerUtils;
import org.websync.connection.dto.ComponentInstanceDto;

public class AddComponentInstanceCommand extends CommandWithDataBase<ComponentInstanceMessage> {
    public AddComponentInstanceCommand(WebSyncService webSyncService) {
        super(webSyncService);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, ComponentInstanceMessage.class));
    }

    @Override
    public Object execute(ComponentInstanceMessage commandData) throws WebSyncException {
        final Module module = webSyncService.getProvider().findByFullName(commandData.projectName);
        ComponentInstanceDto componentInstance = commandData.componentInstance;
        String containerClassName = componentInstance.getContainerClassName();
        String typeName = getNameFromId(componentInstance.componentType);

        WriteAction.runAndWait(() -> {
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(module.getProject());
            PsiClass containerClass = findClass(module, containerClassName);
            PsiField[] fields = containerClass.getFields();
            PsiField lastField = fields[fields.length - 1];

            WriteCommandAction.runWriteCommandAction(module.getProject(),
                    containerClassName + ": add field " + componentInstance.fieldName + "'",
                    "WebSyncAction",
                    () -> {
                        String newFieldText = "public " + typeName + " " + componentInstance.fieldName + ";";
                        PsiFieldImpl newField = (PsiFieldImpl) elementFactory.createFieldFromText(newFieldText, null);

                        // .create annotation
                        String attributeName = componentInstance.initializationAttribute.getName();
                        String attributeQualifiedName = JdiAttribute.getQualifiedNameByName(attributeName);
                        if (attributeQualifiedName == null) {
                            LoggerUtils.print("Unknown initialization attribute: " + attributeName);
                            return;
                        }
                        // .we use only first parameters so far and ignore others
                        Object attributeValue = componentInstance.initializationAttribute.getParameters().get(0).getValues().get(0);
                        String annotationText = "@" + attributeQualifiedName + "(\"" + attributeValue + "\")";
                        PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(annotationText, newField);

                        // .add annotation to field
                        PsiTypeElement typeElement = (PsiTypeElement) newField.getNode().findChildByRoleAsPsiElement(ChildRole.TYPE);
                        newField.addBefore(newAnnotation, typeElement);

                        // .add field after the last field in the class
                        lastField.getParent().addAfter(newField, lastField);
                        JavaCodeStyleManager
                                .getInstance(module.getProject())
                                .shortenClassReferences(newField);
                    });
        });

        return null;
    }
}
