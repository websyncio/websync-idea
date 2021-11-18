package org.websync.connection.commands;

import com.intellij.openapi.module.Module;
import org.websync.WebSyncException;
import org.websync.WebSyncService;
import org.websync.connection.dto.ComponentInstanceDto;
import org.websync.connection.messages.browser.ComponentInstanceMessage;
import org.websync.frameworks.jdi.JdiAttribute;
import org.websync.utils.PsiUtil;

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
        final Module module = webSyncService.getModulesProvider().findProject(commandData.projectName);
        ComponentInstanceDto componentInstance = commandData.componentInstance;
        String containerClassName = componentInstance.parentId;
        String typeName = getNameFromId(componentInstance.componentTypeId);

        // .create annotation
        String attributeName = componentInstance.initializationAttribute.getName();
        String attributeQualifiedName = JdiAttribute.getQualifiedNameByName(attributeName);
        if (attributeQualifiedName == null) {
            throw new WebSyncException("Unknown initialization attribute: " + attributeName);
        }
        // .we use only first parameters so far and ignore others
        Object attributeValue = componentInstance.initializationAttribute.getParameters().get(0).getValues().get(0);
        PsiUtil.addFieldToClass(module, typeName, componentInstance.fieldName, attributeName, attributeValue, containerClassName);
        return null;
    }
}
