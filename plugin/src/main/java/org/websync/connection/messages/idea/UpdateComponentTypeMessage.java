package org.websync.connection.messages.idea;

import org.websync.connection.dto.ComponentTypeDto;
import org.websync.connection.messages.Message;

public class UpdateComponentTypeMessage extends Message {
    private ComponentTypeDto componentType;

    public UpdateComponentTypeMessage(ComponentTypeDto componentType) {
        super("update-component");
        this.componentType = componentType;
    }
}