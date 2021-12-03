package org.websync.connection.commands;

import com.intellij.openapi.module.Module;
import org.websync.WebSyncException;
import org.websync.connection.dto.ComponentInstanceDto;
import org.websync.connection.messages.browser.ComponentInstanceMessage;
import org.websync.frameworks.jdi.JdiAttribute;
import org.websync.psi.JdiProjectsProvider;
import org.websync.utils.PsiUtils;

public class AddComponentInstanceCommand extends CommandWithDataBase<ComponentInstanceMessage> {
    public AddComponentInstanceCommand(JdiProjectsProvider projectsProvider) {
        super(projectsProvider);
    }

    @Override
    public Object execute(String commandDataString) throws WebSyncException {
        return execute(parseCommandData(commandDataString, ComponentInstanceMessage.class));
    }

    @Override
    public Object execute(ComponentInstanceMessage commandData) throws WebSyncException {
        final Module module = projectsProvider.findProject(commandData.projectName);
        ComponentInstanceDto componentInstance = commandData.componentInstance;
        String containerClassName = componentInstance.parentId;
        String typeName = componentInstance.componentTypeId;

        // .create annotation
        String attributeName = componentInstance.initializationAttribute.getName();
        String attributeQualifiedName = JdiAttribute.getQualifiedNameByName(attributeName);
        if (attributeQualifiedName == null) {
            throw new WebSyncException("Unknown initialization attribute: " + attributeName);
        }
        // .we use only first parameters so far and ignore others
        Object attributeValue = componentInstance.initializationAttribute.getParameters().get(0).getValues().get(0);
        PsiUtils.addFieldToClass(module, typeName, componentInstance.fieldName, attributeName, attributeValue, containerClassName);
        //PsiUtils.addImportStatement(module, componentInstance.componentTypeId, containerClassName);
        return null;
    }
}
