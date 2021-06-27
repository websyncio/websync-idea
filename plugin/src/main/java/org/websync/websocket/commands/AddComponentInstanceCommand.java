package org.websync.websocket.commands;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.impl.source.tree.ChildRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.websync.WebSyncException;
import org.websync.jdi.JdiAttribute;
import org.websync.logger.LoggerUtils;
import org.websync.react.dto.ComponentInstanceDto;

public class AddComponentInstanceCommand extends ComponentInstanceCommand {
    @Nullable
    @Override
    protected Object execute(@NotNull WebSyncCommand.Message genericMessage) throws WebSyncException {
        ComponentInstanceCommand.Message message = ((ComponentInstanceCommand.Message) genericMessage);
        final Module module = getWebSyncService().getProvider().findByFullName(message.projectName);
        ComponentInstanceDto componentInstance = message.data;
        String containerClassName = message.getContainerClassName();
        String typeName = getNameFromTypeId(componentInstance.componentType);

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
                            LoggerUtils.print("Unknown initialization attribute: "+attributeName);
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
